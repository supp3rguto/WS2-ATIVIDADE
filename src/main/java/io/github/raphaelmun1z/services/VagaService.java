package io.github.raphaelmun1z.services;

import io.github.raphaelmun1z.dto.req.VagaRequestDTO;
import io.github.raphaelmun1z.dto.res.VagaResponseDTO;
import io.github.raphaelmun1z.entities.Vaga;
import io.github.raphaelmun1z.repositories.VagaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VagaService {
    private final VagaRepository repository;

    public VagaService(VagaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public VagaResponseDTO salvar(VagaRequestDTO dto) {
        return repository.findByFonteAndCodigoVaga(dto.fonte(), dto.codigoVaga())
                .map(VagaResponseDTO::new)
                .orElseGet(() -> {
                    Vaga novaVaga = new Vaga();
                    BeanUtils.copyProperties(dto, novaVaga);
                    Vaga vagaSalva = repository.save(novaVaga);
                    return new VagaResponseDTO(vagaSalva);
                });
    }

    @Transactional
    public void salvarVarias(List<VagaRequestDTO> dtos) {
        dtos.forEach(this::salvar);
    }

    public Page<VagaResponseDTO> listarTodas(Pageable pageable) {
        return repository.findAll(pageable)
                .map(VagaResponseDTO::new);
    }

    public VagaResponseDTO buscarPorId(String id) {
        return repository.findById(id)
                .map(VagaResponseDTO::new)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada com o ID: " + id));
    }

    public Page<VagaResponseDTO> listarComFiltros(VagaRequestDTO filtros, Pageable pageable) {
        Vaga vagaFiltro = new Vaga();
        BeanUtils.copyProperties(filtros, vagaFiltro);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Vaga> exemplo = Example.of(vagaFiltro, matcher);

        return repository.findAll(exemplo, pageable)
                .map(VagaResponseDTO::new);
    }
}