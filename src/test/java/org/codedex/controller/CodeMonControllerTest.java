package org.codedex.controller;

import org.codedex.Model.CodeMon;
import org.codedex.Repository.CodeMonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CodeMonControllerTest {
    //? Mock av vårat CodeMonRepository
    //? Blir som ett låtsasrepo
    @Mock
    private CodeMonRepository codeMonRepository;

    //? Används så att Mockito automatiskt stoppar in vårt
    //? "låtsas-repo" i våran service-klass
    @InjectMocks
    private CodeMonController codeMonController;

    private CodeMon testCodeMon;

    @BeforeEach
    //? BeforeEach = Före varje test så körs detta
    void setUp(){
        //? Skapar 3 codemon vi kommer att kunna testa mot
        testCodeMon = new CodeMon();
        testCodeMon.setName("TestCodeMon");
        testCodeMon.setType("TestingType");
        testCodeMon.setCodeMonGeneration(1);
        testCodeMon.setHp(100);
        testCodeMon.setAttackdmg(20);

        testCodeMon = new CodeMon();
        testCodeMon.setName("TestCodeMon2");
        testCodeMon.setType("TestingType2");
        testCodeMon.setCodeMonGeneration(1);
        testCodeMon.setHp(110);
        testCodeMon.setAttackdmg(30);

        testCodeMon = new CodeMon();
        testCodeMon.setName("TestCodeMon3");
        testCodeMon.setType("TestingType3");
        testCodeMon.setCodeMonGeneration(1);
        testCodeMon.setHp(120);
        testCodeMon.setAttackdmg(40);
    }







}
