<template>
  <v-app>
    <AppToolbar @onAbout="openAboutProjectModal" @onMaps="renderMapsPage"/>
    <v-content>
      <router-view></router-view>
    </v-content>
    <AppAboutModal :state="showAboutModal" @onCancel="closeAboutProjectModal"/>
  </v-app>
</template>

<script lang="ts">
import AppAboutModal from "@/components/AppAboutModal.vue";
import AppToolbar from "@/components/AppToolbar.vue";
import Vue from "vue";
import Component from "vue-class-component";

@Component({
  components: {
    AppToolbar,
    AppAboutModal,
  },
})
export default class App extends Vue {
  public showAboutModal: boolean = false;

  public openAboutProjectModal(): void {
    this.showAboutModal = true;
  }

  public closeAboutProjectModal(): void {
    this.showAboutModal = false;
  }

  public renderMapsPage(): void {
    return;
  }
}
</script>
