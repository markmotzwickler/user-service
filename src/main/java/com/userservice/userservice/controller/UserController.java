package com.userservice.userservice.controller;

import com.userservice.userservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Controller
public class UserController {

    public UserController() {
        super();
    }

    // API - read
    @PreAuthorize("#oauth2.hasScope('user') and #oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    @ResponseBody
    public User findById(@PathVariable final long id) {
        return new User(Integer.parseInt(randomNumeric(2)), "Mark Motzwickler", "motzwickler.mark@gmail.com");
    }

    @PreAuthorize("#oauth2.hasScope('user') and #oauth2.hasScope('read')")
    @GetMapping(value = "/users")
    public List<User> findUsers() {
        User user1 = new User(1, "Mark Motzwickler", "motzwickler.mark@gmail.com");
        User user2 = new User(2, "Tony Stark", "tony.stark@starkindustries.com");
        return List.of(user1, user2);
    }

    // API - write
    @PreAuthorize("#oauth2.hasScope('user') and #oauth2.hasScope('write')")
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User create(@RequestBody final User user) {
        user.setId(Integer.parseInt(randomNumeric(2)));
        return user;
    }

    @PreAuthorize("#oauth2.hasScope('read')")
    @RequestMapping(method = RequestMethod.GET, value = "/users/extra")
    @ResponseBody
    public Map<String, Object> getExtraInfo(Authentication auth) {
        OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
        Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
        System.out.println("User organization is " + details.get("organization"));
        return details;
    }

}
