package br.com.neocamp.partidas_futebol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import br.com.neocamp.partidas_futebol.entity.Clube;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    // Busca clubes por nome
    List<Clube> findByNome(String nome);

    // Busca clubes por sigla do estado
    List<Clube> findBySiglaEstado(String siglaEstado);

    // Busca clubes ativos usando @Query (JPQL)
    @Query("SELECT c FROM Clube c WHERE c.ativo = true")
    List<Clube> buscarClubesAtivos();
}
