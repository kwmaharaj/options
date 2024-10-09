package com.example.demo.service;

import com.example.demo.exceptions.RecordNotFoundException;
import com.example.demo.mapper.CatMapper;
import com.example.demo.model.Cat;
import com.example.demo.repo.CatEntity;
import com.example.demo.repo.CatRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatService {
    private final CatRepo catRepo;

    public Cat createCat(String name) {
        CatEntity catEntity = catRepo.save(new CatEntity(UUID.randomUUID(), name));
        return CatMapper.INSTANCE.toCat(catEntity);
    }

    public Cat getCatById(UUID id){
        CatEntity catEntity = catRepo.findById(id).orElseThrow(() -> new RecordNotFoundException("Cat not found."));
        return CatMapper.INSTANCE.toCat(catEntity);
    }

    public Cat updateCat(Cat cat){
        catRepo.findById(cat.getId()).orElseThrow(() -> new RecordNotFoundException("Cat not found. Cannot update non-existing entity."));
        CatEntity catEntity = CatMapper.INSTANCE.toCatEntity(cat);
        catRepo.save(catEntity);
        return CatMapper.INSTANCE.toCat(catEntity);
    }

    public String deleteCat(UUID id){
        catRepo.findById(id).orElseThrow(() -> new RecordNotFoundException("Cat not found. Cannot delete non-existing entity."));
        catRepo.deleteById(id);
        return "Cat id(%s) is deleted".formatted(id);
    }
}

