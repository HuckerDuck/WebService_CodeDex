package org.codedex.service;


import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonDTO;
import org.codedex.Repository.CodeMonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

//? När projektet blir större kan vi eventuellt lägga all logik här istället
@Service
public class CodeMonService {

    private final CodeMonRepository codeMonRepository;

    @Autowired
    public CodeMonService(CodeMonRepository codeMonRepository) {
        this.codeMonRepository = codeMonRepository;
    }

    public List<CodeMon> getAll() {
        return codeMonRepository.findAll();
    }

    public CodeMon save(CodeMonDTO codeMonDTO) {
        CodeMon codeMon = codeMonDTOToCodeMon(codeMonDTO);
        codeMon = codeMonRepository.save(codeMon);
        return codeMon;
    }

    public CodeMon findCodeMonbyID(String id) {
        return codeMonRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("CodeMon not found ID: " + id));
    }

    public CodeMon updateACodeMon(String id, CodeMonDTO codeMonInformation) {
        return codeMonRepository.findById(id)
                .map(CodeMon -> {


                    //! Kolla att namnet inte är tomt
                    if (codeMonInformation.name() != null) {
                        CodeMon.setName(codeMonInformation.name());
                    }
                    //! Kolla att typen av Javamon inte är tomt
                    if (codeMonInformation.type() != null) {
                        CodeMon.setType(codeMonInformation.type());
                    }
                    if (codeMonInformation.codeMonGeneration() != null) {
                        CodeMon.setCodeMonGeneration(codeMonInformation.codeMonGeneration());
                    }
                    //! Kolla att attack skadan av Javamon inte är tomt
                    if (codeMonInformation.attackdmg() != null) {
                        CodeMon.setAttackdmg(codeMonInformation.attackdmg());
                    }
                    //! Kolla att hp:t av Javamon inte är tomt
                    if (codeMonInformation.hp() != null) {
                        CodeMon.setHp(codeMonInformation.hp());
                    }

                    //! Om allt detta är okej så ska den då spara en Javamon
                    CodeMon updatedCodeMon = codeMonRepository.save(CodeMon);
                    return updatedCodeMon;
                })
                //? Om studenten inte finns, returnera ett 404 Not Found svar
                .orElseThrow(() -> new UsernameNotFoundException("CodeMon not found ID: " + id));
    }

    public void deleteCodeMon(String id) {
        codeMonRepository.findById(id)
                .map(CodeMon -> {
                    codeMonRepository.delete(CodeMon);
                    return ResponseEntity.noContent().<Void>build();
                })
                //? Om Javamon inte finns, returnera ett 404 Not Found svar
                .orElseThrow(() -> new UsernameNotFoundException("CodeMon not found ID: " + id));
    }

    public List<CodeMon> filterCodeMonByCatargory(String catargory, String value) {
        List<CodeMon> codeMons = switch (catargory) {
            case "type" -> codeMonRepository.findByType(value);
            // case "codeMonGeneration" -> codeMonRepository.findByCodeMonGeneration(value);
            case "name" -> codeMonRepository.findByName(value);
            default -> throw new UsernameNotFoundException("CodeMon category not found : " + catargory);
        };
        return codeMons;


    }

    //? Sortera efter hp och generation
    public Page<CodeMon> getCodeMonsByGenerationAndHp(Integer generation, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                //? Skicka in hur många sidor klienten vill ha
                pageable.getPageNumber(),
                //? Skicka in hur många hur stor storlek det ska vara på varje sida
                pageable.getPageSize(),
                //? Sortera det sedan från störst till minst
                Sort.by(Sort.Direction.DESC, "hp")
        );
        return codeMonRepository.findByCodeMonGeneration(generation, sortedPageable);
    }


    private CodeMon codeMonDTOToCodeMon(CodeMonDTO codeMonDTO) {
        CodeMon codeMon = new CodeMon();
        codeMon.setName(codeMonDTO.name());
        codeMon.setType(codeMonDTO.type());
        codeMon.setCodeMonGeneration(codeMonDTO.codeMonGeneration());
        codeMon.setHp(codeMonDTO.hp());
        codeMon.setAttackdmg(codeMonDTO.attackdmg());
        return codeMon;
    }





}
