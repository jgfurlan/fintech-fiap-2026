package br.com.fiap.fintech.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA para T_CONTA baseada no modelo fisico da Fase 6.
 * Sequence: sq_conta
 */
@Entity
@Table(name = "t_conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_conta")
    @SequenceGenerator(name = "sq_conta", sequenceName = "sq_conta", allocationSize = 1)
    @Column(name = "id_conta", nullable = false)
    private Long idConta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_conta_usuario"))
    private Usuario usuario;

    @Column(name = "tp_conta", length = 50, nullable = false)
    private String tpConta;

    @Column(name = "vl_saldo_atual", precision = 15, scale = 2, nullable = false)
    private BigDecimal vlSaldoAtual = BigDecimal.ZERO;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investimento> investimentos = new ArrayList<>();

    public Conta() {}

    public Conta(Usuario usuario, String tpConta, BigDecimal vlSaldoAtual) {
        this.usuario = usuario;
        this.tpConta = tpConta;
        this.vlSaldoAtual = vlSaldoAtual != null ? vlSaldoAtual : BigDecimal.ZERO;
    }

    public Conta(Long idConta, Usuario usuario, String tpConta, BigDecimal vlSaldoAtual) {
        this.idConta = idConta;
        this.usuario = usuario;
        this.tpConta = tpConta;
        this.vlSaldoAtual = vlSaldoAtual != null ? vlSaldoAtual : BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getIdConta() {
        return idConta;
    }

    public void setIdConta(Long idConta) {
        this.idConta = idConta;
    }

    public Long getId() {
        return idConta;
    }

    public void setId(Long id) {
        this.idConta = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTpConta() {
        return tpConta;
    }

    public void setTpConta(String tpConta) {
        this.tpConta = tpConta;
    }

    public String getTipoConta() {
        return tpConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tpConta = tipoConta;
    }

    public BigDecimal getVlSaldoAtual() {
        return vlSaldoAtual;
    }

    public void setVlSaldoAtual(BigDecimal vlSaldoAtual) {
        this.vlSaldoAtual = vlSaldoAtual;
    }

    public BigDecimal getSaldoAtual() {
        return vlSaldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.vlSaldoAtual = saldoAtual;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    public List<Investimento> getInvestimentos() {
        return investimentos;
    }

    public void setInvestimentos(List<Investimento> investimentos) {
        this.investimentos = investimentos;
    }

    public void addTransacao(Transacao transacao) {
        this.transacoes.add(transacao);
        transacao.setConta(this);
    }

    public void addInvestimento(Investimento investimento) {
        this.investimentos.add(investimento);
        investimento.setConta(this);
    }
}
