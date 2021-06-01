package dk.thesocialnetwork.dto;

public class AjaxDTO {
    private String failed;
    private String error;
    private String success;
    private Object model;

    public void setFailed(String failed) {
        this.failed = failed;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
