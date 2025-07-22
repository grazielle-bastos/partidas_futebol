package br.com.neocamp.partidas_futebol.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import br.com.neocamp.partidas_futebol.entity.Clube;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositório Spring Data JPA para a entidade Clube.
 *
 * <p>
 * Responsável por acessar e manipular os dados dos clubes no banco de dados.
 * Oferece métodos prontos para buscas simples e consultas customizadas para filtros avançados.
 * </p>
 *
 * <b>Didática:</b>
 * <ul>
 *   <li>Utilize este repositório para consultar, salvar, atualizar e listar clubes.</li>
 *   <li>Os métodos seguem o padrão de nomenclatura do Spring Data, facilitando a criação automática das queries.</li>
 * </ul>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface ClubeRepository extends JpaRepository<Clube, Long> {

    /**
     * Busca clubes pelo nome (parcial, ignorando maiúsculas/minúsculas), sigla do estado e situação (ativo/inativo).
     *
     * @param nome Parte do nome do clube para busca.
     * @param siglaEstado Sigla do estado do clube.
     * @param ativo Situação do clube (true para ativo, false para inativo).
     * @return Lista de clubes que atendem aos filtros.
     */
    List<Clube> findByNomeContainingIgnoreCaseAndSiglaEstadoAndAtivo(String nome, String siglaEstado, Boolean ativo);

    /**
     * Busca clubes cujo nome contenha a palavra informada (parcial, ignorando maiúsculas/minúsculas).
     *
     * @param nome Parte do nome do clube.
     * @return Lista de clubes encontrados.
     */
    List<Clube> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca clubes pelo nome (parcial) e sigla do estado.
     *
     * @param nome Parte do nome do clube.
     * @param siglaEstado Sigla do estado.
     * @return Lista de clubes encontrados.
     */
    List<Clube> findByNomeContainingIgnoreCaseAndSiglaEstado(String nome, String siglaEstado);

    /**
     * Busca clube por nome exato e sigla do estado (usado para validar duplicidade).
     *
     * @param nome Nome exato do clube.
     * @param siglaEstado Sigla do estado.
     * @return Optional contendo o clube, se encontrado.
     */
    Optional<Clube> findByNomeAndSiglaEstado(String nome, String siglaEstado);

    /**
     * Busca clubes pelo nome (parcial) e situação (ativo/inativo).
     *
     * @param nome Parte do nome do clube.
     * @param ativo Situação do clube.
     * @return Lista de clubes encontrados.
     */
    List<Clube> findByNomeContainingIgnoreCaseAndAtivo (String nome, Boolean ativo);

    /**
     * Busca clubes pela sigla do estado.
     *
     * @param siglaEstado Sigla do estado.
     * @return Lista de clubes encontrados.
     */
    List<Clube> findBySiglaEstado(String siglaEstado);

    /**
     * Busca clubes pela situação (ativo/inativo).
     *
     * @param ativo Situação do clube.
     * @return Lista de clubes encontrados.
     */
    List<Clube> findByAtivo(Boolean ativo);

    /**
     * Busca clubes pela sigla do estado e situação (ativo/inativo).
     *
     * @param siglaEstado Sigla do estado.
     * @param ativo Situação do clube.
     * @return Lista de clubes encontrados.
     */
    List<Clube> findBySiglaEstadoAndAtivo (String siglaEstado, Boolean ativo);

    /**
     * Consulta customizada (JPQL) para buscar clubes com filtros opcionais (nome, estado, ativo) e suporte à paginação.
     *
     * <p>
     * Permite buscas flexíveis, retornando uma página de resultados conforme os filtros informados.
     * </p>
     *
     * @param nome (opcional) Parte do nome do clube.
     * @param siglaEstado (opcional) Sigla do estado.
     * @param ativo (opcional) Situação do clube.
     * @param pageable Parâmetros de paginação e ordenação.
     * @return Página de clubes encontrados.
     */
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
