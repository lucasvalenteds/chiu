import NoiseLevelsTable from "@/components/NoiseLevelsTable.vue";
import i18n from "@/plugins/i18n";
import { mount, Wrapper } from "@vue/test-utils";
import Vue from "vue";
import "../BaseTest";

describe("NoiseLevelsTable component", () => {

    const wrapper: Wrapper<Vue> = mount(NoiseLevelsTable, {
        i18n,
        attachToDocument: true,
    });

    test("It shows a table with noise levels references", () => {
        expect(wrapper.findAll(".noise-level-header").length).toBe(12);
        expect(wrapper.findAll(".noise-level-score").length).toBe(12);
    });
});
