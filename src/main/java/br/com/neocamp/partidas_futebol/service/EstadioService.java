package br.com.neocamp.partidas_futebol.service;

import br.com.neocamp.partidas_futebol.dto.EstadioRequestDto;
import br.com.neocamp.partidas_futebol.dto.EstadioResponseDto;
import br.com.neocamp.partidas_futebol.entity.Estadio;
import br.com.neocamp.partidas_futebol.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class EstadioService {

    // Injeção de dependência do repository de estádio no service
    // O atributo é final para garantir que ele não será alterado após a injeção de dependência
    private final EstadioRepository estadioRepository;

    // Construtor com injeção de dependência do repository de estádio no service
    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        // Atribui o repository de estádio recebido no construtor ao atributo do service
        this.estadioRepository = estadioRepository;
    }

    // Metodo para salvar um estádio
    // Recebe o DTO do estádio pelo corpo da requisição
    public EstadioResponseDto salvar(EstadioRequestDto estadioDto) {
        // Validações de negócio
        if (estadioDto.getNome() == null || estadioDto.getNome().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome inválido: deve ter no mínimo 3 caracteres");
        }
        if (estadioRepository.findByNome(estadioDto.getNome().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com o mesmo nome");
        }
        // Cria um novo estádio com os dados do DTO
        Estadio estadio = new Estadio(
                estadioDto.getNome().trim()
                );
        // Salva o estádio no banco de dados e retorna o DTO de resposta de estádio
        return toResponseDto(estadioRepository.save(estadio));
    }

    // Metodo para buscar um estádio pelo ID
    public EstadioResponseDto buscarPorId(Long id) {
        // Chama o repository para buscar o estádio pelo ID
        // O metodo findById retorna um Optional, então é necessário usar o metodo get() para obter o estádio caso ele exista
        Optional<Estadio> estadioOptional = estadioRepository.findById(id);
        // Verifica se o estádio foi encontrado, se sim, retorna o DTO de resposta de estádio, se não, lança uma exceção com status 404 NOT FOUND
        if (estadioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado");
        }
        // Retorna o DTO de resposta de estádio com os dados do estádio encontrado
        // O metodo get() é usado para obter o estádio do Optional, e o metodo toResponseDto() é usado para converter o estádio em DTO de resposta de estádio
        return toResponseDto(estadioOptional.get());
    }

    // Metodo auxiliar para converter uma entidade de estádio – recebida como parâmetro – em DTO de resposta de estádio
    private EstadioResponseDto toResponseDto(Estadio estadio) {
        // Retorna um DTO de resposta de estádio com os dados da entidade de estádio
        // O construtor do DTO de resposta de estádio é chamado com os atributos da entidade de estádio
        return new EstadioResponseDto(estadio.getId(), estadio.getNome());
    }

}
