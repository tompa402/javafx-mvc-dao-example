package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.MjernaPostaja;
import hr.java.vjezbe.javafx.model.RadioSondaznaMjernaPostaja;
import hr.java.vjezbe.javafx.service.MjernaPostajaService;
import hr.java.vjezbe.javafx.service.implDB.MjernaPostajaServiceImpl;
import hr.java.vjezbe.javafx.util.NovaPostajaConverter;
import javafx.application.Platform;
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
import java.util.List;
import java.util.Optional;

public class PostajeController {

    @FXML
    private TableView<MjernaPostaja> postajeTable;
    @FXML
    private TableColumn<MjernaPostaja, Integer> idColumn;
    @FXML
    private TableColumn<MjernaPostaja, String> nazivColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label nazivLabel;
    @FXML
    private Label zemljopisnaDuzinaLabel;
    @FXML
    private Label zemljopisnaSirinaLabel;
    @FXML
    private Label isRadioSondaznaLabel;
    @FXML
    private Label brojSenzoraLabel;
    @FXML
    private TextField nazivFilterTextField;
    @FXML
    private ComboBox<MjernaPostaja> newPostajaCombobox;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostajeController.class);
    private MjernaPostajaService service;
    private ObservableList<MjernaPostaja> obsPostaje;

    public PostajeController() {
        this.service = new MjernaPostajaServiceImpl();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showPostajaDetails(null);

        postajeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPostajaDetails(newValue));

        newPostajaCombobox.setItems(FXCollections.observableArrayList(
                Arrays.asList(new MjernaPostaja(), new RadioSondaznaMjernaPostaja())));
        newPostajaCombobox.setConverter(new NovaPostajaConverter());

        List<MjernaPostaja> listPostaja = service.getAll();
        if (listPostaja != null) {
            obsPostaje = FXCollections.observableArrayList(listPostaja);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
        postajeTable.setItems(obsPostaje);
    }

    private void showPostajaDetails(MjernaPostaja postaja) {
        if (postaja != null) {
            idLabel.setText(postaja.getId().toString());
            nazivLabel.setText(postaja.getNaziv());
            zemljopisnaDuzinaLabel.setText(postaja.getGeografskaTocka().getGeoX().toString());
            zemljopisnaSirinaLabel.setText(postaja.getGeografskaTocka().getGeoY().toString());
            isRadioSondaznaLabel.setText(postaja instanceof RadioSondaznaMjernaPostaja ? "DA" : "NE");
            brojSenzoraLabel.setText(String.valueOf(postaja.getSenzori().size()));
        } else {
            idLabel.setText("");
            nazivLabel.setText("");
            zemljopisnaDuzinaLabel.setText("");
            zemljopisnaSirinaLabel.setText("");
            isRadioSondaznaLabel.setText("");
            brojSenzoraLabel.setText("");
        }
    }

    public void getPostajaByName() {
        List<MjernaPostaja> listPostaje = service.findByName(nazivFilterTextField.getText());
        if (listPostaje != null) {
            obsPostaje = FXCollections.observableArrayList(listPostaje);
            postajeTable.setItems(obsPostaje);
            postajeTable.getSelectionModel().selectFirst();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeletePostaja() {
        MjernaPostaja selectedPostaja = postajeTable.getSelectionModel().getSelectedItem();
        if (selectedPostaja != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation delete action");
            alert.setHeaderText("This action will have unwanted consequences");
            alert.setContentText("Child items like Senzors etc... will also be deleted." +
                    "\nDo you want to proceed?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (service.delete(selectedPostaja)) {
                    postajeTable.getItems().remove(selectedPostaja);
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
            alert.setTitle("Brisanje postaje");
            alert.setHeaderText("Nije odabrana postaja iz tablice.");
            alert.setContentText("Odaberite postaju iz tablice.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewPostaja() {
        MjernaPostaja tempPostaja = newPostajaCombobox.getValue();
        boolean okClicked = showPostajaEditDialog(tempPostaja);
        if (okClicked) {
            if (service.save(tempPostaja) > 0) {
                obsPostaje.addAll(tempPostaja);
                showPostajaDetails(tempPostaja);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Program error");
                alert.setContentText("If error persist contact your administrator.");
                alert.showAndWait();
            }
        }

        // Update GUI component from non-GUI thread
        Platform.runLater(() -> {
            EventHandler<ActionEvent> newPostajaComboboxEvent = newPostajaCombobox.getOnAction();
            newPostajaCombobox.setOnAction(null);
            newPostajaCombobox.getItems().removeAll();
            newPostajaCombobox.setItems(FXCollections.observableArrayList(
                    Arrays.asList(new MjernaPostaja(), new RadioSondaznaMjernaPostaja())));
            newPostajaCombobox.setPromptText("Novo...");
            newPostajaCombobox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(MjernaPostaja item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Novo...");
                    }
                }
            });
            newPostajaCombobox.setOnAction(newPostajaComboboxEvent);
        });
    }

    @FXML
    private void handleEditPostaja() {
        MjernaPostaja selectedPostaja = postajeTable.getSelectionModel().getSelectedItem();
        if (selectedPostaja != null) {
            boolean okClicked = showPostajaEditDialog(selectedPostaja);
            if (okClicked) {
                if (service.update(selectedPostaja)) {
                    showPostajaDetails(selectedPostaja);
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
            alert.setTitle("Uređivanje mjerne postaje");
            alert.setHeaderText("Nije odabrana mjerna postaja iz tablice.");
            alert.setContentText("Odaberite mjernu postaju iz tablice.");
            alert.showAndWait();
        }
    }

    private boolean showPostajaEditDialog(MjernaPostaja postaja) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/postajaEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit postaje");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            dialogStage.setScene(scene);

            // Set the Object into the controller.
            PostajaEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPostaja(postaja);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException ex) {
            LOGGER.error("Error occurred while creating new/edit dialog: " + ex.getMessage());
            return false;
        }
    }
}
