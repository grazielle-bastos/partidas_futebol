package br.com.neocamp.partidas_futebol.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
/**
 * DTO responsável por transportar os dados de entrada (define/modifica) para operações de criação, atualização e inativação (soft delete) de clubes.
 *
 * @requisito Utilizado nos endpoints de cadastro, edição e inativação de clubes. Recebe as requisições (POST, PUT, DELETE, etc.) da API.
 * @fluxo Recebe os dados do clube enviados pelo cliente (API), valida e repassa para a camada de serviço.
 *         No caso de inativação, o campo 'ativo' é utilizado para marcar o clube como inativo, sem excluir do banco.
 * @implementacao Segue o padrão DTO para separar a camada de apresentação da lógica de negócio.
 *
 * Campos esperados:
 * - id: Identificador do clube (usado em atualizações e inativações)
 * - nome: Nome do clube (mínimo 2 letras)
 * - siglaEstado: Sigla do estado brasileiro (ex: SP, RJ)
 * - dataCriacao: Data de fundação do clube (não pode ser futura)
 * - ativo: Situação do clube (ativo/inativo, usado também para soft delete)
 */
public class ClubeRequestDto {

    /**
     * Identificador único do clube.
     * Usado em operações de atualização e inativação.
     */
    private Long id;

    /**
     * Nome do clube.
     * Deve conter pelo menos 2 letras.
     */
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, message = "Nome deve ter no mínimo 2 letras")
    private String nome;

    /**
     * Sigla do estado brasileiro onde o clube está sediado (ex: SP, RJ).
     */
    @NotBlank(message = "Sigla do estado é obrigatória")
    @Pattern(regexp = "AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO", message = "Sigla do estado inválida")
    private String siglaEstado;

    /**
     * Data de fundação do clube.
     * Não pode ser uma data futura.
     */
    @NotNull(message = "Data de criação é obrigatória")
    @PastOrPresent(message = "Data de criação não pode ser futura")
    private LocalDate dataCriacao;

    /**
     * Situação do clube: ativo ou inativo.
     * Usado também para operações de inativação (soft delete).
     */
    private Boolean ativo;

    /**
     * Construtor vazio.
     *
     * <p>
     * Necessário para frameworks como Spring e Jackson conseguirem criar o objeto automaticamente.
     * Mesmo sem lógica interna, ele permite que o sistema funcione corretamente ao receber dados da API.
     * </p>
     */
    public ClubeRequestDto() {}

    /**
     * Construtor que permite criar um objeto ClubeRequestDto já com todos os atributos preenchidos.
     *
     * <p>
     * Útil quando você já tem todos os dados do clube e quer criar o DTO de uma vez só, sem precisar usar os métodos set.
     * </p>
     *
     * @param id Identificador único do clube (usado em criações, atualizações e inativações).
     * @param nome Nome do clube (mínimo 2 letras).
     * @param siglaEstado Sigla do estado brasileiro onde o clube está sediado (ex: SP, RJ).
     * @param dataCriacao Data de fundação do clube (não pode ser futura).
     * @param ativo Situação do clube: ativo ou inativo.
     */
    public ClubeRequestDto(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
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
     * @return siglaEstado do clube.
     */
    public String getSiglaEstado() {
        return siglaEstado;
    }

    /**
     * Define a sigla do estado brasileiro onde o clube está sediado.
     *
     * @param siglaEstado Novo valor para a sigla do estado do clube.
     */
    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    /**
     * Acessa e retorna a data de fundação do clube.
     *
     * @return dataCriacao do clube.
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
     * Acessa e retorna a situação do clube.
     *
     * @return ativo do clube.
     */
    public Boolean getAtivo() {
        return ativo;
    }

    /**
     * Define a situação do clube.
     *
     * @param ativo Novo valor para a situação do clube.
     */
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
