package com.example.business.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.business.domain.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	List<Tweet> findAllByOrderByIdDesc(); //降順で全てのツイートを取得する
	
	Page<Tweet> findAllByOrderByIdDesc(Pageable pageable);
}
