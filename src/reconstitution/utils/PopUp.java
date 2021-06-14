package reconstitution.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class PopUp extends Alert {

    private final Optional<ButtonType> result;


    public PopUp(AlertType alertType, String title, String headerText, String contentText) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setContentText(contentText);

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.getIcons().addAll(
                new Image(String.valueOf(getClass().getResource("/favicon/64x64.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/32x32.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/16x16.png")))
        );

        this.result = this.showAndWait();
    }

    public PopUp(String title, String headerText) {
        this(AlertType.INFORMATION, title, headerText);
    }

    public PopUp(AlertType alertType, String title, String headerText) {
        this(AlertType.INFORMATION, title, headerText, "");
    }

    public boolean isAccepted() {
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
