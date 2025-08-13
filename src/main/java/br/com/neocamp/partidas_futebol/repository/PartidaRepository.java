package br.com.neocamp.partidas_futebol.repository;

import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    List<Partida> findByClubeMandanteId(Long clubeMandanteId);

    List<Partida> findByClubeVisitanteId(Long clubeVisitanteId);

    Page<Partida> findByClubeMandanteIdAndClubeVisitanteIdAndEstadioId(Long clubeMandanteId, Long clubeVisitanteId, Long estadioId, Pageable pageable);

    Page<Partida> findByClubeMandanteIdAndEstadioId(Long clubeMandanteId, Long estadioId, Pageable pageable);

    Page<Partida> findByClubeVisitanteIdAndEstadioId(Long clubeVisitanteId, Long estadioId, Pageable pageable);

    Page<Partida> findByClubeMandanteId(Long clubeMandanteId, Pageable pageable);

    Page<Partida> findByClubeVisitanteId(Long clubeVisitanteId, Pageable pageable);

    Page<Partida> findByEstadioId(Long estadioId, Pageable pageable);

    Page<Partida> findAll(Pageable pageable);

}
