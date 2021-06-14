package reconstitution.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import reconstitution.Enseignant;
import reconstitution.model.Entrainement;
import reconstitution.model.Evaluation;
import reconstitution.model.MediaExercice;
import reconstitution.utils.PopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class CreationController implements Initializable {

    @FXML
    private MediaController mediaController;

    @FXML
    private RadioButton entrainementMode, evaluationMode;

    @FXML
    private Label nbLettreLabel, tempsLabel;

    @FXML
    private Text count;

    @FXML
    private TextArea consigne, aide, transcription;

    @FXML
    private TextField ocultation, nbMotIncomplet, nbLimiteTemps;

    @FXML
    private CheckBox sensiCasse, motIncomplet, afficherRatio, afficherSolution, limiteTemps;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Enseignant.getStage().setTitle("Reconstitution > Creation exerice");
    }

    @FXML
    private void goback() throws IOException {
        PopUp popUp = new PopUp(Alert.AlertType.CONFIRMATION, "Confirmation", "Vous êtes sûr de quitter la création d'exercice ?");

        if (popUp.isAccepted()) {
            Parent root = FXMLLoader.load(getClass().getResource("/enseignantAccueil.fxml"));
            Enseignant.getStage().setScene(new Scene(root));
            Enseignant.getStage().setMaximized(true);
            Enseignant.getStage().show();
        }
    }

    @FXML
    private void enregistrer() {
        MediaExercice mediaExercice = new MediaExercice(mediaController.getRessource(), mediaController.getImage(), mediaController.isAudio());

        if (entrainementMode.isSelected()) {
            Entrainement entrainement = new Entrainement();
            entrainement.setAfficherRatio(afficherRatio.isSelected());
            entrainement.setAfficherSolution(afficherSolution.isSelected());
            entrainement.setConsigne(consigne.getText());
            entrainement.setCaseSensi(sensiCasse.isSelected());
            entrainement.setOccultation(ocultation.getText());
            entrainement.setMedia(mediaExercice);
            entrainement.setTexte(transcription.getText());
            entrainement.setAide(aide.getText());

            if (motIncomplet.isSelected()) {
                entrainement.setMotIncomplet(true);
                entrainement.setMinLettre(Integer.parseInt(nbMotIncomplet.getText()));
            }

            if (entrainement.Exporter()) {
                new PopUp(Alert.AlertType.INFORMATION, "Enregistrement", "L'entraînement est maintenant enregistré !");
            }
        } else {
            Evaluation evaluation = new Evaluation();
            evaluation.setConsigne(consigne.getText());
            evaluation.setCaseSensi(sensiCasse.isSelected());
            evaluation.setOccultation(ocultation.getText());
            evaluation.setTexte(transcription.getText());
            evaluation.setAide(aide.getText());
            evaluation.setMedia(mediaExercice);

            if (limiteTemps.isSelected()) {
                evaluation.setLimite(true);
                evaluation.setLimiteTemps(Integer.parseInt(nbLimiteTemps.getText()));
            }

            if (evaluation.Exporter()) {
                new PopUp(Alert.AlertType.INFORMATION, "Enregistrement", "L'évaluation est maintenant enregistrée !");
            }

        }

    }

    @FXML
    private void countText() {
        String text = transcription.getText().replaceAll("[^A-Za-z0-9]", " ").replaceAll("  +", " ").trim();

        if (text.length() > 0) {
            StringTokenizer tokens = new StringTokenizer(text);
            int tailleMots = tokens.countTokens();
            count.setText(tailleMots == 1 ? tailleMots + " mot" : tailleMots + " mots");
            count.setVisible(true);
        } else {
            count.setVisible(false);
        }
    }

    @FXML
    private void entrainementMode() {
        evaluationMode.setSelected(false);
        motIncomplet.setDisable(false);
        afficherRatio.setDisable(false);
        afficherSolution.setDisable(false);
        limiteTemps.setDisable(true);
        nbLimiteTemps.setDisable(true);
        tempsLabel.setDisable(true);
        if (motIncomplet.isSelected()) {
            nbMotIncomplet.setDisable(false);
            nbLettreLabel.setDisable(false);
        }
    }

    @FXML
    private void evaluationMode() {
        entrainementMode.setSelected(false);
        motIncomplet.setDisable(true);
        nbMotIncomplet.setDisable(true);
        nbLettreLabel.setDisable(true);
        afficherRatio.setDisable(true);
        afficherSolution.setDisable(true);
        limiteTemps.setDisable(false);
        if (limiteTemps.isSelected()) {
            nbLimiteTemps.setDisable(false);
            tempsLabel.setDisable(false);
        }
    }

    @FXML
    private void limiteMode() {
        boolean toggle = !limiteTemps.isSelected();
        nbLimiteTemps.setDisable(toggle);
        tempsLabel.setDisable(toggle);
    }

    @FXML
    private void incompletMode() {
        boolean toggle = !motIncomplet.isSelected();
        nbMotIncomplet.setDisable(toggle);
        nbLettreLabel.setDisable(toggle);
    }
}
