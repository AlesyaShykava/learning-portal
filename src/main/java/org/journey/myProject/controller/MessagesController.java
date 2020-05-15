package org.journey.myProject.controller;

import org.journey.myProject.domain.Message;
import org.journey.myProject.domain.User;
import org.journey.myProject.repos.UserRepo;
import org.journey.myProject.service.MessageService;
import org.journey.myProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/messages")
public class MessagesController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping
    public String getMessages(
            @RequestParam(required = false, defaultValue = "")
                    String filter,
            Model model) {

        Iterable<Message> messages = messageService.getMessages(filter);
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "allMessages";
    }

    @PostMapping
    public String addMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        message.setAuthor(user);

        if(bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
            model.addAttribute("message", message);
        } else {
            messageService.addMessage(message, file);
            model.addAttribute("message", null);

        }
        model.addAttribute("messages", messageService.findAll());

        return "allMessages";
    }


    @GetMapping("{user}")
    public String getUserMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message) {
        Set<Message> messages = user.getMessages();

        if(currentUser.equals(user)) {
            model.addAttribute("showEditButtons", "true");
            if(message != null) {
                model.addAttribute("showEditForm", "true");
            }
        }

        if(!currentUser.equals(user)) {
            if(user.getSubscribers().contains(currentUser)) {
                model.addAttribute("subscriptionButton", "unsubscribe");
            }
            else model.addAttribute("subscriptionButton", "subscribe");
        }

        model.addAttribute("userVisited", user);
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);

        return "userMessages";
    }

    @PostMapping("{user}/{id}/edit")
    public String editMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") Message message
    ) throws IOException {

        if(user.equals(currentUser)) {
            messageService.editMessage(message, text, tag, file);
        }

        return "redirect:/messages/" + user.getId();
    }

    @PostMapping("{user}/{messageId}/delete")
    public String deleteMessageMappingUserMessages(@AuthenticationPrincipal User currentUser,
                                                   @PathVariable User user,
                                                   @PathVariable Long messageId,
                                                   RedirectAttributes redirectAttributes,
                                                   @RequestHeader(required = false) String referer) {
        if(currentUser.equals(user)) {
            messageService.deleteMessage(messageId);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                    .entrySet()
                    .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

}
