package br.com.neocamp.partidas_futebol.dto;

/**
 * DTO (Data Transfer Object) responsável por representar os dados de requisição de um estádio.
 * Este DTO é utilizado para receber os dados do estádio na requisição da API, permitindo a conversão automática do JSON recebido em um objeto Java.
 *
 * @requisito Utilizado nos endpoints de cadastro, edição, inativação e busca de estádios. Recebe as requisições (POST, PUT, DELETE, etc.) da API.
 * @fluxo Recebe os dados do estádio enviados pelo cliente (API), valida e repassa para a camada de serviço.
 * @implementacao Segue o padrão DTO para separar a camada de apresentação da lógica de negócio.
 *
 * Campos esperados:
 * - id: Identificador do estádio (usado em atualizações e inativações)
 * - nome: Nome do estádio (obrigatório)
 * <p>
 *     Este DTO é essencial para garantir que os dados recebidos na API estejam no formato correto e possam ser processados pela aplicação.
 *    </p>
 */
public class EstadioRequestDto {

    /**
     * Identificador único do estádio.
     * Usado em operações de atualização e inativação.
     */
    private Long id;

    /**
     * Nome do estádio.
     * Deve ser informado e não pode estar vazio.
     */
    private String nome;

    /**
     * Construtor vazio.
     * <p>
     *     Necessário para frameworks como Spring e Jackson conseguirem criar o objeto automaticamente ao converter dados da API.
     *     Mesmo sem lógica interna, ele permite que o sistema funcione corretamente ao receber dados da API.
     * </p>
     * @requisito Permite a criação de instâncias do DTO sem precisar passar parâmetros, facilitando a desserialização de JSON.
     * @fluxo Utilizado pelo Spring para criar o objeto automaticamente ao receber dados da API.
     * @implementacao Necessário para que o Spring e outras bibliotecas de serialização/deserialização consigam instanciar o DTO corretamente.
     */
    public EstadioRequestDto(){}

    /**
     * Construtor com parâmetros para inicializar o DTO de requisição de estádio.
     *
     * <p>
     * Útil quando é necessário criar um DTO já com os dados preenchidos, como ao editar ou buscar um estádio específico, sem precisar usar os métodos setters.
     * </p>
     *
     * @param id Identificador do estádio (usado em atualizações e inativações).
     * @param nome Nome do estádio (obrigatório).
     */
    public EstadioRequestDto(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    /**
     * Obtém o identificador único do estádio.
     * @return Identificador do estádio.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador único do estádio.
     * @param id Identificador do estádio a ser definido.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do estádio.
     * @return Nome do estádio.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do estádio.
     * @param nome Nome do estádio a ser definido.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


}
