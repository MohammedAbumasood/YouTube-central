package com.youtube.central.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;
    @ManyToOne
    AppUser user;
    String description;
    String name;
    Double watchHours;
    boolean isMonetized;
    int totalViews;
    int totalLikeCount;
    int totalSubs;
    @OneToMany
    List<AppUser> subscribers;
    @OneToMany
    List<Video> videos;
    @OneToMany
    List<PlayList> PlayLists;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
