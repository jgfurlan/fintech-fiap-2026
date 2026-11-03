package br.com.fiap.fintech.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA para T_USUARIO baseada no modelo fisico da Fase 6.
 * Sequence: sq_usuario
 */
@Entity
@Table(name = "t_usuario", uniqueConstraints = {
    @UniqueConstraint(name = "uk_usuario_email", columnNames = "ds_email")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_usuario")
    @SequenceGenerator(name = "sq_usuario", sequenceName = "sq_usuario", allocationSize = 1)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nm_usuario", length = 100, nullable = false)
    private String nmUsuario;

    @Column(name = "ds_email", length = 100, nullable = false, unique = true)
    private String dsEmail;

    @Column(name = "ds_senha", length = 50, nullable = false)
    private String dsSenha;

    @Column(name = "dt_cadastro", nullable = false)
    private LocalDate dtCadastro = LocalDate.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conta> contas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meta> metas = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nmUsuario, String dsEmail, String dsSenha) {
        this.nmUsuario = nmUsuario;
        this.dsEmail = dsEmail;
        this.dsSenha = dsSenha;
        this.dtCadastro = LocalDate.now();
    }

    public Usuario(Long idUsuario, String nmUsuario, String dsEmail, String dsSenha, LocalDate dtCadastro) {
        this.idUsuario = idUsuario;
        this.nmUsuario = nmUsuario;
        this.dsEmail = dsEmail;
        this.dsSenha = dsSenha;
        this.dtCadastro = dtCadastro != null ? dtCadastro : LocalDate.now();
    }

    // Getters and Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getId() {
        return idUsuario;
    }

    public void setId(Long id) {
        this.idUsuario = id;
    }

    public String getNmUsuario() {
        return nmUsuario;
    }

    public void setNmUsuario(String nmUsuario) {
        this.nmUsuario = nmUsuario;
    }

    public String getNome() {
        return nmUsuario;
    }

    public void setNome(String nome) {
        this.nmUsuario = nome;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public String getEmail() {
        return dsEmail;
    }

    public void setEmail(String email) {
        this.dsEmail = email;
    }

    public String getDsSenha() {
        return dsSenha;
    }

    public void setDsSenha(String dsSenha) {
        this.dsSenha = dsSenha;
    }

    public String getSenha() {
        return dsSenha;
    }

    public void setSenha(String senha) {
        this.dsSenha = senha;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public LocalDate getDataCadastro() {
        return dtCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dtCadastro = dataCadastro;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }

    public List<Meta> getMetas() {
        return metas;
    }

    public void setMetas(List<Meta> metas) {
        this.metas = metas;
    }

    public void addConta(Conta conta) {
        this.contas.add(conta);
        conta.setUsuario(this);
    }

    public void addMeta(Meta meta) {
        this.metas.add(meta);
        meta.setUsuario(this);
    }
}
