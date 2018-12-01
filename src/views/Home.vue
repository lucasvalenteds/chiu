<template>
  <div>
    <h1>{{ $t("noise.meter.chart.title") }}</h1>
    <NoiseMeterChart/>
  </div>
</template>

<script lang="ts">
import NoiseMeterChart from "@/components/NoiseMeterChart.vue";
import Vue from "vue";
import Component from "vue-class-component";

@Component({
  components: {
    NoiseMeterChart,
  },
})
export default class Home extends Vue {}
</script>
