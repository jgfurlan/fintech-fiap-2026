package br.com.fiap.fintech;

/**
 * Subclasse que representa uma Conta Corrente no sistema Fintech.
 * Herda os atributos e comportamentos de Conta e adiciona regras de cheque especial e taxa de manutenção.
 */
public class ContaCorrente extends Conta {

    // Atributos específicos da Conta Corrente
    private Double limiteChequeEspecial;
    private Double taxaManutencao;

    // Construtor padrão
    public ContaCorrente() {
        super();
        this.limiteChequeEspecial = 0.0;
        this.taxaManutencao = 0.0;
    }

    // Construtor parametrizado utilizando super()
    public ContaCorrente(String agencia, String numeroConta, String titular, Double saldo, Double limiteChequeEspecial, Double taxaManutencao) {
        super(agencia, numeroConta, titular, saldo);
        this.limiteChequeEspecial = (limiteChequeEspecial != null && limiteChequeEspecial >= 0.0) ? limiteChequeEspecial : 0.0;
        this.taxaManutencao = (taxaManutencao != null && taxaManutencao >= 0.0) ? taxaManutencao : 0.0;
    }

    /**
     * Polimorfismo por sobreposição (@Override):
     * Permite saque utilizando o saldo somado ao limite do cheque especial.
     */
    @Override
    public boolean sacar(Double valor) {
        if (valor != null && valor > 0.0) {
            Double saldoDisponivel = getSaldo() + this.limiteChequeEspecial;
            if (saldoDisponivel >= valor) {
                setSaldo(getSaldo() - valor);
                return true;
            }
        }
        return false;
    }

    /**
     * Aplica e debita a taxa mensal de manutenção da conta corrente.
     */
    public boolean aplicarTaxaManutencao() {
        if (this.taxaManutencao != null && this.taxaManutencao > 0.0) {
            setSaldo(getSaldo() - this.taxaManutencao);
            return true;
        }
        return false;
    }

    public Double getSaldoDisponivelTotal() {
        return getSaldo() + (this.limiteChequeEspecial != null ? this.limiteChequeEspecial : 0.0);
    }

    // Getters e Setters
    public Double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(Double limiteChequeEspecial) {
        if (limiteChequeEspecial != null && limiteChequeEspecial >= 0.0) {
            this.limiteChequeEspecial = limiteChequeEspecial;
        }
    }

    public Double getTaxaManutencao() {
        return taxaManutencao;
    }

    public void setTaxaManutencao(Double taxaManutencao) {
        if (taxaManutencao != null && taxaManutencao >= 0.0) {
            this.taxaManutencao = taxaManutencao;
        }
    }
}
