package com.example.business.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//リレーション    
    @ManyToOne
    private Tweet tweet;

    @ManyToOne
    private User user;

    //tweetプロパティのゲッターとセッターを追加
    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

    //userプロパティのゲッターとセッターを追加
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
