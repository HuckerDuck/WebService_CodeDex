package org.codedex.service;

import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonTyps;
import org.codedex.Repository.CodeMonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CodeMonServiceTest {
    @Mock
    private CodeMonRepository codeMonRepository;

    @InjectMocks
    private CodeMonService codeMonService;

    private CodeMon testCodeMonA;
    private CodeMon testCodeMonB;
    private CodeMon testCodeMonC;

    private List<CodeMon> testCodeMonList;

    @BeforeEach
    void setUp() {
        //? Skapar 1 testobjekt
        testCodeMonA = new CodeMon();
        testCodeMonA.setName("TestCodeMonA");
        testCodeMonA.setType(CodeMonTyps.COMPILER);
        testCodeMonA.setCodeMonGeneration(1);
        testCodeMonA.setHp(10);
        testCodeMonA.setAttackdmg(10);

        //? Skapar ett till
        testCodeMonB = new CodeMon();
        testCodeMonB.setName("TestCodeMonB");
        testCodeMonB.setType(CodeMonTyps.COMPILER);
        testCodeMonB.setCodeMonGeneration(1);
        testCodeMonB.setAttackdmg(20);
        testCodeMonB.setHp(20);

        //? Skapar en lista med 2 testobjekt
        testCodeMonList = List.of(testCodeMonA, testCodeMonB);
    }

    @Test
    void getAll_returnsListFromRepository() {
        //? When - Kommer från Mockito
        //? pratar inte med den riktiga databasen

        //? När findAll kallas på, ge tillbaka testCodeMonList
        when(codeMonRepository.findAll()).thenReturn(testCodeMonList);

        //? GetAll i service returnerar sen en findAll i repot
        //? Därmed kommer den att kolla mot testCodeMonListan vi la till
        List<CodeMon> result = codeMonService.getAll();


        //? Vi förväntar oss att det finns 2 codeMons i listan
        assertEquals(2, result.size());

        //? Vi förväntar oss att det första namnet är TestCodeMonA
        assertEquals("TestCodeMonA", result.get(0).getName());

        //? Vi förväntar oss att det andra namnet är TestCodeMonB
        assertEquals("TestCodeMonB", result.get(1).getName());
    }
}
