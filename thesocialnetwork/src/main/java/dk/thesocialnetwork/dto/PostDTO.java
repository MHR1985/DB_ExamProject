package dk.thesocialnetwork.dto;

import dk.thesocialnetwork.model.Person;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.List;

public class PostDTO {


    private Long id;

    private String text;

    private List<Person> likes;

    private List<Person> taggedPeople;

    private LocalDateTime timeStamp;

    private String author;

    public PostDTO(Long id, String text, List<Person> likes, List<Person> taggedPeople, LocalDateTime timeStamp, String author) {
        this.id = id;
        this.text = text;
        this.likes = likes;
        this.taggedPeople = taggedPeople;
        this.timeStamp = timeStamp;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Person> getLikes() {
        return likes;
    }

    public void setLikes(List<Person> likes) {
        this.likes = likes;
    }

    public List<Person> getTaggedPeople() {
        return taggedPeople;
    }

    public void setTaggedPeople(List<Person> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
