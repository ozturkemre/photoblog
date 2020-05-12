package com.ozturkemre.photoblog.repository;

import com.ozturkemre.photoblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getPostsByUsername(String username);
}
