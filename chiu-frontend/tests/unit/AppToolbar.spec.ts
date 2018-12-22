import AppToolbar from "@/components/AppToolbar.vue";
import i18n from "@/plugins/i18n";
import { mount, Wrapper } from "@vue/test-utils";
import Vue from "vue";
import "../BaseTest";

describe("AppToolbar component", () => {

    const wrapper: Wrapper<Vue> = mount(AppToolbar, {
        i18n,
        attachToDocument: true,
    });

    test("It shows the project name", () => {
        expect(wrapper.find("#app-toolbar-title").isVisible()).toBe(true);
    });
    test("About button emits `onAbout` event when clicked", () => {
        const aboutButton: Wrapper<Vue> = wrapper.find("#app-toolbar-about");

        aboutButton.trigger("click");

        expect(wrapper.emitted("onAbout")).toBeDefined();
    });
});
