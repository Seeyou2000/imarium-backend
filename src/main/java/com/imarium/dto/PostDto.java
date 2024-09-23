package com.imarium.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String authorEmail;

    public PostDto(Long id, String title, String content, String authorEmail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
    }
}

