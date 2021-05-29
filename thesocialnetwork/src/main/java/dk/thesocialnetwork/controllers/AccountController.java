package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.model.Image;
import dk.thesocialnetwork.model.User;
import dk.thesocialnetwork.repository.ImageRepository;
import dk.thesocialnetwork.repository.UserRepository;
import dk.thesocialnetwork.util.HelperUtil;
import org.apache.coyote.Response;
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

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String getProfileView(Model model){
        String username = HelperUtil.getUsernameFromLoggedIn();
        User user = userRepository.findUserWithUsername(username);
        model.addAttribute("images", user.getImages());
        model.addAttribute("currentImage", user.getCurrentImg());
        return "profile";
    }

    @PostMapping(path="", consumes={"multipart/form-data"})
    public ResponseEntity uploadImg(@RequestParam("filename") MultipartFile multipart, @RequestParam("imgTitle") String imgTitle) throws IOException {
        if(!multipart.getContentType().equals("image/jpeg") && !multipart.getContentType().equals("image/png") && !multipart.getContentType().equals("image/gif"))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        File directory = new File(System.getProperty("user.dir"));

        String filename = multipart.getOriginalFilename();
        // Needs some way to make sure filename will be unique.
        int fileCount=directory.list().length+1;
        String path = directory + "/images/"+ fileCount + "_" + filename;
        path = path.replace("\\", "/");

        try {
            Image img = new Image(imgTitle, path);
            String username = HelperUtil.getUsernameFromLoggedIn();
            User user = userRepository.findUserWithUsername(username);
            user.getImages().add(img);
            user.setCurrentImg(img);
            userRepository.save(user);
            multipart.transferTo(new File(path));
            return new ResponseEntity( HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
