package io.chiu.backend;

import java.util.UUID;

public class SensorData {
    private final UUID id;
    private final int level;

    public SensorData(UUID id, int level) {
        this.id = id;
        this.level = level;
    }

    public UUID getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "SensorData{" +
            "id=" + id +
            ", level=" + level +
            '}';
    }
}
