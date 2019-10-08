<template>
  <v-container fluid grid-list-xl>
    <v-layout row wrap justify-center>
      <v-flex xs12 sm6 lg4>
          <v-card>
            <v-card-title primary-title>
              <v-icon class="mr-3">hearing</v-icon>
              <h3 class="headline">{{ $t("noise.meter.chart.noise.title") }}</h3>
            </v-card-title>
            <v-divider></v-divider>
            <NoiseMeterChart :apiUrl="apiUrl"/>
          </v-card>
          <v-card class="mt-4">
            <v-card-title primary-title>
              <v-icon class="mr-3">radio</v-icon>
              <h3 class="headline">{{ $t("noise.player.title") }}</h3>
            </v-card-title>
            <v-divider></v-divider>
            <NoisePlayer :tracklist="tracklist"/>
            <v-divider></v-divider>
            <v-card-actions>
              <p class="subheading pa-2">{{ $t("noise.player.source.label") }}</p>
              <a
                target="_blank"
                href="https://freesound.org/"
              >{{ $t("noise.player.source.text") }}</a>
            </v-card-actions>
          </v-card>
      </v-flex>
      <v-flex xs12 sm6 lg4>
        <v-card>
          <v-card-title primary-title>
            <v-icon class="mr-3">help_outline</v-icon>
            <h3 class="headline">{{ $t("noise.levels.table.title") }}</h3>
          </v-card-title>
          <v-divider></v-divider>
          <NoiseLevelsTable/>
          <v-divider></v-divider>
          <v-card-actions>
            <p class="subheading pa-2">{{ $t("noise.levels.table.source.label") }}</p>
            <a
              target="_blank"
              href="https://www.who.int/ceh/capacity/v1_ruido.pdf"
            >{{ $t("noise.levels.table.source.text") }}</a>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script lang="ts">
import NoiseLevelsTable from "@/components/NoiseLevelsTable.vue";
import NoiseMeterChart from "@/components/NoiseMeterChart.vue";
import NoisePlayer from "@/components/NoisePlayer.vue";
import Vue from "vue";
import Component from "vue-class-component";

@Component({
  components: {
    NoiseLevelsTable,
    NoiseMeterChart,
    NoisePlayer,
  },
  props: {
    apiUrl: {
      type: String,
      required: true,
    },
    tracklist: {
      type: Array,
      required: true,
    },
  },
})
export default class Home extends Vue {}
</script>
