package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.model.Drzava;
import hr.java.vjezbe.javafx.model.Model;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.util.DrzavaConverter;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ZupanijaEditDialogController {

    @FXML
    private TextField nazivField;
    @FXML
    private ComboBox<Drzava> drzavaCombobox;

    private Stage dialogStage;
    private Zupanija zupanija;
    private boolean okClicked = false;
    private Model model;

    @FXML
    private void initialize() {
    }

    public void setModel(Model model) {
        this.model = model;
        drzavaCombobox.setItems(FXCollections.observableArrayList(model.getDrzavaService().findAll()));
        drzavaCombobox.setConverter(new DrzavaConverter());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
        nazivField.setText(zupanija.getNaziv());
        drzavaCombobox.getSelectionModel().select(zupanija.getDrzava());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            zupanija.setNaziv(nazivField.getText());
            zupanija.setDrzava(drzavaCombobox.getValue());

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
        // TODO: create css border for combobox
        // drzavaCombobox.getStyleClass().remove("error");

        if (InputValidator.isNullOrEmpty(nazivField.getText())) {
            errorMessage += "Naziv nije ispravano unesen\n";
            nazivField.getStyleClass().add("error");
        }
        if (drzavaCombobox.getValue() == null) {
            errorMessage += "Država nije odabrana\n";
            // drzavaCombobox.getStyleClass().add("error");
        }
        if (errorMessage.isEmpty())
            return true;
        else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Neispravan unos");
            alert.setHeaderText("Ispravite pogrešno ispunjena polja");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}
