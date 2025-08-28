package com.meuprojeto.todolist.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API To-Do List")
                        .version("1.0.0")
                        .description("API RESTful para gerenciamento de tarefas com histórico de alterações. " +
                                "Permite criar, listar, atualizar, deletar tarefas e acompanhar o histórico de mudanças de status.")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@meuprojeto.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083")
                                .description("Servidor de desenvolvimento local")
                ));
    }
}
