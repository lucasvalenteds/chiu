package io.chiu.backend.ingest;

public class NoiseLevel {
    private final int level;

    public NoiseLevel() {
        this.level = -1;
    }

    public NoiseLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "NoiseLevel{" +
            "level=" + level +
            '}';
    }
}
