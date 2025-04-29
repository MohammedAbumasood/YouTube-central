package com.youtube.central.dto;

import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideoDetailsDto {
    String id;
    String name;
    String description;
    LocalDateTime uploadDateTime;
    LocalDateTime updateAt;
    String videoLink;
    List<String> tags;
}
