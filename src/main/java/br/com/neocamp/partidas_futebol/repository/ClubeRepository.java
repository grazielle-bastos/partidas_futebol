package br.com.neocamp.partidas_futebol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import br.com.neocamp.partidas_futebol.entity.Clube;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    // Busca clube por nome exato (único)
    Optional<Clube> findByNome(String nome);

    // Busca clubes cujo nome contenha a palavra (busca parcial, e case insensitive)
    List<Clube> findByNomeContainingIgnoreCase(String nome);

    // Busca clubes por sigla do estado
    List<Clube> findBySiglaEstado(String siglaEstado);

    // Busca clubes ativos usando @Query (JPQL)
    @Query("SELECT c FROM Clube c WHERE c.ativo = true")
    List<Clube> buscarClubesAtivos();
}
