package org.codedex.controller;

import org.codedex.Model.CodeMon;
import org.codedex.Repository.CodeMonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CodeMonControllerTest {

    private CodeMon codeMon1;
    private CodeMon codeMon2;
    private CodeMonRepository mockRepo;
    private CodeMonController controller;


    @BeforeEach
    void setUp() {
        // Skapar CodeMons innan varje test
        codeMon1 = new CodeMon("123", "Testachu", "Byte",
                1, 50, 20);

        codeMon2 = new CodeMon("124", "JUnitail", "Int",
                1, 60, 30);

        // Mock av repository
        mockRepo = mock(CodeMonRepository.class);

        // Kontroller med mockat repository
        controller = new CodeMonController(mockRepo);
    }

    @Test
    void testFindCodeMonByIdExists() {
        when(mockRepo.findById("123")).thenReturn(Optional.of(codeMon1));

        // Söker efter id 123, som finns
        Optional<CodeMon> result = mockRepo.findById("123");

        assertTrue(result.isPresent());
        assertEquals("Testachu", result.get().getName());
        verify(mockRepo, times(1)).findById("123");
    }

    @Test
    void testFindCodeMonByIdNotExists() {
        when(mockRepo.findById("999")).thenReturn(Optional.empty());

        // Söker efter id 999, som inte finns
        Optional<CodeMon> result = mockRepo.findById("999");

        assertFalse(result.isPresent());
        verify(mockRepo, times(1)).findById("999");
    }


    @Test
    void getAll() {
        when(mockRepo.findAll()).thenReturn(Arrays.asList(codeMon1, codeMon2));

        List<CodeMon> result = controller.getAll();

        assertEquals(2, result.size());
        assertEquals("Testachu", result.get(0).getName());
        assertEquals("JUnitail", result.get(1).getName());
        verify(mockRepo, times(1)).findAll();
    }
}


