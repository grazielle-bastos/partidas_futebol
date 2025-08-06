package br.com.neocamp.partidas_futebol.dto.estadioDto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EstadioRequestDto {

    private Long id;

    @NotBlank(message = "O campo nome não pode ser nulo")
    @Size(min = 3, max = 100, message = "O nome do estádio deve ter entre 3 e 100 caracteres")
    private String nome;

    
    public EstadioRequestDto(){}

    
    public EstadioRequestDto(Long id, String nome){
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
