export default [
  {
    path: "/excel",
    name: "excel-hotswap",
    component: () =>
      import(
        /* webpackChunkName: "tank-excel" */ "@/views/tank/ExcelHotSwap.vue"
        ),
    meta: {
      title: "Excel",
      layout: "ui",
      category: "Tank",
    },
  },
  {
    path: "/java",
    name: "java-hotswap",
    component: () =>
      import(
        /* webpackChunkName: "tank-java" */ "@/views/tank/JavaHotSwap.vue"
        ),
    meta: {
      title: "Java",
      layout: "ui",
      category: "Tank",
    },
  },
  {
    path: "/player/query",
    name: "player-query",
    component: () =>
      import(
        /* webpackChunkName: "tank-java" */ "@/views/tank/PlayerQuery.vue"
        ),
    meta: {
      title: "Player",
      layout: "ui",
      category: "Tank",
    },
  },
  {
    path: "/gm/currency",
    name: "gm-currency",
    component: () =>
      import(
        /* webpackChunkName: "tank-java" */ "@/views/tank/CurrencyGm.vue"
        ),
    meta: {
      title: "Currency",
      layout: "ui",
      category: "Tank",
    },
  },
  {
    path: "/signin",
    name: "signin",
    component: () =>
      import(
        /* webpackChunkName: "auth-signin" */ "@/views/tank/TankSigninPage.vue"
        ),
    meta: {
      auth: true,
      layout: "auth",
      title: "SignIn",
    },
  },
];
