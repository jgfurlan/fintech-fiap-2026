package br.com.fiap.fintech;

/**
 * Subclasse que representa uma Conta Poupança no sistema Fintech.
 * Herda atributos e comportamentos de Conta e adiciona rendimento mensal e dia de aniversário.
 */
public class ContaPoupanca extends Conta {

    // Atributos específicos da Conta Poupança
    private Double taxaRendimento; // em percentual mensal (ex: 0.5 para 0.5% ao mês)
    private Integer diaAniversario; // dia do mês em que o rendimento é creditado

    // Construtor padrão
    public ContaPoupanca() {
        super();
        this.taxaRendimento = 0.0;
        this.diaAniversario = 1;
    }

    // Construtor parametrizado utilizando super()
    public ContaPoupanca(String agencia, String numeroConta, String titular, Double saldo, Double taxaRendimento, Integer diaAniversario) {
        super(agencia, numeroConta, titular, saldo);
        this.taxaRendimento = (taxaRendimento != null && taxaRendimento >= 0.0) ? taxaRendimento : 0.0;
        this.diaAniversario = (diaAniversario != null && diaAniversario >= 1 && diaAniversario <= 31) ? diaAniversario : 1;
    }

    /**
     * Polimorfismo por sobreposição (@Override):
     * Na poupança, o saque é estritamente limitado ao saldo existente (sem cheque especial).
     */
    @Override
    public boolean sacar(Double valor) {
        if (valor != null && valor > 0.0 && getSaldo() >= valor) {
            setSaldo(getSaldo() - valor);
            return true;
        }
        return false;
    }

    /**
     * Calcula o rendimento mensal projetado com base no saldo atual e na taxa.
     */
    public Double calcularRendimento() {
        if (this.taxaRendimento != null && this.taxaRendimento > 0.0 && getSaldo() > 0.0) {
            return getSaldo() * (this.taxaRendimento / 100.0);
        }
        return 0.0;
    }

    /**
     * Credita o rendimento calculado diretamente ao saldo da poupança.
     * Retorna o valor creditado.
     */
    public Double creditarRendimento() {
        Double rendimento = calcularRendimento();
        if (rendimento > 0.0) {
            setSaldo(getSaldo() + rendimento);
        }
        return rendimento;
    }

    // Getters e Setters
    public Double getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(Double taxaRendimento) {
        if (taxaRendimento != null && taxaRendimento >= 0.0) {
            this.taxaRendimento = taxaRendimento;
        }
    }

    public Integer getDiaAniversario() {
        return diaAniversario;
    }

    public void setDiaAniversario(Integer diaAniversario) {
        if (diaAniversario != null && diaAniversario >= 1 && diaAniversario <= 31) {
            this.diaAniversario = diaAniversario;
        }
    }
}
