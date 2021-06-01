package dk.thesocialnetwork.dto;

public class FollowsDTO {
    public String follower;
    public String target;

    public FollowsDTO(String follower, String target) {
        this.follower = follower;
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
