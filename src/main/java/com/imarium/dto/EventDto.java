package com.imarium.dto;

import lombok.Data;

@Data
public class EventDto {
    private Long id;
    private String content;
    private String date;

    public EventDto(Long id, String content, String date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }
}
