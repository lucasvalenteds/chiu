<template>
  <v-toolbar app class="primary">
    <v-toolbar-title class="headline">
      <span id="app-toolbar-title" class="white--text">{{ $t("app.name") }}</span>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn id="app-toolbar-about" class="white--text" @click="$emit('onAbout')" flat>{{ $t("app.toolbar.about") }}</v-btn>
  </v-toolbar>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component
export default class AppToolbar extends Vue {}
</script>
