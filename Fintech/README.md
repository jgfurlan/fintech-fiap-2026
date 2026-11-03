# Sistema Fintech - Atividade: Herança, Polimorfismo e Encapsulamento (FIAP)

## 📌 Visão Geral do Projeto
Projeto acadêmico em Java para a plataforma **Fintech (FIAP)**, implementando e aprofundando os pilares da Programação Orientada a Objetos:
- **Abstração:** Modelagem realista do domínio financeiro digital.
- **Encapsulamento:** Atributos privados (`private`), métodos assessores (getters e setters), construtores padrão e parametrizados.
- **Lógica de Negócio Real:** Remoção de `System.out.println` de dentro das classes de modelo, utilizando retornos primitivos/objetos (`boolean`, `Double`, etc.) e validações consistentes.
- **Herança:** Relação hierárquica clara de superclasse (`Conta`) e subclasses (`ContaCorrente`, `ContaPoupanca`) reaproveitando atributos e métodos com `super(...)`.
- **Polimorfismo:** Sobrescrita de métodos (`@Override`) com comportamentos específicos em tempo de execução (saque com limite de cheque especial em `ContaCorrente` vs saque limitado ao saldo em `ContaPoupanca`), manipulados via referências polimórficas da superclasse.

---

## 🏛️ Estrutura de Classes do Domínio

### 1. Hierarquia de Contas (Herança e Polimorfismo)

```
        ┌──────────────┐
        │    Conta     │  (Superclasse)
        └───────┬──────┘
                │
       ┌────────┴────────┐
       ▼                 ▼
┌───────────────┐ ┌───────────────┐
│ ContaCorrente │ │ ContaPoupanca │  (Subclasses)
└───────────────┘ └───────────────┘
```

#### A. Superclasse `Conta`
- **Atributos privados:** `agencia` (`String`), `numeroConta` (`String`), `titular` (`String`), `saldo` (`Double`).
- **Construtores:** Padrão (`Conta()`) e Parametrizado (`Conta(...)`).
- **Métodos de negócio:**
  - `depositar(Double valor): boolean`
  - `sacar(Double valor): boolean`
  - `transferir(Double valor, Conta contaDestino): boolean`
  - `consultarSaldo(): Double`

#### B. Subclasse `ContaCorrente` (extends `Conta`)
- **Atributos adicionais:** `limiteChequeEspecial` (`Double`), `taxaManutencao` (`Double`).
- **Construtores:** Padrão e Parametrizado com chamada a `super(...)`.
- **Polimorfismo (`@Override`):**
  - `sacar(Double valor): boolean` -> Permite sacar além do saldo, utilizando a margem do cheque especial.
- **Métodos específicos:**
  - `aplicarTaxaManutencao(): boolean`
  - `getSaldoDisponivelTotal(): Double`

#### C. Subclasse `ContaPoupanca` (extends `Conta`)
- **Atributos adicionais:** `taxaRendimento` (`Double`), `diaAniversario` (`Integer`).
- **Construtores:** Padrão e Parametrizado com chamada a `super(...)`.
- **Polimorfismo (`@Override`):**
  - `sacar(Double valor): boolean` -> Estritamente restrito ao saldo existente (sem cheque especial).
- **Métodos específicos:**
  - `calcularRendimento(): Double`
  - `creditarRendimento(): Double`

---

### 2. Classes Complementares do Sistema Fintech
- **`Usuario`**: Gestão cadastral do cliente (id, nome, cpf, email, telefone, contaAtiva).
- **`CartaoCredito`**: Controle de compras, limites, fatura e bloqueio de segurança.
- **`Investimento`**: Aplicações financeiras, resgate e cálculo de rentabilidade projetada.
- **`Orcamento`**: Planejamento financeiro mensal, controle de teto de gastos e alertas.
- **`Transacao`**: Registro de movimentações financeiras com categorização e estorno.

---

### 3. Classes de Execução e Demonstração

#### A. `Teste` (`br.com.fiap.fintech.Teste`)
Classe executável principal que demonstra detalhadamente:
1. Instanciação via **Construtor Padrão** e atribuição de atributos via **Setters** (Encapsulamento).
2. Instanciação via **Construtor Parametrizado** utilizando `super(...)` da superclasse.
3. Operações reais de negócio: depósitos, saques, transferências entre contas e cálculo de rendimentos.
4. Demonstração explícita de **Polimorfismo em Tempo de Execução (`@Override`)**:
   - Duas contas instanciadas (`ContaCorrente` e `ContaPoupanca`) referenciadas pelo tipo comum `Conta`.
   - Tentativa de saque de mesmo valor (R$ 1.300,00) com saldo de R$ 1.000,00: a `ContaCorrente` autoriza usando o cheque especial, enquanto a `ContaPoupanca` recusa com segurança.
5. Iteração polimórfica sobre um array de `Conta[]`.

#### B. `Main` (`br.com.fiap.fintech.Main`)
Ponto de entrada que executa diretamente a classe `Teste`.

---

## 🚀 Como Executar no IntelliJ IDEA
1. Abra o **IntelliJ IDEA**.
2. Clique em **Open** e selecione a pasta `Fintech`.
3. Navegue até `src/br/com/fiap/fintech/Teste.java` (ou `Main.java`).
4. Clique com o botão direito e selecione **Run 'Teste.main()'** (ou `Shift + F10`).
