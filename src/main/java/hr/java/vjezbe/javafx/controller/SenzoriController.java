package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.*;
import hr.java.vjezbe.javafx.util.NoviSenzorConverter;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.Arrays;

public class SenzoriController {

    @FXML
    private TableView<Senzor> senzoriTable;
    @FXML
    private TableColumn<Senzor, Integer> idColumn;
    @FXML
    private TableColumn<Senzor, String> vrstaColumn;
    @FXML
    private TableColumn<Senzor, String> postajaColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label vrstaLabel;
    @FXML
    private Label postajaLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label mjernaJedinicaLabel;
    @FXML
    private Label preciznostLabel;
    @FXML
    private Label vrijednostLabel;
    @FXML
    private Label radSenzoraLabel;
    @FXML
    private ComboBox<Senzor> newSenzorCombobox;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;


    private static final Logger LOGGER = LoggerFactory.getLogger(SenzoriController.class);
    private ObservableList<Senzor> obsSenzori;
    private Model model;

    public SenzoriController() {
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        vrstaColumn.setCellValueFactory(cellData -> {
            Senzor s = cellData.getValue();
            if (s instanceof SenzorTemperature) {
                return new SimpleStringProperty("Temperaturni");
            } else if (s instanceof SenzorVlage) {
                return new SimpleStringProperty("Vlažni");
            } else {
                return new SimpleStringProperty("Tlačni");
            }
        });
        postajaColumn.setCellValueFactory(cellData -> cellData.getValue().getPostaja().nazivProperty());

        showSenzorDetails(null);

        senzoriTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSenzorDetails(newValue));

        newSenzorCombobox.setItems(FXCollections.observableArrayList(
                Arrays.asList(new SenzorTemperature(), new SenzorVlage(), new SenzorTlaka())));
        newSenzorCombobox.setConverter(new NoviSenzorConverter());
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        newSenzorCombobox.setDisable(true);
    }

    public void setModel(Model model) {
        this.model = model;
        obsSenzori = FXCollections.observableArrayList(model.getSenzorService().findAll());
        senzoriTable.setItems(obsSenzori);
    }

    private void showSenzorDetails(Senzor senzor) {
        if (senzor != null) {
            idLabel.setText(senzor.getId().toString());
            if (senzor instanceof SenzorTemperature) {
                vrstaLabel.setText("Temperaturni");
                nazivLabel.setText(((SenzorTemperature) senzor).getNaziv());
            } else if (senzor instanceof SenzorVlage) {
                vrstaLabel.setText("Vlažni");
                nazivLabel.setText("<bez imena>");
            } else {
                vrstaLabel.setText("Tlačni");
                nazivLabel.setText("<bez imena>");
            }
            postajaLabel.setText(senzor.getPostaja().getNaziv());
            mjernaJedinicaLabel.setText(senzor.getMjernaJedinica());
            preciznostLabel.setText(senzor.getPreciznost().toString());
            vrijednostLabel.setText(String.valueOf(senzor.getVrijednost()));
            radSenzoraLabel.setText(senzor.getRadSenzora().toString());
        } else {
            idLabel.setText("");
            vrstaLabel.setText("");
            postajaLabel.setText("");
            nazivLabel.setText("");
            mjernaJedinicaLabel.setText("");
            preciznostLabel.setText("");
            vrijednostLabel.setText("");
            radSenzoraLabel.setText("");
        }
    }

    @FXML
    private void handleDeleteSenzor() {
        int selectedIndex = senzoriTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            senzoriTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje senzora");
            alert.setHeaderText("Nije odabran senzor iz tablice.");
            alert.setContentText("Odaberite senzor iz tablice.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewSenzor() {
        Senzor tempSenzor = newSenzorCombobox.getValue();
        boolean okClicked = showSenzorEditDialog(tempSenzor);
        if (okClicked) {
            model.getSenzorService().save(tempSenzor);
            obsSenzori.add(tempSenzor);
        }

        // Update GUI component from non-GUI thread
        Platform.runLater(() -> {
            EventHandler<ActionEvent> newSenzorComboboxEvent = newSenzorCombobox.getOnAction();
            newSenzorCombobox.setOnAction(null);
            newSenzorCombobox.getItems().removeAll();
            newSenzorCombobox.setItems(FXCollections.observableArrayList(
                    Arrays.asList(new SenzorTemperature(), new SenzorVlage(), new SenzorTlaka())));
            newSenzorCombobox.setPromptText("Novo...");
            newSenzorCombobox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Senzor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Novo...");
                    }
                }
            });
            newSenzorCombobox.setOnAction(newSenzorComboboxEvent);
        });
    }

    @FXML
    private void handleEditSenzor() {
        Senzor selectedSenzor = senzoriTable.getSelectionModel().getSelectedItem();
        if (selectedSenzor != null) {
            boolean okClicked = showSenzorEditDialog(selectedSenzor);
            if (okClicked) {
                showSenzorDetails(selectedSenzor);
                // because column Mjesto(naziv) isn't updated automatically
                senzoriTable.getColumns().get(0).setVisible(false);
                senzoriTable.getColumns().get(0).setVisible(true);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uređivanje senzora");
            alert.setHeaderText("Nije odabran senzor iz tablice.");
            alert.setContentText("Odaberite senzor iz tablice.");

            alert.showAndWait();
        }
    }

    private boolean showSenzorEditDialog(Senzor senzor) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/senzorEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit senzor");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the Object into the controller.
            SenzorEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSenzor(senzor);
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
