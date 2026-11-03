package br.com.fiap.fintech;

/**
 * Classe que representa um Cartão de Crédito no sistema Fintech.
 */
public class CartaoCredito {

    // Atributos privados (encapsulamento)
    private String numeroCartao;
    private String nomeTitular;
    private Double limiteTotal;
    private Double limiteDisponivel;
    private String dataValidade;
    private String cvv;
    private Double faturaAtual;
    private Boolean bloqueado;

    // Construtor padrão
    public CartaoCredito() {
        this.limiteTotal = 0.0;
        this.limiteDisponivel = 0.0;
        this.faturaAtual = 0.0;
        this.bloqueado = false;
    }

    // Construtor com parâmetros
    public CartaoCredito(String numeroCartao, String nomeTitular, Double limiteTotal, Double limiteDisponivel, String dataValidade, String cvv, Double faturaAtual) {
        this.numeroCartao = numeroCartao;
        this.nomeTitular = nomeTitular;
        this.limiteTotal = (limiteTotal != null && limiteTotal >= 0.0) ? limiteTotal : 0.0;
        this.limiteDisponivel = (limiteDisponivel != null && limiteDisponivel >= 0.0) ? limiteDisponivel : 0.0;
        this.dataValidade = dataValidade;
        this.cvv = cvv;
        this.faturaAtual = (faturaAtual != null && faturaAtual >= 0.0) ? faturaAtual : 0.0;
        this.bloqueado = false;
    }

    // Métodos de negócio com lógica real e retornos
    public boolean autorizarCompra(Double valor) {
        if (!this.bloqueado && valor != null && valor > 0.0 && this.limiteDisponivel >= valor) {
            this.limiteDisponivel -= valor;
            this.faturaAtual += valor;
            return true;
        }
        return false;
    }

    public boolean pagarFatura(Double valor) {
        if (valor != null && valor > 0.0 && this.faturaAtual >= valor) {
            this.faturaAtual -= valor;
            this.limiteDisponivel += valor;
            return true;
        }
        return false;
    }

    public boolean alterarLimite(Double novoLimite) {
        if (novoLimite != null && novoLimite >= this.faturaAtual) {
            Double limiteUtilizado = this.limiteTotal - this.limiteDisponivel;
            this.limiteTotal = novoLimite;
            this.limiteDisponivel = novoLimite - limiteUtilizado;
            return true;
        }
        return false;
    }

    public void bloquearCartao() {
        this.bloqueado = true;
    }

    public void desbloquearCartao() {
        this.bloqueado = false;
    }

    public String obterFinalCartao() {
        if (numeroCartao != null && numeroCartao.length() >= 4) {
            return numeroCartao.substring(numeroCartao.length() - 4);
        }
        return "****";
    }

    // Getters e Setters
    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public Double getLimiteTotal() {
        return limiteTotal;
    }

    public void setLimiteTotal(Double limiteTotal) {
        this.limiteTotal = limiteTotal;
    }

    public Double getLimiteDisponivel() {
        return limiteDisponivel;
    }

    public void setLimiteDisponivel(Double limiteDisponivel) {
        this.limiteDisponivel = limiteDisponivel;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getFaturaAtual() {
        return faturaAtual;
    }

    public void setFaturaAtual(Double faturaAtual) {
        this.faturaAtual = faturaAtual;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }
}
