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

    public ReadFile() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.obj)", "*.obj");
        fileChooser.getExtensionFilters().add(extFilter);
    }

    public String openFile(Stage primaryStage, VirtualMachine virtualMachine, TableView codeTable) {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        String filePath = null;
        try {
            if (selectedFile != null) {
                filePath = selectedFile.getPath();
                virtualMachine.prepareFile(filePath);
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
