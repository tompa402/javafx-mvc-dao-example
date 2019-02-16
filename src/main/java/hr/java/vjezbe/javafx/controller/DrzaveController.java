package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Drzava;
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

public class DrzaveController {

    @FXML
    private TableView<Drzava> drzavaTable;
    @FXML
    private TableColumn<Drzava, Integer> idColumn;
    @FXML
    private TableColumn<Drzava, String> nazivColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label povrsinaLabel;
    @FXML
    private Label zupanijaLabel;
    @FXML
    private TextField nazivFilterTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button newButton;

    private static final Logger LOGGER = LoggerFactory.getLogger(DrzaveController.class);
    private ObservableList<Drzava> obsDrzave;
    private Model model;

    public DrzaveController() {
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showDrzavaDetails(null);

        drzavaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDrzavaDetails(newValue));
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        newButton.setDisable(true);
    }

    public void setModel(Model model) {
        this.model = model;
        obsDrzave = FXCollections.observableArrayList(model.getDrzavaService().findAll());
        drzavaTable.setItems(obsDrzave);
    }

    private void showDrzavaDetails(Drzava drzava) {
        if (drzava != null) {
            idLabel.setText(drzava.getId().toString());
            nazivLabel.setText(drzava.getNaziv());
            povrsinaLabel.setText(drzava.getPovrsina().toString());
            zupanijaLabel.setText(String.valueOf(drzava.getZupanije().size()));
        } else {
            idLabel.setText("");
            nazivLabel.setText("");
            povrsinaLabel.setText("");
            zupanijaLabel.setText("");
        }
    }

    @FXML
    private void getDrzavaByName() {
        obsDrzave = FXCollections.observableArrayList(model.getDrzavaService()
                .findByName(nazivFilterTextField.getText()));
        drzavaTable.setItems(obsDrzave);
    }

    @FXML
    private void handleDeleteDrzava() {
        int selectedIndex = drzavaTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            drzavaTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje države");
            alert.setHeaderText("Nije odabrana država iz tablice");
            alert.setContentText("Odaberite dražavu iz tablice.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewDrzava() {
        Drzava tempDrzava = new Drzava();
        boolean okClicked = showDrzavaEditDialog(tempDrzava);
        if (okClicked) {
            model.getDrzavaService().save(tempDrzava);
            obsDrzave.add(tempDrzava);
        }
    }

    @FXML
    private void handleEditDrzava() {
        Drzava selectedDrzava = drzavaTable.getSelectionModel().getSelectedItem();
        if (selectedDrzava != null) {
            boolean okClicked = showDrzavaEditDialog(selectedDrzava);
            if (okClicked) {
                showDrzavaDetails(selectedDrzava);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uređivanje države");
            alert.setHeaderText("Nije odabrana država iz tablice");
            alert.setContentText("Odaberite dražavu iz tablice.");

            alert.showAndWait();
        }
    }

    private boolean showDrzavaEditDialog(Drzava drzava) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/DrzavaEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New/Edit Država");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the Object into the controller.
            DrzavaEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDrzava(drzava);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException ex) {
            LOGGER.error("Error occurred while creating new/edit dialog: " + ex.getMessage());
            return false;
        }
    }
}
