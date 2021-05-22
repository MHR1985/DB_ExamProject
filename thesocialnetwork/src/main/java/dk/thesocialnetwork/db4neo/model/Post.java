package dk.thesocialnetwork.db4neo.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
@Node
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    public String text;

    @Relationship(type = "LIKED")
    private List<Person> likes;

    @Relationship(type = "TAGGED_IN")
    public List<Person> taggedPeople;

    public LocalDateTime timeStamp;

    public Post(){

    }

    public Post(Long id, String text, List<Person> likes, List<Person> taggedPeople, LocalDateTime timeStamp) {
        this.id = id;
        this.text = text;
        this.likes = likes;
        this.taggedPeople = taggedPeople;
        this.timeStamp = timeStamp;
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
}
