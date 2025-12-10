# Tasks API

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-brightgreen)
![Maven](https://img.shields.io/badge/Maven-4.0.0-blue)
![MySQL](https://img.shields.io/badge/Database-PostgreSQL-orange)
![JPA](https://img.shields.io/badge/JPA-Hibernate-yellow)
![Docker](https://img.shields.io/badge/Docker-enabled-2496ED)
![Tests](https://img.shields.io/badge/Tests-JUnit5-lightgrey)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-purple)

API REST profissional para gerenciamento de tarefas, construída com **Java 21**, **Spring Boot 3**, arquitetura limpa em camadas e integração com banco de dados **PostgreSQL**.

---

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3.3**
* **Spring Web**
* **Spring Data JPA**
* **PostgreSQL**
* **Docker & Docker Compose**
* **Maven**
* **JUnit 5**
* **Lombok**

---

## 📌 Funcionalidades

* Criar tarefas
* Listar tarefas
* Buscar tarefa por ID
* Atualizar tarefa
* Exclusão lógica
* Paginação e ordenação
* Validações avançadas
* Camadas separadas para Controller, Service, Repository e DTOs

---

## 📁 Estrutura principal do Projeto

```
src/main/java/com/seuprojeto/tasks
├── controller
├── service
├── repository
├── model
├── dto
└── exception
```

---

## 🗄 Banco de Dados

A API utiliza **PostgreSQL**, com criação automática de tabelas via JPA.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:postgresql://localhost:3306/tasksdb
spring.datasource.username=root
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=update
```

---

## 🧪 Testes

Testes unitários implementados com:

* JUnit 5
* Mockito
* Spring Boot Test

---

## 🐳 Docker

A aplicação possui suporte total ao Docker.

### Docker Compose

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: tasksdb
    ports:
      - "3306:3306"
```

---

## ▶️ Como Executar

Clone o repositório:

```
git clone https://github.com/seuusuario/tasks-api.git
```

Execute com Maven:

```
mvn spring-boot:run
```

---

## 📫 Endpoints Principais

### Criar tarefa

```
POST /api/tasks
```

### Buscar todas

```
GET /api/tasks
```

### Buscar por ID

```
GET /api/tasks/{id}
```

### Atualizar

```
PUT /api/tasks/{id}
```

### Exclusão lógica

```
DELETE /api/tasks/{id}
```

---

## 🏗 Futuras Implementações

* Filtros por status
* Autenticação (JWT)
* Logs avançados
* Testes de integração 100%

---

## 📜 Licença

Distribuído sob a licença MIT.

---

## 👨‍💻 Autor

Projeto desenvolvido por **Alessando Nascimento** e **John Helder** com foco em arquitetura robusta e boas práticas profissionais.
 