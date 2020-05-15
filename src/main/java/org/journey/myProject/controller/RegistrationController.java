package org.journey.myProject.controller;

import org.journey.myProject.domain.User;
import org.journey.myProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registrationGet() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("passwordConfirmation") String passwordConfirmation,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        boolean isErrorsPresent = false;

        if(StringUtils.isEmpty(passwordConfirmation)) {
            model.addAttribute("passwordConfirmationError", "Password confirmation can not be empty");
        }
        else if (user.getPassword() != null && !user.getPassword().equals(passwordConfirmation)) {
            isErrorsPresent = true;
            model.addAttribute("passwordConfirmationError", "Passwords are different");
        }

        if(bindingResult.hasErrors()) {
            isErrorsPresent = true;
            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
        }

        if(isErrorsPresent) {
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }

}

