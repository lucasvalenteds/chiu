<template>
  <v-dialog v-model="state" persistent width="500">
    <v-card>
      <v-card-title id="about-modal-title" class="headline lighten-2" primary-title>{{ $t("app.about.modal.title") }}</v-card-title>
      <v-card-text id="about-modal-content">{{ $t("app.about.modal.message") }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          id="about-modal-cancel"
          color="grey darken-4"
          text
          @click="$emit('onCancel')"
        >{{ $t("app.about.modal.button.close") }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component({
  props: {
    state: {
      type: Boolean,
      required: true,
      default: false,
    },
  },
})
export default class AppAboutModal extends Vue {}
</script>
