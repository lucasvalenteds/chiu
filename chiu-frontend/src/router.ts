import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: "hash",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "home",
      props: {
        apiUrl: process.env.VUE_APP_API_URL,
      },
      component: () => import("@/views/Home.vue"),
    },
  ],
});
