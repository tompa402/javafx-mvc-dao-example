package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.service.ZupanijaService;
import hr.java.vjezbe.javafx.service.implDB.ZupanijaServiceImpl;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijeController.class);
    private ZupanijaService service;
    private ObservableList<Zupanija> obsZupanije;

    public ZupanijeController() {
        this.service = new ZupanijaServiceImpl();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showZupanijaDetails(null);

        zupanijaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showZupanijaDetails(newValue));

        List<Zupanija> listZupanije = service.getAll();
        if (listZupanije != null) {
            obsZupanije = FXCollections.observableArrayList(listZupanije);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
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
        List<Zupanija> listZupanije = service.findByName(nazivFilterTextField.getText());
        if (listZupanije != null) {
            obsZupanije = FXCollections.observableArrayList(listZupanije);
            zupanijaTable.setItems(obsZupanije);
            zupanijaTable.getSelectionModel().selectFirst();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleDeleteZupanija() {
        Zupanija selectedZupanija = zupanijaTable.getSelectionModel().getSelectedItem();
        if (selectedZupanija != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation delete action");
            alert.setHeaderText("This action will have unwanted consequences");
            alert.setContentText("Child items like Mjesto, MjernaPostaja etc... will also be deleted." +
                    "\nDo you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (service.delete(selectedZupanija)) {
                    zupanijaTable.getItems().remove(selectedZupanija);
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
            if (service.save(tempZupanija) > 0) {
                obsZupanije.add(tempZupanija);
                showZupanijaDetails(tempZupanija);
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
    private void handleEditZupanija() {
        Zupanija selectedZupanija = zupanijaTable.getSelectionModel().getSelectedItem();
        if (selectedZupanija != null) {
            boolean okClicked = showZupanijaEditDialog(selectedZupanija);
            if (okClicked) {
                if (service.update(selectedZupanija)) {
                    showZupanijaDetails(selectedZupanija);
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
            AnchorPane page = loader.load();

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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException ex) {
            LOGGER.error("Error occurred while creating new/edit dialog: " + ex.getMessage());
            return false;
        }
    }
}
