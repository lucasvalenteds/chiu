import Vue from "vue";
import VueI18n from "vue-i18n";

Vue.use(VueI18n);

const i18n = new VueI18n({
  locale: "en",
  messages: {
    br: require("@/i18n/pt-br.json"),
    en: require("@/i18n/en-us.json"),
  },
});

export default i18n;
