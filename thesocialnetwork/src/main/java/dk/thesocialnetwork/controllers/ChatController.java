package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.dto.PaginationDTO;
import dk.thesocialnetwork.logic.ChatClient;
import dk.thesocialnetwork.logic.ChatClientHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
@RequestMapping("/chat")
@CrossOrigin
public class ChatController {
    @GetMapping("/send")
    public RedirectView send(@RequestParam(name = "name", required = true, defaultValue = "Martin") String name, @RequestParam(name = "target", required = true, defaultValue = "Muggi") String target, @RequestParam(name = "message", required = false, defaultValue = "Hello World") String message) {
        ChatClientHandler handler = new ChatClientHandler();
        ChatClient client = handler.createClient(name, target);
        client.sendMessage(message);
        return new RedirectView("/chat?name=" + name + "&target=" + target);
    }

    @GetMapping("/notifications")
    public String notifications(@RequestParam(name = "name", required = true, defaultValue = "martin") String name, Model model) {
        ChatClientHandler handler = new ChatClientHandler();
        List<String> notifications = new ArrayList<>();
        notifications.addAll(handler.getNotifications(name));
//        notifications = removeDuplicates(notifications);
        model.addAttribute("name", name);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @GetMapping("")
    public String chat(@RequestParam(name = "name", required = true, defaultValue = "martin") String name, @RequestParam(name = "target", required = true, defaultValue = "muggi") String target, @RequestParam(name = "index", required = false, defaultValue = "0") int index, Model model) {
        ChatClientHandler handler = new ChatClientHandler();
        ChatClient client = handler.createClient(name, target);
        List<String> history = client.getJedisChatHistory(index, 10 );
        Collections.reverse(history);
        model.addAttribute("name", name);
        model.addAttribute("target", target);
        model.addAttribute("history", history);
        model.addAttribute("index", index);
        model.addAttribute("historyLength", history.size());
        return "chat";
    }

    @GetMapping("/pagination")
    public ResponseEntity<List<String>> pagination(@RequestBody PaginationDTO pDTO) {
        try {
            ChatClientHandler handler = new ChatClientHandler();
            ChatClient client = handler.createClient(pDTO.getName(), pDTO.getTarget());
            List<String> history = client.getJedisChatHistory(pDTO.getIndex(), 10 );
            Collections.reverse(history);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
