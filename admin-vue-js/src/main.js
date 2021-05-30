import Vue from 'vue';
import App from './App.vue';
import vuetify from '@/plugin/vuetify.js';
import i18n from '@/i18n/i18n.js';

import '@/plugin/base.js';
import '@/plugin/lodash.js';
import '@/plugin/vuelidate.js';
import '@/plugin/myPlugin.js';
import '@/plugin/video.js';
import '@/plugin/chart.js';

import '@/style/index.scss'; // global css


import router from '@/router/router.js';
import store from '@/store/store.js';

// simulation data
import '@/mock/mock.js';

import { sync } from 'vuex-router-sync';

sync(store, router);

Vue.config.productionTip = false;

new Vue({
    el: '#app',
    router,
    store,
    i18n,
    vuetify,
    render: h => h(App)
});

