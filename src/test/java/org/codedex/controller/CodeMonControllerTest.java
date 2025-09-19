package org.codedex.controller;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonDTO;
import org.codedex.Model.CodeMonTyps;
import org.codedex.Repository.CodeMonRepository;
import org.codedex.service.CodeMonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(CodeMonController.class)
@Import(CodeMonControllerTest.TestConfig.class)
class CodeMonControllerTest {
    //? Med TestConfiguration kan vi skapa en mock av servicen
    //? aka en låtsas version av servicen

    //? Utan denna så kommer inte testet att fungera
    //? Eftersom controllern är beroende av servicen
    @TestConfiguration
    static class TestConfig {
        @Bean
        CodeMonService codeMonService() {
            return Mockito.mock(CodeMonService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CodeMonService codeMonService;

    private CodeMonController controller;
    private CodeMonRepository mockRepo;
    private List<CodeMon> codeMonList;

    private CodeMon TestCodeMonA;
    private CodeMon TestCodeMonB;
    private CodeMon TestCodeMonC;

    @BeforeEach
    void setUp() {
        // Mock-repo och "databaslista"
        codeMonList = new ArrayList<>();
        mockRepo = mock(CodeMonRepository.class);

        TestCodeMonA = new CodeMon();
        TestCodeMonA.setName("TestCodeMonA");
        TestCodeMonA.setType(CodeMonTyps.Compiler);
        TestCodeMonA.setCodeMonGeneration(1);
        TestCodeMonA.setHp(50);
        TestCodeMonA.setAttackdmg(50);

        TestCodeMonB = new CodeMon();
        TestCodeMonB.setName("TestCodeMonB");
        TestCodeMonB.setType(CodeMonTyps.Compiler);
        TestCodeMonB.setCodeMonGeneration(1);
        TestCodeMonB.setHp(50);
        TestCodeMonB.setAttackdmg(50);

        TestCodeMonC = new CodeMon();
        TestCodeMonC.setName("TestCodeMonC");
        TestCodeMonC.setType(CodeMonTyps.Compiler);
        TestCodeMonC.setCodeMonGeneration(1);
        TestCodeMonC.setHp(50);
        TestCodeMonC.setAttackdmg(50);

        codeMonList.add(TestCodeMonA);
        codeMonList.add(TestCodeMonB);
        codeMonList.add(TestCodeMonC);

        // Skapar controller
        controller = new CodeMonController(codeMonService);

        //Mock setup:
        // Mock findAll
        when(mockRepo.findAll()).thenReturn(new ArrayList<>(codeMonList));

        // Mock findById
        when(mockRepo.findById(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            return codeMonList.stream()
                    .filter(cm -> id.equals(cm.getName()))
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

    }

    @Test
    void testGetAll() {
        // Mockar service och den returnerar codeMonList
        when(codeMonService.getAll()).thenReturn(codeMonList);

        // Anropa controllerns metod
        List<CodeMon> result = controller.getAll().getBody();

        // Assertions
        assertEquals(3, result.size());
        assertEquals("TestCodeMonA", result.get(0).getName());
        assertEquals("TestCodeMonB", result.get(1).getName());
        assertEquals("TestCodeMonC", result.get(2).getName());
    }

    @Test
    void testFindCodeMonByIdExists() {
        // Mockar service så att den returnerar testCodeMonA med ID 1
        when(codeMonService.findCodeMonbyID("1")).thenReturn(TestCodeMonA);

        CodeMon response = codeMonService.findCodeMonbyID("1");

        assertNotNull(response);
        assertEquals("TestCodeMonA", response.getName());
    }


    @Test
    void testFindCodeMonByIdNotExists() {
        // Mocka service
        when(codeMonService.findCodeMonbyID("1000"))
                .thenThrow(new UsernameNotFoundException("Not found"));

        // Anropa service direkt
        assertThrows(UsernameNotFoundException.class, () -> {
            codeMonService.findCodeMonbyID("1000");
        });
    }

    @Test
    void testSave() throws Exception {

        CodeMonDTO newCodeMon = new CodeMonDTO("Mockmon", CodeMonTyps.Compiler, 1, 90, 35, null);


        CodeMon saved = new CodeMon();
        saved.setName("Mockmon");
        saved.setType(CodeMonTyps.Compiler);
        saved.setCodeMonGeneration(1);
        saved.setHp(90);
        saved.setAttackdmg(35);

        when(codeMonService.save(any(CodeMonDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/codemons/") // byt till din endpoint-path
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newCodeMon)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mockmon"))
                .andExpect(jsonPath("$.type").value(CodeMonTyps.Compiler.name()))
                .andExpect(jsonPath("$.hp").value(90))
                .andExpect(jsonPath("$.attackdmg").value(35));
    }

    @Test
    void testDeleteCodeMonFound() {
        // Ser till att listan innehåller testCodeMonA
        assertTrue(codeMonList.contains(TestCodeMonA));

        // mocka deleteCodeMon så att testCodeMonA tas bort ur listan
        doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            codeMonList.removeIf(cm -> cm.getName().equals("TestCodeMonA") && id.equals("1"));
            return null;
        }).when(codeMonService).deleteCodeMon("1");

        ResponseEntity<Void> response = controller.deleteACodMon("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(codeMonList.contains(TestCodeMonA));
    }


    @Test
    void testDeleteCodeMonNotFound() {
        // mocka att service kastar exception när id inte finns
        doThrow(new UsernameNotFoundException("Not found"))
                .when(codeMonService).deleteCodeMon("100");

        // act
        ResponseEntity<Void> response = controller.deleteACodMon("100");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
