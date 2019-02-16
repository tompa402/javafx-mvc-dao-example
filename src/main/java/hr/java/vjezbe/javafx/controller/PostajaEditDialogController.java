package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.model.*;
import hr.java.vjezbe.javafx.util.MjestoConverter;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class PostajaEditDialogController {

    @FXML
    private TextField nazivField;
    @FXML
    private TextField geoXField;
    @FXML
    private TextField geoYField;
    @FXML
    private TextField visinaField;
    @FXML
    private ComboBox<Mjesto> mjestoCombobox;
    @FXML
    private Label visinaLabel;

    private Stage dialogStage;
    private MjernaPostaja postaja;
    private boolean okClicked = false;
    private Model model;

    @FXML
    private void initialize() {

    }

    public void setModel(Model model) {
        this.model = model;
        mjestoCombobox.setItems(FXCollections.observableArrayList(model.getMjestoService().findAll()));
        mjestoCombobox.setConverter(new MjestoConverter());

        if (postaja instanceof RadioSondaznaMjernaPostaja) {
            visinaField.setVisible(true);
            visinaLabel.setVisible(true);
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPostaja(MjernaPostaja postaja) {
        this.postaja = postaja;
        nazivField.setText(postaja.getNaziv());
        mjestoCombobox.getSelectionModel().select(postaja.getMjesto());
        if (postaja.getId() != 0) {
            geoXField.setText(postaja.getGeografskaTocka().getGeoX().toString());
            geoYField.setText(postaja.getGeografskaTocka().getGeoY().toString());

            if (postaja instanceof RadioSondaznaMjernaPostaja) {
                visinaField.setText(String.valueOf(((RadioSondaznaMjernaPostaja) postaja).dohvatiVisinuPostaje()));
            }
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            postaja.setNaziv(nazivField.getText());
            postaja.setMjesto(mjestoCombobox.getValue());
            postaja.setGeografskaTocka(new GeografskaTocka(
                    new BigDecimal(geoXField.getText()),
                    new BigDecimal(geoYField.getText())));
            if (postaja instanceof RadioSondaznaMjernaPostaja) {
                ((RadioSondaznaMjernaPostaja) postaja).podesiVisinuPostaje(Integer.parseInt(visinaField.getText()));
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
        geoXField.getStyleClass().remove("error");
        geoYField.getStyleClass().remove("error");
        visinaField.getStyleClass().remove("error");


        if (InputValidator.isNullOrEmpty(nazivField.getText())) {
            errorMessage += "Naziv nije ispravano unesen\n";
            nazivField.getStyleClass().add("error");
        }
        if (mjestoCombobox.getValue() == null) {
            errorMessage += "Mjesto nije odabrano\n";
        }
        if (!InputValidator.isBigDecimal(geoXField.getText())) {
            errorMessage += "Zempljopisna dužina nije u ispravnom formatu\n";
            geoXField.getStyleClass().add("error");
        }
        if (!InputValidator.isBigDecimal(geoYField.getText())) {
            errorMessage += "Zempljopisna širina nije u ispravnom formatu\n";
            geoYField.getStyleClass().add("error");
        }
        if (postaja instanceof RadioSondaznaMjernaPostaja) {
            if (!InputValidator.isInteger(visinaField.getText())) {
                errorMessage += "Visina postaje nije u ispravnom formatu\n";
                visinaField.getStyleClass().add("error");
            }
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
