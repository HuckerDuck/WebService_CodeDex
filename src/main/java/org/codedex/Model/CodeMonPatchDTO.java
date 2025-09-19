package org.codedex.Model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;

//? This is a patch DTO
//? Used to update a CodeMon


public class CodeMonPatchDTO {

    //? JsonSetter används för att ignorera null värden från klienten
    //? Men den gör också att om man skickar NULL med flit så får du
    //? en exception (400) tillbaka
    @JsonSetter(nulls = Nulls.FAIL)
    @NotBlank(message = "Name cannot be blank")
    private String name;


    @JsonSetter(nulls = Nulls.FAIL)
    private CodeMonTyps type;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 1, message = "Number of generation cannot be 1 or less")
    @Max(value = 5, message = "The maximum amount of generation is 5")
    private Integer codeMonGeneration;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 1, message = "HP number number cannot be less then 1")
    private Integer hp;

    @JsonSetter(nulls = Nulls.FAIL)
    @Min(value = 1, message = "Attack Damage number cannot be less then 1")
    private Integer attackdmg;

    @JsonSetter(nulls = Nulls.FAIL)
    //? Past or Present innebär helt enkelt att datumet
    //? Får vara i dåtid och i nutid
    //? Men inte i framtiden
    @PastOrPresent(message = "createdAt cannot be in the future")
    private Date createdAt;


    //? Getter & Setter
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public CodeMonTyps getType() {return type;}
    public void setType(CodeMonTyps type) {this.type = type;}
    public Integer getCodeMonGeneration() {return codeMonGeneration;}
    public void setCodeMonGeneration(Integer codeMonGeneration) {
        this.codeMonGeneration = codeMonGeneration;}
    public Integer getHp() {return hp;}
    public void setHp(Integer hp) {this.hp = hp;}
    public Integer getAttackdmg() {return attackdmg;}
    public void setAttackdmg(Integer attackdmg) {this.attackdmg = attackdmg;}
    public Date getCreatedAt() {return createdAt;}
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}
}
