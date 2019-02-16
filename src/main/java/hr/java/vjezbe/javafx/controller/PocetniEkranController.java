package hr.java.vjezbe.javafx.controller;

import hr.java.vjezbe.javafx.application.Main;
import hr.java.vjezbe.javafx.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PocetniEkranController {

    private Model model;

    private static final Logger LOGGER = LoggerFactory.getLogger(PocetniEkranController.class);
    private Main mainApp;

    public PocetniEkranController() {
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void setModel(Model model) {
        this.model = model;
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
            controller.setModel(model);
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
            controller.setModel(model);
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
            controller.setModel(model);
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
            controller.setModel(model);
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
            LOGGER.error("Error occurred while changing layout: " + ex.getMessage());
        }
    }
}
