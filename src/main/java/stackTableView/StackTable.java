package stackTableView;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class StackTable {

    private final static String STACK_TABLE_ID = "#stack";
    final TableView<MemoryStack> stackTableView;
    final ObservableList<TableColumn<MemoryStack, ?>> stackTableColumns;
    final TableColumn<MemoryStack, String> addressColumn;
    final TableColumn<MemoryStack, String> valueColumn;


    public StackTable(Scene scene) {
        stackTableView = (TableView<MemoryStack>) scene.lookup(STACK_TABLE_ID);
        stackTableColumns = stackTableView.getColumns();
        addressColumn = (TableColumn<MemoryStack, String>) stackTableColumns.get(0);
        addressColumn.setCellValueFactory(new PropertyValueFactory<MemoryStack, String>("address"));
        valueColumn = (TableColumn<MemoryStack, String>) stackTableColumns.get(1);
        valueColumn.setCellValueFactory(new PropertyValueFactory<MemoryStack, String>("value"));
    }

    public TableView<MemoryStack> getStackTableView() {
        return stackTableView;
    }
}
