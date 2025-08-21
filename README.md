# To-Do List API

API RESTful para gerenciamento de tarefas com histórico de alterações.

## Tecnologias
- Java 17+
- Spring Boot
- MySQL 8
- Maven
- Swagger/OpenAPI

## Como rodar o projeto

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   ```
2. Configure o banco de dados MySQL:
   - Execute o script `src/main/resources/database/schema.sql` para criar as tabelas.
   - Ajuste o arquivo `src/main/resources/application.properties` com seu usuário e senha do MySQL.
3. Compile e execute o projeto:
   ```bash
   mvn spring-boot:run
   ```
4. Acesse a documentação da API:
   - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Exemplos de requisições

### Criar tarefa
```bash
curl -X POST http://localhost:8080/tarefas -H "Content-Type: application/json" -d '{"titulo": "Comprar camisa branca", "descricao": "Ir ao shopping", "status": "PENDENTE"}'
```

### Listar tarefas
```bash
curl http://localhost:8080/tarefas
```

### Atualizar status da tarefa
```bash
curl -X PATCH "http://localhost:8080/tarefas/1/status?status=CONCLUIDA"
```

### Ver histórico da tarefa
```bash
curl http://localhost:8080/tarefas/1/history
```

## Scripts SQL
Os scripts para criação do banco estão em `src/main/resources/database/schema.sql`.

---

Qualquer dúvida, consulte a documentação Swagger ou entre em contato!
