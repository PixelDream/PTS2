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
import reconstitution.Etudiant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EtudiantController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Etudiant.getStage().setTitle("Reconstitution > Accueil");
    }

    @FXML
    private void openExercise() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/faireExercice.fxml"));
        Etudiant.getStage().setScene(new Scene(root));
        Etudiant.getStage().setMaximized(true);
        Etudiant.getStage().show();
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
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Etudiant.getStage().getScene().getWindow());
        stage.show();
    }
}
