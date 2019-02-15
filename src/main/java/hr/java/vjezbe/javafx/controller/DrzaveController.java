package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Drzava;
import hr.java.vjezbe.javafx.service.DrzavaService;
import hr.java.vjezbe.javafx.service.implDB.DrzavaServiceImpl;
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
import java.util.List;
import java.util.Optional;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(DrzaveController.class);
    private DrzavaService service;
    private ObservableList<Drzava> obsDrzave;

    public DrzaveController() {
        this.service = new DrzavaServiceImpl();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showDrzavaDetails(null);

        drzavaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDrzavaDetails(newValue));

        List<Drzava> listDrzave = service.getAll();
        if (listDrzave != null) {
            obsDrzave = FXCollections.observableArrayList(listDrzave);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
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
        List<Drzava> listDrzava = service.findByName(nazivFilterTextField.getText());
        if (listDrzava != null) {
            obsDrzave = FXCollections.observableArrayList(listDrzava);
            drzavaTable.setItems(obsDrzave);
            drzavaTable.getSelectionModel().selectFirst();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteDrzava() {
        Drzava selectedDrzava = drzavaTable.getSelectionModel().getSelectedItem();
        if (selectedDrzava != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation delete action");
            alert.setHeaderText("This action will have unwanted consequences");
            alert.setContentText("Child items like Županija, Mjesto, etc... will also be deleted." +
                    "\nDo you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (service.delete(selectedDrzava)) {
                    drzavaTable.getItems().remove(selectedDrzava);
                } else {
                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setTitle("Error");
                    alertError.setHeaderText("Program error");
                    alertError.setContentText("If error persist contact your administrator.");
                    alertError.showAndWait();
                }
            }

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
            if (service.save(tempDrzava) > 0) {
                obsDrzave.add(tempDrzava);
                showDrzavaDetails(tempDrzava);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Program error");
                alert.setContentText("If error persist contact your administrator.");
                alert.showAndWait();
            }

        }
    }

    @FXML
    private void handleEditDrzava() {
        Drzava selectedDrzava = drzavaTable.getSelectionModel().getSelectedItem();
        if (selectedDrzava != null) {
            boolean okClicked = showDrzavaEditDialog(selectedDrzava);
            if (okClicked) {
                if (service.update(selectedDrzava)) {
                    showDrzavaDetails(selectedDrzava);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Program error");
                    alert.setContentText("If error persist contact your administrator.");
                    alert.showAndWait();
                }
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
            AnchorPane page = loader.load();

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
