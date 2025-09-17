package org.codedex.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonTyps;
import org.codedex.service.CodeMonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

//? Added a new test class
//? Wanted a class more for filtering

@WebMvcTest(CodeMonController.class)
public class CodeMonFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CodeMonService codeMonService;

    private CodeMon testCodeMonA;
    private CodeMon testCodeMonB;
    private CodeMon testCodeMonC;

    @BeforeEach
    void setUp() {
        // Skapar testobjekt
        testCodeMonA = new CodeMon();
        testCodeMonA.setName("TestCodeMonA");
        testCodeMonA.setType(CodeMonTyps.COMPILER);
        testCodeMonA.setCodeMonGeneration(1);
        testCodeMonA.setHp(50);
        testCodeMonA.setAttackdmg(50);

        //? Denna har mest HP
        testCodeMonB = new CodeMon();
        testCodeMonB.setName("TestCodeMonB");
        testCodeMonB.setType(CodeMonTyps.COMPILER);
        testCodeMonB.setCodeMonGeneration(1);
        testCodeMonB.setAttackdmg(30);
        testCodeMonB.setHp(30);

        //? Denna har mest attackdmg
        testCodeMonC = new CodeMon();
        testCodeMonC.setName("TestCodeMonC");
        testCodeMonC.setType(CodeMonTyps.COMPILER);
        testCodeMonC.setCodeMonGeneration(1);
        testCodeMonC.setAttackdmg(10);
        testCodeMonC.setHp(10);
    }

    //! Detta Test testar skickar tillbaka ett OK (kod 200) svar och
    //! en lista med 3 codemons som är sorterade
    @Test
    void testGetCodeMonsByGenerationAndSorting() throws Exception {
        // Skapande av page och listan
        Page<CodeMon> page = new PageImpl<>(
                //? Vi skapar en lista med de tre codemons vi skapade ovan
                //? Vi kommer att använda detta i testet nedan sedan
                List.of(testCodeMonA, testCodeMonB, testCodeMonC),
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
        mockMvc.perform(get("/codemons/generation/{gen}/top", 1)
                )

                .andExpect(status().isOk())
                //? Vi förväntar oss sedan att första attack numret är 50
                .andExpect(jsonPath("$.content[0].hp").value(50))
                .andExpect(jsonPath("$.content[1].hp").value(30))
                .andExpect(jsonPath("$.content[2].hp").value(10));
    }

    @Test
    void testGetCodeMonGen1andGetAListDescendingFromAttackDamageExplicit() throws Exception {
        Page <CodeMon> page = new PageImpl<>(
                List.of(testCodeMonA, testCodeMonB, testCodeMonC),
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

        mockMvc.perform(get("/codemons/generation/{gen}/top?sortBy=attackdmg", 1))
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
