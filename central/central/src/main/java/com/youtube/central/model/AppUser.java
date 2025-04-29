package com.youtube.central.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity  //Helps to create table in database
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;
    String name;
    @Column(unique = true)
    String email;
    String password;
    @Column(unique = true)
    Long PhoneNumber;
    LocalDate dob;
    String gender;
    String country;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
