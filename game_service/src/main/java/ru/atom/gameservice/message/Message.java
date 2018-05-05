package ru.atom.gameservice.message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

//@JsonIgnoreProperties(value = { "name" })
public class Message {
    private final Topic topic;
    private final String data;

    //@JsonIgnore
    private  String name;

    @JsonCreator
    public Message(@JsonProperty("topic") Topic topic, @JsonProperty("data")String data, @JsonProperty("name")String name) {
        this.topic = topic;
        this.data = data;
        this.name = name;
    }

    //@JsonIgnore
    public void setName(String name) {
        this.name = name;
    }

    //@JsonIgnore
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
