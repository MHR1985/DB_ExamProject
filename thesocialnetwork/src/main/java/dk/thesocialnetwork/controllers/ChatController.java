package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.logic.ChatClient;
import dk.thesocialnetwork.logic.ChatClientHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String chat(@RequestParam(name = "name", required = true, defaultValue = "martin") String name, @RequestParam(name = "target", required = true, defaultValue = "muggi") String target, Model model) {
        ChatClientHandler handler = new ChatClientHandler();
        ChatClient client = handler.createClient(name, target);
        List<String> history = client.getJedisChatHistory();
        Collections.reverse(history);
        model.addAttribute("name", name);
        model.addAttribute("target", target);
        model.addAttribute("history", history);
        return "chat";
    }
}
