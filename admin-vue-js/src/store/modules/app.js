import { set } from '@/util/vuexUtils.js';

// vue 包含的数组操作方法（变异方法）将会触发视图更新
// push() 从数组末端添加项
// pop() 从数组末端删除项
// shift() 从数组头部删除项
// unshift() 从数组头部添加项
// splice(index,num) 删除 ,num希望删除元素的数量
// sort() 排序
// reverse() 反转
export default {
    namespaced: true,

    state: {
        snackbar: null,
        appSearch: null,
        appSearchSelector: null,
        loginDialog: null,

        // 是否是移动端
        mobile: false,
        // 是否是ios系统
        ios: false,
        // 是否是safari浏览器
        safari: false,
        // 是否是微信内置浏览器
        weChat: false
    },

    mutations: {
        setSnackbar: set('snackbar'),
        setAppSearch: set('appSearch'),
        setAppSearchSelector: set('appSearchSelector'),
        setLoginDialog: set('loginDialog'),


        setMobile: set('mobile'),
        setIos: set('ios'),
        setSafari: set('safari'),
        setWeChat: set('weChat'),

        setGroupOperation: set('groupOperation'),
        setGroupInfo: set('groupInfo')
    }
};
