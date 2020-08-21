<template>
  <v-data-table :headers="noiseLevelHeaders" :items="noiseLevelScore" hide-default-footer>
    <template slot="items" slot-scope="props">
      <td class="noise-level-score text-xs-center">{{ props.item.level }}</td>
      <td class="noise-level-header">{{ props.item.description }}</td>
    </template>
  </v-data-table>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

@Component
export default class NoiseLevelsTable extends Vue {
  get noiseLevelHeaders(): any[] {
    return [
      {
        text: this.$t("noise.levels.table.headers.level"),
        value: "level",
        sortable: false,
        align: "center",
      },
      {
        text: this.$t("noise.levels.table.headers.description"),
        value: "description",
        sortable: false,
      },
    ];
  }

  get noiseLevelScore(): Array<{ level: string; description: string }> {
    return [150, 130, 120, 110, 100, 90, 80, 70, 60, 50, 10, 0]
      .map((level: number) => ({
        level: level.toString(),
        description: this.$t(`noise.levels.table.data.${level}`) as string,
      }));
  }
}
</script>
