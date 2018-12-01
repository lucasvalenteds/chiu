<template>
    <ve-gauge :data="chartData" :settings="chartSettings"></ve-gauge>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component
export default class NoiseMeterChart extends Vue {
  public chartData = {
    columns: ["type", "value"],
    rows: [
      {
        type: "decibels",
        value: 0,
      },
    ],
  };

  get chartSettings() {
    return {
      dataName: {
        decibels: this.$t("noise.meter.chart.label") as string,
      },
      seriesMap: {
        decibels: {
          min: 0,
          max: 120,
        },
      },
    };
  }

  public mounted(): void {
    this.updateNoiseMeterCurrentValue(0);
  }

  public updateNoiseMeterCurrentValue(value: number): void {
    setInterval(() => {
      this.chartData.rows[0].value = Math.floor(Math.random() * 100) + 1;
    }, 250);
  }
}
</script>
