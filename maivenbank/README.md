# MaivenBank - Sistema Bancário

Sistema bancário desenvolvido com Spring Boot, seguindo as melhores práticas de arquitetura MVC, SOLID, Clean Code e Design Patterns.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- SQL Server
- Maven
- Lombok

## Arquitetura

O projeto segue a arquitetura MVC (Model-View-Controller) com as seguintes camadas:

- **Model**: Entidades e DTOs que representam os dados do sistema
- **Repository**: Interfaces para acesso a dados utilizando Spring Data JPA
- **Service**: Lógica de negócios do sistema
- **Controller**: Endpoints da API REST para comunicação com o cliente

## Estrutura de Pastas

```
src/main/java/com/maivenbank/
├── config/             # Configurações do Spring
├── controller/         # Controladores REST
├── dto/                # Objetos de Transferência de Dados
├── exception/          # Exceções personalizadas
├── model/              # Entidades JPA
├── repository/         # Repositórios Spring Data
├── service/            # Interfaces de serviço
│   └── impl/           # Implementações de serviço
└── util/               # Classes utilitárias
```

## Entidades Principais

1. **Cliente**: Representa o cliente do banco
2. **Conta**: Representa as contas bancárias dos clientes (Corrente, Poupança, etc.)
3. **Transacao**: Representa as transações realizadas nas contas (Depósito, Saque, Transferência, etc.)

## Funcionalidades

- Cadastro e gerenciamento de clientes
- Criação e gerenciamento de contas bancárias
- Operações bancárias:
  - Depósito
  - Saque
  - Transferência entre contas
- Histórico de transações
- Consulta de saldo

## Configuração do Banco de Dados

O sistema utiliza o SQL Server como banco de dados. As credenciais de acesso são configuradas no arquivo `application.properties`.

```properties
spring.datasource.url=jdbc:sqlserver://OI\\SQLSERVEREXPRESS:1433;databaseName=maivenbank;encrypt=false
spring.datasource.username=sa
spring.datasource.password=1234
```

## Como Executar

1. Clone o repositório
2. Configure o banco de dados SQL Server conforme especificado no arquivo `application.properties`
3. Execute o comando Maven para compilar o projeto:

```bash
mvn clean install
```

4. Execute a aplicação:

```bash
mvn spring-boot:run
```

5. A aplicação estará disponível em http://localhost:8080

## Endpoints da API

### Clientes

- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `GET /api/clientes/cpf/{cpf}` - Buscar cliente por CPF
- `POST /api/clientes` - Criar um novo cliente
- `PUT /api/clientes/{id}` - Atualizar um cliente existente
- `DELETE /api/clientes/{id}` - Excluir um cliente

### Contas

- `GET /api/contas` - Listar todas as contas
- `GET /api/contas/{id}` - Buscar conta por ID
- `GET /api/contas/numero/{numero}` - Buscar conta por número
- `GET /api/contas/cliente/{clienteId}` - Listar contas de um cliente
- `POST /api/contas` - Criar uma nova conta
- `PUT /api/contas/{id}` - Atualizar uma conta existente
- `DELETE /api/contas/{id}` - Excluir uma conta
- `POST /api/contas/{numero}/deposito?valor=XXX` - Realizar um depósito
- `POST /api/contas/{numero}/saque?valor=XXX` - Realizar um saque
- `POST /api/contas/transferencia?numeroOrigem=XXX&numeroDestino=YYY&valor=ZZZ` - Realizar uma transferência

### Transações

- `GET /api/transacoes` - Listar todas as transações
- `GET /api/transacoes/{id}` - Buscar transação por ID
- `GET /api/transacoes/conta/{contaId}` - Listar transações de uma conta
- `GET /api/transacoes/conta/numero/{numeroConta}` - Listar transações por número da conta
- `POST /api/transacoes` - Registrar uma nova transação

## Licença

Este projeto está licenciado sob a licença MIT. 