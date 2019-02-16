package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.model.*;
import hr.java.vjezbe.javafx.util.PostajaConverter;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class SenzorEditDialogController {

    @FXML
    private TextField nazivField;
    @FXML
    private TextField vrijednostField;
    @FXML
    private ComboBox<RadSenzora> radSenzoraComboBox;
    @FXML
    private ComboBox<MjernaPostaja> postajaComboBox;
    @FXML
    private Label nazivLabel;

    private Stage dialogStage;
    private Senzor senzor;
    private boolean okClicked = false;
    private Model model;

    @FXML
    private void initialize() {

    }

    public void setModel(Model model) {
        this.model = model;
        radSenzoraComboBox.setItems(FXCollections.observableArrayList(RadSenzora.values()));
        postajaComboBox.setItems(FXCollections.observableArrayList(model.getPostajaService().findAll()));
        postajaComboBox.setConverter(new PostajaConverter());

        if (senzor instanceof SenzorTemperature) {
            nazivField.setVisible(true);
            nazivLabel.setVisible(true);
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSenzor(Senzor senzor) {
        this.senzor = senzor;

        radSenzoraComboBox.getSelectionModel().select(senzor.getRadSenzora());
        postajaComboBox.getSelectionModel().select(senzor.getPostaja());
        if (senzor.getId() != 0) {
            vrijednostField.setText(senzor.getVrijednost().toString());
            if (senzor instanceof SenzorTemperature) {
                nazivField.setText(((SenzorTemperature) senzor).getNaziv());
            }
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            senzor.setVrijednost(new BigDecimal(vrijednostField.getText()));
            senzor.setRadSenzora(radSenzoraComboBox.getValue());
            senzor.setPostaja(postajaComboBox.getValue());
            if (senzor instanceof SenzorTemperature) {
                ((SenzorTemperature) senzor).setNaziv(nazivField.getText());
            }

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
        vrijednostField.getStyleClass().remove("error");

        if (senzor instanceof SenzorTemperature) {
            if (InputValidator.isNullOrEmpty(nazivField.getText())) {
                errorMessage += "Naziv nije ispravano unesen\n";
                nazivField.getStyleClass().add("error");
            }
        }

        if (!InputValidator.isBigDecimal(vrijednostField.getText())) {
            errorMessage += "Vrijednost nije u ispravnom formatu\n";
            vrijednostField.getStyleClass().add("error");
        }
        if (radSenzoraComboBox.getValue() == null) {
            errorMessage += "Rad senzora nije odabran\n";
        }
        if (postajaComboBox.getValue() == null) {
            errorMessage += "Postaja nije odabrana\n";
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
