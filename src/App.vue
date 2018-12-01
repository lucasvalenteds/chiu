<template>
  <v-app>
    <AppToolbar/>
    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";
import AppToolbar from "@/components/AppToolbar.vue";

@Component({
  components: {
    AppToolbar
  }
})
export default class App extends Vue {}
</script>
