package br.com.fiap.fintech;

/**
 * Classe que representa uma Aplicação Financeira / Investimento no sistema Fintech.
 */
public class Investimento {

    // Atributos privados (encapsulamento)
    private Integer id;
    private String nomeProduto;
    private Double valorInvestido;
    private Double rentabilidadeAnual; // percentual anual (ex: 12.5 para 12.5% a.a.)
    private Integer prazoMeses;
    private String tipoInvestimento;

    // Construtor padrão
    public Investimento() {
        this.valorInvestido = 0.0;
        this.rentabilidadeAnual = 0.0;
        this.prazoMeses = 0;
    }

    // Construtor com parâmetros
    public Investimento(Integer id, String nomeProduto, Double valorInvestido, Double rentabilidadeAnual, Integer prazoMeses, String tipoInvestimento) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.valorInvestido = (valorInvestido != null && valorInvestido >= 0.0) ? valorInvestido : 0.0;
        this.rentabilidadeAnual = (rentabilidadeAnual != null && rentabilidadeAnual >= 0.0) ? rentabilidadeAnual : 0.0;
        this.prazoMeses = (prazoMeses != null && prazoMeses >= 0) ? prazoMeses : 0;
        this.tipoInvestimento = tipoInvestimento;
    }

    // Métodos de negócio com lógica real e retornos
    public boolean aplicar(Double valor) {
        if (valor != null && valor > 0.0) {
            this.valorInvestido += valor;
            return true;
        }
        return false;
    }

    public boolean resgatar(Double valor) {
        if (valor != null && valor > 0.0 && this.valorInvestido >= valor) {
            this.valorInvestido -= valor;
            return true;
        }
        return false;
    }

    public Double calcularRendimento() {
        if (this.valorInvestido > 0.0 && this.rentabilidadeAnual > 0.0 && this.prazoMeses > 0) {
            return this.valorInvestido * (this.rentabilidadeAnual / 100.0) * (this.prazoMeses / 12.0);
        }
        return 0.0;
    }

    public Double getSaldoTotalEstimado() {
        return this.valorInvestido + calcularRendimento();
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(Double valorInvestido) {
        if (valorInvestido != null && valorInvestido >= 0.0) {
            this.valorInvestido = valorInvestido;
        }
    }

    public Double getRentabilidadeAnual() {
        return rentabilidadeAnual;
    }

    public void setRentabilidadeAnual(Double rentabilidadeAnual) {
        if (rentabilidadeAnual != null && rentabilidadeAnual >= 0.0) {
            this.rentabilidadeAnual = rentabilidadeAnual;
        }
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        if (prazoMeses != null && prazoMeses >= 0) {
            this.prazoMeses = prazoMeses;
        }
    }

    public String getTipoInvestimento() {
        return tipoInvestimento;
    }

    public void setTipoInvestimento(String tipoInvestimento) {
        this.tipoInvestimento = tipoInvestimento;
    }
}
