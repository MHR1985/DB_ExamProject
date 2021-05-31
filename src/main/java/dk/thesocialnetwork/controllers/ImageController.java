package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.model.Image;
import dk.thesocialnetwork.model.User;
import dk.thesocialnetwork.repository.UserRepository;
import dk.thesocialnetwork.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.activation.FileTypeMap;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

@Controller
@RequestMapping("/image")
@CrossOrigin
public class ImageController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<byte[]> getActiveImage() throws IOException, URISyntaxException {
        String userName = HelperUtil.getUsernameFromLoggedIn();
        File img = getUserImage(userName);
        if (img == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<byte[]> getImage(@PathVariable String username) throws IOException, URISyntaxException {
        File img = getUserImage(username);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
    }

    public File getUserImage(String username) throws URISyntaxException {
        User user = userRepository.findUserWithUsername(username);
        if (user == null || user.getCurrentImg() == null){
            URL resource = getClass().getClassLoader().getResource("images/anonym.jpg");
            return new File(resource.toURI());
        }
        Image activeImage = user.getCurrentImg();
        File img = new File(activeImage.getUrl());
        return img;
    }

}


