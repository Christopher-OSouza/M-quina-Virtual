/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/**
 * Pega a tabela da interface e insere os valores corretos para serem mostrados na tela.
 */
package stackTableView;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StackTable {

    private final String STACK_TABLE_ID = "#stack";
    private final TableView<MemoryStack> stackTableView;
    private final ObservableList<TableColumn<MemoryStack, ?>> stackTableColumns;
    private final TableColumn<MemoryStack, String> addressColumn;
    private final TableColumn<MemoryStack, String> valueColumn;

    /**
     * Construtor que realiza a interação de uma tabela por código de programação com a tabela criada na ‘interface’ gráfica,
     * que mostrará a pilha que a máquina virtual está armazenando conforme realiza as instruções que está sendo
     * analisada no arquivo de entrada.
     *
     * @param scene é a tela que contém a interface gráfica da máquina virtual.
     */
    public StackTable(Scene scene) {
        stackTableView = (TableView<MemoryStack>) scene.lookup(STACK_TABLE_ID);
        stackTableColumns = stackTableView.getColumns();
        addressColumn = (TableColumn<MemoryStack, String>) stackTableColumns.get(0);
        addressColumn.setCellValueFactory(new PropertyValueFactory<MemoryStack, String>("address"));
        valueColumn = (TableColumn<MemoryStack, String>) stackTableColumns.get(1);
        valueColumn.setCellValueFactory(new PropertyValueFactory<MemoryStack, String>("value"));
    }

    /**
     * @return a tabela que mostrará a pilha que a máquina virtual está armazenando conforme realiza as instruções.
     */
    public TableView<MemoryStack> getStackTableView() {
        return stackTableView;
    }
}
