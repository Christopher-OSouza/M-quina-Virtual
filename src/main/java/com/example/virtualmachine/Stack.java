package com.example.virtualmachine;

public class Stack {
    public String valor;
    public int coluna;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public Stack(String valor, int coluna) {
        this.valor = valor;
        this.coluna = coluna;
    }
}
