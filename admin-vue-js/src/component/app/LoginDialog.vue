<template>
    <v-dialog v-model="dialog" width="500">
        <v-tabs v-model="tabs">
            <v-tabs-slider />

            <v-tab>
                {{ $t('user.loginByPassword') }}
            </v-tab>

        </v-tabs>

        <v-tabs-items v-model="tabs">
            <v-tab-item>
                <v-card>
                    <v-card-text>
                        <v-form>
                            <v-text-field
                                v-model="account"
                                :label="$t('user.account')"
                                :error-messages="accountErrors"
                                prepend-icon="person"
                                type="text"
                                required
                                @input="$v.account.$touch()"
                                @blur="$v.account.$touch()"
                                @keyup.exact.enter="loginByAccount()"
                            />

                            <v-text-field
                                v-model="password"
                                :label="$t('user.password')"
                                :error-messages="passwordErrors"
                                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                :type="showPassword ? 'text' : 'password'"
                                prepend-icon="lock"
                                required
                                @input="$v.password.$touch()"
                                @blur="$v.password.$touch()"
                                @click:append="showPassword = !showPassword"
                                @keyup.exact.enter="loginByAccount()"
                            />

                            <v-row />
                        </v-form>
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer />
                        <v-btn color="primary" :loading="loading" :disabled="loading" @click="loginByAccount()">
                            {{ $t('user.login') }}
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-tab-item>
        </v-tabs-items>

        <v-spacer />
    </v-dialog>
</template>


<script>
import { mapMutations, mapState } from 'vuex';
import { email, maxLength, minLength, numeric, required } from 'vuelidate/lib/validators';
import { loginByAccountApi, logoutApi } from '@/apiHttp/loginApi.js';
import { removeXToken, setXToken } from '@/util/authUtils.js';
import { simpleError, simpleSuccess } from '@/util/noticeUtils.js';

export default {
    name: 'LoginDialog',

    validations: {
        account: {
            required,
            email,
            minLength: minLength(1),
            maxLength: maxLength(11),
            numeric
        },

        password: {
            required,
            minLength: minLength(1),
            maxLength: maxLength(16)
        },

        accountVerifyCode: {
            required,
            minLength: minLength(1),
            maxLength: maxLength(4),
            numeric
        }
    },

    data: () => ({

        tabs: null,
        loading: false,

        showPassword: false,

        dialog: true,

        account: '',
        password: ''
    }),
    computed: {
        ...mapState('user', ['loggedIn', 'avatar']),

        accountErrors() {
            const errors = [];
            if (!this.$v.account.$dirty) {
                return errors;
            }

            if (!this.$v.account.required) {
                errors.push(this.$t('user.accountError'));
                return errors;
            }

            // if (!this.$v.account.email && !(this.$v.account.numeric && this.$v.account.minLength && this.$v.account.maxLength)) {
            //     errors.push(this.$t('user.mobileOrEmailError'));
            //     return errors;
            // }

            return errors;
        },

        passwordErrors() {
            const errors = [];
            if (!this.$v.password.$dirty) {
                return errors;
            }
            if (!this.$v.password.required) {
                errors.push(this.$t('user.passwordEmpty'));
                return errors;
            }
            if (!this.$v.password.minLength) {
                errors.push(this.$t('user.passwordMinLengthError'));
                return errors;
            }
            if (!this.$v.password.maxLength) {
                errors.push(this.$t('user.passwordMaxLengthError'));
                return errors;
            }
            // const regx = new RegExp(/^(?=.*[a-zA-Z])(?=.*\d)[^]{1,16}$/);
            // if (!regx.test(this.password)) {
            //     errors.push(this.$t('user.passwordFormatError'));
            //     return errors;
            // }
            return errors;
        },

        accountVerifyCodeErrors() {
            const errors = [];
            if (!this.$v.accountVerifyCode.$dirty) {
                return errors;
            }
            if (!this.$v.accountVerifyCode.required) {
                errors.push(this.$t('user.verifyCodeEmpty'));
                return errors;
            }
            if (!(this.$v.accountVerifyCode.numeric && this.$v.accountVerifyCode.minLength && this.$v.accountVerifyCode.maxLength)) {
                errors.push(this.$t('user.verifyCodeError'));
                return errors;
            }
            return errors;
        }
    },

    methods: {
        ...mapMutations('user', ['setLoggedIn', 'setWsLoggedIn', 'clearUserState']),

        init() {
            this.$v.$reset();

            this.showPassword = false;
            this.loading = false;
            this.account = '';
            this.password = '';
            this.accountVerifyCode = '';

            const phoneVerify = this.$refs.phoneVerify;
            if (!_.isNil(phoneVerify)) {
                phoneVerify.init();
            }
        },

        loginByAccount() {
            this.$v.$touch();
            if (!_.isEmpty(this.accountErrors) || !_.isEmpty(this.passwordErrors)) {
                return;
            }

            if (_.isEmpty(this.account)) {
                simpleError(this.$t('notice.loginNameEmptyError'));
                return;
            }

            if (_.isEmpty(this.password)) {
                simpleError(this.$t('notice.loginPasswordEmptyError'));
                return;
            }

            this.loading = true;

            // 登录
            loginByAccountApi(this.account, this.password, this.accountVerifyKey, this.accountVerifyCode).then(response => {
                this.afterLogin(response);
                this.loading = false;
            }).catch(() => {
                this.loading = false;
            });
        },

        afterLogin(loginResponse) {
            const data = loginResponse.data;

            // 设置cookie
            setXToken(data.token);

            this.dialog = false;
            this.init();

            simpleSuccess(this.$t('notice.loginSuccess'));

            // 拉取用户信息
            this.$store.dispatch('user/getUserInfo', this.$vuetify);
        },


        logout() {
            logoutApi().then(response => {
                this.setLoggedIn(false);
                this.setWsLoggedIn(false);

                removeXToken();

                this.clearUserState(this.$vuetify);

                simpleSuccess(this.$t('notice.logoutSuccess'));

                if (!_.isEqual(this.$route.path, '/')) {
                    this.$router.push({ path: '/' });
                }
            });
        },

        showDialog(value) {
            if (_.startsWith(this.$route.path, '/register')) {
                simpleError(this.$t('notice.registerNotCompletedError'));
                return;
            }

            this.dialog = value;
            if (this.dialog === true) {
                this.init();
            }
        }
    }

};
</script>
