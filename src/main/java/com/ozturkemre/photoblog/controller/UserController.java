package com.ozturkemre.photoblog.controller;


import com.ozturkemre.photoblog.dto.PostDto;
import com.ozturkemre.photoblog.service.PostService;
import com.ozturkemre.photoblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @GetMapping("/get/{username}")
    public ResponseEntity<List<PostDto>> getPostsByUserName(@PathVariable @RequestBody String username) {
        return new ResponseEntity(userService.getUserDetails(username), HttpStatus.OK);
    }

    @PutMapping("/set/{username}/{mode}")
    public ResponseEntity setUserMode(@PathVariable @RequestBody String username, @PathVariable @RequestBody String mode) {
        return userService.setUserMode(username, mode);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity deleteUser(@PathVariable @RequestBody String username) {
        return userService.deleteUser(username);

    }
}