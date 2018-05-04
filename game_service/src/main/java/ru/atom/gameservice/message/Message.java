package ru.atom.gameservice.message;

public class Message {
    private final Topic topic;
    private final String data;

    private final String name;

    public Message(Topic topic, String data, String name) {
        this.topic = topic;
        this.data = data;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getData() {
        return data;
    }

}
