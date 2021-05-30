import Vue from 'vue';

// when importing from vuetify/lib, the necessary styles are automatically imported for you.
import Vuetify from 'vuetify/lib';
import colors from 'vuetify/lib/util/colors.js';
import '@mdi/font/css/materialdesignicons.css';
import { Touch } from 'vuetify/lib/directives';

import i18n from '@/i18n/i18n.js';

Vue.use(Vuetify, {
    directives: {
        Touch
    }
});

const vuetify = new Vuetify({
    icons: {
        iconfont: 'mdi' // default - only for display purposes
    },
    lang: {
        t: (key, ...params) => i18n.t(key, params)
    },
    // 默认theme
    theme: {
        light: true,
        // dark: true,
        themes: {
            dark: {
                appBarDrawerColor: colors.shades.black,
                backgroundColor: colors.grey.darken3
            },
            light: {
                appBarDrawerColor: colors.shades.white,
                backgroundColor: colors.grey.lighten4
            }
        }
    }
});

export default vuetify;
