package br.com.neocamp.partidas_futebol.entity;

import jakarta.persistence.*;

/**
 * Entidade JPA que representa um estádio de futebol no sistema.
 *
 * <p>
 *     Esta classe é mapeada para a tabela "estadio" no banco de dados e contém os principais atributos de um estádio,
 *     como nome.
 * </p>
 *
 * <b>Responsabilidades:</b>
 * <ul>
 *     <li>Mapear os dados do estádio para o banco de dados usando JPA/Hibernate.</li>
 *     <li>Permitir operações de cadastro, busca, atualização, inativação, e listagem de estádios.</li>
 * </ul>
 *
 * <b>Campos:</b>
 * <ul>
 *     <li><b>id</b>: Identificador único do estádio (chave primária).</li>
 *     <li><b>nome</b>: Nome do estádio.</li>
 *     <li><b>unique</b>: O nome do estádio deve ser único no banco de dados.</li>
 * </ul>
 *
 * <b>Didática:</b>
 * <ul>
 *     <li>Utilize esta entidade para manipular os dados dos estádios via repositórios Spring Data JPA.</li>
 *     <li>Os atributos são persistidos automaticamente no banco de dados.</li>
 * </ul>
 *
 * Tabela "estadio" no banco de dados.
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Entity
@Table(name = "estadio",
       uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Estadio {

    /**
     * Identificador único do estádio.
     *
     * <p>
     *     Mapeado como chave primária da tabela "estadio" no banco de dados.
     *     Gerado automaticamente pelo banco (estratégia IDENTITY).
     * </p>
     * @jpa @Id indica chave primária. @GeneratedValue define geração automática.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do estádio.
     *
     * <p>
     *     Este campo é obrigatório e deve ser único no banco de dados.
     *     Ele representa o nome oficial do estádio de futebol.
     * </p>
     *
     * @jpa @Column(nullable = false, length = 100, unique = true) define as restrições do campo no banco de dados.
     */
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    /**
     * Construtor padrão da entidade Estadio.
     *
     * <p>
     *     Utilizado pelo JPA para criar instâncias da entidade.
     *     Não deve ser utilizado diretamente no código da aplicação.
     *     Não possui parâmetros e inicializa os atributos com valores padrão.
     * </p>
     *
     * <b>Didática:</b>
     * <ul>
     *     <li>Utilize este construtor apenas para instâncias criadas pelo JPA.</li>
     *     <li>Evite instanciá-lo diretamente no código da aplicação.</li>
     *     <li>Os atributos devem ser configurados através dos setters ou diretamente após a instância ser criada.</li>
     * </ul>
     */
    public Estadio() {}

    /**
     * Construtor com o nome do estádio.
     * @param nome Nome do estádio a ser atribuído à instância.
     *             <p>
     *             Este construtor é utilizado para criar uma instância de Estadio com o nome fornecido.
     *             </p>
     */
    public Estadio(String nome) {
        this.nome = nome;
    }

    /**
     * Construtor completo da entidade Estádio.
     *
     * <p>
     *     Utilizado para criar uma instância de Estadio com ID e nome fornecidos.
     *     Este construtor é útil para cenários onde o ID já é conhecido (por exemplo, ao recuperar um estádio existente do banco de dados).
     *
     * @param id Identificador único do estádio (geralmente gerado pelo banco de dados).
     * @param nome Nome do estádio a ser atribuído à instância.
     */
    public Estadio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Obtém o identificador único do estádio.
     * @return O ID do estádio.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do estádio.
     * @param id O ID do estádio a ser atribuído.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do estádio.
     * @return O nome do estádio.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do estádio.
     * @param nome O nome do estádio a ser atribuído.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

}
