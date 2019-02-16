package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.model.Model;
import hr.java.vjezbe.javafx.model.VrstaMjesta;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.util.ZupanijaConverter;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MjestoEditDialogController {

    @FXML
    private TextField nazivField;
    @FXML
    private ComboBox<Zupanija> zupanijaCombobox;
    @FXML
    private ComboBox<VrstaMjesta> vrstaCombobox;

    private Stage dialogStage;
    private Mjesto mjesto;
    private boolean okClicked = false;
    private Model model;

    @FXML
    private void initialize() {
    }

    public void setModel(Model model) {
        this.model = model;
        zupanijaCombobox.setItems(FXCollections.observableArrayList(model.getZupanijaService().findAll()));
        zupanijaCombobox.setConverter(new ZupanijaConverter());
        vrstaCombobox.getItems().setAll(VrstaMjesta.values());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
        nazivField.setText(mjesto.getNaziv());
        zupanijaCombobox.getSelectionModel().select(mjesto.getZupanija());
        vrstaCombobox.getSelectionModel().select(mjesto.getVrstaMjesta());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            mjesto.setNaziv(nazivField.getText());
            mjesto.setZupanija(zupanijaCombobox.getValue());
            mjesto.setVrstaMjesta(vrstaCombobox.getValue());

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        nazivField.getStyleClass().remove("error");

        if (InputValidator.isNullOrEmpty(nazivField.getText())) {
            errorMessage += "Naziv nije ispravano unesen\n";
            nazivField.getStyleClass().add("error");
        }
        if (zupanijaCombobox.getValue() == null) {
            errorMessage += "Županija nije odabrana\n";
        }
        if (vrstaCombobox.getValue() == null) {
            errorMessage += "Vrsta mjesta nije odabrana\n";
        }

        if (errorMessage.isEmpty())
            return true;
        else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Neispravan unos podataka");
            alert.setHeaderText("Ispravite pogrešno ispunjena polja");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}
