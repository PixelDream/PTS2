package reconstitution.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import reconstitution.Enseignant;
import reconstitution.model.Entrainement;
import reconstitution.model.Evaluation;
import reconstitution.model.Exercice;
import reconstitution.model.MediaExercice;
import reconstitution.utils.PopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class CreationController implements Initializable {

    private Exercice exercice = null;
    private boolean isEdit = false;

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

        Platform.runLater(() -> {
            if (exercice != null) editerExercice();
        });

        ocultation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (ocultation.getText().length() > 1) {
                String s = ocultation.getText().substring(0, 1);
                ocultation.setText(s);
            }
        });
    }

    private void editerExercice() {
        ocultation.setText(exercice.getOccultation());
        sensiCasse.setSelected(exercice.isCaseSensi());
        consigne.setText(exercice.getConsigne());
        transcription.setText(exercice.getText());
        aide.setText(exercice.getAide());

        mediaController.playWith(exercice.getMedia());

        if (exercice instanceof Entrainement) {
            entrainementMode();
            Entrainement entrainement = (Entrainement) exercice;
            if (entrainement.isMotIncomplet()) {
                incompletMode();
                motIncomplet.setSelected(true);
                nbMotIncomplet.setText(String.valueOf(entrainement.getMinLettre()));
            }

            afficherSolution.setSelected(entrainement.isAfficherSolution());
            afficherRatio.setSelected(entrainement.isAfficherRatio());
        } else if (exercice instanceof Evaluation) {
            evaluationMode();
            Evaluation evaluation = (Evaluation) exercice;

            if (evaluation.isLimite()) {
                limiteTemps.setSelected(true);
                limiteMode();
                nbLimiteTemps.setText(String.valueOf(evaluation.getLimiteTemps()));
            }
        } else {
            new PopUp("Erreur", "Ce fichier n'est pas un exercice, il n'est pas possible de le modifier");
        }
    }

    @FXML
    private void goback() throws IOException {
        PopUp popUp = new PopUp(Alert.AlertType.CONFIRMATION, "Confirmation", "Vous êtes sûr de quitter la création d'exercice ?");
        if (popUp.isAccepted()) Enseignant.changeScene("/enseignantAccueil.fxml");
    }

    @FXML
    private void enregistrer() {
        MediaExercice mediaExercice;

        if (isEdit) {
            try {
                mediaExercice = exercice.getMedia();
            } catch (Exception e) {
                mediaExercice = new MediaExercice(mediaController.getRessource(), mediaController.getImage(), mediaController.isAudio());
            }
        } else {
            mediaExercice = new MediaExercice(mediaController.getRessource(), mediaController.getImage(), mediaController.isAudio());
        }

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
    private void entrainementMode(Event event) {
        event.consume();
        if (!entrainementMode.isSelected()) entrainementMode.setSelected(true);
        entrainementMode();
    }

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
    private void evaluationMode(Event event) {
        event.consume();
        if (!evaluationMode.isSelected()) evaluationMode.setSelected(true);
        evaluationMode();
    }

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

    public void setExercice(Exercice exercice) {
        this.isEdit = true;
        this.exercice = exercice;
    }
}
