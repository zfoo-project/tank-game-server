import i18n from '@/i18n/i18n.js';

import { set } from '@/util/vuexUtils.js';
import { getXToken } from '@/util/authUtils.js';
import { getUserProfileApi } from '@/apiHttp/loginApi.js';
import { isBlank } from '@/util/stringUtils.js';
import { settingConstant } from '@/constant/constant.js';

export default {
    namespaced: true,

    state: {
        loggedIn: false,
        wsLoggedIn: false,

        id: '0',
        adminAuth: 0,
        name: '',
        avatar: '',
        setting: {}
    },

    actions: {
        // 获取用户信息
        getUserInfo: async({ commit, state }, vuetify) => {
            if (state.loggedIn) {
                return;
            }
            if (isBlank(getXToken())) {
                return;
            }
            getUserProfileApi(getXToken()).then(response => {
                const data = response.data;

                // 用户信息
                if (!isBlank(data.avatar)) {
                    commit('setAvatar', data.avatar);
                }
                commit('setId', data.id);
                commit('setName', data.userName);
                commit('setSetting', data.setting);

                commit('setLoggedIn', true);

                setTimeout(() => {
                    // 用户自定义设置
                    vuetify.theme.dark = settingConstant.theme.dark === data.setting.theme;
                    i18n.locale = settingConstant.language.cn.type === data.setting.language ? settingConstant.language.cn.value : settingConstant.language.en.value;
                }, 500);
            });
        }

    },


    mutations: {
        setLoggedIn: set('loggedIn'),
        setWsLoggedIn: set('wsLoggedIn'),
        setToken: set('token'),
        setId: set('id'),
        setName: set('name'),
        setAvatar: set('avatar'),
        setSetting: set('setting'),

        clearUserState(state, vuetify) {
            state.id = 0;
            state.name = '';
            state.avatar = '';
            state.setting = {};
            vuetify.theme.dark = false;
        }
    }

};
