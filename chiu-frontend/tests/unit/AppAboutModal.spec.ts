import AppAboutModal from "@/components/AppAboutModal.vue";
import i18n from "@/plugins/i18n";
import { mount, Wrapper } from "@vue/test-utils";
import Vue from "vue";
import "../BaseTest";

describe("AppAboutModal component", () => {

    const wrapper: Wrapper<Vue> = mount(AppAboutModal, {
        i18n,
        attachToDocument: true,
        propsData: {
            state: true,
        },
    });

    test("Close button emits `onCancel` events when clicked", () => {
        const closeButton: Wrapper<Vue> = wrapper.find("#about-modal-cancel");

        closeButton.trigger("click");

        expect(wrapper.emitted("onCancel")).toBeDefined();
    });
    test("It shows title, description and a button to the source code", () => {
        expect(wrapper.find("#about-modal-title").isVisible()).toBe(true);
        expect(wrapper.find("#about-modal-content").isVisible()).toBe(true);
        expect(wrapper.find("#about-modal-source").isVisible()).toBe(true);
    });
});
