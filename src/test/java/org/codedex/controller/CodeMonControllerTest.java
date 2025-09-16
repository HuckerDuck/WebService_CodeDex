package org.codedex.controller;

import org.codedex.Model.CodeMon;

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
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CodeMonController.class)
@Import(CodeMonControllerTest.TestConfig.class)
public class CodeMonControllerTest {

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

    //? Vi behöver 3 codemons som vi testar mot
    private CodeMon testCodeMonA;
    private CodeMon testCodeMonB;
    private CodeMon testCodeMonC;


    @BeforeEach
    //? Vi skapar de tre codemons som vi testar mot
    //? Dessa kommer vi att försöka sortera med
    void setUp() {
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
    }

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


    }




}


