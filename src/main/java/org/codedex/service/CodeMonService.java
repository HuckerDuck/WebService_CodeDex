package org.codedex.service;

import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonDTO;
import org.codedex.Model.CodeMonTyps;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

//? När projektet blir större kan vi eventuellt lägga all logik här istället
@Service
public class
CodeMonService {

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


    public List<CodeMon> filterCodeMonByCatargory(String catargory, String value) 
        return switch (catargory) {
            case "type" -> {
                if (isValidEnum(value)) {
                    yield codeMonRepository.findByType(CodeMonTyps.valueOf(value));
                } else {
                    throw new UsernameNotFoundException("CodeMon invalid value : " + catargory);
                }
            }
            case "codeMonGeneration" -> codeMonRepository.findByCodeMonGeneration(value);

            case "name" -> codeMonRepository.findByName(value);
            default -> throw new UsernameNotFoundException("CodeMon category not found : " + catargory);
        };
    }

    public List<String> getAllTypes() {
        List<String> message = new ArrayList<>();
        for (CodeMonTyps type : CodeMonTyps.values()) {
            List<CodeMon> codeMons = codeMonRepository.findByType(type);

            int numberOfCodeMons = codeMons.size();
            message.add(type + " : " + numberOfCodeMons);
        }
        return message;
    }

    public List<String> getAllTypes(Integer gen) {
        List<String> message = new ArrayList<>();
        if (gen != null) message.add("Gen " + gen + " has");
        for (CodeMonTyps type : CodeMonTyps.values()) {
            List<CodeMon> codeMons;
            if (gen != null) codeMons = codeMonRepository.findByTypeAndCodeMonGeneration(type, gen);
            else codeMons = codeMonRepository.findByType(type);
            message.add(type + " : " + codeMons.size());
        }
        return message;
    }

    public List<CodeMon> getCodeMonAfter(Date after) {
        return codeMonRepository.findAllWithCreatedAfter(after);
    }
    public List<CodeMon> getCodeMonBefore(Date before) {
        return codeMonRepository.findAllWithCreatedBefore(before);
    }

    //? Sortera efter hp eller skada (attackdmg)
    public Page<CodeMon> getCodeMonsByGenerationAndSorting(Integer generation,
            String sortBy, Pageable pageable) {
        //? Tillåtna fält som man kan sortera på
        Set<String> allowedInputs = Set.of("hp", "attackdmg");

        //? Standard som den kommer att sortera efter
        //? I detta fall är det skada (attackdmg)
        String defaultSortBy = "attackdmg";

        //? Om sortby, alltså det som användaren lägg in är tom.
        //? Sortera då på -> attackdmg
        if (sortBy == null || !allowedInputs.contains(sortBy)) {
            sortBy = defaultSortBy;
        }

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(sortBy).descending()
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


    public boolean isValidEnum(String input) {
        for (CodeMonTyps s : CodeMonTyps.values()) {
            if (s.name().equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }


}
