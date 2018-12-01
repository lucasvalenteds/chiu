import Vue from "vue";
import VueI18n from "vue-i18n";

Vue.use(VueI18n);

const i18n = new VueI18n({
    locale: "br",
    messages: {
        br: require("@/i18n/pt-br.json")
    }
});

export default i18n;