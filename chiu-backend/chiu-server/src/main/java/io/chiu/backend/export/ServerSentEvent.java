package io.chiu.backend.export;

public class ServerSentEvent<T> {
    private final String id;
    private final String event;
    private final T data;

    public ServerSentEvent(String id, String event, T data) {
        this.id = id;
        this.event = event;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ServerSentEvent{" +
            "id='" + id + '\'' +
            ", event='" + event + '\'' +
            ", data=" + data +
            '}';
    }
}
