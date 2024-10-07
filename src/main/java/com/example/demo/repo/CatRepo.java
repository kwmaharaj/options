package com.example.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CatRepo extends JpaRepository<CatEntity, UUID> {

}
