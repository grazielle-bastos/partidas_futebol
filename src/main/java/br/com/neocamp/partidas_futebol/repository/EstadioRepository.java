package br.com.neocamp.partidas_futebol.repository;

import br.com.neocamp.partidas_futebol.entity.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade Estádio.
 *
 * <p>
 *     Responsável por acessar e manipular os dados dos estádios no banco de dados.
 *     Oferece métodos prontos para buscas simples e consultas customizadas.
 * </p>
 *
 * <b>Didática:</b>
 * <ul>
 *     <li>Utilize este repositório para consultar, salvar, atualizar e listar estádios.</li>
 *     <li>Os métodos seguem o padrão de nomenclatura do Spring Data, facilitando a criação automática das queries.</li>
 * </ul>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    /**
     * Busca um estádio pelo nome exato.
     *
     * @param nome Nome do estádio a ser buscado.
     * @return Um Optional contendo o estádio encontrado, ou vazio se não existir.
     */
    Optional<Estadio> findByNome(String nome);

}
