package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.Post;
import com.mypictureapp.mypicture.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {

    Page<Post> findByPosterOrderByCreatedAtDesc(User poster, Pageable pageable);

}
