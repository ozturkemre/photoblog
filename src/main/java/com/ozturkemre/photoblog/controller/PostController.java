package com.ozturkemre.photoblog.controller;


import com.ozturkemre.photoblog.dto.PostDto;
import com.ozturkemre.photoblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostDto postDto) {
        postService.createPost(postDto);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> showAllPosts() {
        return new ResponseEntity<>(postService.showAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PostDto> getSinglePost(@PathVariable @RequestBody Long id) {
        return new ResponseEntity<>(postService.getSinglePost(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable @RequestBody Long id) {
        postService.deletePostById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity editSinglePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        postService.editSinglePost(id, postDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
