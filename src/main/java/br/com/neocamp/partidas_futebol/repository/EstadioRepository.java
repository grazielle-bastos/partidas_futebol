package br.com.neocamp.partidas_futebol.repository;

import br.com.neocamp.partidas_futebol.entity.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    Optional<Estadio> findByNome(String nome);

    Page<Estadio> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Estadio> findAll(Pageable pageable);

}
