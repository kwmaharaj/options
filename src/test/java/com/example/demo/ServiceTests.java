package com.example.demo;

import com.example.demo.exceptions.RecordNotFoundException;
import com.example.demo.mapper.CatMapper;
import com.example.demo.model.Cat;
import com.example.demo.repo.CatEntity;
import com.example.demo.repo.CatRepo;
import com.example.demo.service.CatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ServiceTests {

    @Autowired
    private CatService catService;

    @MockBean
    private CatRepo mockCatRepo;

    @Test
    void createCat_success() throws Exception {
        //Some setup
        String catNameToCreate = "GreenServiceCat";
        CatEntity catEntityMock = new CatEntity(UUID.randomUUID(), catNameToCreate);//mock repo, can be in a setup.

        //Mock repo
        when(this.mockCatRepo.save(any())).thenReturn(catEntityMock);

        //Test
        Cat createdCat = this.catService.createCat(catNameToCreate);

        //Assert
        assertNotNull(createdCat);
        assertEquals(catEntityMock.getName(), createdCat.getName());
        verify(this.mockCatRepo, times(1)).save(any(CatEntity.class));
    }

    @Test
    void getCatById_found() {
        //Some setup
        String catNameToFind = "FindMeCat";
        CatEntity catEntityMock = new CatEntity(UUID.randomUUID(), catNameToFind);//mock repo, can be in a setup.

        //Mocks
        when(this.mockCatRepo.findById(any(UUID.class))).thenReturn(Optional.of(catEntityMock));

        //Test
        Cat foundCat = this.catService.getCatById(catEntityMock.getId());

        //Assert
        assertNotNull(foundCat);
        assertEquals(catEntityMock.getName(), foundCat.getName());
        verify(this.mockCatRepo, times(1)).findById(catEntityMock.getId());
    }



    @Test
    void getCatById_notFound() {
        //Some setup
        String catNameToFind = "FindMeNotCat";
        CatEntity catEntityMock = new CatEntity(UUID.randomUUID(), catNameToFind);//mock repo, can be in a setup.

        //Mock
        when(this.mockCatRepo.findById(any(UUID.class))).thenReturn(Optional.empty());

        //assert
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            this.catService.getCatById(catEntityMock.getId());
        });
        assertEquals("Cat not found.", exception.getMessage());
        verify(this.mockCatRepo, times(1)).findById(catEntityMock.getId());
    }


    @Test
    void updateCat_success() {
        UUID catId = UUID.randomUUID();

        //Some setup
        String oldCatNameToUpdate = "OldCat";
        CatEntity oldCatEntityMock = new CatEntity(catId, oldCatNameToUpdate);

        String newCatNameToUpdate = "NewCat";
        CatEntity newCatEntityMock = new CatEntity(catId, newCatNameToUpdate);

        Cat catUpdateRequest = CatMapper.INSTANCE.toCat(newCatEntityMock);

        //mock
        when(this.mockCatRepo.findById(any(UUID.class))).thenReturn(Optional.of(oldCatEntityMock));
        when(this.mockCatRepo.save(any(CatEntity.class))).thenReturn(newCatEntityMock);

        //Test
        Cat updatedCat = this.catService.updateCat(catUpdateRequest);

        //Assert
        assertNotNull(updatedCat);
        assertEquals(catUpdateRequest.getName(), updatedCat.getName());
        verify(this.mockCatRepo, times(1)).findById(catId);
        verify(this.mockCatRepo, times(1)).save(any(CatEntity.class));
    }


    @Test
    void updateCat_notFound() {
        //Some setup
        UUID catId = UUID.randomUUID();
        Cat catUpdateRequest = CatMapper.INSTANCE.toCat(CatEntity.builder()
                .id(catId)
                .name("UpdatedCatName")
                .build()
        );

        //mock
        when(mockCatRepo.findById(any(UUID.class))).thenReturn(Optional.empty());

        //Test
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            catService.updateCat(catUpdateRequest);
        });

        //assert
        assertEquals("Cat not found. Cannot update non-existing entity.", exception.getMessage());
        verify(mockCatRepo, times(1)).findById(catId);
        verify(mockCatRepo, never()).save(any(CatEntity.class));
    }


    @Test
    void deleteCat_success() {
        UUID catId = UUID.randomUUID();

        //Some setup
        String catToDeleteName = "DeleteThisCat";
        CatEntity catEntityToDelete = new CatEntity(catId, catToDeleteName);

        //Mocks
        when(mockCatRepo.findById(any(UUID.class))).thenReturn(Optional.of(catEntityToDelete));

        //tst
        String response = catService.deleteCat(catEntityToDelete.getId());

        //assert
        assertEquals("Cat id(" + catEntityToDelete.getId() + ") is deleted", response);
        verify(mockCatRepo, times(1)).findById(catEntityToDelete.getId());
        verify(mockCatRepo, times(1)).deleteById(catEntityToDelete.getId());
    }


    @Test
    void deleteCat_notFound() {
        //setup
        UUID catId = UUID.randomUUID();

        //mocks
        when(mockCatRepo.findById(any(UUID.class))).thenReturn(Optional.empty());

        //test
        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            catService.deleteCat(catId);
        });

        //assert
        assertEquals("Cat not found. Cannot delete non-existing entity.", exception.getMessage());
        verify(mockCatRepo, times(1)).findById(catId);
        verify(mockCatRepo, never()).deleteById(catId);
    }
}