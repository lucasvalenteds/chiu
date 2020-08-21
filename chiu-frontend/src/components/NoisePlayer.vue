<template>
    <div>
        <v-flex xs12>
            <v-card-subtitle class="ma-0 pa-1">
                {{ $t("noise.player.message") }}
            </v-card-subtitle>
            <v-flex xs12 class="mt-8">
               <v-slider
                 :value="currentNoiseLevel"
                 :label="$t('noise.player.level')"
                 :min="0"
                 :max="150"
                 :step="5"
                 :color="colorNoiseLevel"
                 :thumb-color="colorNoiseLevel"
                 thumb-label="always"
                 ticks
                 tick-size="2"
                 @change="onLevelChange"
               ></v-slider>
            </v-flex>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn outlined color="primary" outline :disabled="isPlaying" @click="playArbitratyNoiseLevel">
                    {{ $t("noise.player.enable") }}
                </v-btn>
                <v-btn outlined color="warning" outline :disabled="!isPlaying" @click="resetNoiseLevel">
                    {{ $t("noise.player.disable") }}
                </v-btn>
            </v-card-actions>
        </v-flex>
    </div>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

export interface SoundTrack {
    levelFilter: (level: number) => boolean;
    file: string;
}

export interface ColorRange {
    matches: (level: number) => boolean;
    color: string;
}

@Component({
    props: {
        tracklist: {
            type: Array as () => SoundTrack[],
            default: [],
        },
    },
})
export default class NoisePlayer extends Vue {
    public noiseLevel: number = 0;
    public isPlaying: boolean = false;

    public player: HTMLAudioElement = new Audio();
    public sliderColorRange: ColorRange[] = [
        {
            matches: (level: number) => level >= 1 && level < 50,
            color: "#4CAF50",
        },
        {
            matches: (level: number) => level >= 50 && level <= 105,
            color: "#FFC107",
        },
        {
            matches: (level: number) => level > 105,
            color: "#F44336",
        },
        {
            matches: (level: number) => true,
            color: "",
        },
    ];

    public get currentNoiseLevel(): number {
        return this.noiseLevel;
    }

    public get colorNoiseLevel(): string {
        return this.sliderColorRange
            .filter((range: ColorRange) => range.matches(this.currentNoiseLevel))
            .map((range: ColorRange) => range.color)
            .shift()!!;
    }

    public created(): void {
        this.player = this.selectTrackBasedOnLevel(this.noiseLevel, this.$props.tracklist);
    }

    public destroyed(): void {
        this.stopPlaying();
    }

    public startPlaying(): void {
        this.isPlaying = true;
        this.player.loop = true;
        this.player.play();
    }

    public stopPlaying(): void {
        this.isPlaying = false;
        this.player.currentTime = 0.0;
        this.player.pause();
    }

    public playArbitratyNoiseLevel(): void {
        this.onLevelChange(25);
    }

    public resetNoiseLevel(): void {
        this.stopPlaying();
        this.noiseLevel = 0;
    }

    public selectTrackBasedOnLevel(level: number, tracks: SoundTrack[]): HTMLAudioElement {
        const soundTrack: SoundTrack = tracks
            .filter((it: SoundTrack) => it.levelFilter(level))
            .shift()!!;

        return new Audio(soundTrack.file);
    }

    public onLevelChange(level: number): void {
        this.noiseLevel = level;

        if (this.noiseLevel === 0) {
            this.resetNoiseLevel();
        } else {
            this.stopPlaying();
            this.player = this.selectTrackBasedOnLevel(this.noiseLevel, this.$props.tracklist);
            this.startPlaying();
        }
    }
}
</script>
