package hr.java.vjezbe.javafx.service.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionHandler {

    public static void errorMessage(Exception ex) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("If error persist, please contact your administrator");
        alert.setContentText(ex.getLocalizedMessage());

        StringWriter sw = new StringWriter();
        //remove this comments if you want to show user the stack trace..
        //PrintWriter pw = new PrintWriter(sw);
        //ex.printStackTrace(pw);
        sw.write(ex.getLocalizedMessage() + "\nError occurred at " + LocalDateTime.now().format(formatter));
        String exceptionText = sw.toString();

        //Label label = new Label("The exception stacktrace was:");
        Label label = new Label("The exception details was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
