package org.codedex.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.codedex.Model.CodeMon;
import org.codedex.Repository.CodeMonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CodeMonControllerTest {

    private CodeMonController controller;
    private CodeMonRepository mockRepo;
    private List<CodeMon> codeMonList;

    private CodeMon codeMon1;
    private CodeMon codeMon2;

    @BeforeEach
    void setUp() {
        // Mock-repo och "databaslista"
        codeMonList = new ArrayList<>();
        mockRepo = mock(CodeMonRepository.class);

        // Skapar testobjekt
        codeMon1 = new CodeMon();
        codeMon1.setName("Testachu");
        codeMon1.setType("Int");
        codeMon1.setHp(100);
        codeMon1.setAttackdmg(30);
        codeMonList.add(codeMon1);

        codeMon2 = new CodeMon();
        codeMon2.setName("JUnitail");
        codeMon2.setType("String");
        codeMon2.setHp(80);
        codeMon2.setAttackdmg(25);
        codeMonList.add(codeMon2);

        // Mock findAll
        when(mockRepo.findAll()).thenReturn(new ArrayList<>(codeMonList));

        // Mock findById
        when(mockRepo.findById(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return codeMonList.stream()
                    .filter(cm -> id.equals(cm.getName())) // använder namn som "id" här för enkelhet
                    .findFirst();
        });

        // Mock save
        when(mockRepo.save(any(CodeMon.class))).thenAnswer(invocation -> {
            CodeMon cm = invocation.getArgument(0);
            if (!codeMonList.contains(cm)) codeMonList.add(cm);
            return cm;
        });

        // Mock delete
        doAnswer(invocation -> {
            CodeMon cm = invocation.getArgument(0);
            codeMonList.remove(cm);
            return null;
        }).when(mockRepo).delete(any(CodeMon.class));

        // Skapar controller
        controller = new CodeMonController(mockRepo);
    }

    @Test
    void testGetAll() {
        List<CodeMon> result = controller.getAll();

        assertEquals(2, result.size());
        assertEquals("Testachu", result.get(0).getName());
        assertEquals("JUnitail", result.get(1).getName());
    }

    @Test
    void testFindCodeMonByIdExists() {
        ResponseEntity<CodeMon> response = controller.findJavaMonbyID("Testachu");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Testachu", response.getBody().getName());
    }

    @Test
    void testFindCodeMonByIdNotExists() {
        ResponseEntity<CodeMon> response = controller.findJavaMonbyID("DoesNotExist");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSave() {
        CodeMon newCodeMon = new CodeMon();
        newCodeMon.setName("Mockmon");
        newCodeMon.setType("Int");
        newCodeMon.setHp(90);
        newCodeMon.setAttackdmg(20);

        ResponseEntity<CodeMon> response = controller.save(newCodeMon);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mockmon", response.getBody().getName());
    }

    @Test
    void testDeleteCodeMonFound() {
        ResponseEntity<Void> response = controller.deleteAStudent("Testachu");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(codeMonList.contains(codeMon1));
    }

    @Test
    void testDeleteCodeMonNotFound() {
        ResponseEntity<Void> response = controller.deleteAStudent("DoesNotExist");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
