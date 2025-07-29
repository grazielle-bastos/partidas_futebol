package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDate;

/**
 * Entidade JPA que representa um clube de futebol no sistema.
 *
 * <p>
 * Esta classe é mapeada para a tabela "clube" no banco de dados e contém os principais atributos de um clube,
 * como nome, sigla do estado, data de criação e situação (ativo/inativo).
 * </p>
 *
 * <b>Responsabilidades:</b>
 * <ul>
 *   <li>Mapear os dados do clube para o banco de dados usando JPA/Hibernate.</li>
 *   <li>Permitir operações de cadastro, busca, atualização, inativação, e listagem de clubes.</li>
 * </ul>
 *
 * <b>Campos:</b>
 * <ul>
 *   <li><b>id</b>: Identificador único do clube (chave primária).</li>
 *   <li><b>nome</b>: Nome do clube.</li>
 *   <li><b>siglaEstado</b>: Sigla do estado brasileiro onde o clube está sediado.</li>
 *   <li><b>dataCriacao</b>: Data de fundação do clube.</li>
 *   <li><b>ativo</b>: Situação do clube (ativo ou inativo).</li>
 * </ul>
 *
 * <b>Didática:</b>
 * <ul>
 *   <li>Utilize esta entidade para manipular os dados dos clubes via repositórios Spring Data JPA.</li>
 *   <li>Os atributos são persistidos automaticamente no banco de dados.</li>
 * </ul>
 *
 * Tabela "clube" no banco de dados.
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Entity
@Table(name="clube")
public class Clube {

    /**
     * Identificador único do clube.
     *
     * <p>
     * Mapeado como chave primária da tabela "clube" no banco de dados.
     * Gerado automaticamente pelo banco (estratégia IDENTITY).
     * </p>
     *
     * @jpa @Id indica chave primária. @GeneratedValue define geração automática.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do clube.
     *
     * <p>
     * Campo obrigatório, com limite de 100 caracteres.
     * Persistido na coluna "nome" da tabela.
     * </p>
     *
     * @jpa @Column(nullable = false, length = 100) garante não nulo e tamanho máximo.
     */
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Sigla do estado brasileiro onde o clube está sediado.
     *
     * <p>
     * Campo obrigatório, com exatamente 2 caracteres.
     * Persistido na coluna "sigla_estado".
     * </p>
     *
     * @jpa @Column(name = "sigla_estado", nullable = false, length = 2)
     */
    @Column(name = "sigla_estado", nullable = false, length = 2)
    private String siglaEstado;

    /**
     * Data de fundação do clube.
     *
     * <p>
     * Campo obrigatório, persistido na coluna "data_criacao".
     * </p>
     *
     * @jpa @Column(name = "data_criacao", nullable = false)
     */
    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    /**
     * Situação do clube: ativo ou inativo.
     *
     * <p>
     * Campo obrigatório, usado para indicar se o clube está disponível para operações (soft delete).
     * </p>
     *
     * @jpa @Column(nullable = false)
     */
    @Column(nullable = false)
    private Boolean ativo;

    /**
     * Construtor vazio da entidade Clube.
     *
     * <p>
     * Necessário para o funcionamento do JPA/Hibernate, que utiliza este construtor para instanciar objetos ao recuperar dados do banco.
     * Não recebe parâmetros e não possui lógica interna.
     * </p>
     *
     * <b>Didática:</b> Sempre inclua um construtor vazio em entidades JPA para garantir a compatibilidade com os frameworks de persistência.
     */
    public Clube(){}

    /**
     * Construtor que permite criar um objeto Clube já com todos os atributos principais preenchidos (exceto o id).
     *
     * <p>
     * Útil para instanciar um clube novo antes de salvar no banco de dados, informando nome, sigla do estado, data de criação e situação (ativo/inativo).
     * Não define o id, pois ele será gerado automaticamente pelo banco.
     * </p>
     *
     * @param nome Nome do clube (mínimo 2 letras).
     * @param siglaEstado Sigla do estado brasileiro onde o clube está sediado (ex: SP, RJ).
     * @param dataCriacao Data de fundação do clube (não pode ser futura).
     * @param ativo Situação do clube: ativo ou inativo.
     */
    public Clube(String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    /**
     * Construtor completo da entidade Clube.
     *
     * <p>
     * Permite criar um objeto Clube já com todos os atributos definidos, inclusive o id.
     * É útil em situações onde você precisa instanciar um clube já existente no banco de dados,
     * como em operações de atualização, conversão de dados ou em testes automatizados.
     * </p>
     *
     * @param id Identificador único do clube (geralmente atribuído pelo banco de dados).
     * @param nome Nome do clube (mínimo 2 letras).
     * @param siglaEstado Sigla do estado brasileiro onde o clube está sediado (ex: SP, RJ).
     * @param dataCriacao Data de fundação do clube.
     * @param ativo Situação do clube: ativo ou inativo.
     */
    public Clube(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    /**
     * Acessa e retorna o identificador único do clube.
     *
     * @return id do clube.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do clube.
     *
     * @param id Novo valor para o id do clube.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Acessa e retorna o nome do clube.
     *
     * @return nome do clube.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do clube.
     *
     * @param nome Novo valor para o nome do clube.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Acessa e retorna a sigla do estado brasileiro onde o clube está sediado.
     *
     * @return sigla do estado do clube.
     */
    public String getSiglaEstado() {
        return siglaEstado;
    }

    /**
     * Define a sigla do estado brasileiro onde o clube está sediado.
     *
     * @param siglaEstado Novo valor para a sigla do estado.
     */
    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    /**
     * Acessa e retorna a data de fundação do clube.
     *
     * @return data de fundação do clube.
     */
    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    /**
     * Define a data de fundação do clube.
     *
     * @param dataCriacao Novo valor para a data de fundação do clube.
     */
    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * Acessa e retorna a situação do clube (ativo ou inativo).
     *
     * @return true se o clube está ativo, false se está inativo.
     */
    public Boolean getAtivo() {
        return ativo;
    }

    /**
     * Define a situação do clube (ativo ou inativo).
     *
     * @param ativo Novo valor para a situação do clube.
     */
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
