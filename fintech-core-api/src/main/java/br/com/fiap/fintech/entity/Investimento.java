package br.com.fiap.fintech.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade JPA para T_INVESTIMENTO baseada no modelo fisico da Fase 6.
 * Sequence: sq_investimento
 */
@Entity
@Table(name = "t_investimento")
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_investimento")
    @SequenceGenerator(name = "sq_investimento", sequenceName = "sq_investimento", allocationSize = 1)
    @Column(name = "id_investimento", nullable = false)
    private Long idInvestimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conta", nullable = false, foreignKey = @ForeignKey(name = "fk_investimento_conta"))
    private Conta conta;

    @Column(name = "tp_investimento", length = 50, nullable = false)
    private String tpInvestimento;

    @Column(name = "vl_aplicado", precision = 15, scale = 2, nullable = false)
    private BigDecimal vlAplicado;

    @Column(name = "dt_aplicacao", nullable = false)
    private LocalDate dtAplicacao;

    @Column(name = "tx_rendimento", precision = 5, scale = 2)
    private BigDecimal txRendimento;

    public Investimento() {}

    public Investimento(Conta conta, String tpInvestimento, BigDecimal vlAplicado,
                        LocalDate dtAplicacao, BigDecimal txRendimento) {
        this.conta = conta;
        this.tpInvestimento = tpInvestimento;
        this.vlAplicado = vlAplicado;
        this.dtAplicacao = dtAplicacao != null ? dtAplicacao : LocalDate.now();
        this.txRendimento = txRendimento;
    }

    public Investimento(Long idInvestimento, Conta conta, String tpInvestimento, BigDecimal vlAplicado,
                        LocalDate dtAplicacao, BigDecimal txRendimento) {
        this.idInvestimento = idInvestimento;
        this.conta = conta;
        this.tpInvestimento = tpInvestimento;
        this.vlAplicado = vlAplicado;
        this.dtAplicacao = dtAplicacao != null ? dtAplicacao : LocalDate.now();
        this.txRendimento = txRendimento;
    }

    // Getters and Setters
    public Long getIdInvestimento() {
        return idInvestimento;
    }

    public void setIdInvestimento(Long idInvestimento) {
        this.idInvestimento = idInvestimento;
    }

    public Long getId() {
        return idInvestimento;
    }

    public void setId(Long id) {
        this.idInvestimento = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getTpInvestimento() {
        return tpInvestimento;
    }

    public void setTpInvestimento(String tpInvestimento) {
        this.tpInvestimento = tpInvestimento;
    }

    public String getTipoInvestimento() {
        return tpInvestimento;
    }

    public void setTipoInvestimento(String tipoInvestimento) {
        this.tpInvestimento = tipoInvestimento;
    }

    public BigDecimal getVlAplicado() {
        return vlAplicado;
    }

    public void setVlAplicado(BigDecimal vlAplicado) {
        this.vlAplicado = vlAplicado;
    }

    public BigDecimal getValorAplicado() {
        return vlAplicado;
    }

    public void setValorAplicado(BigDecimal valorAplicado) {
        this.vlAplicado = valorAplicado;
    }

    public LocalDate getDtAplicacao() {
        return dtAplicacao;
    }

    public void setDtAplicacao(LocalDate dtAplicacao) {
        this.dtAplicacao = dtAplicacao;
    }

    public LocalDate getDataAplicacao() {
        return dtAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dtAplicacao = dataAplicacao;
    }

    public BigDecimal getTxRendimento() {
        return txRendimento;
    }

    public void setTxRendimento(BigDecimal txRendimento) {
        this.txRendimento = txRendimento;
    }

    public BigDecimal getTaxaRendimento() {
        return txRendimento;
    }

    public void setTaxaRendimento(BigDecimal taxaRendimento) {
        this.txRendimento = taxaRendimento;
    }
}
