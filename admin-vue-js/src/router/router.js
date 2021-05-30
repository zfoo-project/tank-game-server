import Vue from 'vue';
import Router from 'vue-router';
import VueMeta from 'vue-meta';
import i18n from '@/i18n/i18n.js';
import { getXToken } from '@/util/authUtils.js';
import { isBlank } from '@/util/stringUtils.js';

Vue.use(Router);
Vue.use(VueMeta);

const router = new Router({
    mode: 'history',
    base: '/',
    scrollBehavior: () => ({ x: 0, y: 0 }),
    routes: [
        {
            path: '/',
            name: 'Home',
            meta: {
                auth: false,
                title: 'home.title'
            },
            component: () => import('@/view/home/Home.vue')
        },

        {
            path: '/player',
            name: 'player',
            meta: {
                auth: true,
                title: 'player.title'
            },
            component: () => import('@/view/player/Players.vue')
        },

        {
            path: '/javaHotSwap',
            name: 'JavaHotSwap',
            meta: {
                auth: false
            },
            component: () => import('@/view/hotswap/JavaHotSwap.vue')
        },

        {
            path: '/excelHotSwap',
            name: 'ExcelHotSwap',
            meta: {
                auth: false
            },
            component: () => import('@/view/hotswap/ExcelHotSwap.vue')
        }
    ]
});

router.beforeEach((to, from, next) => {
    // 验证权限
    const auth = !isBlank(getXToken());
    if (to.meta.auth && !auth) {
        next({ path: '/' });
        return;
    }
    // 修改标题
    document.title = i18n.t(to.meta.title);
    next();
});

export default router;
