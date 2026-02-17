package io.github.raphaelmun1z.dto.req;

import jakarta.validation.constraints.NotBlank;

public record VagaRequestDTO(
        @NotBlank(message = "O código da vaga é obrigatório")
        String codigoVaga,

        @NotBlank(message = "O título da vaga é obrigatório")
        String titulo,

        String empresa,
        String salario,
        String dataAnuncio,
        String local,
        String descricao,
        String regime,

        @NotBlank(message = "O link de candidatura é obrigatório")
        String linkCandidatura,

        String modalidade,

        @NotBlank(message = "A fonte da vaga é obrigatória")
        String fonte
) {}