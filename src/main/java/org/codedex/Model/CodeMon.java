package org.codedex.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("JavaMons")
public class CodeMon {
    @Id
    private String id;

    @NotBlank (message = "Name cannot be blank")
    private String name;

    @NotBlank (message = "Type cannot be blank")
    private String type;

    @NotNull
    @Min (value = 1, message = "Number of generation cannot be 0 or less")
    @Max (value = 5, message = "The maximum amount of generation is 5")
    private Integer codeMonGeneration;

    @NotNull (message = "HP of the JavaMon cannot be null")
    @Min(value = 1, message = "HP number needs to be atleast 1")
    private Integer hp;

    @NotNull (message = "AttackDamage of the JavaMon cannot be null")
    @Min(value = 1, message = "Attack Damage number needs to be atleast 1")
    private Integer attackdmg;


    //? Tom konstruktor
    public CodeMon() {
    }

    //? Konstruktor med attribut

    public CodeMon(String name, String type, Integer codeMonGeneration, Integer hp, Integer attackdmg) {
        this.name = name;
        this.type = type;
        this.codeMonGeneration = codeMonGeneration;
        this.hp = hp;
        this.attackdmg = attackdmg;
    }

    //? Getter & Setter
    public String getId() {return id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public Integer getHp() {return hp;}
    public void setHp(Integer hp) {this.hp = hp;}
    public Integer getAttackdmg() {return attackdmg;}
    public void setAttackdmg(Integer attackdmg) {this.attackdmg = attackdmg;}
    public Integer getCodeMonGeneration() {return codeMonGeneration;}
    public void setCodeMonGeneration(Integer codeMonGeneration) {this.codeMonGeneration = codeMonGeneration;}
}
