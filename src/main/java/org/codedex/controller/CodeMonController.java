package org.codedex.controller;

import org.codedex.Model.CodeMon;
import org.codedex.Repository.CodeMonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/javamons")
public class CodeMonController {

    private final CodeMonRepository codeMonRepository;

    @Autowired
    public CodeMonController(CodeMonRepository codeMonRepository) {
        this.codeMonRepository = codeMonRepository;
    }

    //? Metod för att hämta alla
    @GetMapping
    public List<CodeMon> getAll(){
        return codeMonRepository.findAll();
    }

    //? Metod för att hämta en specifik
    @GetMapping("/{id}")
    public ResponseEntity <CodeMon> findJavaMonbyID(@PathVariable String id){
        return codeMonRepository.findById(id)
                .map(CodeMon -> ResponseEntity.ok(CodeMon))
                .orElse(ResponseEntity.notFound().build());
    }

    //? Metod för att lägga till en Javamon
    @PostMapping
    public ResponseEntity <CodeMon> save(@RequestBody CodeMon codeMon){
           CodeMon savedCodeMon = codeMonRepository.save(codeMon);
           return ResponseEntity.status(201).body(savedCodeMon);
    }

    //? Uppdatera delar av en Javamon
    @PatchMapping("/{id}")
    public ResponseEntity <CodeMon> updateAStudent(@PathVariable String id, @RequestBody
    CodeMon codeMonInformation){
        return codeMonRepository.findById(id)
                .map (CodeMon -> {


                    //! Kolla att namnet inte är tomt
                    if (codeMonInformation.getName() != null){
                        CodeMon.setName(codeMonInformation.getName());
                    }
                    //! Kolla att typen av Javamon inte är tomt
                    if (codeMonInformation.getType() != null){
                        CodeMon.setType(codeMonInformation.getType());
                    }
                    //! Kolla att attack skadan av Javamon inte är tomt
                    if (codeMonInformation.getAttackdmg() != null){
                        CodeMon.setAttackdmg(CodeMon.getAttackdmg());
                    }
                    //! Kolla att hp:t av Javamon inte är tomt
                    if (codeMonInformation.getHp() != null){
                        CodeMon.setHp(CodeMon.getHp());
                    }

                    //! Om allt detta är okej så ska den då spara en Javamon
                    CodeMon updatedCodeMon = codeMonRepository.save(CodeMon);
                    return ResponseEntity.ok(updatedCodeMon);
                })
                //? Om studenten inte finns, returnera ett 404 Not Found svar
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //? Ta bort en JavaMon med id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAStudent(@PathVariable String id){
        return codeMonRepository.findById(id)
                .map (CodeMon -> {
                    codeMonRepository.delete(CodeMon);
                    return ResponseEntity.noContent().<Void>build();
                })
                //? Om Javamon inte finns, returnera ett 404 Not Found svar
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
