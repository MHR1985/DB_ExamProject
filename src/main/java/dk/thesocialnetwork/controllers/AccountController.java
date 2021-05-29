package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.model.Image;
import dk.thesocialnetwork.model.User;
import dk.thesocialnetwork.repository.ImageRepository;
import dk.thesocialnetwork.repository.UserRepository;
import dk.thesocialnetwork.util.HelperUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Controller
@CrossOrigin
@RequestMapping("/profile")
public class AccountController {


    UserRepository userRepository;

    ImageRepository imageRepository;

    public AccountController(UserRepository userRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping("")
    public String getProfileView(Model model){
        String username = HelperUtil.getUsernameFromLoggedIn();
        if(username == null || username.equals("anonymousUser"))
            return "redirect:/login";
        User user = userRepository.findUserWithUsername(username);
        model.addAttribute("images", user.getImages());
        model.addAttribute("currentImage", user.getCurrentImg());
        return "profile";
    }

    @PostMapping("/picture")
    public String changeProfilePicture(@RequestParam("imageTitle") String imageTitle){
        String username = HelperUtil.getUsernameFromLoggedIn();
        User user = userRepository.findUserWithUsername(username);
        Image image = imageRepository.findImageWithUsername(username, imageTitle);
        if (image == null)
            return "redirect:profile";
        user.setCurrentImg(image);
        userRepository.save(user);
        return "redirect:";
    }

    @PostMapping(path="", consumes={"multipart/form-data"})
    public ResponseEntity uploadImg(@RequestParam("filename") MultipartFile multipart, @RequestParam("imgTitle") String imgTitle) throws IOException {
        if(!multipart.getContentType().equals("image/jpeg") && !multipart.getContentType().equals("image/png") && !multipart.getContentType().equals("image/gif"))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        String username = HelperUtil.getUsernameFromLoggedIn();
        Image previousImage = imageRepository.findImageWithUsername(username,imgTitle);
        if (previousImage != null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        File directory = new File("images/");

        String filename = multipart.getOriginalFilename();
        // Needs some way to make sure filename will be unique.
        int fileCount=directory.list().length+1;
        String path = "images/" + fileCount + "_" + filename;

        path = path.replace("\\", "/");

        try {
            Image img = new Image(imgTitle, path);
            User user = userRepository.findUserWithUsername(username);
            user.getImages().add(img);
            user.setCurrentImg(img);
            userRepository.save(user);
            String serverPath = System.getProperty("user.dir") + "\\" + path;
            serverPath = serverPath.replace("\\", "/");
            multipart.transferTo(new File(serverPath));
            return new ResponseEntity( HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
