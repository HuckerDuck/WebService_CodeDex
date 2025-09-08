package org.codedex.service;

import org.codedex.Model.CodeMon;
import org.codedex.Model.CodeMonDTO;

//? När projektet blir större kan vi eventuellt lägga all logik här istället
public class CodeMonService {






    private CodeMon codeMonDTOToCodeMon(CodeMonDTO codeMonDTO) {
        CodeMon codeMon = new CodeMon();
        codeMon.setName(codeMonDTO.name());
        codeMon.setCodeMonGeneration(codeMonDTO.codeMonGeneration());
        codeMon.setHp(codeMonDTO.hp());
        codeMon.setAttackdmg(codeMonDTO.attackdmg());
        return codeMon;
    };
}
