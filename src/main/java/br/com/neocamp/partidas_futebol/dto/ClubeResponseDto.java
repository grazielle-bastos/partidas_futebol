package br.com.neocamp.partidas_futebol.dto;

import java.time.LocalDate;

/**
 * DTO responsável por transportar os dados de saída (resposta/retorno) das operações com clubes.
 *
 * @requisito Utilizado nos endpoints de busca, listagem, cadastro e atualização de clubes. Responde as requisições (GET, POST, PUT, etc.) da API.
 * @fluxo Recebe os dados do clube da camada de serviço e os retorna para o cliente (API).
 *         Garante que apenas os dados necessários sejam expostos na resposta.
 * @implementacao Segue o padrão DTO para separar a camada de apresentação da lógica de negócio.
 *
 * Campos retornados:
 * - id: Identificador do clube.
 * - nome: Nome do clube.
 * - siglaEstado: Sigla do estado brasileiro onde o clube está sediado.
 * - dataCriacao: Data de fundação do clube.
 * - ativo: Situação do clube (ativo/inativo).
 */
public class ClubeResponseDto {

    /**
     * Identificador único do clube.
     * Usado para distinguir cada clube nas respostas da API.
     */
    private Long id;

    /**
     * Nome do clube.
     * Exibido nas respostas para identificação do clube.
     */
    private String nome;

    /**
     * Sigla do estado brasileiro onde o clube está sediado.
     * Exibido nas respostas para identificação do estado.
     */
    private String siglaEstado;

    /**
     * Data de fundação do clube.
     * Exibida nas respostas para informar a data de fundação do clube.
     */
    private LocalDate dataCriacao;

    /**
     * Situação do clube: ativo ou inativo.
     * Exibida nas respostas para informar a situação do clube, indicando se o clube está disponível para operações.
     */
    private Boolean ativo;

    /**
     * Construtor vazio.
     *
     * <p>
     * Necessário para frameworks como Spring e Jackson conseguirem criar o objeto automaticamente ao converter dados da API.
     * Mesmo sem lógica interna, ele permite que o sistema funcione corretamente ao receber dados da API.
     * </p>
     */
    public ClubeResponseDto() {}

    /**
     * Construtor que permite criar um objeto ClubeResponseDto já com todos os atributos preenchidos.
     *
     * <p>
     * Útil quando você já tem todos os dados do clube e quer criar o DTO de uma vez só, sem precisar usar os métodos set.
     * </p>
     *
     * @param id Identificador único do clube.
     * @param nome Nome do clube.
     * @param siglaEstado Sigla do estado brasileiro onde o clube está sediado.
     * @param dataCriacao Data de fundação do clube.
     * @param ativo Situação do clube: ativo ou inativo.
     */
    public ClubeResponseDto(Long id, String nome, String siglaEstado, LocalDate dataCriacao, Boolean ativo) {
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

