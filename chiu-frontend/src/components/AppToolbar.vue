<template>
  <v-toolbar flat color="grey lighten-5">
    <v-toolbar-title>
      <span id="app-toolbar-title" class="body-1 font-weight-medium text-uppercase">{{ $t("app.name") }}</span>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn id="app-toolbar-about" @click="$emit('onAbout')" text>{{ $t("app.toolbar.about") }}</v-btn>
  </v-toolbar>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component
export default class AppToolbar extends Vue {}
</script>
