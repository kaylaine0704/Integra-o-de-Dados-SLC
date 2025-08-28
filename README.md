# API To-Do List

API RESTful para gerenciamento de tarefas com histórico de alterações, desenvolvida com Spring Boot e MySQL.

## 📋 Funcionalidades

- ✅ Criar, listar, atualizar e deletar tarefas
- ✅ Gerenciar status das tarefas (PENDENTE, EM_ANDAMENTO, CONCLUIDA)
- ✅ Histórico de alterações de status
- ✅ Validação de dados
- ✅ Documentação automática com Swagger

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.4**
- **MySQL 8**
- **Spring Data JPA**
- **Spring Validation**
- **Swagger/OpenAPI**

## 📦 Instalação e Configuração

### 1. Pré-requisitos

- Java 21 ou superior
- MySQL 8
- Maven
- Postman (para testes)

### 2. Configuração do Banco de Dados

#### Criar o banco de dados:

```sql
CREATE DATABASE todolist;
```

#### Executar o script de criação das tabelas:

```sql
-- Script para criação das tabelas do sistema To-Do List

CREATE TABLE tarefa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    status VARCHAR(20) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_conclusao DATETIME
);

CREATE TABLE historico_tarefa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_tarefa BIGINT NOT NULL,
    status_anterior VARCHAR(20) NOT NULL,
    novo_status VARCHAR(20) NOT NULL,
    data_ocorrencia DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tarefa) REFERENCES tarefa(id)
);
```

### 3. Configuração da Aplicação

Edite o arquivo `src/main/resources/application.properties` com suas credenciais do MySQL:

```properties
# Configuração MySQL Database
spring.datasource.url=jdbc:mysql://localhost:3306/todolist?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Configuração JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Porta do servidor
server.port=8083
```

### 4. Executar a Aplicação

```bash
# Compilar e executar
mvn spring-boot:run

# Ou compilar e executar o JAR
mvn clean package
java -jar target/todolist-0.0.1-SNAPSHOT.jar
```

## 📚 Documentação da API

A documentação da API está disponível através do Swagger UI:

- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8083/v3/api-docs

## 🧪 Testes com Postman

### Collection do Postman

Crie uma nova collection no Postman com os seguintes endpoints:

### 1. Criar Tarefa
- **Método**: POST
- **URL**: `http://localhost:8083/api/tarefas`
- **Body** (JSON):
```json
{
  "titulo": "Comprar leite",
  "descricao": "Comprar 2 litros de leite integral",
  "status": "PENDENTE"
}
```

### 2. Listar Todas as Tarefas
- **Método**: GET
- **URL**: `http://localhost:8083/api/tarefas`

### 3. Buscar Tarefa por ID
- **Método**: GET
- **URL**: `http://localhost:8083/api/tarefas/{id}`

### 4. Atualizar Tarefa
- **Método**: PUT
- **URL**: `http://localhost:8083/api/tarefas/{id}`
- **Body** (JSON):
```json
{
  "titulo": "Comprar leite e pão",
  "descricao": "Comprar 2 litros de leite e 1 pão francês",
  "status": "EM_ANDAMENTO"
}
```

### 5. Deletar Tarefa
- **Método**: DELETE
- **URL**: `http://localhost:8083/api/tarefas/{id}`

### 6. Atualizar Status
- **Método**: PATCH
- **URL**: `http://localhost:8083/api/tarefas/{id}/status?status=CONCLUIDA`

### 7. Buscar por Status
- **Método**: GET
- **URL**: `http://localhost:8083/api/tarefas/status/PENDENTE`

### 8. Buscar por Título
- **Método**: GET
- **URL**: `http://localhost:8083/api/tarefas/buscar?titulo=Comprar`

### 9. Histórico da Tarefa
- **Método**: GET
- **URL**: `http://localhost:8083/api/tarefas/{id}/history`

### 10. Histórico Geral
- **Método**: GET
- **URL**: `http://localhost:8083/api/historico`

## 🔧 Validações

A API inclui validações automáticas:

- Título obrigatório (mínimo 2 palavras, pelo menos 1 letra)
- Status válido (PENDENTE, EM_ANDAMENTO, CONCLUIDA)
- Data de conclusão automática quando status é CONCLUIDA

## 📊 Exemplos de Uso

### Criar uma tarefa:
```bash
curl -X POST http://localhost:8083/api/tarefas \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Estudar Spring Boot",
    "descricao": "Estudar Spring Boot para projeto da faculdade",
    "status": "PENDENTE"
  }'
```

### Atualizar status para concluído:
```bash
curl -X PATCH "http://localhost:8083/api/tarefas/1/status?status=CONCLUIDA"
```

## 🐛 Troubleshooting

### Erro de Conexão com MySQL
Verifique se:
- MySQL está rodando
- Credenciais estão corretas no application.properties
- Banco de dados 'todolist' existe

### Erro de Porta Ocupada
Altere a porta no application.properties ou pare o processo usando a porta 8083.

## 📝 Licença

Este projeto está sob a licença MIT.
