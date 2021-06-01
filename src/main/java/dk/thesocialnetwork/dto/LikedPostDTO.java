package dk.thesocialnetwork.dto;

public class LikedPostDTO {
    private String postId;
    private String likedPerson;

    public LikedPostDTO() {
    }

    public LikedPostDTO(String postId, String likedPerson) {
        this.postId = postId;
        this.likedPerson = likedPerson;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getLikedPerson() {
        return likedPerson;
    }

    public void setLikedPerson(String likedPerson) {
        this.likedPerson = likedPerson;
    }
}
