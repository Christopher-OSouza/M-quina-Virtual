/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/* Prepara os valores para serem imprimidos na interface */

package codeTableView;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CodeTable {
    private final static String CODE_TABLE_ID = "#tableCode";
    private final TableView<Commands> codeTableView;


    public CodeTable(Scene scene) {
        codeTableView = (TableView<Commands>) scene.lookup(CODE_TABLE_ID);
        ObservableList<TableColumn<Commands, ?>> tableColumns = codeTableView.getColumns();
        TableColumn<Commands, String> lineColumn = (TableColumn<Commands, String>) tableColumns.get(0);
        lineColumn.setCellValueFactory(new PropertyValueFactory<>("row"));
        TableColumn<Commands, String> jmpColumn = (TableColumn<Commands, String>) tableColumns.get(1);
        jmpColumn.setCellValueFactory(new PropertyValueFactory<>("instruction1"));
        TableColumn<Commands, String> instructionColumn = (TableColumn<Commands, String>) tableColumns.get(2);
        instructionColumn.setCellValueFactory(new PropertyValueFactory<>("instruction2"));
        TableColumn<Commands, String> firstValueColumn = (TableColumn<Commands, String>) tableColumns.get(3);
        firstValueColumn.setCellValueFactory(new PropertyValueFactory<>("instruction3"));
        TableColumn<Commands, String> secondValueColumn = (TableColumn<Commands, String>) tableColumns.get(4);
        secondValueColumn.setCellValueFactory(new PropertyValueFactory<>("instruction4"));
    }

    public TableView<Commands> getCodeTableView() {
        return codeTableView;
    }
}
