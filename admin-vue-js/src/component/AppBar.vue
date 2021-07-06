<template>
    <div>
        <v-navigation-drawer
            v-if="loggedIn"
            v-model="drawer"
            color="appBarDrawerColor"
            :expand-on-hover="$vuetify.breakpoint.mdAndUp ? true : false"
            app
            clipped
        >
            <template v-if="!$vuetify.breakpoint.mdAndUp">
                <v-list-item>
                    <v-list-item-avatar>
                        <v-img :src="require(`@/asset/logo/zfoo_normal.jpg`)" />
                    </v-list-item-avatar>
                    <v-list-item-title>
                        {{ appConstant.appName }}
                    </v-list-item-title>
                </v-list-item>

                <v-divider />
            </template>

            <v-list dense color="appBarDrawerColor">
                <v-list-item v-for="item in items" :key="item.text" @click="goTo(item.to)">
                    <v-list-item-action>
                        <v-icon>{{ item.icon }}</v-icon>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title>
                            {{ $t(item.text) }}
                        </v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>

            <template v-slot:append>
                <v-btn text small class="pa-0 ma-0">
                    {{ appConstant.appVersion }}
                </v-btn>
            </template>
        </v-navigation-drawer>

        <v-app-bar app clipped-left :dense="$vuetify.breakpoint.mdAndUp ? false : true">
            <v-app-bar-nav-icon @click.stop="drawer = !drawer" />

            <v-avatar v-if="$vuetify.breakpoint.mdAndUp" v-ripple size="40" @click="goHome()">
                <img :src="require(`@/asset/logo/zfoo_normal.jpg`)">
            </v-avatar>

            <!--            <v-toolbar-title v-if="$vuetify.breakpoint.mdAndUp">-->
            <!--                {{ appConstant.appName }}-->
            <!--            </v-toolbar-title>-->

            <v-spacer />

            <!-- 右上角用户头像点击-->
            <v-menu v-if="loggedIn" offset-x left>
                <template v-slot:activator="{ on }">
                    <v-avatar v-ripple size="40" v-on="on">
                        <v-img :src="require(`@/asset/icon/default_avatar.gif`)" />
                    </v-avatar>
                </template>

                <v-list>
                    <v-list-item>
                        <v-list-item-icon>
                            <v-icon>mdi-face</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>{{ $t('user.userCenter') }}</v-list-item-title>
                    </v-list-item>

                    <v-list-item @click="$refs.settingDialog.showDialog(true)">
                        <v-list-item-icon>
                            <v-icon>mdi-cog-outline</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>{{ $t('user.userSetting') }}</v-list-item-title>
                    </v-list-item>

                    <v-list-item @click="$refs.loginDialog.logout()">
                        <v-list-item-icon>
                            <v-icon>mdi-logout</v-icon>
                        </v-list-item-icon>
                        <v-list-item-title>{{ $t('user.logout') }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-menu>
            <v-btn v-else icon color="primary" @click.stop="$refs.loginDialog.showDialog(true)">
                <v-icon large>mdi-account-circle</v-icon>
            </v-btn>

            <!-- 登录界面 -->
            <login-dialog ref="loginDialog" />

            <!-- 设置界面 -->
            <setting-dialog ref="settingDialog" />
        </v-app-bar>
    </div>
</template>

<script>
import { appConstant } from '@/constant/constant.js';
import { mapMutations, mapState } from 'vuex';
import { simpleError } from '@/util/noticeUtils.js';

export default {
    name: 'AppBar',
    components: {
        LoginDialog: () => import('./app/LoginDialog.vue'),
        SettingDialog: () => import('./app/SettingDialog.vue')
    },
    data: () => ({
        appConstant,

        drawer: null,
        items: [
            { icon: 'mdi-home-outline', text: 'home.main', to: '/' },
            { icon: 'mdi-account-alert-outline', text: 'player.title', to: '/player' },
            { icon: 'mdi-language-java', text: 'javaHotswap', to: '/javaHotSwap' },
            { icon: 'mdi-file-excel-outline', text: 'excelHotswap', to: '/excelHotSwap' },
            { icon: 'mdi-currency-btc', text: 'gm.currency', to: '/currency' },
            { icon: 'mdi-account-convert', text: 'gm.level', to: '/playerLevel' }

        ]
    }),
    computed: {
        ...mapState('user', ['loggedIn', 'adminAuth', 'avatar'])
    },
    created() {
        this.init();
    },
    methods: {
        ...mapMutations('app', ['setLoginDialog']),

        init() {
            setTimeout(() => {
                const loginDialog = this.$refs.loginDialog;
                if (_.isNil(loginDialog)) {
                    this.init();
                    return;
                }
                this.setLoginDialog(loginDialog);
            }, 500);
        },

        goTo(to) {
            if (_.isEqual(to, '/')) {
                this.goHome();
                return;
            }
            if (!this.loggedIn) {
                simpleError(this.$t('notice.notSignInError'));
                return;
            }
            if (_.isEqual(this.$route.path, to)) {
                return;
            }
            this.$router.push({ path: to });
        },

        goHome() {
            if (!_.isEqual(this.$route.path, '/')) {
                this.$router.push({ path: '/' });
            }
        }
    }
};
</script>
