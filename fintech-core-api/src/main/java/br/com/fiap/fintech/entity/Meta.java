package br.com.fiap.fintech.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade JPA para T_META baseada no modelo fisico da Fase 6.
 * Sequence: sq_meta
 */
@Entity
@Table(name = "t_meta")
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_meta")
    @SequenceGenerator(name = "sq_meta", sequenceName = "sq_meta", allocationSize = 1)
    @Column(name = "id_meta", nullable = false)
    private Long idMeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_meta_usuario"))
    private Usuario usuario;

    @Column(name = "ds_meta", length = 255, nullable = false)
    private String dsMeta;

    @Column(name = "vl_objetivo", precision = 15, scale = 2, nullable = false)
    private BigDecimal vlObjetivo;

    @Column(name = "dt_limite")
    private LocalDate dtLimite;

    @Column(name = "vl_atual", precision = 15, scale = 2)
    private BigDecimal vlAtual = BigDecimal.ZERO;

    public Meta() {}

    public Meta(Usuario usuario, String dsMeta, BigDecimal vlObjetivo, LocalDate dtLimite, BigDecimal vlAtual) {
        this.usuario = usuario;
        this.dsMeta = dsMeta;
        this.vlObjetivo = vlObjetivo;
        this.dtLimite = dtLimite;
        this.vlAtual = vlAtual != null ? vlAtual : BigDecimal.ZERO;
    }

    public Meta(Long idMeta, Usuario usuario, String dsMeta, BigDecimal vlObjetivo, LocalDate dtLimite, BigDecimal vlAtual) {
        this.idMeta = idMeta;
        this.usuario = usuario;
        this.dsMeta = dsMeta;
        this.vlObjetivo = vlObjetivo;
        this.dtLimite = dtLimite;
        this.vlAtual = vlAtual != null ? vlAtual : BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(Long idMeta) {
        this.idMeta = idMeta;
    }

    public Long getId() {
        return idMeta;
    }

    public void setId(Long id) {
        this.idMeta = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDsMeta() {
        return dsMeta;
    }

    public void setDsMeta(String dsMeta) {
        this.dsMeta = dsMeta;
    }

    public String getDescricao() {
        return dsMeta;
    }

    public void setDescricao(String descricao) {
        this.dsMeta = descricao;
    }

    public BigDecimal getVlObjetivo() {
        return vlObjetivo;
    }

    public void setVlObjetivo(BigDecimal vlObjetivo) {
        this.vlObjetivo = vlObjetivo;
    }

    public BigDecimal getValorObjetivo() {
        return vlObjetivo;
    }

    public void setValorObjetivo(BigDecimal valorObjetivo) {
        this.vlObjetivo = valorObjetivo;
    }

    public LocalDate getDtLimite() {
        return dtLimite;
    }

    public void setDtLimite(LocalDate dtLimite) {
        this.dtLimite = dtLimite;
    }

    public LocalDate getDataLimite() {
        return dtLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dtLimite = dataLimite;
    }

    public BigDecimal getVlAtual() {
        return vlAtual;
    }

    public void setVlAtual(BigDecimal vlAtual) {
        this.vlAtual = vlAtual;
    }

    public BigDecimal getValorAtual() {
        return vlAtual;
    }

    public void setValorAtual(BigDecimal valorAtual) {
        this.vlAtual = valorAtual;
    }
}
