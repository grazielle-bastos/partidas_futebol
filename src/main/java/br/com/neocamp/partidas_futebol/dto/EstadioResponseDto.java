package br.com.neocamp.partidas_futebol.dto;

/**
 * DTO responsável por transportar os dados de saída (resposta/retorno) das operações com estádios.
 *
 * @requisito Utilizado nos endpoints de busca, listagem, cadastro e atualização de estádios. Responde as requisições (GET, POST, PUT, etc.) da API.
 * @fluxo Recebe os dados do estádio da camada de serviço e os retorna para o cliente (API).
 *         Garante que apenas os dados necessários sejam expostos na resposta.
 * @implementacao Segue o padrão DTO para separar a camada de apresentação da lógica de negócio
 *
 * Campos retornados:
 * - id: Identificador do estádio.
 * - nome: Nome do estádio.
 */
public class EstadioResponseDto {

    /**
     * Identificador único do estádio.
     * Usado para distinguir cada estádio nas respostas da API.
     */
    private Long id;

    /**
     * Nome do estádio.
     * Exibido nas respostas para identificação do estádio.
     */
    private String nome;

    /**
     * Construtor vazio.
     *
     * <p>
     *     Necessário para frameworks como Spring e Jackson conseguirem criar o objeto automaticamente ao converter dados da API.
     *     Mesmo sem lógica interna, ele permite que o sistema funcione corretamente ao receber dados da API.
     * </p>
     * @requisito Permite a criação de instâncias do DTO sem precisar passar parâmetros, facilitando a desserialização de JSON.
     * @fluxo Utilizado pelo Spring para criar o objeto automaticamente ao receber dados da API.
     * @implementacao Necessário para que o Spring e outras bibliotecas de serialização/deserialização consigam instanciar o DTO corretamente.
     */
    public EstadioResponseDto(){}

    /**
     * Construtor com parâmetros para inicializar o DTO de resposta de estádio com os dados necessários.
     *
     * <p>
     *     Útil quando é necessário criar uma instância do DTO já com os dados preenchidos, como ao retornar um estádio após cadastro ou atualização, sem precisar usar os métodos setter.
     * </p>
     *
     * @param id Identificador único do estádio
     * @param nome Nome do estádio
     */
    public EstadioResponseDto(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    /**
     * Obtém o identificador único do estádio.
     * @return id do estádio.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do estádio.
     * @param id Novo valor para o id do estádio.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do estádio.
     * @return nome do estádio.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do estádio.
     * @param nome Novo valor para o nome do estádio.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

}
