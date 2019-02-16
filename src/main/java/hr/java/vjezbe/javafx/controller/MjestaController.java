package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MjestaController {

    @FXML
    private TableView<Mjesto> mjestaTable;
    @FXML
    private TableColumn<Mjesto, Integer> idColumn;
    @FXML
    private TableColumn<Mjesto, String> nazivColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label nazivZupanijaLabel;
    @FXML
    private Label vrstaMjestaLabel;
    @FXML
    private Label brojPostajaLabel;
    @FXML
    private TextField nazivFilterTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button newButton;

    private static final Logger LOGGER = LoggerFactory.getLogger(MjestaController.class);
    private ObservableList<Mjesto> obsMjesta;
    private Model model;

    public MjestaController() {
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showMjestoDetails(null);

        mjestaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMjestoDetails(newValue));
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        newButton.setDisable(true);
    }

    public void setModel(Model model) {
        this.model = model;
        obsMjesta = FXCollections.observableArrayList(model.getMjestoService().findAll());
        mjestaTable.setItems(obsMjesta);
    }

    private void showMjestoDetails(Mjesto mjesto) {
        if (mjesto != null) {
            idLabel.setText(mjesto.getId().toString());
            nazivLabel.setText(mjesto.getNaziv());
            nazivZupanijaLabel.setText(mjesto.getZupanija().getNaziv());
            vrstaMjestaLabel.setText(mjesto.getVrstaMjesta().toString());
            brojPostajaLabel.setText(String.valueOf(mjesto.getMjernePostaje().size()));
        } else {
            idLabel.setText("");
            nazivLabel.setText("");
            nazivZupanijaLabel.setText("");
            vrstaMjestaLabel.setText("");
            brojPostajaLabel.setText("");
        }
    }

    public void getMjestaByName() {
        obsMjesta = FXCollections.observableArrayList(model.getMjestoService()
                .findByName(nazivFilterTextField.getText()));
        mjestaTable.setItems(obsMjesta);
    }

    @FXML
    private void handleDeleteMjesto() {
        int selectedIndex = mjestaTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            mjestaTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje mjesta");
            alert.setHeaderText("Nije odabrano mjesto iz tablice.");
            alert.setContentText("Odaberite mjesto iz tablice.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewMjesto() {
        Mjesto tempMjesto = new Mjesto();
        boolean okClicked = showMjestoEditDialog(tempMjesto);
        if (okClicked) {
            model.getMjestoService().save(tempMjesto);
            obsMjesta.add(tempMjesto);
        }
    }

    @FXML
    private void handleEditMjesto() {
        Mjesto selectedMjesto = mjestaTable.getSelectionModel().getSelectedItem();
        if (selectedMjesto != null) {
            boolean okClicked = showMjestoEditDialog(selectedMjesto);
            if (okClicked) {
                showMjestoDetails(selectedMjesto);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uređivanje mjesta");
            alert.setHeaderText("Nije odabrano mjesto iz tablice");
            alert.setContentText("Odaberite mjesto iz tablice.");

            alert.showAndWait();
        }
    }

    private boolean showMjestoEditDialog(Mjesto mjesto) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/MjestoEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit mjesta");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the Object into the controller.
            MjestoEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMjesto(mjesto);
            controller.setModel(model);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException ex) {
            LOGGER.error("Error occurred while creating new/edit dialog: " + ex.getMessage());
            return false;
        }
    }
}
