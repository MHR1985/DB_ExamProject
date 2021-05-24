package dk.thesocialnetwork.dto;

import java.util.List;

public class CreatePostDTO {

    private String text;

    private String author;

    private List<String> taggedPeople;

    public CreatePostDTO(String text, String author, List<String> taggedPeople) {
        this.text = text;
        this.author = author;
        this.taggedPeople = taggedPeople;
    }

    public CreatePostDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getTaggedPeople() {
        return taggedPeople;
    }

    public void setTaggedPeople(List<String> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }
}
