package com.example.demo.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
@Table(name = "cat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CatEntity {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;
}

