package org.codedex.controller;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonDTO;
import org.codedex.Repository.CodeMonRepository;
import org.codedex.service.CodeMonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private CodeMon testCodeMonA;
    private CodeMon testCodeMonB;
    private CodeMon testCodeMonC;

    @BeforeEach
    void setUp() {
        // Mock-repo och "databaslista"
        codeMonList = new ArrayList<>();
        mockRepo = mock(CodeMonRepository.class);

        // Skapar testobjekt
        testCodeMonA = new CodeMon();
        testCodeMonA.setName("TestCodeMonA");
        testCodeMonA.setType("TestTypeA");
        testCodeMonA.setCodeMonGeneration(1);
        testCodeMonA.setAttackdmg(10);
        testCodeMonA.setHp(100);

        //? Denna har mest HP
        testCodeMonB = new CodeMon();
        testCodeMonB.setName("TestCodeMonB");
        testCodeMonB.setType("TestTypeB");
        testCodeMonB.setCodeMonGeneration(1);
        testCodeMonB.setAttackdmg(50);
        testCodeMonB.setHp(120);

        //? Denna har mest attackdmg
        testCodeMonC = new CodeMon();
        testCodeMonC.setName("TestCodeMonC");
        testCodeMonC.setType("TestTypeC");
        testCodeMonC.setCodeMonGeneration(1);
        testCodeMonC.setAttackdmg(30);
        testCodeMonC.setHp(200);

        // Lägger till objekten i listan
        codeMonList.add(testCodeMonA);
        codeMonList.add(testCodeMonB);
        codeMonList.add(testCodeMonC);


        // Mocka repository
        mockRepo = Mockito.mock(CodeMonRepository.class);
        when(mockRepo.findAll()).thenReturn(new ArrayList<>(codeMonList));

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
        List<CodeMon> result = controller.getAll();

        // Assertions
        assertEquals(3, result.size());
        assertEquals("TestCodeMonA", result.get(0).getName());
        assertEquals("TestCodeMonB", result.get(1).getName());
        assertEquals("TestCodeMonC", result.get(2).getName());
    }


    @Test
    void testFindCodeMonByIdExists() {
        // Mockar service så att den returnerar testCodeMonA med ID 1
        when(codeMonService.findCodeMonbyID("1")).thenReturn(testCodeMonA);

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
        CodeMonDTO newCodeMon = new CodeMonDTO("Mockmon", "Int", 1, 90, 35);

        CodeMon saved = new CodeMon();
        saved.setName("Mockmon");
        saved.setType("Int");
        saved.setCodeMonGeneration(1);
        saved.setHp(90);
        saved.setAttackdmg(35);

        when(codeMonService.save(any(CodeMonDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/javamons") // byt till din endpoint-path
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newCodeMon)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mockmon"))
                .andExpect(jsonPath("$.type").value("Int"))
                .andExpect(jsonPath("$.hp").value(90))
                .andExpect(jsonPath("$.attackdmg").value(35));
    }


    @Test
    void testDeleteCodeMonFound() {
        // Ser till att listan innehåller testCodeMonA
        assertTrue(codeMonList.contains(testCodeMonA));

        // mocka deleteCodeMon så att testCodeMonA tas bort ur listan
        doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            codeMonList.removeIf(cm -> cm.getName().equals("TestCodeMonA") && id.equals("1"));
            return null;
        }).when(codeMonService).deleteCodeMon("1");

        ResponseEntity<Void> response = controller.deleteACodMon("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(codeMonList.contains(testCodeMonA));
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


    //! Detta Test testar skickar tillbaka ett OK (kod 200) svar och
    //! en lista med 3 codemons som är sorterade
    @Test
    void testGetCodeMonsByGenerationAndSorting() throws Exception {
        // Skapande av page och listan
        Page<CodeMon> page = new PageImpl<>(
                //? Vi skapar en lista med de tre codemons vi skapade ovan
                //? Vi kommer att använda detta i testet nedan sedan
                List.of(testCodeMonB, testCodeMonC, testCodeMonA),
                PageRequest.of(0,3 ),
                3
        );

        when(codeMonService.getCodeMonsByGenerationAndSorting(
                //? Detta betyder att vi testar mot generation 1
                eq(1),

                //? Vi använder isNull för att vi vill testa de fall
                //? Där användaren inte väljer att sortera på något exakt
                //? Då är det null som skickas vilket vi har godkänt från controllern
                isNull(),

                //?
                any(Pageable.class)
        )).thenReturn(page);

        //? Vi testar att mocka mot en server med URL:en nedan
        //? Vi låtsas ha controllern igång
        mockMvc.perform(get("/api/javamons/generation/{gen}/top", 1)
                )
                // Vi föräntar oss att
                .andExpect(status().isOk())
                // Vi förväntar oss att det finns 3 objekt i
                //? Vi förväntar oss sedan att första attack numret är 50
                .andExpect(jsonPath("$.content[0].attackdmg").value(50))

                .andExpect(jsonPath("$.content[1].attackdmg").value(30))
                .andExpect(jsonPath("$.content[2].attackdmg").value(10));


        System.out.println();
        System.out.println("Testet lyckades");
        System.out.println("Nummer 0 matchade hp-värdet 50, nummer 1 matchade värdet 30 och nummer 3" +
                "matchade värdet 10");
        System.out.println();
    }

    @Test
    void testGetCodeMonGen1andGetAListDescendingFromAttackDamageExplicit() throws Exception {
        Page <CodeMon> page = new PageImpl<>(
                List.of(testCodeMonB, testCodeMonC, testCodeMonA),
                PageRequest.of(0,3),
                3
        );

        //? Mock -> inställningar.
        //? Vi använder listan över, ställer in controller på Gen 1 och attackdmg

        when(codeMonService.getCodeMonsByGenerationAndSorting(
                //? Ställer in att kolla mot generation 1
                eq(1),
                //? Ställer in den på att kolla mot attackdmg
                eq("attackdmg"),
                any(Pageable.class)

        )).thenReturn(page);

        mockMvc.perform(get("/api/javamons/generation/{gen}/top?sortBy=attackdmg", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].attackdmg").value(50))
                .andExpect(jsonPath("$.content[1].attackdmg").value(30))
                .andExpect(jsonPath("$.content[2].attackdmg").value(10));

        System.out.println();
        System.out.println("Testet lyckades");
        System.out.println("Nummer 0 matchade attackdmg-värdet 50, nummer 1 matchade värdet 30 och nummer 3" +
                "matchade värdet 10");
        System.out.println();
    }
}
