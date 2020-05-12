package com.ozturkemre.photoblog.service;

import com.ozturkemre.photoblog.dto.PostDto;
import com.ozturkemre.photoblog.exception.PostNotFoundException;
import com.ozturkemre.photoblog.model.Post;
import com.ozturkemre.photoblog.repository.PostRepository;
import com.ozturkemre.photoblog.repository.UserRepository;
import com.ozturkemre.photoblog.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public ResponseEntity setUserMode(String username, String mode) {
        if (isAdmin()) {
            com.ozturkemre.photoblog.model.User user = userRepository.findByUserName(username).orElseThrow(() ->
                    new UsernameNotFoundException("No user name found named " + username));
            user.setRole(mode);
            userRepository.save(user);
            List<PostDto> posts = postService.getPostsByUserName(username);
            posts.stream().forEach((p) -> {
                Post post = postRepository.findById(p.getId()).orElseThrow(() -> new PostNotFoundException("Post not found for id: " + p.getId()));
                post.setRole(mode);
                postRepository.save(post);
            });
            return ResponseEntity.ok(new MessageResponse("User " + username + "'s mode successfully changed to " + mode));
        }
        return ResponseEntity.ok(new MessageResponse("You have not permission to change someone's mode!"));
    }

    public ResponseEntity deleteUser(String username) {
        if (isAdmin()) {
            com.ozturkemre.photoblog.model.User user = userRepository.findByUserName(username).orElseThrow(() ->
                    new UsernameNotFoundException("No user name found named " + username));
            userRepository.delete(user);
            return ResponseEntity.ok(new MessageResponse("User " + username + " is successfully deleted"));

        }
        return ResponseEntity.ok(new MessageResponse("You have not permission to delete user"));
    }

    public boolean isAdmin() {
        User loggedInUser = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user logged in"));
        return loggedInUser.getAuthorities().iterator().next().toString().equals("ROLE_ADMIN");
    }

    public boolean isMod() {
        User loggedInUser = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user logged in"));
        return loggedInUser.getAuthorities().iterator().next().toString().equals("ROLE_MODERATOR");
    }

    public HashMap getUserDetails(String username) {
        HashMap<String, Object> userDetails = new HashMap<String, Object>();
        com.ozturkemre.photoblog.model.User user = userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("No user name found named " + username));
        com.ozturkemre.photoblog.model.User user1 = new com.ozturkemre.photoblog.model.User();
        user1.setId(user.getId());
        user1.setUserName(user.getUserName());
        user1.setRole(user.getRole());
        user1.setAvatar(user.getAvatar());
        userDetails.put("user", user1);
        List<PostDto> posts = postService.getPostsByUserName(username);
        userDetails.put("posts", posts);
        return userDetails;

    }
}
