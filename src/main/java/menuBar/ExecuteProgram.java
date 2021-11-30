/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/* Responsável por executar o programa (normal e passo a passo) */

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

    public void executeNextStep(VirtualMachine virtualMachine, TableView codeTable) {
        int position = virtualMachine.getPosition();
        //Foca na posição da tabela, quando o próximo passo é solicitado
        codeTable.requestFocus();
        codeTable.getSelectionModel().select(position);
        codeTable.getFocusModel().focus(position);
        codeTable.scrollTo(position);
        virtualMachine.analiseCommand();
    }

    public void openGitHub(){
        try{
            URI link = new URI(GITHUB_LINK);
            Desktop.getDesktop().browse(link);
        }catch(Exception error){
            System.out.println("Error to open the GitHub link: " + error);
        }
    }
}
