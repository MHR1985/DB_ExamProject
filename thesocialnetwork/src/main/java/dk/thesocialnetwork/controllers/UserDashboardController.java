package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.chat.implementation.ChatClient;
import dk.thesocialnetwork.chat.implementation.ChatClientHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserDashboardController {
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(name = "name", required = false, defaultValue = "TheDude") String name, Model model) {


        model.addAttribute("name", name);
        return "dashboard";
    }
}
