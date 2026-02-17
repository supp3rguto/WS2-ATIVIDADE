package io.github.raphaelmun1z.repositories;

import io.github.raphaelmun1z.entities.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, String> {
    Optional<Vaga> findByFonteAndCodigoVaga(String fonte, String codigoVaga);
}