package org.codedex.controller;

import org.codedex.Model.JavaMon;
import org.codedex.Repository.JavaMonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/javamons")
public class JavaMonController {
    @Autowired
    JavaMonRepository javaMonRepository;


    //? Metod för att hämta alla
    @GetMapping
    public List<JavaMon> getAll(){
        return javaMonRepository.findAll();
    }

    //? Metod för att hämta en specifik
    @GetMapping("/{id}")
    public ResponseEntity <JavaMon> findJavaMonbyID(@PathVariable String id){
        return javaMonRepository.findById(id)
                .map(JavaMon -> ResponseEntity.ok(JavaMon))
                .orElse(ResponseEntity.notFound().build());
    }

    //? Metod för att lägga till en Javamon
    @PostMapping
    public ResponseEntity <JavaMon> save(@RequestBody JavaMon javaMon){
           JavaMon savedJavaMon = javaMonRepository.save(javaMon);
           return ResponseEntity.status(201).body(savedJavaMon);

           //
    }

    //? Uppdatera delar av en Javamon
    @PatchMapping("/{id}")
    public ResponseEntity <JavaMon> updateAStudent(@PathVariable String id, @RequestBody
    JavaMon JavaMonInformation){
        return javaMonRepository.findById(id)
                .map (JavaMon -> {


                    //! Kolla att namnet inte är tomt
                    if (JavaMonInformation.getName() != null){
                        JavaMon.setName(JavaMonInformation.getName());
                    }
                    //! Kolla att typen av Javamon inte är tomt
                    if (JavaMonInformation.getType() != null){
                        JavaMon.setType(JavaMonInformation.getType());
                    }
                    //! Kolla att attack skadan av Javamon inte är tomt
                    if (JavaMonInformation.getAttackdmg() != null){
                        JavaMon.setAttackdmg(JavaMon.getAttackdmg());
                    }
                    //! Kolla att hp:t av Javamon inte är tomt
                    if (JavaMonInformation.getHp() != null){
                        JavaMon.setHp(JavaMon.getHp());
                    }

                    //! Om allt detta är okej så ska den då spara en Javamon
                    JavaMon updatedJavaMon = javaMonRepository.save(JavaMon);
                    return ResponseEntity.ok(updatedJavaMon);
                })
                //? Om studenten inte finns, returnera ett 404 Not Found svar
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //? Ta bort en JavaMon med id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAStudent(@PathVariable String id){
        return javaMonRepository.findById(id)
                .map (JavaMon -> {
                    javaMonRepository.delete(JavaMon);
                    return ResponseEntity.noContent().<Void>build();
                })
                //? Om Javamon inte finns, returnera ett 404 Not Found svar
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
