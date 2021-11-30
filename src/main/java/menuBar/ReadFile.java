/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/**
 * Responsável por ler o arquivo ".obj" que foi gerado pelo compilador
 */

package menuBar;

import codeTableView.Commands;
import com.example.virtualmachine.VirtualMachine;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReadFile {

    private final FileChooser fileChooser = new FileChooser();

    /**
     * Construtor da classe que define que o arquivo que o usuário escolher para  a máquina virtual deverá ser do tipo ".obj".
     */
    public ReadFile() {
        //Pega apenas extensões .obj
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.obj)", "*.obj");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * Responsável por abrir o arquivo para o usuário e inserir o código contido no arquivo na interface gráfica, onde o
     * usuário pode validar se de fato é esse arquivo que desejava abrir.
     *
     * @param primaryStage seria a interface gráfica, para saber onde será mostrado o campo que conterá o arquivo.
     * @param virtualMachine é a classe que realiza toda a lógica de programação da máquina virtual, passando por parâmetro
     *                       para o método "prepareFile()" o conteúdo do arquivo escolhido pelo usuário.
     * @param codeTable é a tabela que mostrará o código contido no arquivo escolhido pelo usuário.
     * @return o caminho do arquivo que o usuário escolheu, se caso for vazio ou "null" o usuário ainda não escolheu
     *         nenhum arquivo.
     */
    public String openFile(Stage primaryStage, VirtualMachine virtualMachine, TableView codeTable) {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        String filePath = null;
        try {
            if (selectedFile != null) {
                filePath = selectedFile.getPath();
                virtualMachine.prepareFile(filePath);
                //Pega lista de comandos (instruções) para mostrar na interface
                List<Commands> commandsList = virtualMachine.getCommands();
                codeTable.setItems(FXCollections.observableList(commandsList));
            }
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
