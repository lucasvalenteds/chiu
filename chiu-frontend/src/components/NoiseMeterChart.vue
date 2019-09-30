<template>
  <div>
    <v-alert
      id="noise-meter-error"
      :value="showError"
      type="warning"
      class="black--text"
    >{{ $t("noise.meter.chart.noise.error") }}</v-alert>
    <ve-gauge id="noise-meter-chart" :data="chartData" :settings="chartSettings"></ve-gauge>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

export interface Noise {
  id: string;
  level: number;
}

@Component({
  props: {
    apiUrl: {
      type: String,
      required: true,
    },
  },
})
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
          max: 150,
          axisLine: {
            lineStyle: {
              color: [[0.38, "#4CAF50"], [0.7, "#FFC107"], [1, "#F44336"]],
            },
          },
        },
      },
    };
  }

  public showError: boolean = true;
  public noiseSource?: EventSource;

  public created(): void {
    this.noiseSource = new EventSource(this.$props.apiUrl);
    this.noiseSource.addEventListener("error", (event: Event) => {
      this.chartData.rows[0].value = 0;
      this.showError = true;
    });
  }

  public mounted(): void {
    this.noiseSource!.addEventListener("noise", (event: Event) => {
      const noise: Noise = JSON.parse((event as MessageEvent).data);

      this.chartData.rows[0].value = noise.level;
      this.showError = false;
    });
  }

  public destroyed(): void {
    this.noiseSource!.close();
  }
}
</script>
