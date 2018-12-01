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
        decibels: this.$t("noise.meter.chart.noise.label") as string,
      },
      seriesMap: {
        decibels: {
          min: 0,
          max: 120,
          axisLine: {
            lineStyle: {
              color: [[0.38, "#4CAF50"], [0.7, "#FFC107"], [1, "#F44336"]],
            },
          },
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
