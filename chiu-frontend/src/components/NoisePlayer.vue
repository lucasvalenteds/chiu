<template>
    <div>
        <v-flex xs12>
            <v-card-subtitle class="ma-0 pa-1">
                {{ $t("noise.player.message") }}
            </v-card-subtitle>
            <v-flex xs12 class="mt-8 mb-0 pb-0">
               <v-slider
                 :value="currentNoiseLevel"
                 :label="$t('noise.player.level')"
                 :min="0"
                 :max="150"
                 :step="5"
                 :color="colorNoiseLevel"
                 :thumb-color="colorNoiseLevel"
                 :track-color="colorNoiseLevel"
                 thumb-label="always"
                 ticks
                 tick-size="2"
                 @change="onLevelChange"
               ></v-slider>
            </v-flex>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="grey darken-3" class="white--text" depressed :disabled="isPlaying" @click="playArbitratyNoiseLevel">
                    {{ $t("noise.player.enable") }}
                </v-btn>
                <v-btn color="grey darken-3" class="white--text" depressed :disabled="!isPlaying" @click="resetNoiseLevel">
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
            color: "#4caf50",
        },
        {
            matches: (level: number) => level >= 50 && level <= 105,
            color: "#fb8c00",
        },
        {
            matches: (level: number) => level > 105,
            color: "#ff5252",
        },
        {
            matches: (level: number) => true,
            color: "grey",
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
