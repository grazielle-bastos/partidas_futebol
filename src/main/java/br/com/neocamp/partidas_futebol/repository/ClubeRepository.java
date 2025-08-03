package br.com.neocamp.partidas_futebol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import br.com.neocamp.partidas_futebol.entity.Clube;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ClubeRepository extends JpaRepository<Clube, Long> {

    
    List<Clube> findByNomeContainingIgnoreCaseAndSiglaEstadoAndAtivo(String nome, String siglaEstado, Boolean ativo);

    
    List<Clube> findByNomeContainingIgnoreCase(String nome);

    
    List<Clube> findByNomeContainingIgnoreCaseAndSiglaEstado(String nome, String siglaEstado);

    
    Optional<Clube> findByNomeAndSiglaEstado(String nome, String siglaEstado);

    
    List<Clube> findByNomeContainingIgnoreCaseAndAtivo (String nome, Boolean ativo);

    
    List<Clube> findBySiglaEstado(String siglaEstado);

    
    List<Clube> findByAtivo(Boolean ativo);

    
    List<Clube> findBySiglaEstadoAndAtivo (String siglaEstado, Boolean ativo);

    
    @Query("SELECT c FROM Clube c WHERE" +
    "(:nome IS NULL OR c.nome LIKE %:nome%) AND" +
    "(:siglaEstado IS NULL OR c.siglaEstado = :siglaEstado) AND" +
    "(:ativo IS NULL OR c.ativo = :ativo)")
    Page<Clube> buscarClubesPorPaginacao(
            @Param("nome") String nome,
            @Param("siglaEstado") String siglaEstado,
            @Param("ativo") Boolean ativo,
            Pageable pageable
    );
}
