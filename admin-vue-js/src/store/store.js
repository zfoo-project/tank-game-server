import Vue from 'vue';
import Vuex from 'vuex';
// Modules
import app from '@/store/modules/app.js';
import user from '@/store/modules/user.js';

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        app,
        user
    }
});

export default store;
