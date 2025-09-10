package org.codedex.controller;

import jakarta.validation.Valid;
import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonDTO;
import org.codedex.service.CodeMonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/javamons")
public class CodeMonController {

    CodeMonService codeMonService;

    @Autowired
    public CodeMonController(CodeMonService codeMonService) {
        this.codeMonService = codeMonService;
    }

    //? Metod för att hämta alla
    @GetMapping
    public List<CodeMon> getAll(){
        return codeMonService.getAll();
    }

    //? Metod för att hämta en specifik
    @GetMapping("/{id}")
    public ResponseEntity <?> findCodeMonbyID(@PathVariable String id){
        try {
            return ResponseEntity.ok(codeMonService.findCodeMonbyID(id));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //? Metod för att lägga till en Codemon
    @PostMapping
    public ResponseEntity <CodeMon> save(@Valid @RequestBody CodeMonDTO codeMonDTO){

           CodeMon savedCodeMon = codeMonService.save(codeMonDTO);

           return ResponseEntity.status(201).body(savedCodeMon);

           //
    }

    //? Uppdatera delar av en CodMon
    @PatchMapping("/{id}")
    public ResponseEntity <?> updateACodeMon(@PathVariable String id, @RequestBody
    CodeMonDTO codeMonInformation){
        try{
            CodeMon codeMon = codeMonService.updateACodeMon(id,codeMonInformation);
            return ResponseEntity.ok(codeMon);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //? Ta bort en JavaMon med id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteACodMon(@PathVariable String id){
        try{
            codeMonService.deleteCodeMon(id);
            return ResponseEntity.noContent().build();
        }catch (UsernameNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
