package hr.java.vjezbe.javafx.application;

import hr.java.vjezbe.javafx.controller.PocetniEkranController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private BorderPane rootLayout;
    private Stage primaryStage;

    public Main() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Meteorološka postaja");
        initPocetniEkran();

        // close custom running threads on javafx app exit
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void initPocetniEkran() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/PocetniEkran.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

            PocetniEkranController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException ex) {
            LOGGER.error("Error occurred while creating initial window: " + ex.getMessage());
        }
    }

    /**
     * Returns the main stage.
     *
     * @return primaryStage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
