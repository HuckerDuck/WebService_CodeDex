package org.codedex.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("JavaMons")
public class JavaMon {
    @Id
    private String id;

    @NotBlank (message = "Name cannot be blank")
    private String name;

    @NotBlank (message = "Type cannot be blank")
    private String type;

    @NotNull (message = "HP of the JavaMon cannot be null")
    @Min(value = 1, message = "HP number needs to be atleast 1")
    private Integer hp;

    @NotNull (message = "AttackDamage of the JavaMon cannot be null")
    @Min(value = 1, message = "Attack Damage number needs to be atleast 1")
    private Integer attackdmg;

    //? Tom konstruktor
    public JavaMon() {
    }

    //? Konstruktor med attribut
    public JavaMon(String id, String name, String type, Integer hp, Integer attackdmg) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.attackdmg = attackdmg;
    }

    //? Getter & Setter
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public Integer getHp() {return hp;}
    public void setHp(Integer hp) {this.hp = hp;}
    public Integer getAttackdmg() {return attackdmg;}
    public void setAttackdmg(Integer attackdmg) {this.attackdmg = attackdmg;}
}
