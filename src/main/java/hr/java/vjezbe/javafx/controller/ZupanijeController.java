package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Model;
import hr.java.vjezbe.javafx.model.Zupanija;
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

public class ZupanijeController {

    @FXML
    private TableView<Zupanija> zupanijaTable;
    @FXML
    private TableColumn<Zupanija, Integer> idColumn;
    @FXML
    private TableColumn<Zupanija, String> nazivColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label nazivDrzavaLabel;
    @FXML
    private Label mjestaLabel;
    @FXML
    private TextField nazivFilterTextField;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button newButton;

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijeController.class);
    private ObservableList<Zupanija> obsZupanije;
    private Model model;

    public ZupanijeController() {
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showZupanijaDetails(null);

        zupanijaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showZupanijaDetails(newValue));
        deleteButton.setDisable(true);
        editButton.setDisable(true);
        newButton.setDisable(true);
    }

    public void setModel(Model model) {
        this.model = model;
        obsZupanije = FXCollections.observableArrayList(model.getZupanijaService().findAll());
        zupanijaTable.setItems(obsZupanije);
    }

    private void showZupanijaDetails(Zupanija zupanija) {
        if (zupanija != null) {
            idLabel.setText(zupanija.getId().toString());
            nazivLabel.setText(zupanija.getNaziv());
            nazivDrzavaLabel.setText(zupanija.getDrzava().getNaziv());
            mjestaLabel.setText(String.valueOf(zupanija.getMjesta().size()));
        } else {
            idLabel.setText("");
            nazivLabel.setText("");
            nazivDrzavaLabel.setText("");
            mjestaLabel.setText("");
        }
    }

    public void getZupanijaByName() {
        obsZupanije = FXCollections.observableArrayList(model.getZupanijaService()
                .findByName(nazivFilterTextField.getText()));
        zupanijaTable.setItems(obsZupanije);
    }


    @FXML
    private void handleDeleteZupanija() {
        int selectedIndex = zupanijaTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            zupanijaTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje županije");
            alert.setHeaderText("Nije odabrana županija iz tablice");
            alert.setContentText("Odaberite županiju iz tablice.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewZupanija() {
        Zupanija tempZupanija = new Zupanija();
        boolean okClicked = showZupanijaEditDialog(tempZupanija);
        if (okClicked) {
            model.getZupanijaService().save(tempZupanija);
            obsZupanije.add(tempZupanija);
        }
    }

    @FXML
    private void handleEditZupanija() {
        Zupanija selectedZupanija = zupanijaTable.getSelectionModel().getSelectedItem();
        if (selectedZupanija != null) {
            boolean okClicked = showZupanijaEditDialog(selectedZupanija);
            if (okClicked) {
                showZupanijaDetails(selectedZupanija);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uređivanje županije");
            alert.setHeaderText("Nije odabrana županija iz tablice");
            alert.setContentText("Odaberite županiju iz tablice.");

            alert.showAndWait();
        }
    }

    private boolean showZupanijaEditDialog(Zupanija zupanija) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ZupanijaEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New/Edit županija");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the Object into the controller.
            ZupanijaEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setZupanija(zupanija);
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
