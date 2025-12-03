# Daily IU Services - Backend API

API REST desenvolvida em Kotlin com Spring Boot para o sistema Daily IU, uma plataforma de suporte para pessoas com incontinência urinária.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando o Projeto](#executando-o-projeto)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Documentação da API](#documentação-da-api)
- [Testes](#testes)
- [Desenvolvimento](#desenvolvimento)
- [Desenvolvedores](#desenvolvedores)

## 🎯 Sobre o Projeto

O Daily IU Services é uma API REST que fornece serviços backend para uma plataforma de suporte à saúde, focada em pessoas com incontinência urinária. A aplicação oferece funcionalidades como:

- **Autenticação e Gerenciamento de Usuários**: Sistema de login com JWT, recuperação de senha e perfis de usuário
- **Rede Social**: Criação, edição e interação com conteúdo (posts, comentários, curtidas)
- **Exercícios e Treinos**: Gerenciamento de exercícios, treinos e planos de treino personalizados
- **Calendário**: Registro e acompanhamento de eventos de micção
- **Onboarding**: Sistema de perguntas para personalização da experiência do usuário
- **Relatórios**: Geração de relatórios de saúde e progresso
- **Mídia**: Armazenamento de imagens e arquivos no Azure Blob Storage
- **Administração**: Painel administrativo para gerenciamento de usuários e conteúdo
- **Contato e Suporte**: Sistema de envio de e-mails para suporte e solicitações profissionais

## 🛠 Tecnologias Utilizadas

- **Linguagem**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.0
- **Banco de Dados**: Microsoft SQL Server 2019
- **Armazenamento**: Azure Blob Storage (com Azurite para desenvolvimento local)
- **Autenticação**: JWT (JSON Web Tokens)
- **Documentação**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Containerização**: Docker e Docker Compose
- **Testes**: JUnit 5, MockWebServer, GreenMail
- **Validação**: Jakarta Validation
- **Mapeamento**: MapStruct

## 📦 Requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.6+** (ou use o `mvnw` incluído no projeto)
- **Docker** e **Docker Compose** (para executar a infraestrutura local)
- **Git** (para clonar o repositório)

### Verificando as Instalações

```bash
# Verificar versão do Java
java -version

# Verificar versão do Maven
mvn -version

# Verificar versão do Docker
docker --version
docker compose version
```

## 🚀 Instalação

1. **Clone o repositório**

```bash
git clone https://github.com/raulbana/tcc-tads-backend.git
cd tcc-tads-backend
```

2. **Compile o projeto**

```bash
# Usando o Maven Wrapper (recomendado)
./mvnw clean install

# Ou usando Maven instalado
mvn clean install
```

## ⚙️ Configuração

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# Banco de Dados
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=dailyiu;encrypt=false;trustServerCertificate=true
DB_USER=sa
DB_PASSWORD=SuaSenhaSegura123!

# Azure Blob Storage (para desenvolvimento local com Azurite)
AZURE_STORAGE_CONNECTION_STRING=DefaultEndpointsProtocol=http;AccountName=devstoreaccount1;AccountKey=Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==;BlobEndpoint=http://localhost:10000/devstoreaccount1;
AZURE_STORAGE_CONTAINER_NAME=media

# JWT
JWT_SECRET=sua-chave-secreta-jwt-super-segura-aqui

# E-mail (Gmail)
NOREPLY_EMAIL_URL=seu-email@gmail.com
NOREPLY_EMAIL_PASSWORD=sua-senha-de-app
SUPPORT_EMAIL_URL=suporte@dailyiu.com
```

### Configuração do E-mail (Gmail)

Para usar o Gmail como servidor SMTP:

1. Ative a verificação em duas etapas na sua conta Google
2. Gere uma "Senha de app" em: https://myaccount.google.com/apppasswords
3. Use a senha de app gerada no campo `NOREPLY_EMAIL_PASSWORD`

### Configuração do Azure Blob Storage

#### Desenvolvimento Local (Azurite)

O projeto já está configurado para usar Azurite (emulador local do Azure Storage) através do Docker Compose. As credenciais padrão do Azurite já estão configuradas no exemplo acima.

#### Produção

Para produção, substitua a `AZURE_STORAGE_CONNECTION_STRING` pela string de conexão real da sua conta Azure Storage.

## ▶️ Executando o Projeto

### Opção 1: Executar com Docker Compose (Recomendado)

Esta opção inicia toda a infraestrutura (SQL Server, Azurite) e a aplicação:

```bash
# Iniciar todos os serviços
docker compose -f compose-test.yaml up -d

# Verificar logs
docker compose -f compose-test.yaml logs -f backend

# Parar os serviços
docker compose -f compose-test.yaml down
```

A aplicação estará disponível em: `http://localhost:8080`

### Opção 2: Executar apenas a Infraestrutura com Docker Compose

Execute apenas o banco de dados e o storage, e rode a aplicação localmente:

```bash
# Iniciar apenas SQL Server e Azurite
docker compose up -d

# Executar a aplicação localmente
./mvnw spring-boot:run

# Ou executar o JAR
./mvnw clean package
java -jar target/daily-iu-services-0.0.1-SNAPSHOT.jar
```

### Opção 3: Executar Localmente (sem Docker)

Se você tiver SQL Server e Azure Storage configurados localmente:

1. Configure as variáveis de ambiente no seu sistema ou no arquivo `.env`
2. Execute:

```bash
./mvnw spring-boot:run
```

### Verificando se está Funcionando

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health (se o Actuator estiver configurado)

## 📁 Estrutura do Projeto

```
tcc-tads-backend/
├── src/
│   ├── main/
│   │   ├── kotlin/br/ufpr/tads/daily_iu_services/
│   │   │   ├── adapter/
│   │   │   │   ├── input/          # Controllers (API REST)
│   │   │   │   │   ├── admin/      # Endpoints administrativos
│   │   │   │   │   ├── calendar/   # Calendário e eventos
│   │   │   │   │   ├── contact/    # Contato e suporte
│   │   │   │   │   ├── content/    # Conteúdo da rede social
│   │   │   │   │   ├── exercise/   # Exercícios e treinos
│   │   │   │   │   ├── media/      # Upload de mídia
│   │   │   │   │   ├── preferences/# Preferências do usuário
│   │   │   │   │   ├── questions/  # Perguntas de onboarding
│   │   │   │   │   ├── reports/    # Relatórios
│   │   │   │   │   └── user/       # Autenticação e usuários
│   │   │   │   └── output/         # Repositórios (JPA)
│   │   │   ├── config/             # Configurações (CORS, Swagger)
│   │   │   ├── domain/
│   │   │   │   ├── entity/         # Entidades do domínio
│   │   │   │   ├── service/        # Lógica de negócio
│   │   │   │   └── validator/      # Validadores customizados
│   │   │   └── exception/          # Tratamento de exceções
│   │   └── resources/
│   │       ├── application.yaml    # Configurações da aplicação
│   │       └── templates/          # Templates de e-mail
│   └── test/                       # Testes unitários e de integração
├── compose.yaml                    # Docker Compose (infraestrutura)
├── compose-test.yaml               # Docker Compose (infraestrutura + app)
├── Dockerfile                      # Imagem Docker da aplicação
├── pom.xml                         # Configuração Maven
└── README.md                       # Este arquivo
```

## 📚 Documentação da API

A documentação interativa da API está disponível através do Swagger UI quando a aplicação está em execução:

**URL**: http://localhost:8080/swagger-ui.html

A documentação inclui:
- Todos os endpoints disponíveis
- Parâmetros de requisição e resposta
- Modelos de dados (DTOs)
- Exemplos de uso
- Possibilidade de testar os endpoints diretamente

### Principais Endpoints

#### Autenticação
- `POST /v1/users` - Criar usuário
- `POST /v1/users/login` - Login
- `POST /v1/users/password/forgot` - Solicitar recuperação de senha
- `POST /v1/users/password/reset` - Redefinir senha

#### Conteúdo
- `GET /v1/content` - Listar conteúdo
- `POST /v1/content` - Criar conteúdo
- `PUT /v1/content/{id}` - Atualizar conteúdo
- `DELETE /v1/content/{id}` - Deletar conteúdo

#### Exercícios
- `GET /v1/exercises` - Listar exercícios
- `POST /v1/workout-plans` - Criar plano de treino
- `POST /v1/workouts` - Registrar treino

#### Calendário
- `GET /v1/calendar` - Obter eventos do calendário
- `POST /v1/calendar` - Registrar evento

#### Administração
- `GET /v1/admin/users` - Listar usuários (admin)
- `POST /v1/admin/assign-role` - Atribuir papel (admin)

## 🧪 Testes

### Executar Todos os Testes

```bash
./mvnw test
```

### Executar Testes com Cobertura

```bash
./mvnw test jacoco:report
```

O relatório de cobertura será gerado em: `target/site/jacoco/index.html`

### Executar Testes de Integração

Os testes de integração usam H2 (banco em memória) e GreenMail (servidor SMTP mock) para não depender de serviços externos.

## 💻 Desenvolvimento

### Configuração do IDE

Recomenda-se usar IntelliJ IDEA ou VS Code com extensões Kotlin.

### Formatação de Código

O projeto segue as convenções padrão do Kotlin. Certifique-se de configurar o formatter do IDE.

### Estrutura de Commits

Siga o padrão de commits semânticos:
- `feat:` Nova funcionalidade
- `fix:` Correção de bug
- `docs:` Documentação
- `refactor:` Refatoração
- `test:` Testes
- `chore:` Tarefas de manutenção

### Scripts Úteis

```bash
# Limpar e compilar
./mvnw clean compile

# Executar apenas testes unitários
./mvnw test

# Gerar JAR executável
./mvnw clean package

# Verificar dependências
./mvnw dependency:tree
```

## 👥 Desenvolvedores

Este projeto foi desenvolvido como parte do Trabalho de Conclusão de Curso (TCC) do curso de Tecnologia em Análise e Desenvolvimento de Sistemas da UFPR.

**Equipe:**
- Alisson Gabriel Santos
- Gabriel Alamartini Troni
- Leonardo Felipe Salgado
- Pedro Henrique Souza
- Raul Ferreira Bana

## 📄 Licença

Este projeto está sob a licença especificada no arquivo `LICENSE`.

## 🆘 Suporte

Para dúvidas ou problemas:
1. Consulte a documentação do Swagger UI
2. Verifique os logs da aplicação
3. Entre em contato com a equipe de desenvolvimento

---

**Desenvolvido com ❤️ pela equipe Daily IU**

