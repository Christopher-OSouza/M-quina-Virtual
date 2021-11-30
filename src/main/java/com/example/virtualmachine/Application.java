/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/*
 * Realiza a construção da interface e realiza a interação com toda a lógica de programação da máquina virtual.
 */

package com.example.virtualmachine;

import codeTableView.CodeTable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import menuBar.ExecuteProgram;
import menuBar.ReadFile;
import stackTableView.StackTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Application extends javafx.application.Application {

    private final static String RESULT_TEXT_AREA_ID = "#resultTextArea";
    private final static String MENU_BAR_ID = "#menuBar";
    private final static String NEXT_STEP_BUTTON_ID = "#nextStepButton";
    private static String filePath = null;

    private final static int SCREEN_WIDTH = 1250;
    private final static int SCREEN_HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("virtual-machine.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setTitle("Máquina Virtual");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icones/CompilerIcon.png"))));
        stage.setScene(scene);
        stage.show();

        final TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("");
        textInputDialog.setHeaderText("Digite um número:");
        textInputDialog.setContentText("");

        final TextArea resultTextArea = (TextArea) scene.lookup(RESULT_TEXT_AREA_ID);

        final Alert alertEmptyFile = new Alert(Alert.AlertType.ERROR);
        alertEmptyFile.setTitle("Arquivo em branco");
        alertEmptyFile.setContentText("Por favor, escolhe um arquivo antes!");

        final Alert endOfExecutionAlert = new Alert(Alert.AlertType.INFORMATION);
        endOfExecutionAlert.setTitle("Fim da execução");
        endOfExecutionAlert.setContentText("A execução do programa finalizou!");

        /* Creating table code and stack */
        StackTable stackTable = new StackTable(scene);
        CodeTable codeTable = new CodeTable(scene);

        final Button nextStepButton = (Button) scene.lookup(NEXT_STEP_BUTTON_ID);
        Object[] array = {textInputDialog, resultTextArea, endOfExecutionAlert, nextStepButton, stackTable.getStackTableView()};
        List<Object> objects = Arrays.asList(array);
        VirtualMachine virtualMachine = new VirtualMachine(objects);

        final MenuBar menuBar = (MenuBar) scene.lookup(MENU_BAR_ID);
        final List<Menu> menus = menuBar.getMenus();
        final MenuItem openFileButton = menus.get(0).getItems().get(0);
        final List<MenuItem> executeButtons = menus.get(1).getItems();
        final MenuItem normalExecution = executeButtons.get(0);
        final MenuItem stepByStepExecution = executeButtons.get(1);
        final MenuItem openGitHub = menus.get(2).getItems().get(0);

        ReadFile readFile = new ReadFile();
        openFileButton.setOnAction(actionEvent -> filePath = readFile.openFile(stage, virtualMachine, codeTable.getCodeTableView()));
        ExecuteProgram executeProgram = new ExecuteProgram();
        normalExecution.setOnAction(actionEvent -> executeProgram.executeNormalCode(virtualMachine, alertEmptyFile, nextStepButton, filePath));
        stepByStepExecution.setOnAction(actionEvent -> executeProgram.executeStepByStepCode(virtualMachine, alertEmptyFile, nextStepButton, filePath));
        openGitHub.setOnAction(actionEvent -> executeProgram.openGitHub());
        normalExecution.setOnAction(actionEvent -> executeProgram.executeNormalCode(virtualMachine, alertEmptyFile, nextStepButton, filePath));
        stepByStepExecution.setOnAction(actionEvent -> executeProgram.executeStepByStepCode(virtualMachine, alertEmptyFile, nextStepButton, filePath));
        nextStepButton.setOnAction(actionEvent -> executeProgram.executeNextStep(virtualMachine, codeTable.getCodeTableView()));
        nextStepButton.setVisible(false);
    }

    public static void main(String[] args) {
        launch();
    }
}