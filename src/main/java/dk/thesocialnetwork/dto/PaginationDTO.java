package dk.thesocialnetwork.dto;

public class PaginationDTO {
    int index;
    String name;
    String target;

    public PaginationDTO() {
    }

    public PaginationDTO(int index, String name, String target) {
        this.index = index;
        this.name = name;
        this.target = target;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
