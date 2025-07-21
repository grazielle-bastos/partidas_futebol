package br.com.neocamp.partidas_futebol.repository;

import br.com.neocamp.partidas_futebol.entity.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Define a interface EstadioRepository que estende JpaRepository para realizar operações de CRUD no banco de dados para a entidade Estadio
// A interface JpaRepository já possui métodos prontos para realizar operações de CRUD no banco de dados, como save(), findById(), findAll(), delete(), etc.
// Além disso, ela também possui métodos para realizar consultas personalizadas, como findByNome(), findBySiglaEstado(), etc.
public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    // Metodo que busca estadio por nome (único)
    Optional<Estadio> findByNome(String nome);

}
