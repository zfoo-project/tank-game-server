import { createRouter, createWebHistory } from "vue-router";
import { getToken } from "@/utils/authUtils";

import TankRoutes from "./tank.routes";
import AuthRoutes from "./auth.routes";
import UIRoutes from "./ui.routes";
import LandingRoutes from "./landing.routes";
import UtilityRoutes from "./utility.routes";
import PagesRoutes from "./pages.routes";
import ChartsRoutes from "./charts.routes";
import AppsRoutes from "./apps.routes";
import _ from "lodash";

export const routes = [
  {
    path: "/",
    redirect: "/dashboard",
  } as any,
  {
    path: "/dashboard",
    meta: {
      layout: "landing",
    },
    component: () => import("@/views/pages/DashBoard.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    name: "error",
    component: () =>
      import(/* webpackChunkName: "error" */ "@/views/errors/NotFoundPage.vue"),
  },
  ...TankRoutes,
  ...LandingRoutes,
  ...AuthRoutes,
  ...PagesRoutes,
  ...UtilityRoutes,
  ...UIRoutes,
  ...ChartsRoutes,
  ...AppsRoutes,
];

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [];

const router = createRouter({
  history: createWebHistory(),
  // hash模式：createWebHashHistory，history模式：createWebHistory
  // process.env.NODE_ENV === "production"

  routes: routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

router.beforeEach((to, from, next) => {
  // 验证权限
  const auth = getToken();
  if (_.isNil(auth)) {
    if (!_.isEqual(to.meta.auth, true)) {
      next({ path: '/signin' });
      return;
    }
  }
  next();
});

export default router;
