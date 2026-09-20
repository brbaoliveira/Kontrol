package com.example.kontrol.model;

public class Usuario {
    private Long id;
    private String user;
    private String nome;
    private String email;
    private String dataNasc;
    private String senha;

    public Usuario(Long id, String user, String nome, String email, String dataNasc, String senha) {
        this.id = id;
        this.user = user;
        this.nome = nome;
        this.email = email;
        this.dataNasc = dataNasc;
        this.senha = senha;
    }
    public Usuario(Long id, String user, String nome, String email, String dataNasc) {
        this.id = id;
        this.user = user;
        this.nome = nome;
        this.email = email;
        this.dataNasc = dataNasc;
    }
    public Usuario(Long id, String user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNasc() {
        return dataNasc;
    }
    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
