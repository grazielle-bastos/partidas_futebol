package br.com.neocamp.partidas_futebol.repository;

import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    List<Partida> findByClubeMandanteId(Long clubeMandanteId);

    List<Partida> findByClubeVisitanteId(Long clubeVisitanteId);

}
