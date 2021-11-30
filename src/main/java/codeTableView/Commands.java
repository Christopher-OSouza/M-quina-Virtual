/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/*
 * Classe modelo para armazenar um comando (instrução), com seus respectivos valores.
 */

package codeTableView;

public class Commands {
    private String instruction1;
    private String instruction2;
    private String instruction3;
    private String instruction4;
    private int row;

    public String getInstruction1() {
        return instruction1;
    }

    public void setInstruction1(String instruction1) {
        this.instruction1 = instruction1;
    }

    public String getInstruction2() {
        return instruction2;
    }

    public void setInstruction2(String instruction2) {
        this.instruction2 = instruction2;
    }

    public String getInstruction3() {
        return instruction3;
    }

    public void setInstruction3(String instruction3) {
        this.instruction3 = instruction3;
    }

    public String getInstruction4() {
        return instruction4;
    }

    public void setInstruction4(String instruction4) {
        this.instruction4 = instruction4;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}


