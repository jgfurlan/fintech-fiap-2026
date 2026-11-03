package br.com.fiap.fintech;

/**
 * Superclasse que representa uma Conta Bancária no sistema Fintech.
 * Fornece atributos e comportamentos comuns a todas as contas.
 */
public class Conta {

    // Atributos privados (encapsulamento)
    private String agencia;
    private String numeroConta;
    private String titular;
    private Double saldo;

    // Construtor padrão
    public Conta() {
        this.saldo = 0.0;
    }

    // Construtor parametrizado
    public Conta(String agencia, String numeroConta, String titular, Double saldo) {
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = (saldo != null && saldo >= 0.0) ? saldo : 0.0;
    }

    // Métodos de negócio com lógica real e retornos (sem System.out.println)
    public boolean depositar(Double valor) {
        if (valor != null && valor > 0.0) {
            this.saldo += valor;
            return true;
        }
        return false;
    }

    public boolean sacar(Double valor) {
        if (valor != null && valor > 0.0 && this.saldo >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    public boolean transferir(Double valor, Conta contaDestino) {
        if (contaDestino != null && valor != null && valor > 0.0) {
            if (this.sacar(valor)) {
                contaDestino.depositar(valor);
                return true;
            }
        }
        return false;
    }

    public Double consultarSaldo() {
        return this.saldo;
    }

    // Getters e Setters
    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        if (saldo != null) {
            this.saldo = saldo;
        }
    }
}
