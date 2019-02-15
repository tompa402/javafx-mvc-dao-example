package hr.java.vjezbe.javafx.service.niti;

import hr.java.vjezbe.javafx.model.Model;
import hr.java.vjezbe.javafx.service.SenzorService;
import hr.java.vjezbe.javafx.service.implDB.SenzorServiceImpl;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SenzorThreadService implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenzorThreadService.class);
    private final SenzorService service;
    private final Model model;

    public SenzorThreadService(Model model) {
        this.model = model;
        this.service = new SenzorServiceImpl();
    }

    @Override
    public void run() {
        int brojSenzora = service.getNegativeActiveSensors();
        if (model.getToggleValue() && brojSenzora > 0) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Senzor");
                alert.setHeaderText("Senzor notification");
                alert.setContentText("Senzor ispod nule.");
                alert.showAndWait();
            });
            LOGGER.info("Number of active sensors that are also active is: " + brojSenzora);
        } else if (brojSenzora < 0) {
            LOGGER.error("Unable to get data from database.");
        } else {
            LOGGER.info("There isn't any active or negative value sensor.");
        }
    }
}
