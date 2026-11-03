# Atomic Feature Spec: [FIAP-POO] Classes do Sistema Fintech

## 1. Isolated Goal
Implementar o conjunto de classes em Java para o sistema Fintech atendendo integralmente às especificações da atividade avaliativa da FIAP:
- Mínimo de 4 classes de domínio do sistema Fintech (excluindo Login/Autenticação).
- Atributos tipados coerentemente (`String`, `Double`, `Integer`, etc.) com encapsulamento (modificadores `private` e getters/setters).
- Construtores padrão (`public Class()`) e parametrizados (`public Class(...)`).
- Métodos representativos de regras de negócio contendo `System.out.println` explicativo.
- Classe executável `Main` para demonstração de uso.
- Estrutura de projeto configurada para IntelliJ IDEA e exportação empacotada em arquivo `.zip`.

## 2. Classes Modeladas
1. **`Conta`**: Gestão da conta bancária digital (agência, número, saldo, titular, tipo). Métodos: `depositar`, `sacar`, `consultarSaldo`, `transferir`.
2. **`Transacao`**: Registro de movimentações financeiras (id, descrição, valor, tipo, categoria, data). Métodos: `registrarTransacao`, `categorizar`, `estornar`, `gerarComprovante`.
3. **`Investimento`**: Gestão de aplicações financeiras (id, nomeProduto, valorInvestido, rentabilidadeAnual, prazoMeses, tipoInvestimento). Métodos: `aplicar`, `resgatar`, `calcularRendimento`, `exibirExtrato`.
4. **`CartaoCredito`**: Gestão de cartão e limites (numeroCartao, nomeTitular, limiteTotal, limiteDisponivel, dataValidade, cvv, faturaAtual). Métodos: `autorizarCompra`, `pagarFatura`, `alterarLimite`, `bloquearCartao`.
5. **`Orcamento`**: Controle orçamentário por categoria (id, mesReferencia, categoria, limiteMensal, gastoAtual). Métodos: `definirMeta`, `acompanharMeta`, `enviarAlertaLimite`.
6. **`Usuario`**: Perfil do cliente Fintech (id, nome, cpf, email, telefone). Métodos: `atualizarDados`, `exibirPerfil`, `verificarStatusConta`.
7. **`Main`**: Classe de execução para teste de instanciação e invocação de todos os métodos.

## 3. Step-by-Step Implementation Map
- [ ] Criar estrutura do projeto IntelliJ em `fintech-java/`
- [ ] Implementar `br.com.fiap.fintech.Conta`
- [ ] Implementar `br.com.fiap.fintech.Transacao`
- [ ] Implementar `br.com.fiap.fintech.Investimento`
- [ ] Implementar `br.com.fiap.fintech.CartaoCredito`
- [ ] Implementar `br.com.fiap.fintech.Orcamento`
- [ ] Implementar `br.com.fiap.fintech.Usuario`
- [ ] Implementar `br.com.fiap.fintech.Main`
- [ ] Gerar arquivos de configuração do IntelliJ IDEA (`.iml`, `.idea/`)
- [ ] Gerar arquivo compactado `Fintech.zip` para entrega na plataforma

## 4. Verification Array
- [ ] Todas as classes compiláveis sem erros de sintaxe.
- [ ] Construtores padrão e parametrizados implementados em todas as classes.
- [ ] Atributos privados com tipos coerentes (`String`, `Double`, `Integer`).
- [ ] Métodos implementados com mensagens `System.out.println` claras.
- [ ] Arquivo `Fintech.zip` gerado e pronto para entrega.
