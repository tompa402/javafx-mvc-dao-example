package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.service.MjestoService;
import hr.java.vjezbe.javafx.service.implDB.MjestoServiceImpl;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MjestaController.class);
    private MjestoService service;
    private ObservableList<Mjesto> obsMjesta;

    public MjestaController() {
        this.service = new MjestoServiceImpl();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showMjestoDetails(null);

        mjestaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMjestoDetails(newValue));

        List<Mjesto> listMjesta = service.getAll();
        if (listMjesta != null) {
            obsMjesta = FXCollections.observableArrayList(listMjesta);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
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
        List<Mjesto> listMjesta = service.findByName(nazivFilterTextField.getText());
        if (listMjesta != null) {
            obsMjesta = FXCollections.observableArrayList(listMjesta);
            mjestaTable.setItems(obsMjesta);
            mjestaTable.getSelectionModel().selectFirst();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteMjesto() {
        Mjesto selectedMjesto = mjestaTable.getSelectionModel().getSelectedItem();
        if (selectedMjesto != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation delete action");
            alert.setHeaderText("This action will have unwanted consequences");
            alert.setContentText("Child items like MjernaPostaja, Senzor etc... will also be deleted." +
                    "\nDo you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (service.delete(selectedMjesto)) {
                    mjestaTable.getItems().remove(selectedMjesto);
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
            if (service.save(tempMjesto) > 0) {
                obsMjesta.addAll(tempMjesto);
                showMjestoDetails(tempMjesto);
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
    private void handleEditMjesto() {
        Mjesto selectedMjesto = mjestaTable.getSelectionModel().getSelectedItem();
        if (selectedMjesto != null) {
            boolean okClicked = showMjestoEditDialog(selectedMjesto);
            if (okClicked) {
                if (service.update(selectedMjesto)) {
                    showMjestoDetails(selectedMjesto);
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
            alert.setTitle("Uređivanje mjesta");
            alert.setHeaderText("Nije odabrano mjesto iz tablice.");
            alert.setContentText("Odaberite mjesto iz tablice.");
            alert.showAndWait();
        }
    }

    private boolean showMjestoEditDialog(Mjesto mjesto) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/MjestoEditDialog.fxml"));
            AnchorPane page = loader.load();

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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException ex) {
            LOGGER.error("Error occurred while creating new/edit dialog: " + ex.getMessage());
            return false;
        }
    }
}
