package org.journey.myProject.controller;

import org.journey.myProject.domain.Role;
import org.journey.myProject.domain.User;
import org.journey.myProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Email;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("usersList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUsersList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "usersList";
    }

    @PostMapping("usersList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {

        userService.updateUser(user, username, form);

        return "redirect:/user/usersList";
    }

    @GetMapping("usersList/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("profile")
    public String updateProfile(
            @RequestParam String password,
            @RequestParam String passwordConfirmation,
            @RequestParam @Email String email,
            Model model,
            @AuthenticationPrincipal User user) {
        if(passwordConfirmation == null || password == null) {
            if(password == null) {
                model.addAttribute("passwordError", "Password field can not be empty.");
            }
            if(passwordConfirmation == null) {
                model.addAttribute("passwordConfirmationError", "Password confirmation can not be empty.");
            }
            model.addAttribute("user", user);
            return "profile";
        }
        else if (!password.equals(passwordConfirmation)){
            model.addAttribute("passwordConfirmationError", "Password are not equals.");
            model.addAttribute("user", user);
            return "profile";
        }
        userService.updateProfile(password, email, user);
        model.addAttribute("user", user);
        return "redirect:/user/profile";
    }

    @GetMapping("profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("usersList/{userToDelete}/delete")
    public String deleteUser(@AuthenticationPrincipal User currentUser,
                             @PathVariable User userToDelete) {
        if(currentUser.getRoles().contains(Role.ADMIN) && !userToDelete.getRoles().contains(Role.ADMIN)) {
            userService.deleteUser(userToDelete.getId());
        }
        return "redirect:/user/usersList";
    }

    @GetMapping("{user}/subscrList/{active}")
    public String listOfSubscr(@AuthenticationPrincipal User currentUser,
                                    @PathVariable User user, Model model,
                                    @PathVariable String active) {
        model.addAttribute("subscribers", user.getSubscribers());
        model.addAttribute("subscriptions", user.getSubscriptions());
        model.addAttribute("listOfSubscriptionsCurrentUser", userService.getSubscriptionsList(currentUser.getId()));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("visitedUser", user);
        model.addAttribute("activeTab", active);
        return "subscriptionsList";
    }


    @PostMapping("{user}/unsubscribe")
    public String unsubscribe (@AuthenticationPrincipal User currentUser,
                               @PathVariable User user, Model model,
                               RedirectAttributes redirectAttributes,
                               @RequestHeader(required = false) String referer) {
        userService.unsubscribe(currentUser, user);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

    @PostMapping("{user}/subscribe")
    public String subscribe (@AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                             RedirectAttributes redirectAttributes,
                             @RequestHeader(required = false) String referer) {
        userService.subscribe(currentUser, user);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
}