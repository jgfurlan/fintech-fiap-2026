package br.com.fiap.fintech.entity;

import jakarta.persistence.*;

/**
 * Entidade JPA para T_CATEGORIA baseada no modelo fisico da Fase 6.
 * Sequence: sq_categoria
 * tp_categoria: 'R' (Receita) ou 'D' (Despesa)
 */
@Entity
@Table(name = "t_categoria")
public class Categoria {

    public static final String TIPO_RECEITA = "R";
    public static final String TIPO_DESPESA = "D";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_categoria")
    @SequenceGenerator(name = "sq_categoria", sequenceName = "sq_categoria", allocationSize = 1)
    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @Column(name = "nm_categoria", length = 50, nullable = false)
    private String nmCategoria;

    @Column(name = "tp_categoria", length = 1, nullable = false)
    private String tpCategoria;

    public Categoria() {}

    public Categoria(String nmCategoria, String tpCategoria) {
        this.nmCategoria = nmCategoria;
        this.tpCategoria = tpCategoria;
    }

    public Categoria(Long idCategoria, String nmCategoria, String tpCategoria) {
        this.idCategoria = idCategoria;
        this.nmCategoria = nmCategoria;
        this.tpCategoria = tpCategoria;
    }

    // Getters and Setters
    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Long getId() {
        return idCategoria;
    }

    public void setId(Long id) {
        this.idCategoria = id;
    }

    public String getNmCategoria() {
        return nmCategoria;
    }

    public void setNmCategoria(String nmCategoria) {
        this.nmCategoria = nmCategoria;
    }

    public String getNome() {
        return nmCategoria;
    }

    public void setNome(String nome) {
        this.nmCategoria = nome;
    }

    public String getTpCategoria() {
        return tpCategoria;
    }

    public void setTpCategoria(String tpCategoria) {
        this.tpCategoria = tpCategoria;
    }

    public boolean isReceita() {
        return TIPO_RECEITA.equalsIgnoreCase(this.tpCategoria);
    }

    public boolean isDespesa() {
        return TIPO_DESPESA.equalsIgnoreCase(this.tpCategoria);
    }
}
