package com.example.virtualmachine;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {

    private final static String CODE_TABLE_ID = "#tableCode";
    private final static String RESULT_TEXT_AREA_ID = "#resultTextArea";
    private final static String MENU_BAR_ID = "#menuBar";
    private final static String NEXT_STEP_BUTTON_ID = "#nextStepButton";
    private final static String STACK_TABLE_ID = "#stack";
    private static String filePath = null;

    private final static int SCREEN_WIDTH = 1250;
    private final static int SCREEN_HEIGHT = 800;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setTitle("Máquina Virtual");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icones/CompilerIcon.png"))));
        stage.setScene(scene);
        stage.show();

        //region TEXT INPUT DIALOG
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("");
        textInputDialog.setHeaderText("Digite um número:");
        textInputDialog.setContentText("");
        //endregion

        //region TEXT AREA
        final TextArea resultTextArea = (TextArea) scene.lookup(RESULT_TEXT_AREA_ID);
        //endregion

        //region ALERTS
        Alert alertEmptyFile = new Alert(Alert.AlertType.ERROR);
        alertEmptyFile.setTitle("Arquivo em branco");
        alertEmptyFile.setContentText("Por favor, escolhe um arquivo antes!");

        Alert endOfExecutionAlert = new Alert(Alert.AlertType.INFORMATION);
        endOfExecutionAlert.setTitle("Fim da execução");
        endOfExecutionAlert.setContentText("A execução do programa finalizou!");
        //endregion

         final TableView<Stack> stackTable = (TableView<Stack>) scene.lookup(STACK_TABLE_ID);
         final ObservableList<TableColumn<Stack, ?>> stackTableColumns;
        stackTableColumns = stackTable.getColumns();
        final TableColumn<Stack, String> addressColumn=(TableColumn<Stack, String>) stackTableColumns.get(0);
        addressColumn.setCellValueFactory(new PropertyValueFactory<Stack, String>("coluna"));
        final TableColumn<Stack, String> valueColumn=(TableColumn<Stack, String>) stackTableColumns.get(1);
        valueColumn.setCellValueFactory(new PropertyValueFactory<Stack, String>("valor"));


        //region TABLE VIEW AND COLUMNS
        final TableView<Comando> codeTable = (TableView<Comando>) scene.lookup(CODE_TABLE_ID);
        final ObservableList<TableColumn<Comando, ?>> tableColumns = codeTable.getColumns();

        final TableColumn<Comando, String> lineColumn = (TableColumn<Comando, String>) tableColumns.get(0);
        lineColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("linha"));

        final TableColumn<Comando, String> jmpColumn = (TableColumn<Comando, String>) tableColumns.get(1);
        jmpColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("instrucao1"));

        final TableColumn<Comando, String> instructionColumn = (TableColumn<Comando, String>) tableColumns.get(2);
        instructionColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("instrucao2"));

        final TableColumn<Comando, String> firstValueColumn = (TableColumn<Comando, String>) tableColumns.get(3);
        firstValueColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("instrucao3"));

        final TableColumn<Comando, String> secondValueColumn = (TableColumn<Comando, String>) tableColumns.get(4);
        secondValueColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("instrucao4"));
        //endregion*/

        //region BUTTON
        final Button nextStepButton = (Button) scene.lookup(NEXT_STEP_BUTTON_ID);
        Object[] array = {textInputDialog, resultTextArea, endOfExecutionAlert,nextStepButton,stackTable};
        List<Object> objects = Arrays.asList(array);
        VirtualMachine virtualMachine = new VirtualMachine(objects);

        nextStepButton.setOnAction(actionEvent -> executeNextStep(virtualMachine, codeTable,stackTable));
        nextStepButton.setVisible(false);
        //#endregion

        //region FILE CHOOSER
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        //endregion

        //region MENUBAR ITEMS
        // MENUBAR
        final MenuBar menuBar = (MenuBar) scene.lookup(MENU_BAR_ID);
        final List<Menu> menus = menuBar.getMenus();

        // FILE PICKER BUTTONS
        final MenuItem openFileButton = menus.get(0).getItems().get(0);
        openFileButton.setOnAction(actionEvent -> openFile(fileChooser, stage, virtualMachine, codeTable,stackTable));

        // EXECUTE BUTTONS
        final List<MenuItem> executeButtons = menus.get(1).getItems();
        final MenuItem normalExecution = executeButtons.get(0);
        normalExecution.setOnAction(actionEvent -> executeNormalCode(virtualMachine, alertEmptyFile, nextStepButton));
        final MenuItem stepByStepExecution = executeButtons.get(1);
        stepByStepExecution.setOnAction(actionEvent -> executeStepByStepCode(virtualMachine, alertEmptyFile, nextStepButton));
        //endregion
    }


    private void executeNormalCode(VirtualMachine virtualMachine, Alert alertEmptyFile, Button nextStepButton) {
        try {
            try {
                if (filePath == null) {
                    alertEmptyFile.show();
                } else {
                    virtualMachine.limparDados();
                    nextStepButton.setVisible(false);
                    virtualMachine.rodar();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void openFile(FileChooser fileChooser, Stage primaryStage, VirtualMachine virtualMachine, TableView codeTable,TableView stackTable) {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        try {
            if (selectedFile != null) {
                filePath=selectedFile.getPath();
                virtualMachine.prepararArquivo(filePath);
                List<Comando> comandoList = virtualMachine.getComandos();
                codeTable.setItems(FXCollections.observableList(comandoList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeStepByStepCode(VirtualMachine virtualMachine, Alert alertEmptyFile, Button nextStepButton) {
        try {
            try {
                if (filePath == null) {
                    alertEmptyFile.show();
                } else {
                    virtualMachine.limparDados();
                    nextStepButton.setVisible(true);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeNextStep(VirtualMachine virtualMachine, TableView codeTable,TableView stackTable) {
        int position = virtualMachine.getPosicao();
        codeTable.requestFocus();
        codeTable.getSelectionModel().select(position);
        codeTable.getFocusModel().focus(position);
        codeTable.scrollTo(position);
        virtualMachine.analisarComando();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}