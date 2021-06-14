package reconstitution.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import reconstitution.Enseignant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EnseignantController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Enseignant.getStage().setTitle("Reconstitution > Accueil");
    }

    @FXML
    private void newExercise() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/creationExercice.fxml"));
        Enseignant.getStage().setScene(new Scene(root));
        Enseignant.getStage().setAlwaysOnTop(true);
        Enseignant.getStage().setMaximized(true);
        Enseignant.getStage().show();
    }

    @FXML
    private void showAbout() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/aPropos.fxml"));

        stage.getIcons().addAll(
                new Image(String.valueOf(getClass().getResource("/favicon/64x64.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/32x32.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/16x16.png")))
        );

        stage.setScene(new Scene(root));
        stage.setTitle("A propos");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Enseignant.getStage().getScene().getWindow());
        stage.show();
    }

    @FXML
    private void correctExercise() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/correctionExercice.fxml"));
        Enseignant.getStage().setScene(new Scene(root));
        Enseignant.getStage().setAlwaysOnTop(true);
        Enseignant.getStage().setMaximized(true);
        Enseignant.getStage().show();
    }
}
