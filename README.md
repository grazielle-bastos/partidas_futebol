# API de Partidas de Futebol

Projeto em Java com Spring Boot para gerenciar clubes, estádios e partidas de futebol.

## Tecnologias Utilizadas

- **[Java 17+](https://dev.java/learn/)**: Linguagem principal do projeto.
- **[Spring Boot](https://spring.io/projects/spring-boot)**: Framework para facilitar o desenvolvimento de aplicações Java.
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: Simplifica o acesso a dados com JPA.
- **[JUnit](https://junit.org/junit5/) e [Mockito](https://site.mockito.org/)**: Ferramentas para testes automatizados.
- **[MySQL](https://dev.mysql.com/doc/)**: Banco de dados relacional.

## Como Executar o Projeto

### 1. Clone o repositório

```bash
git clone https://github.com/grazielle-bastos/partidas_futebol.git
cd partidas_futebol
```

### 2. Configure o Banco de Dados

- Crie um banco de dados MySQL chamado `futebol`.
- Certifique-se de que as tabelas necessárias estejam criadas (o Spring Boot pode criar automaticamente se configurado).

### 3. Configure o acesso ao MySQL

Edite o arquivo `src/main/resources/application.properties` com seu usuário e senha do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/futebol
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 4. Execute o Projeto

```bash
./mvnw spring-boot:run
```

A API ficará disponível em [http://localhost:8080](http://localhost:8080).

## Referência

Este projeto foi desenvolvido com apoio e orientação da [Teamcubation](https://teamcubation.com/), seguindo o seu documento de requisitos "Projeto Final".

Agradecimentos à Teamcubation pelo incentivo e suporte durante o desenvolvimento deste projeto.
