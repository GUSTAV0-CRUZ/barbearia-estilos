# Barbearia Estilo — Sistema Web de Agendamento (MVP)

Solução web *mobile-first* para automatização de agendamentos e gestão de serviços para barbearias, desenvolvida para solucionar gargalos no atendimento manual via WhatsApp.

---

## 🎯 Sobre o Projeto

Este MVP foi desenvolvido para atender às necessidades de **barbeiros solo**, permitindo que os clientes reservem os seus horários de forma autónoma e sem necessidade de cadastro burocrático ou senha. O proprietário gerencia a sua grade semanal e o catálogo de serviços por meio de uma Chave de Acesso Administrativa (*Admin Key*).

O projeto está alinhado ao **ODS 8 (Trabalho Decente e Crescimento Econômico - Meta 8.3)**, promovendo a modernização tecnológica e a eficiência operacional de microempreendimentos.

---

## 🚀 Tecnologias Utilizadas
* **Backend:** Java 17+, Spring Boot (Web, Data JPA, Validation)
* **Frontend:** Thymeleaf, HTML5, CSS3 (Responsivo)
* **Base de Dados:** MySQL
* **Segurança:** Interceptor personalizado com validação por chave (*Admin Key*)

---

## ⚙️ Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/GUSTAVO-CRUZ/barbearia-estilos.git
   ```
## ⚙️ Como Executar o Projeto

### 1. Clone o repositório

```bash
git clone https://github.com/GUSTAVO-CRUZ/barbearia-estilos.git
cd barbearia-estilos
```

### 2. Configure as variáveis de ambiente

O projeto utiliza variáveis de ambiente para configurar a base de dados e os parâmetros da barbearia.

Você pode alterar os valores padrão diretamente no arquivo `src/main/resources/application.yaml` ou definir as seguintes variáveis de ambiente no sistema:

| Variável | Valor padrão |
|---|---|
| `APP_BARBERSHOP_NAME` | `Silva & Estilo` |
| `APP_BARBER_NAME` | `Marcos Silva` |
| `APP_ADMIN_KEY` | `SILVA2024` |
| `MSQL_ROOT` | `root` |
| `MSQL_PASSWORD` | `password123` |

### 3. Execute a aplicação

Execute o comando abaixo no terminal, na raiz do projeto:

```bash
./mvnw spring-boot:run
```

Você também pode executar a aplicação diretamente pela sua IDE.

### 4. Acesse a aplicação

- **Site público:** http://localhost:8080/
- **Painel administrativo:** http://localhost:8080/admin

No painel administrativo, utilize a chave configurada em `APP_ADMIN_KEY` para desbloquear as funcionalidades e gerenciar o catálogo.

## 🧪 Testes Automatizados

O projeto conta com uma suíte de testes completa, estruturada para garantir a confiabilidade e a integridade do sistema:

- **Testes Unitários de Domínio:** Validam isoladamente as regras de negócio e validações críticas nas entidades principais (`ServiceItem` e `Appointment`), assegurando que o domínio se mantenha consistente sem depender de base de dados ou contexto web.
- **Testes Web (Camada de Controladores):** Utilizam o ecossistema do Spring com `MockMvc` para validar o comportamento dos controladores (`AppointmentController`, `AdminController`, `ServiceItemController`), o mapeamento de rotas, parâmetros, respostas de redirecionamento e a segurança protegida pelo interceptor.

### Como Executar os Testes

Para executar toda a suíte de testes automatizados via terminal, utilize o Maven Wrapper:
```bash
./mvnw test
```

Caso queira executar uma classe de teste específica de forma isolada, pode utilizar a flag -Dtest:
```bash
./mvnw test -Dtest=AppointmentControllerTest
```