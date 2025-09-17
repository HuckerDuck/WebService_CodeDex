package org.codedex.controller;

import jakarta.validation.Valid;
import org.codedex.Model.*;
import org.codedex.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/codemons/")
public class CodeMonController {

    CodeMonService codeMonService;

    @Autowired
    public CodeMonController(CodeMonService codeMonService) {
        this.codeMonService = codeMonService;
    }

    //? Metod för att hämta alla
    @GetMapping
    public List<CodeMon> getAll() {
        return codeMonService.getAll();
    }

    //? Metod för att hämta en specifik
    @GetMapping("/{id}")
    public ResponseEntity<?> findCodeMonbyID(@PathVariable String id) {
        try {
            return ResponseEntity.ok(codeMonService.findCodeMonbyID(id));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //? Metod för att lägga till en CodeMon
    @PostMapping
    public ResponseEntity<CodeMon> save(@Valid @RequestBody CodeMonDTO codeMonDTO) {

        CodeMon savedCodeMon = codeMonService.save(codeMonDTO);

        return ResponseEntity.status(201).body(savedCodeMon);

    }

    //? Uppdatera delar av en CodMon
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateACodeMon(@PathVariable String id, @Valid @RequestBody
    CodeMonDTO codeMonInformation) {
        try {
            CodeMon codeMon = codeMonService.updateACodeMon(id, codeMonInformation);
            return ResponseEntity.ok(codeMon);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //? Ta bort en Codemon med id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteACodMon(@PathVariable String id) {
        try {
            codeMonService.deleteCodeMon(id);
            return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //? Metod för att hämta alla som passar igenom ett filter
    @GetMapping("/filter/{category}/{value}")
    public ResponseEntity<?> filterCodeMonByCatargory(@PathVariable String category, @PathVariable String value) {

        try {
            List<CodeMon> codeMonList = codeMonService.filterCodeMonByCategory(category, value);

            return ResponseEntity.ok(codeMonList);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllTypes(@RequestBody(required = false) Integer gen) {
        List<String> types = codeMonService.getAllTypes(gen);
        return ResponseEntity.ok(types);
    }

    @GetMapping("/after")
    public ResponseEntity<List<CodeMon>> getAllAfter(@RequestBody Date after) {
        return ResponseEntity.ok(codeMonService.getCodeMonAfter(after));
    }
    @GetMapping("/before")
    public ResponseEntity<List<CodeMon>> getAllBefore(@RequestBody Date before) {
        return ResponseEntity.ok(codeMonService.getCodeMonAfter(before));

    }



    //? Metod för att hämta alla JavaMon av en viss generation
    //? Baserad på max-hp eller max-attackdmg
    @GetMapping("/generation/{gen}/top")
    public ResponseEntity<?> getCodeMonsByGenerationAndSorting(
            @PathVariable Integer gen,
            //? Man behöver inte skriva in men annars kan man skriva in
            //? hp eller attackdmg
            @RequestParam(required = false) String sortBy,
            Pageable pageable)
    {
        Page<CodeMon> codeMonPage = codeMonService.
                getCodeMonsByGenerationAndSorting(gen, sortBy, pageable);

        if (codeMonPage.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(codeMonPage);
    }
}
