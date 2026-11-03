package br.com.fiap.fintech;

/**
 * Classe que representa o Usuário / Cliente no sistema Fintech.
 */
public class Usuario {

    // Atributos privados (encapsulamento)
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private Boolean contaAtiva;

    // Construtor padrão
    public Usuario() {
        this.contaAtiva = true;
    }

    // Construtor com parâmetros
    public Usuario(Integer id, String nome, String cpf, String email, String telefone, Boolean contaAtiva) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.contaAtiva = (contaAtiva != null) ? contaAtiva : true;
    }

    // Métodos de negócio com lógica real
    public boolean atualizarDados(String novoEmail, String novoTelefone) {
        if (novoEmail != null && !novoEmail.trim().isEmpty() && novoTelefone != null && !novoTelefone.trim().isEmpty()) {
            this.email = novoEmail;
            this.telefone = novoTelefone;
            return true;
        }
        return false;
    }

    public void desativarConta() {
        this.contaAtiva = false;
    }

    public void ativarConta() {
        this.contaAtiva = true;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getContaAtiva() {
        return contaAtiva;
    }

    public void setContaAtiva(Boolean contaAtiva) {
        this.contaAtiva = contaAtiva;
    }
}
