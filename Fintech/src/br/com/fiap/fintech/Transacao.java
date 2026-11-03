package br.com.fiap.fintech;

/**
 * Classe que representa uma Transação Financeira no sistema Fintech.
 */
public class Transacao {

    // Atributos privados (encapsulamento)
    private Integer id;
    private String descricao;
    private Double valor;
    private String tipo; // "RECEITA" ou "DESPESA"
    private String categoria;
    private String data;
    private Boolean estornada;

    // Construtor padrão
    public Transacao() {
        this.valor = 0.0;
        this.estornada = false;
    }

    // Construtor com parâmetros
    public Transacao(Integer id, String descricao, Double valor, String tipo, String categoria, String data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = (valor != null && valor >= 0.0) ? valor : 0.0;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
        this.estornada = false;
    }

    // Métodos de negócio com lógica real e retornos
    public boolean categorizar(String novaCategoria) {
        if (novaCategoria != null && !novaCategoria.trim().isEmpty()) {
            this.categoria = novaCategoria;
            return true;
        }
        return false;
    }

    public boolean estornar() {
        if (!this.estornada) {
            this.estornada = true;
            return true;
        }
        return false;
    }

    public String gerarComprovante() {
        return String.format("COMPROVANTE [#%d] Data: %s | Tipo: %s | Descrição: %s | Categoria: %s | Valor: R$ %.2f | Estornada: %s",
                id, data, tipo, descricao, categoria, valor, estornada ? "SIM" : "NÃO");
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        if (valor != null && valor >= 0.0) {
            this.valor = valor;
        }
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getEstornada() {
        return estornada;
    }

    public void setEstornada(Boolean estornada) {
        this.estornada = estornada;
    }
}
