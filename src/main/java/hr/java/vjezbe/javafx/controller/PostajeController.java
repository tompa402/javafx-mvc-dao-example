package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.MjernaPostaja;
import hr.java.vjezbe.javafx.model.Model;
import hr.java.vjezbe.javafx.model.RadioSondaznaMjernaPostaja;
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
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;

    private static final Logger LOGGER = LoggerFactory.getLogger(PostajeController.class);
    private ObservableList<MjernaPostaja> obsPostaje;
    private Model model;

    public PostajeController() {
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nazivColumn.setCellValueFactory(cellData -> cellData.getValue().nazivProperty());

        showPostajaDetails(null);

        postajeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPostajaDetails(newValue));

        deleteButton.setDisable(true);
        editButton.setDisable(true);
        newPostajaCombobox.setDisable(true);

        newPostajaCombobox.setItems(FXCollections.observableArrayList(
                Arrays.asList(new MjernaPostaja(), new RadioSondaznaMjernaPostaja())));
        newPostajaCombobox.setConverter(new NovaPostajaConverter());
    }

    public void setModel(Model model) {
        this.model = model;
        obsPostaje = FXCollections.observableArrayList(model.getPostajaService().findAll());
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
        obsPostaje = FXCollections.observableArrayList(model.getPostajaService()
                .findByName(nazivFilterTextField.getText()));
        postajeTable.setItems(obsPostaje);
        postajeTable.setItems(obsPostaje);
    }

    @FXML
    private void handleDeletePostaja() {
        int selectedIndex = postajeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            postajeTable.getItems().remove(selectedIndex);
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
            model.getPostajaService().save(tempPostaja);
            obsPostaje.add(tempPostaja);
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
                showPostajaDetails(selectedPostaja);
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
