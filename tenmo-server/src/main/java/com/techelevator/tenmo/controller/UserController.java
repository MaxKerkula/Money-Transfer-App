package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/user")
    public List<User> findAll() {
        return userDao.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable int id) {
        return userDao.getUserById(id);
    }

    @GetMapping("/user/username/{username}")//TO DO check path
    public User findByUsername(@PathVariable String username) {
        return userDao.findByUsername(username);
    }

    @GetMapping("/user/list")
    public List<User> findAllExcludingCurrentUser(Principal principal){
        return userDao.findAllExcludingCurrentUser(principal.getName());
    }
}





