package dk.thesocialnetwork.controllers;

import dk.thesocialnetwork.util.HelperUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@CrossOrigin
public class UserDashboardController {
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(name = "name", required = true, defaultValue = "martin") String name, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if(!name.equals(userName))
            return "redirect:/login";
        model.addAttribute("name", name);
        return "dashboard";
    }

    @GetMapping("")
    public String getHomePage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if (HelperUtil.isAuthenticated()) {
            return "redirect:/dashboard?name="+userName;
        }
        return "redirect:/login";
    }
}
