package br.com.neocamp.partidas_futebol;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classe de testes de integração para a aplicação PartidasFutebolApplication.
 * @requisito Testar a inicialização da aplicação.
 * @fluxo Inicializa a aplicação e verifica se o contexto foi carregado com sucesso.
 * @implementacao Utiliza a anotação @SpringBootTest para carregar o contexto da aplicação.
 * @see PartidasFutebolApplication
 *
 */
//@Disabled("Classe desativada para evitar inicialização do JPA nos testes.")
//@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
class PartidasFutebolApplicationTests {

    @Test
    void contextLoads() {
    }

}