package ru.atom.gameservice.message;

public class Message {
    private final Topic topic;
    private final String data;

    public Message(Topic topic, String data) {
        this.topic = topic;
        this.data = data;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getData() {
        return data;
    }

}
