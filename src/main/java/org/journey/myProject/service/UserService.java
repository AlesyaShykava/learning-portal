package org.journey.myProject.service;

import org.journey.myProject.domain.Role;
import org.journey.myProject.domain.User;
import org.journey.myProject.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws AuthenticationException {
        User userFromDB = userRepo.findByUsername(username);

        if(userFromDB == null) {
            throw new AuthenticationNotFoundException("Invalid credentials");
        }

        return userFromDB;
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepo.save(user);
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepo.save(user);
    }

    public List<User> getSubscriptionsList(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        User user1 = userOptional.get();
        List<User> list = new ArrayList<>();
        for(User user : user1.getSubscriptions()) {
            list.add(user);
        }
        return list;
    }

    public static class AuthenticationNotFoundException extends AuthenticationException {
        public AuthenticationNotFoundException(String message) {
            super(message);
        }
    }

    public boolean addUser(User user) {
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            sendActivationLink(user);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if(user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public void updateUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if(roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public void updateProfile(String password, String email, User user) {
        if(!password.equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(password));
        }

        boolean isEmailUpdated = (email != null && !email.equals(user.getEmail())) || user.getEmail() != null && !user.getEmail().equals(email);

        if(isEmailUpdated) {
            user.setEmail(email);
            if(!StringUtils.isEmpty(user.getEmail())) {
                user.setActivationCode(UUID.randomUUID().toString());
                sendActivationLink(user);
            }
            user.setActive(false);
        }

        userRepo.save(user);
    }

    private void sendActivationLink(User user) {
        String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Journey, visit next link: http://localhost:8080/activate/%s",
                user.getUsername(),
                user.getActivationCode()
        );

        mailSender.send(user.getEmail(), "Activation code", message);
    }

    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }
}
