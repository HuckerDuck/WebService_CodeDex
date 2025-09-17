package org.codedex.Model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CodeMonDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull (message = "Invalid type")
        CodeMonTyps type,

        @NotNull (message = "CodeMonGeneration cannot be null")
        @Min(value = 1, message = "Number of generation cannot be 0 or less")
        @Max(value = 5, message = "The maximum amount of generation is 5")
        Integer codeMonGeneration,

        @NotNull (message = "HP of the JavaMon cannot be null")
        @Min(value = 1, message = "HP number needs to be atleast 1")
        Integer hp,

        @NotNull (message = "AttackDamage of the JavaMon cannot be null")
        @Min(value = 1, message = "Attack Damage number needs to be atleast 1")
        Integer attackdmg
) {

}
