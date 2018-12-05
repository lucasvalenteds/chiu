import Vue from "vue";
import App from "./App.vue";
import "./plugins/charts";
import i18n from "./plugins/i18n";
import "./plugins/vuetify";
import router from "./router";

Vue.config.productionTip = false;

new Vue({
  router,
  i18n,
  render: (h) => h(App),
}).$mount("#app");
