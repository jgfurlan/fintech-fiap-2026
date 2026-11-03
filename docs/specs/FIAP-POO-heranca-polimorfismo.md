# Atomic Feature Spec: [FIAP-POO] Herança e Polimorfismo no Sistema Fintech

## 1. Isolated Goal
Implementar os conceitos de Herança, Polimorfismo e Encapsulamento completo no projeto Java Fintech, atendendo integralmente às especificações da atividade avaliativa:
1. Mínimo de 3 classes definidas e implementadas para o projeto Fintech.
2. Mínimo de 1 relação de herança (superclasse e subclasses coerentes):
   - Superclasse: `Conta`
   - Subclasses: `ContaCorrente` e `ContaPoupanca`
3. Implementação de Polimorfismo:
   - Sobrescrita de método (`@Override`) em `sacar(Double valor)` nas subclasses (regra de cheque especial/taxa para ContaCorrente e regra de saldo/limite para ContaPoupanca).
   - Invocação polimórfica via referência da superclasse `Conta`.
4. Implementação de lógica de negócio real nos métodos, removendo `System.out.println` interno e utilizando valores de retorno (`boolean`, `Double`, etc.).
5. Encapsulamento rigoroso: atributos privados, construtores padrão e parametrizados (utilizando `super(...)`), e getters/setters.
6. Classe executável `Teste` (e `Main`) instanciando os objetos, atribuindo valores e demonstrando a execução polimórfica e regras de negócio.
7. Atualização da exportação compactada `Fintech.zip`.

## 2. Modelagem das Classes e Hierarquia

### A. Superclasse `Conta`
- **Atributos:**
  - `agencia` (`String`)
  - `numeroConta` (`String`)
  - `titular` (`String`)
  - `saldo` (`Double`)
- **Construtores:** Padrão (`Conta()`) e Parametrizado (`Conta(String agencia, String numeroConta, String titular, Double saldo)`).
- **Métodos (com lógica real e sem `System.out.println`):**
  - `depositar(Double valor): boolean` -> Valida se valor > 0, soma ao saldo e retorna `true`; caso contrário retorna `false`.
  - `sacar(Double valor): boolean` -> Valida se valor > 0 e saldo >= valor, deduz do saldo e retorna `true`; caso contrário retorna `false`.
  - `transferir(Double valor, Conta destino): boolean` -> Valida saque nesta conta e depósito na conta destino, retornando `true` ou `false`.
  - `consultarSaldo(): Double` -> Retorna o saldo atual.
  - Getters e Setters para todos os atributos.

### B. Subclasse `ContaCorrente` (extends `Conta`)
- **Atributos Adicionais:**
  - `limiteChequeEspecial` (`Double`)
  - `taxaManutencao` (`Double`)
- **Construtores:** Padrão e Parametrizado invocando `super(agencia, numeroConta, titular, saldo)`.
- **Métodos Polimórficos / Próprios:**
  - `@Override public boolean sacar(Double valor)`: Permite saque se `valor <= (saldo + limiteChequeEspecial)`. Atualiza saldo (podendo ficar negativo até o limite).
  - `aplicarTaxaManutencao(): void`: Desconta a taxa de manutenção do saldo.
  - Getters e Setters.

### C. Subclasse `ContaPoupanca` (extends `Conta`)
- **Atributos Adicionais:**
  - `taxaRendimento` (`Double`) (percentual mensal, ex: 0.5%)
  - `diaAniversario` (`Integer`)
- **Construtores:** Padrão e Parametrizado invocando `super(...)`.
- **Métodos Polimórficos / Próprios:**
  - `@Override public boolean sacar(Double valor)`: Permite saque somente se `valor <= saldo` (não possui limite de crédito).
  - `calcularRendimentoMensal(): Double`: Calcula valor de rendimento com base no saldo e na taxa.
  - `creditarRendimento(): Double`: Credita os juros/rendimentos diretamente ao saldo.
  - Getters e Setters.

### D. Classes Complementares Mantidas e Refatoradas
- `Usuario`, `Investimento`, `CartaoCredito`, `Orcamento`, `Transacao` ajustadas para lógica de negócio real com métodos de retorno em vez de `System.out.println` interno.

### E. Classes de Demonstração / Execução
- `Teste`: Classe principal demonstrando instanciação das classes com construtores, atribuição de atributos, demonstração clara de herança e de polimorfismo em tempo de execução (Array ou referências de `Conta` apontando para `ContaCorrente` e `ContaPoupanca`).
- `Main`: Ponto de entrada que delega para `Teste` ou executa a suite de demonstração.

## 3. Step-by-Step Implementation Map
- [ ] Refatorar `Conta.java` com regras de negócio reais, retornos e sem `System.out.println`.
- [ ] Criar `ContaCorrente.java` herdando de `Conta`, com construtores `super`, atributos próprios e `@Override` em `sacar`.
- [ ] Criar `ContaPoupanca.java` herdando de `Conta`, com construtores `super`, atributos próprios e métodos de rendimento.
- [ ] Refatorar métodos de `CartaoCredito.java`, `Investimento.java`, `Orcamento.java`, `Transacao.java` e `Usuario.java` para remover `System.out.println` dos métodos e aplicar regras reais com retornos.
- [ ] Criar `Teste.java` demonstrando instanciação com construtores, invocação de métodos e polimorfismo.
- [ ] Atualizar `Main.java`.
- [ ] Atualizar documentação `Fintech/README.md`.
- [ ] Compilar todas as classes com `javac` e executar `Teste`.
- [ ] Empacotar `Fintech.zip` atualizado.

## 4. Verification Array
- [ ] Compilação com `javac -d out $(find Fintech/src -name "*.java")` sem erros nem warnings.
- [ ] Execução de `Teste` e `Main` com `java -cp out br.com.fiap.fintech.Teste`.
- [ ] Verificação de herança funcional com `super(...)`.
- [ ] Verificação de polimorfismo dinâmico comprovado em tempo de execução.
- [ ] Verificação de que não existem `System.out.println` em regras de negócio internas das classes de domínio.
- [ ] `Fintech.zip` gerado e estruturado conforme exigência do IntelliJ.
