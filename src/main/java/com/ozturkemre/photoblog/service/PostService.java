package com.ozturkemre.photoblog.service;

import com.ozturkemre.photoblog.dto.PostDto;
import com.ozturkemre.photoblog.exception.PostNotFoundException;
import com.ozturkemre.photoblog.model.Post;
import com.ozturkemre.photoblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;


    public List<PostDto> showAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setPic(post.getPic());
        postDto.setUsername(post.getUsername());
        postDto.setRole(post.getRole());
        return postDto;
    }

    public void createPost(PostDto postDto) {
        Post post = mapFromDtoToPost(postDto);
        postRepository.save(post);
    }

    private Post mapFromDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        User loggedInUser = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user logged in"));
        post.setPic(postDto.getPic());
        post.setUsername(loggedInUser.getUsername());
        post.setCreatedOn(Instant.now());
        post.setRole(loggedInUser.getAuthorities().iterator().next().toString());
        return post;
    }


    public PostDto getSinglePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        return mapFromPostToDto(post);
    }

    public void deletePostById(Long id) {
        User loggedInUser = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user logged in"));

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        if (post.getUsername().equals(loggedInUser.getUsername()) ||
                loggedInUser.getAuthorities().iterator().next().toString().equals("ROLE_ADMIN")) {
            postRepository.deleteById(id);
        }
    }

    public void editSinglePost(Long id, PostDto postDto) {
        User loggedInUser = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user logged in"));
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        if (post.getUsername().equals(loggedInUser.getUsername())) {
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setPic(postDto.getPic());
            post.setUpdatedOn(Instant.now());
        }
        postRepository.save(post);


    }

    public List<PostDto> getPostsByUserName(String username) {
        List<Post> posts = postRepository.getPostsByUsername(username);
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }


}
