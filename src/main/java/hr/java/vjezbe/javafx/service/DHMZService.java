package hr.java.vjezbe.javafx.service;

import hr.java.vjezbe.javafx.model.dhmz.Vrijeme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.net.URL;

public class DHMZService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DHMZService.class);
    private static final String DHMZ_PATH = "http://vrijeme.hr/hrvatska1_n.xml";

    public DHMZService() {
    }

    public Vrijeme extractData() {
        Vrijeme logElement = null;
        LOGGER.info("Getting data from DHMZ");
        try {
            JAXBContext jc = JAXBContext.newInstance(Vrijeme.class);

            Unmarshaller um = jc.createUnmarshaller();
            um.setEventHandler(
                    event -> {
                        throw new RuntimeException(event.getMessage(),
                                event.getLinkedException());
                    });
            logElement = (Vrijeme) um.unmarshal(new URL(DHMZ_PATH));
        } catch (Exception ex) {
            LOGGER.info("Failed to get data from DHMZ. \nError: " + ex.getMessage());
        }
        return logElement;
    }
}
