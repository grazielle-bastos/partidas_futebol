package br.com.neocamp.partidas_futebol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import br.com.neocamp.partidas_futebol.entity.Clube;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    // Metodo que busca clube por nome exato (único)
    Optional<Clube> findByNome(String nome);

    // Metodo que busca clubes cujo nome contenha a palavra (busca parcial, e case insensitive)
    List<Clube> findByNomeContainingIgnoreCase(String nome);

    // Metodo que busca clubes por sigla do estado
    List<Clube> findBySiglaEstado(String siglaEstado);

    // Metodo que busca clubes ativos usando @Query (JPQL)
    // JPQL é uma linguagem de consulta orientada a objetos, similar ao SQL, mas usando os nomes das entidades e atributos em vez dos nomes das tabelas e colunas
    @Query("SELECT c FROM Clube c WHERE c.ativo = true")
    List<Clube> buscarClubesAtivos();
}
