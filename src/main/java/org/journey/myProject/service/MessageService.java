package org.journey.myProject.service;

import org.journey.myProject.domain.Message;
import org.journey.myProject.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
public class MessageService {
    @Autowired
    UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    MessageRepo messageRepo;

    public void deleteMessage(Long messageId) {
        messageRepo.deleteById(messageId);
    }

    public Iterable<Message> getMessages(String filter) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        }
        else messages = messageRepo.findAll();

        return messages;
    }

    public void addMessage(Message message, MultipartFile file) throws IOException {
        safeFile(message, file);
        messageRepo.save(message);
    }

    private void safeFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            message.setFilename(resultFileName);
        }
    }

    public void editMessage(Message message, String text, String tag, MultipartFile file) throws IOException {
        if (!StringUtils.isEmpty(text)) {
            message.setText(text);
        }
        if (!StringUtils.isEmpty(tag)) {
            message.setTag(tag);
        }
        safeFile(message, file);
        messageRepo.save(message);
    }

    public Iterable<Message> findAll() {
        return messageRepo.findAll();
    }
}
