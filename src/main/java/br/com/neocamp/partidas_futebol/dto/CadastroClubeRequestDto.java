package br.com.neocamp.partidas_futebol.dto;

import java.time.LocalDate;

public class CadastroClubeRequestDto {

    private int id;

    private String nome;

    private String estado;

    private LocalDate data;

    private Boolean status;

    public CadastroClubeRequestDto(int id, String nome, String estado, LocalDate data, Boolean status) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.data = data;
        this.status = status;
    }

    public int getIdClube(){
        return id;
    }

    public void setIdClube(int id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public LocalDate getData(){
        return data;
    }

    public void setData(LocalDate data){
        this.data = data;
    }

    public Boolean getStatus(){
        return status;
    }

    public void setStatus(Boolean status){
        this.status = status;
    }

}
