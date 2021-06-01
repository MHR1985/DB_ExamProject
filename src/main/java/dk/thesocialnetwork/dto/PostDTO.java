package dk.thesocialnetwork.dto;

import java.util.List;

public class PostDTO {
    private String id;
    private String text;
    private List<String> taggedPeople;
    private String timeStamp;
    private String author;

    public PostDTO(String id, String text, List<String> taggedPeople, String timeStamp, String author) {
        this.id = id;
        this.text = text;
        this.taggedPeople = taggedPeople;
        this.timeStamp = timeStamp;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
