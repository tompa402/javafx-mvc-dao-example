package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Model;
import hr.java.vjezbe.javafx.model.dhmz.Grad;
import hr.java.vjezbe.javafx.model.dhmz.Vrijeme;
import hr.java.vjezbe.javafx.service.DHMZService;
import hr.java.vjezbe.javafx.service.niti.SenzorThreadService;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PocetniEkranController {

    @FXML
    private TableView<Grad> gradPostajaTable;
    @FXML
    private TableColumn<Grad, String> imeColumn;
    @FXML
    private TableColumn<Grad, BigDecimal> latColumn;
    @FXML
    private TableColumn<Grad, BigDecimal> lonColumn;
    @FXML
    private Label dateLabel;
    @FXML
    private Label hourLabel;
    @FXML
    private Label tempLabel;
    @FXML
    private Label vlagaLabel;
    @FXML
    private Label tlakLabel;
    @FXML
    private Label tlakTendLabel;
    @FXML
    private Label vjetarSmjerLabel;
    @FXML
    private Label vjetarBrzinaLabel;
    @FXML
    private Label vrijemeLabel;
    @FXML
    private Label vrijemeZnakLabel;
    @FXML
    private TextField nazivFilterTextField;

    private static final Logger LOGGER = LoggerFactory.getLogger(PocetniEkranController.class);
    private Main mainApp;
    private DHMZService service;
    private ObservableList<Grad> obsGradovi;
    private Model model;

    public PocetniEkranController() {
        this.service = new DHMZService();
        this.model = new Model(true);
    }

    @FXML
    private void initialize() {
        imeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaziv()));
        latColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLat()));
        lonColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLon()));

        showPostajaGradDetails(null);

        gradPostajaTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPostajaGradDetails(newValue));

        Vrijeme data = service.extractData();
        if (data != null) {
            obsGradovi = FXCollections.observableArrayList(data.getGrad());
            dateLabel.setText(data.getDatumTermin().getDatum());
            hourLabel.setText(data.getDatumTermin().getTermin().toString() + " h");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program error");
            alert.setContentText("If error persist contact your administrator.");
            alert.showAndWait();
        }
        gradPostajaTable.setItems(obsGradovi);

        ScheduledExecutorService executor =  Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new SenzorThreadService(model), 0, 10, TimeUnit.SECONDS);
    }

    private void showPostajaGradDetails(Grad gradPostaja) {
        if (gradPostaja != null) {
            tempLabel.setText(gradPostaja.getPodatci().getTemp().toString());
            vlagaLabel.setText(gradPostaja.getPodatci().getVlaga());
            tlakLabel.setText(gradPostaja.getPodatci().getTlak());
            tlakTendLabel.setText(gradPostaja.getPodatci().getVlakTend());
            vjetarSmjerLabel.setText(gradPostaja.getPodatci().getVjetarSmjer());
            vjetarBrzinaLabel.setText(gradPostaja.getPodatci().getVjetarBrzina());
            vrijemeLabel.setText(gradPostaja.getPodatci().getVrijeme());
            vrijemeZnakLabel.setText(gradPostaja.getPodatci().getVrijemeZnak());
        } else {
            tempLabel.setText("");
            vlagaLabel.setText("");
            tlakLabel.setText("");
            tlakTendLabel.setText("");
            vjetarSmjerLabel.setText("");
            vjetarBrzinaLabel.setText("");
            vrijemeLabel.setText("");
            vrijemeZnakLabel.setText("");
        }
    }

    @FXML
    private void getGradPostajaByName() {
        if (InputValidator.isNullOrEmpty(nazivFilterTextField.getText())) {

            gradPostajaTable.setItems(obsGradovi);
            showPostajaGradDetails(null);
        } else {
            ObservableList<Grad> obsGradoviFilter = obsGradovi.stream().filter(g ->
                    g.getNaziv().contains(nazivFilterTextField.getText())).
                    collect(Collectors.toCollection(FXCollections::observableArrayList));
            gradPostajaTable.setItems(obsGradoviFilter);
            gradPostajaTable.getSelectionModel().selectFirst();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Shows the Object overview inside the root layout.
     */
    public void showDrzaveLayout() {
        try {
            // Load Object overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Drzave.fxml"));
            AnchorPane drzaveOwerview = (AnchorPane) loader.load();

            // Set Object overview into the center of root layout.
            mainApp.getRootLayout().setCenter(drzaveOwerview);

            // Give the controller access to the main app.
            DrzaveController controller = loader.getController();
            //controller.setMainApp(mainApp);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while changing layout: " + ex.getMessage());
        }
    }

    public void showZupanijeLayout() {
        try {
            // Load Object overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Zupanije.fxml"));
            AnchorPane zupanijeOwerview = (AnchorPane) loader.load();

            // Set Object overview into the center of root layout.
            mainApp.getRootLayout().setCenter(zupanijeOwerview);

            // Give the controller access to the main app.
            ZupanijeController controller = loader.getController();
            //controller.setMainApp(mainApp);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while changing layout: " + ex.getMessage());
        }
    }

    public void showMjestaLayout() {
        try {
            // Load Object overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Mjesta.fxml"));
            AnchorPane mjestaOwerview = (AnchorPane) loader.load();

            // Set Object overview into the center of root layout.
            mainApp.getRootLayout().setCenter(mjestaOwerview);

            // Give the controller access to the main app.
            MjestaController controller = loader.getController();
            //controller.setMainApp(mainApp);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while changing layout: " + ex.getMessage());
        }
    }

    public void showPostajeLayout() {
        try {
            // Load Object overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/Postaje.fxml"));
            AnchorPane postajeOwerview = (AnchorPane) loader.load();

            // Set Object overview into the center of root layout.
            mainApp.getRootLayout().setCenter(postajeOwerview);

            // Give the controller access to the main app.
            PostajeController controller = loader.getController();
            //controller.setMainApp(mainApp);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while changing layout: " + ex.getMessage());
        }
    }

    public void showSenzoriLayout() {
        try {
            // Load Object overview.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Senzori.fxml"));
            //loader.setLocation(Main.class.getResource("/Senzori.fxml"));
            AnchorPane senzoriOwerview = (AnchorPane) loader.load();

            // Set Object overview into the center of root layout.
            mainApp.getRootLayout().setCenter(senzoriOwerview);

            // Give the controller access to the main app.
            SenzoriController controller = loader.getController();
            controller.setModel(model);
        } catch (IOException ex) {
            System.out.println(ex);
            LOGGER.error("Error occurred while changing layout: " + ex.getMessage());
        }
    }
}
