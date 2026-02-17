package io.github.raphaelmun1z.dto.res;

import io.github.raphaelmun1z.entities.Vaga;

public record VagaResponseDTO(
        String id,
        String codigoVaga,
        String titulo,
        String empresa,
        String salario,
        String dataAnuncio,
        String local,
        String descricao,
        String regime,
        String linkCandidatura,
        String modalidade,
        String fonte
) {
    public VagaResponseDTO(Vaga vaga) {
        this(
                vaga.getId(),
                vaga.getCodigoVaga(),
                vaga.getTitulo(),
                vaga.getEmpresa(),
                vaga.getSalario(),
                vaga.getDataAnuncio(),
                vaga.getLocal(),
                vaga.getDescricao(),
                vaga.getRegime(),
                vaga.getLinkCandidatura(),
                vaga.getModalidade(),
                vaga.getFonte()
        );
    }
}