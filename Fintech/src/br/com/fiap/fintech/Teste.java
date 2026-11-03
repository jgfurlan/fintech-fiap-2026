package br.com.fiap.fintech;

/**
 * Classe executável Teste para demonstração e validação de Herança, Polimorfismo e Encapsulamento.
 */
public class Teste {

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("      SISTEMA FINTECH - ATIVIDADE: HERANÇA E POLIMORFISMO      ");
        System.out.println("===============================================================\n");

        // -------------------------------------------------------------
        // 1. INSTANCIAÇÃO COM CONSTRUTOR PADRÃO E SETTERS (ENCAPSULAMENTO)
        // -------------------------------------------------------------
        System.out.println(">>> 1. TESTE DE CONSTRUTOR PADRÃO E ENCAPSULAMENTO <<<");
        ContaCorrente cc1 = new ContaCorrente();
        cc1.setAgencia("0001");
        cc1.setNumeroConta("1001-1");
        cc1.setTitular("João Victor Furlan");
        cc1.setSaldo(500.00);
        cc1.setLimiteChequeEspecial(300.00);
        cc1.setTaxaManutencao(15.00);

        System.out.println("Conta Corrente criada:");
        System.out.println(" - Titular: " + cc1.getTitular());
        System.out.println(" - Agência/Conta: " + cc1.getAgencia() + " / " + cc1.getNumeroConta());
        System.out.println(" - Saldo Inicial: R$ " + cc1.consultarSaldo());
        System.out.println(" - Limite Cheque Especial: R$ " + cc1.getLimiteChequeEspecial());
        System.out.println(" - Saldo Total Disponível: R$ " + cc1.getSaldoDisponivelTotal());
        System.out.println();

        // -------------------------------------------------------------
        // 2. INSTANCIAÇÃO COM CONSTRUTOR PARAMETRIZADO E HERANÇA (SUPER)
        // -------------------------------------------------------------
        System.out.println(">>> 2. TESTE DE CONSTRUTOR PARAMETRIZADO (HERANÇA COM SUPER) <<<");
        ContaPoupanca cp1 = new ContaPoupanca("0001", "2002-2", "Mariana Souza", 1200.00, 0.65, 5);
        System.out.println("Conta Poupança criada:");
        System.out.println(" - Titular: " + cp1.getTitular());
        System.out.println(" - Agência/Conta: " + cp1.getAgencia() + " / " + cp1.getNumeroConta());
        System.out.println(" - Saldo Inicial: R$ " + cp1.consultarSaldo());
        System.out.println(" - Taxa Rendimento Mensal: " + cp1.getTaxaRendimento() + "%");
        System.out.println(" - Dia Aniversário: " + cp1.getDiaAniversario());
        System.out.println();

        // -------------------------------------------------------------
        // 3. DEMONSTRAÇÃO DE MÉTODOS DE NEGÓCIO E INTERAÇÕES ENTRE CLASSES
        // -------------------------------------------------------------
        System.out.println(">>> 3. OPERAÇÕES DE NEGÓCIO E TRANSFERÊNCIA ENTRE CONTAS <<<");
        
        // Depósito
        boolean depOk = cc1.depositar(200.00);
        System.out.println("Depósito de R$ 200,00 em CC1 efetuado? " + (depOk ? "SIM" : "NÃO"));
        System.out.println("Novo saldo CC1: R$ " + cc1.consultarSaldo());

        // Transferência da Poupança para a Corrente
        boolean transOk = cp1.transferir(400.00, cc1);
        System.out.println("Transferência de R$ 400,00 da Poupança para CC1 efetuada? " + (transOk ? "SIM" : "NÃO"));
        System.out.println("Saldo restante Poupança: R$ " + cp1.consultarSaldo());
        System.out.println("Novo saldo CC1 após receber transferência: R$ " + cc1.consultarSaldo());

        // Rendimento na Conta Poupança
        Double rendimento = cp1.creditarRendimento();
        System.out.println("Rendimento creditado na Poupança: R$ " + String.format("%.2f", rendimento));
        System.out.println("Saldo da Poupança após rendimento: R$ " + String.format("%.2f", cp1.consultarSaldo()));

        // Taxa de manutenção na Conta Corrente
        boolean taxaOk = cc1.aplicarTaxaManutencao();
        System.out.println("Taxa de manutenção aplicada em CC1? " + (taxaOk ? "SIM" : "NÃO"));
        System.out.println("Saldo CC1 após taxa: R$ " + cc1.consultarSaldo());
        System.out.println();

        // -------------------------------------------------------------
        // 4. DEMONSTRAÇÃO DE POLIMORFISMO EM TEMPO DE EXECUÇÃO
        // -------------------------------------------------------------
        System.out.println(">>> 4. DEMONSTRAÇÃO EXPLÍCITA DE POLIMORFISMO (@Override) <<<");
        System.out.println("Tratando objetos de ContaCorrente e ContaPoupanca através de referências polimórficas de Conta:");

        // Referências da superclasse Conta apontando para instâncias das subclasses
        Conta contaGenerica1 = new ContaCorrente("0001", "3003-3", "Carlos Mendes", 1000.00, 500.00, 10.00);
        Conta contaGenerica2 = new ContaPoupanca("0001", "4004-4", "Beatriz Lima", 1000.00, 0.50, 15);

        Double valorTentativaSaque = 1300.00;
        System.out.println("Tentativa de saque de R$ " + valorTentativaSaque + " em ambas as contas (Saldo de cada uma = R$ 1000.00):");

        // Invocação polimórfica em contaGenerica1 (ContaCorrente)
        boolean saqueCC = contaGenerica1.sacar(valorTentativaSaque);
        System.out.println(" - [ContaCorrente] Saque de R$ " + valorTentativaSaque + " autorizado? " + (saqueCC ? "SIM (Usou cheque especial!)" : "NÃO"));
        System.out.println("   Saldo da ContaCorrente após saque: R$ " + contaGenerica1.consultarSaldo());

        // Invocação polimórfica em contaGenerica2 (ContaPoupanca)
        boolean saqueCP = contaGenerica2.sacar(valorTentativaSaque);
        System.out.println(" - [ContaPoupanca] Saque de R$ " + valorTentativaSaque + " autorizado? " + (saqueCP ? "SIM" : "NÃO (Poupança não possui limite extra!)"));
        System.out.println("   Saldo da ContaPoupanca após saque: R$ " + contaGenerica2.consultarSaldo());
        System.out.println();

        // Iteração polimórfica via array de superclasse
        System.out.println(">>> 5. ITERAÇÃO POLIMÓRFICA EM ARRAY DE CONTAS <<<");
        Conta[] contasFintech = { cc1, cp1, contaGenerica1, contaGenerica2 };
        for (Conta c : contasFintech) {
            System.out.println(String.format("Conta: %s | Titular: %-18s | Saldo: R$ %8.2f | Tipo Efetivo: %s",
                    c.getNumeroConta(), c.getTitular(), c.consultarSaldo(), c.getClass().getSimpleName()));
        }

        System.out.println("\n===============================================================");
        System.out.println("       TESTES CONCLUÍDOS COM SUCESSO E SEM ERROS!             ");
        System.out.println("===============================================================");
    }
}
