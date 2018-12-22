import NoiseMeterChart from "@/components/NoiseMeterChart.vue";
import i18n from "@/plugins/i18n";
import { mount, Wrapper } from "@vue/test-utils";
import Vue from "vue";
import "../BaseTest";

describe("NoiseMeterChart component", () => {

    const wrapper: Wrapper<Vue> = mount(NoiseMeterChart, {
        i18n,
        attachToDocument: true,
        propsData: {
            apiUrl: "http://localhost:8080",
        },
    });

    test("It shows an error banner when the back-end is not available", () => {
        const errorBanner: Wrapper<Vue> = wrapper.find("#noise-meter-error");
        expect(errorBanner.isVisible()).toBe(false);

        wrapper.setData({ showError: true });

        expect(errorBanner.isVisible()).toBe(true);
    });
    test("The speedometer is visible", () => {
        expect(wrapper.find("#noise-meter-chart").isVisible()).toBe(true);
    });
    test("The speedometer changes the value when back-end sends a new event", () => fail("Do not know how to test it"));
});
