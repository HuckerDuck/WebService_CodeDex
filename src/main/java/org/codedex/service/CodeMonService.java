package org.codedex.service;

import org.codedex.Model.*;
import org.codedex.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

//? När projektet blir större kan vi eventuellt lägga all logik här istället
@Service
public class CodeMonService {

    //? Sortera efter hp eller skada (attackdmg)
    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of("hp", "attackdmg");

    //? Standard fältet som kommer att komma in i metod är attackdmg när inget anges
    private static final String DEFAULT_SORT_PROPERTY = "attackdmg";

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
                    //! Kolla att createdAt av Javamon inte är tomt
                    if (codeMonInformation.createdAt() != null) {
                        CodeMon.setCreatedAt(codeMonInformation.createdAt());
                    }

                    //! Om allt detta är okej så ska den då spara en Javamon
                    CodeMon updatedCodeMon = codeMonRepository.save(CodeMon);
                    return updatedCodeMon;
                })
                //? Om studenten inte finns, returnera ett 404 Not Found svar
                .orElseThrow(() -> new UsernameNotFoundException("CodeMon not found ID: " + id));
    }

    public void deleteCodeMon(String id) {
        CodeMon codeMon = codeMonRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("CodeMon not found ID: " + id));
        codeMonRepository.delete(codeMon);
    }


    public List<CodeMon> filterCodeMonByCategory(String category, String value){
        return switch (category) {
            case "type" -> {
                if (isValidEnum(value)) {
                    yield codeMonRepository.findByType(CodeMonTyps.valueOf(value));
                } else {
                    throw new UsernameNotFoundException("CodeMon invalid value : " + category);
                }
            }
            case "codeMonGeneration" -> codeMonRepository.findByCodeMonGeneration(value);

            case "name" -> codeMonRepository.findByName(value);
            default -> throw new UsernameNotFoundException("CodeMon category not found : " + category);
        };
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

        //? Om sortby, alltså det som användaren lägg in är tom.
        //? Sortera då på -> attackdmg
        if (sortBy == null || !ALLOWED_SORT_PROPERTIES.contains(sortBy)) {
            sortBy = DEFAULT_SORT_PROPERTY;
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
