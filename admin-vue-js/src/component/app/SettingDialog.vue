<template>
    <v-dialog v-model="dialog" width="500">
        <v-card>
            <v-list-item>
                <v-list-item-icon>
                    <v-icon>mdi-cog-outline mdi-spin</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                    <v-list-item-title>{{ $t('setting.app') }}</v-list-item-title>
                </v-list-item-content>
            </v-list-item>

            <v-expansion-panels hover>
                <v-expansion-panel>
                    <v-expansion-panel-header>
                        {{ $t('setting.theme') }}
                        <template v-slot:actions>
                            <v-icon>mdi-theme-light-dark</v-icon>
                        </template>
                    </v-expansion-panel-header>
                    <v-expansion-panel-content>
                        <v-switch v-model="theme" :label="$t('setting.darkTheme')" />
                    </v-expansion-panel-content>
                </v-expansion-panel>

                <v-expansion-panel>
                    <v-expansion-panel-header>
                        {{ $t('setting.language') }}
                        <template v-slot:actions>
                            <v-icon>mdi-translate</v-icon>
                        </template>
                    </v-expansion-panel-header>
                    <v-expansion-panel-content>
                        <v-radio-group v-model="language" row dense>
                            <v-radio
                                v-for="(group, index) in languageGroup"
                                :key="index"
                                :value="group.type"
                                :label="$t(group.value)"
                                color="primary"
                            />
                        </v-radio-group>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-expansion-panels>
        </v-card>
    </v-dialog>
</template>


<script>
import { mapMutations, mapState } from 'vuex';
import { saveSettingApi } from '@/apiHttp/settingApi.js';
import { simpleSuccess } from '@/util/noticeUtils.js';
import { settingConstant } from '@/constant/constant.js';

export default {
    name: 'SettingDialogDialog',

    data: () => ({
        tabs: null,
        loading: false,
        dialog: false,

        theme: true,
        language: 0,

        languageGroup: [
            {
                type: 0,
                value: 'setting.cnLanguage'
            },
            {
                type: 1,
                value: 'setting.enLanguage'
            }
        ]
    }),
    computed: {
        ...mapState('user', ['loggedIn', 'setting'])
    },

    watch: {
        dialog: function() {
            if (this.dialog) {
                this.theme = this.setting.theme === settingConstant.theme.dark;
                this.language = this.setting.language;
            } else {
                let flag = false;
                const themeType = this.theme ? settingConstant.theme.dark : settingConstant.theme.light;
                const languageType = this.language === settingConstant.language.cn.type ? settingConstant.language.cn : settingConstant.language.en;

                if (themeType !== this.setting.theme) {
                    flag = true;
                }
                if (languageType.type !== this.setting.language) {
                    flag = true;
                }

                if (flag) {
                    saveSettingApi(themeType, languageType.type).then(response => {
                        this.setSetting({
                            theme: themeType,
                            language: languageType.type
                        });
                        this.$vuetify.theme.dark = themeType === settingConstant.theme.dark;
                        this.$i18n.locale = languageType.value;

                        simpleSuccess(this.$t('notice.settingSuccess'));
                    });
                }
            }
        }
    },

    methods: {
        ...mapMutations('user', ['setLoggedIn', 'setSetting']),

        showDialog(value) {
            this.dialog = value;
        }
    }

};
</script>
