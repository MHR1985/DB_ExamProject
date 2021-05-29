package dk.thesocialnetwork.dto;

import java.util.List;

public class CreatePostDTO {

    private String text;

    public CreatePostDTO(String text) {
        this.text = text;
    }

    public CreatePostDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
