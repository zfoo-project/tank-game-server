import Vue from 'vue';
import VueI18n from 'vue-i18n';
import en from 'vuetify/lib/locale/en.js';
import cn from 'vuetify/lib/locale/zh-Hans.js';
import enLocal from './en/local.js';
import enCodeEnum from './en/codeEnum.js';
import cnLocal from './cn/local.js';
import cnCodeEnum from './cn/codeEnum.js';


Vue.use(VueI18n);

const messages = {
    cn: {
        $vuetify: cn,
        ...cnLocal,
        ...cnCodeEnum
    },
    en: {
        $vuetify: en,
        ...enLocal,
        ...enCodeEnum
    }
};

// Create VueI18n instance with options
const i18n = new VueI18n({
    locale: 'cn', // set default language
    messages // set locale messages
});

// vue i18n is in here，go to src/plugin/vuetify.js to see vuetify i18n
export default i18n;
