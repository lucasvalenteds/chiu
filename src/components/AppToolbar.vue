<template>
    <v-toolbar app>
      <v-toolbar-title class="headline">
        <span>Chiu</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn @click="$emit('onAbout')" flat>Sobre</v-btn>
    </v-toolbar>
</template>

<script lang="ts">
import Vue from "vue";
import Component from 'vue-class-component';

@Component
export default class AppToolbar extends Vue {}
</script>
