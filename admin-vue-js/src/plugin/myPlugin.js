import Vue from 'vue';

const myPlugin = {
    hello() {
        console.log('hello');
    }
};

// 而在@/module/vuex/view中测试
Vue.prototype.$myPlugin = myPlugin;


// make $store = $storeManager
// Vue.prototype.$storeManager = storeManager;
// Vue.prototype.$store = storeManager;
