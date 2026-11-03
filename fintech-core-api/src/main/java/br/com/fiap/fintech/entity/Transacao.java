package br.com.fiap.fintech.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade JPA para T_TRANSACAO baseada no modelo fisico da Fase 6.
 * Sequence: sq_transacao
 * Concentra receitas (tp_transacao = 'R') e despesas (tp_transacao = 'D').
 */
@Entity
@Table(name = "t_transacao")
public class Transacao {

    public static final String TIPO_RECEITA = "R";
    public static final String TIPO_DESPESA = "D";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_transacao")
    @SequenceGenerator(name = "sq_transacao", sequenceName = "sq_transacao", allocationSize = 1)
    @Column(name = "id_transacao", nullable = false)
    private Long idTransacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conta", nullable = false, foreignKey = @ForeignKey(name = "fk_transacao_conta"))
    private Conta conta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false, foreignKey = @ForeignKey(name = "fk_transacao_categ"))
    private Categoria categoria;

    @Column(name = "vl_transacao", precision = 15, scale = 2, nullable = false)
    private BigDecimal vlTransacao;

    @Column(name = "dt_transacao", nullable = false)
    private LocalDate dtTransacao;

    @Column(name = "ds_transacao", length = 255)
    private String dsTransacao;

    @Column(name = "tp_transacao", length = 1, nullable = false)
    private String tpTransacao;

    public Transacao() {}

    public Transacao(Conta conta, Categoria categoria, BigDecimal vlTransacao,
                     LocalDate dtTransacao, String dsTransacao, String tpTransacao) {
        this.conta = conta;
        this.categoria = categoria;
        this.vlTransacao = vlTransacao;
        this.dtTransacao = dtTransacao != null ? dtTransacao : LocalDate.now();
        this.dsTransacao = dsTransacao;
        this.tpTransacao = tpTransacao;
    }

    public Transacao(Long idTransacao, Conta conta, Categoria categoria, BigDecimal vlTransacao,
                     LocalDate dtTransacao, String dsTransacao, String tpTransacao) {
        this.idTransacao = idTransacao;
        this.conta = conta;
        this.categoria = categoria;
        this.vlTransacao = vlTransacao;
        this.dtTransacao = dtTransacao != null ? dtTransacao : LocalDate.now();
        this.dsTransacao = dsTransacao;
        this.tpTransacao = tpTransacao;
    }

    // Getters and Setters
    public Long getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(Long idTransacao) {
        this.idTransacao = idTransacao;
    }

    public Long getId() {
        return idTransacao;
    }

    public void setId(Long id) {
        this.idTransacao = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getVlTransacao() {
        return vlTransacao;
    }

    public void setVlTransacao(BigDecimal vlTransacao) {
        this.vlTransacao = vlTransacao;
    }

    public BigDecimal getValor() {
        return vlTransacao;
    }

    public void setValor(BigDecimal valor) {
        this.vlTransacao = valor;
    }

    public LocalDate getDtTransacao() {
        return dtTransacao;
    }

    public void setDtTransacao(LocalDate dtTransacao) {
        this.dtTransacao = dtTransacao;
    }

    public LocalDate getData() {
        return dtTransacao;
    }

    public void setData(LocalDate data) {
        this.dtTransacao = data;
    }

    public String getDsTransacao() {
        return dsTransacao;
    }

    public void setDsTransacao(String dsTransacao) {
        this.dsTransacao = dsTransacao;
    }

    public String getDescricao() {
        return dsTransacao;
    }

    public void setDescricao(String descricao) {
        this.dsTransacao = descricao;
    }

    public String getTpTransacao() {
        return tpTransacao;
    }

    public void setTpTransacao(String tpTransacao) {
        this.tpTransacao = tpTransacao;
    }

    public String getTipoTransacao() {
        return tpTransacao;
    }

    public void setTipoTransacao(String tipoTransacao) {
        this.tpTransacao = tipoTransacao;
    }

    public boolean isReceita() {
        return TIPO_RECEITA.equalsIgnoreCase(this.tpTransacao);
    }

    public boolean isDespesa() {
        return TIPO_DESPESA.equalsIgnoreCase(this.tpTransacao);
    }
}
