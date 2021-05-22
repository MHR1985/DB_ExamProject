package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.chat.implementation.ChatClient;
import dk.thesocialnetwork.chat.implementation.ChatClientHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
public class ChatController {
    @GetMapping("/chat/send")
    public RedirectView send(@RequestParam(name = "name", required = false, defaultValue = "TheDude") String name, @RequestParam(name = "target", required = false, defaultValue = "Duderino") String target,@RequestParam(name = "message", required = false, defaultValue = "Hello World") String message, Model model) {
        ChatClientHandler handler = new ChatClientHandler();
        ChatClient client = handler.createClient(name,target);
        client.sendMessage(message);

        model.addAttribute("name", name);
        model.addAttribute("target", target);
        model.addAttribute("message", message);

        return new RedirectView("/chat?name="+name+"&target=" + target);
    }

    @GetMapping("/chat/notifications")
    public String notifications(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        ChatClientHandler handler = new ChatClientHandler();
        List<String> notifications = handler.getNotifications(name);
        notifications = removeDuplicates(notifications);
        model.addAttribute("name",name);
        model.addAttribute("notifications", notifications);

        return "notifications";
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "name", required = false, defaultValue = "TheDude") String name, @RequestParam(name = "target", required = false, defaultValue = "Duderino") String target, Model model) {
        ChatClientHandler handler = new ChatClientHandler();
        ChatClient client = handler.createClient(name,target);
        List<String> history = client.getJedisChatHistory();
        Collections.reverse(history);
        model.addAttribute("name", name);
        model.addAttribute("target", target);
        model.addAttribute("history",history);
        return "chat";
    }

    private List<String> removeDuplicates(List<String> list){
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
