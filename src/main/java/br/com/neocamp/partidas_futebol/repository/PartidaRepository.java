package br.com.neocamp.partidas_futebol.repository;

import br.com.neocamp.partidas_futebol.entity.Clube;
import br.com.neocamp.partidas_futebol.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartidaRepository extends JpaRepository<Partida, Long> {


}
