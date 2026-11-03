package br.com.fiap.fintech;

/**
 * Classe que representa um Orçamento / Planejamento Financeiro no sistema Fintech.
 */
public class Orcamento {

    // Atributos privados (encapsulamento)
    private Integer id;
    private String mesReferencia;
    private String categoria;
    private Double limiteMensal;
    private Double gastoAtual;

    // Construtor padrão
    public Orcamento() {
        this.limiteMensal = 0.0;
        this.gastoAtual = 0.0;
    }

    // Construtor com parâmetros
    public Orcamento(Integer id, String mesReferencia, String categoria, Double limiteMensal, Double gastoAtual) {
        this.id = id;
        this.mesReferencia = mesReferencia;
        this.categoria = categoria;
        this.limiteMensal = (limiteMensal != null && limiteMensal >= 0.0) ? limiteMensal : 0.0;
        this.gastoAtual = (gastoAtual != null && gastoAtual >= 0.0) ? gastoAtual : 0.0;
    }

    // Métodos de negócio com lógica real e retornos
    public boolean adicionarGasto(Double valor) {
        if (valor != null && valor > 0.0) {
            this.gastoAtual += valor;
            return true;
        }
        return false;
    }

    public boolean definirMeta(Double novoLimite) {
        if (novoLimite != null && novoLimite >= 0.0) {
            this.limiteMensal = novoLimite;
            return true;
        }
        return false;
    }

    public Double calcularPercentualUtilizado() {
        if (this.limiteMensal > 0.0) {
            return (this.gastoAtual / this.limiteMensal) * 100.0;
        }
        return 0.0;
    }

    public boolean isLimiteExcedido() {
        return this.gastoAtual > this.limiteMensal;
    }

    public boolean isAlertaAtencao() {
        return this.gastoAtual >= (this.limiteMensal * 0.8) && !isLimiteExcedido();
    }

    public Double calcularSaldoRestante() {
        return this.limiteMensal - this.gastoAtual;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(String mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getLimiteMensal() {
        return limiteMensal;
    }

    public void setLimiteMensal(Double limiteMensal) {
        if (limiteMensal != null && limiteMensal >= 0.0) {
            this.limiteMensal = limiteMensal;
        }
    }

    public Double getGastoAtual() {
        return gastoAtual;
    }

    public void setGastoAtual(Double gastoAtual) {
        if (gastoAtual != null && gastoAtual >= 0.0) {
            this.gastoAtual = gastoAtual;
        }
    }
}
