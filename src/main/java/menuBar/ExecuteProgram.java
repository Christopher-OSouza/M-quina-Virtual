/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/**
 * Responsável por executar o programa (normal e passo a passo)
 */
package menuBar;

import com.example.virtualmachine.VirtualMachine;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.awt.*;
import java.net.URI;

public class ExecuteProgram {

    private final static String GITHUB_LINK = "https://github.com/Christopher-OSouza/M-quina-Virtual";

    public ExecuteProgram() {
    }

    /**
     * Método responsável por verificar se o usuário está tentando executar um programa sem escolher um arquivo antes
     * e também, responsável por executar o código normal, sem ser passo a passo, executando o programa inteiro de uma vez.
     *
     * @param virtualMachine a classe que realiza toda a lógica de programação da máquina virtual, onde será "resetada"
     *                       as principais variáveis utilizadas nesta classe e executar uma nova execução da máquina virtual.
     * @param alertEmptyFile para alertar o usuário que ele está tentando executar um programa sem inserir um arquivo de
     *                       entrada para ser analisado antes.
     * @param nextStepButton para fazer o botão da execução passo a passo ficar invisível.
     * @param filePath       que contém o caminho do arquivo inserido pelo usuário através da ‘interface’ gráfica, se o caminho
     *                       for vazio ou "null" indica que ele não escolheu nenhum arquivo ainda.
     */
    public void executeNormalCode(VirtualMachine virtualMachine, Alert alertEmptyFile, Button nextStepButton, String filePath) {
        try {
            try {
                if (filePath == null) {
                    alertEmptyFile.show();
                } else {
                    virtualMachine.cleanDate();
                    nextStepButton.setVisible(false);
                    virtualMachine.run();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Responsável por verificar se o usuário está tentando executar um programa sem escolher um arquivo antes e também,
     * disponibilizar um botão na interface gráfica para o usuário selecionar a função passo a passo.
     *
     * @param virtualMachine a classe que realiza toda a lógica de programação da máquina virtual, onde será "resetada"
     *                       as principais variáveis utilizadas nesta classe para uma próxima execução.
     * @param alertEmptyFile para alertar o usuário que ele está tentando executar um programa sem inserir um arquivo de
     *                       entrada para ser analisado antes.
     * @param nextStepButton para fazer o botão da execução passo a passo ficar visível para o usuário ir selecionando,
     *                       conforme ele deseja executar a próxima instrução.
     * @param filePath       que contém o caminho do arquivo inserido pelo usuário através da ‘interface’ gráfica, se o caminho
     *                       for vazio ou "null" indica que ele não escolheu nenhum arquivo ainda.
     */
    public void executeStepByStepCode(VirtualMachine virtualMachine, Alert alertEmptyFile, Button nextStepButton, String filePath) {
        try {
            try {
                if (filePath == null) {
                    alertEmptyFile.show();
                } else {
                    virtualMachine.cleanDate();
                    nextStepButton.setVisible(true);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método responsável por executar o código passo a passo, ou seja, executando uma instrução de cada vez do arquivo
     * de entrada escolhido pelo usuário conforme ele pressiona o botão "Próxima Instrução", esse processo é conhecido
     * também, como "debbuging".
     *
     * @param virtualMachine a classe que realiza toda a lógica de programação da máquina virtual, onde será executada
     *                       uma instrução de cada vez.
     * @param codeTable      é a tabela que contém os dados do arquivo de entrada escolhido pelo usuário que permitirá o usuário
     *                       verificar a linha focada na instrução que a máquina virtual está executando conforme ele pressiona
     *                       o botão de "Próxima Instrução".
     */
    public void executeNextStep(VirtualMachine virtualMachine, TableView codeTable) {
        int position = virtualMachine.getPosition();
        // Foca na posição da tabela, quando o próximo passo é solicitado
        codeTable.requestFocus();
        codeTable.getSelectionModel().select(position);
        codeTable.getFocusModel().focus(position);
        codeTable.scrollTo(position);
        virtualMachine.analiseCommand();
    }

    /**
     * Abrir o GitHub que contém o repositório do projeto da Máquina Virtual.
     */
    public void openGitHub() {
        try {
            URI link = new URI(GITHUB_LINK);
            Desktop.getDesktop().browse(link);
        } catch (Exception error) {
            System.out.println("Error to open the GitHub link: " + error);
        }
    }
}
