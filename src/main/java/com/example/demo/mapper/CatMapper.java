package com.example.demo.mapper;

import com.example.demo.model.Cat;
import com.example.demo.repo.CatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CatMapper {
    CatMapper INSTANCE = Mappers.getMapper(CatMapper.class);

    CatEntity toCatEntity(Cat cat);
    Cat toCat(CatEntity catEntity);

}
