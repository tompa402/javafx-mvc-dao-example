package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.model.Drzava;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class DrzavaEditDialogController {
    @FXML
    private TextField nazivField;
    @FXML
    private TextField povrsinaField;

    private Stage dialogStage;
    private Drzava drzava;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
        nazivField.setText(drzava.getNaziv());
        povrsinaField.setText(drzava.getPovrsina().equals(BigDecimal.ZERO) ? "" : drzava.getPovrsina().toString());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            drzava.setNaziv(nazivField.getText());
            drzava.setPovrsina(new BigDecimal(povrsinaField.getText()));
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
        povrsinaField.getStyleClass().remove("error");

        if (InputValidator.isNullOrEmpty(nazivField.getText())) {
            errorMessage += "Naziv nije ispravano unesen\n";
            nazivField.getStyleClass().add("error");
        }
        if (!InputValidator.isBigDecimal(povrsinaField.getText())) {
            errorMessage += "Površina nije ispravno unesena\n";
            povrsinaField.getStyleClass().add("error");
        }
        if (errorMessage.isEmpty())
            return true;
        else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}
