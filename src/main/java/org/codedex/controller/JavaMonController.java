package org.codedex.controller;

import org.codedex.Model.JavaMon;
import org.codedex.Repository.JavaMonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/javamons")
public class JavaMonController {
    //? Injisering av repo
    private final JavaMonRepository javaMonRepository;

    //? Konstruktor
    public JavaMonController(JavaMonRepository javaMonRepository) {
        this.javaMonRepository = javaMonRepository;
    }

    //? Metod för att hämta alla
    @GetMapping
    public List<JavaMon> getAll(){
        return javaMonRepository.findAll();
    }

    //? Metod för att hämta en specifik
    @GetMapping("/{id}")
    public JavaMon getASpecfic(@PathVariable String id ){
        return javaMonRepository.findById(id).orElseThrow
                (()-> new IllegalArgumentException("No JavaMon found with id " + id));
    }

    //? Metod för att lägga till en Javamon
    @PostMapping
    public ResponseEntity <JavaMon> save(@RequestBody JavaMon javaMon){
           JavaMon savedJavaMon = javaMonRepository.save(javaMon);
           return ResponseEntity.status(201).body(savedJavaMon);
    }


}
