package dk.thesocialnetwork.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.*;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "Account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    private String password;

    private Role role;

    public enum Role{
        USER
    }

    private boolean active;
    @OneToOne
    private Image currentImg;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images  = new ArrayList<>();

    public User(Long id, String username, String password, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.active = active;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.active = true;
    }


    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Image getCurrentImg() {
        return currentImg;
    }

    public void setCurrentImg(Image currentImg) {
        this.currentImg = currentImg;
    }
}
