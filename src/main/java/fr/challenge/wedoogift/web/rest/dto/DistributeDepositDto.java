package fr.challenge.wedoogift.web.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class DistributeDepositDto {
    private final String MANDATORY_FIELD = "Le paramètre est obligatoire";

    @NotNull(message = MANDATORY_FIELD)
    private Long idCompany;
    @NotNull(message = MANDATORY_FIELD)
    private Long idUser;
    @NotNull(message = MANDATORY_FIELD)
    private Integer amount;
}
