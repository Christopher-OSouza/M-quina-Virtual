/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/* Classe para armazenar os valores na stack e poder imprimir na interface */

package stackTableView;

public class MemoryStack {
    public String value;
    public int address;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public MemoryStack(String value, int address) {
        this.value = value;
        this.address = address;
    }
}
