package com.youtube.central.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Videos")
public class Video {

    @Id
    String id;  // This id will be generated inside firebase
    String name;
    String description;
    LocalDateTime uploadDateTime;
    LocalDateTime updateAt;
    String videoLink;
    String thumbnailLink;
    @OneToMany
    List<Tag> tags;

}
