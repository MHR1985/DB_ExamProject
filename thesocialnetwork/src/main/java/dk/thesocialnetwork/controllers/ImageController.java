package dk.thesocialnetwork.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.activation.FileTypeMap;
import java.io.*;
import java.nio.file.Files;

@Controller
@RequestMapping("/image")
@CrossOrigin
public class ImageController {


   @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        File img = new File("thesocialnetwork/images/"+fileName);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
    }


    //Use first line to save file to path, delete method after use
    public void saveFileToRelative(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        File test = new File("thesocialnetwork/images/"+fileName);
        PrintWriter writer = new PrintWriter(test, "UTF-8");
        writer.println("The first line");
        writer.println("The second line");
        writer.close();

    }
}


