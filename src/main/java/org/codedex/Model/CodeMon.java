package org.codedex.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.processing.Generated;
import java.util.Date;

@Document("JavaMons")
public class CodeMon {

    @Id

    private String id;

    private String name;

    private CodeMonTyps type;

    private Integer codeMonGeneration;

    private Integer hp;

    private Integer attackdmg;

    private Date createdAt;


    //? Tom konstruktor
    public CodeMon() {
    }

    //? Konstruktor med attribut

    public CodeMon( String name, CodeMonTyps type, Integer codeMonGeneration, Integer hp, Integer attackdmg, Date createdAt) {
        this.name = name;
        this.type = type;
        this.codeMonGeneration = codeMonGeneration;
        this.hp = hp;
        this.attackdmg = attackdmg;
        this.createdAt = createdAt;
    }

    //? Getter & Setter

    public Date getCreatedAt() {return createdAt;}
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}
    public String getId() {return id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public CodeMonTyps getType() {return type;}
    public void setType(CodeMonTyps type) {this.type = type;}
    public Integer getHp() {return hp;}
    public void setHp(Integer hp) {this.hp = hp;}
    public Integer getAttackdmg() {return attackdmg;}
    public void setAttackdmg(Integer attackdmg) {this.attackdmg = attackdmg;}
    public Integer getCodeMonGeneration() {return codeMonGeneration;}
    public void setCodeMonGeneration(Integer codeMonGeneration) {this.codeMonGeneration = codeMonGeneration;}
}
