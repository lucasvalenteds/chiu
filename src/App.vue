<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title class="headline">
        <span>Chiu</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn flat>
        Sobre
      </v-btn>
    </v-toolbar>

    <v-content>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component
export default class App extends Vue {}
</script>
