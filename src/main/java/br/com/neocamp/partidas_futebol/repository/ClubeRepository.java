package br.com.neocamp.partidas_futebol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import br.com.neocamp.partidas_futebol.entity.Clube;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Repository de Clube que extende JpaRepository e JpaSpecificationExecutor
public interface ClubeRepository extends JpaRepository<Clube, Long> {
    // Metodo que busca clube por nome exato (único)
    //Optional<Clube> findByNome(String nome);

    // Metodo que busca clube por nome e sigla do estado e ativo ou inativo
    List<Clube> findByNomeContainingIgnoreCaseAndSiglaEstadoAndAtivo(String nome, String siglaEstado, Boolean ativo);

    // Metodo que busca clubes cujo nome contenha a palavra (busca parcial, e case insensitive)
    List<Clube> findByNomeContainingIgnoreCase(String nome);

    // Metodo que busca clubes por nome e sigla do estado
    List<Clube> findByNomeContainingIgnoreCaseAndSiglaEstado(String nome, String siglaEstado);

    // Metodo que busca clubes por nome e sigla do estado (único)
    Optional<Clube> findByNomeAndSiglaEstado(String nome, String siglaEstado);

    // Metodo que busca clubes por nome e ativo ou inativo
    List<Clube> findByNomeContainingIgnoreCaseAndAtivo (String nome, Boolean ativo);

    // Metodo que busca clubes por sigla do estado
    List<Clube> findBySiglaEstado(String siglaEstado);

    // Metodo que busca clubes por ativo ou inativo
    List<Clube> findByAtivo(Boolean ativo);

    // Metodo que busca clubes por sigla do estado e ativo ou inativo
    List<Clube> findBySiglaEstadoAndAtivo (String siglaEstado, Boolean ativo);

    // Consulta JPQL simples, filtrando por nome, estado e ativo (todos opcionais) com paginação
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
