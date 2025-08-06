package br.com.neocamp.partidas_futebol.dto.estadioDto;


public class EstadioResponseDto {

    
    private Long id;

    
    private String nome;

    
    public EstadioResponseDto(){}

    
    public EstadioResponseDto(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getNome() {
        return nome;
    }

    
    public void setNome(String nome) {
        this.nome = nome;
    }

}
