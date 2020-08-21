<template>
  <v-container fluid grid-list-xl>
    <v-layout row wrap justify-center class="grey lighten-5">
      <v-flex xs12 sm6 lg4>
          <v-card outlined>
            <v-card-title>
              <v-icon class="mr-4">hearing</v-icon>
              {{ $t("noise.meter.chart.noise.title") }}
            </v-card-title>
            <NoiseMeterChart :apiUrl="apiUrl"/>
          </v-card>
          <v-card outlined class="mt-4">
            <v-card-title primary-title>
              <v-icon class="mr-4">radio</v-icon>
              {{ $t("noise.player.title") }}
            </v-card-title>
            <NoisePlayer :tracklist="tracklist"/>
            <v-card-actions>
            <v-spacer></v-spacer>
              <v-btn text href="https://freesound.org/">
                {{ $t("noise.player.source.label") }}
              </v-btn>
            </v-card-actions>
          </v-card>
      </v-flex>
      <v-flex xs12 sm6 lg4>
        <v-card outlined>
          <v-card-title>
            <v-icon class="mr-4">leaderboard</v-icon>
            {{ $t("noise.levels.table.title") }}
          </v-card-title>
          <NoiseLevelsTable/>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn text href="https://www.who.int/ceh/capacity/v1_ruido.pdf">
              {{ $t("noise.levels.table.source.label") }}
            </v-btn>
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
