package reconstitution.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import reconstitution.Etudiant;
import reconstitution.model.Entrainement;
import reconstitution.model.EtudiantData;
import reconstitution.model.Evaluation;
import reconstitution.model.Exercice;
import reconstitution.utils.PopUp;
import reconstitution.utils.PopUpName;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciceController implements Initializable {

    private Exercice exercice;
    private TimerTask timerTask;

    @FXML
    private Text consigne, aide, timer, count, titreMode;

    @FXML
    private TextFlow transcription;

    @FXML
    private Menu solution, enregistrer;

    @FXML
    private TextField proposition;

    @FXML
    private Button btnProposition;

    @FXML
    private MediaController mediaController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exercice = Exercice.Importer();

        consigne.setText(exercice.getConsigne());
        aide.setText(exercice.getAide());

        transcription.getChildren().addAll(exercice.getTextOccultAsList());

        if (exercice instanceof Evaluation) {
            Evaluation evaluation = (Evaluation) exercice;

            enregistrer.setVisible(true);

            Etudiant.getStage().setTitle("Reconstitution > Évaluation");
            titreMode.setText("Mode Évaluation");

            runTimer(evaluation.isLimite() ? evaluation.getLimiteTemps() : 0);
        } else if (exercice instanceof Entrainement) {
            Entrainement entrainement = (Entrainement) exercice;

            if (entrainement.isAfficherSolution()) solution.setVisible(true);

            Etudiant.getStage().setTitle("Reconstitution > Entraînement");
            titreMode.setText("Mode Entraînement");

            runTimer(0);
        } else {
            System.out.println("Erreur");
        }

        mediaController.playWith(exercice.getMedia());
    }

    private void runTimer(int minutes) {
        timerTask = new TimerTask() {
            final int unQuart = minutes / 4;
            int i = minutes * 60;

            public void run() {
                if (minutes != 0) {
                    if ((i / 60 - 1) <= unQuart) timer.setStyle("-fx-fill: red;");
                    timer.setText(String.format("Il reste: %02d:%02d", i / 60, i % 60));
                    if (i <= 0) fin();
                    i--;
                } else {
                    timer.setText(String.format("Temps passé : %02d:%02d", i / 60, i % 60));
                    i++;
                }
            }
        };


        Timer timerExo = new Timer("TimerCount");
        timerExo.schedule(timerTask, 0, 1000L);
    }

    @FXML
    private void proposition() {
        if (proposition.getText().equalsIgnoreCase("")) return;

        if (exercice.proposer(proposition.getText())) {
            transcription.getChildren().clear();
            transcription.getChildren().addAll(exercice.getTextOccultAsList());

            if (exercice instanceof Entrainement) {
                Entrainement entrainement = (Entrainement) exercice;
                int nb = entrainement.getNbNombreDecouv();
                if (entrainement.isAfficherRatio() && nb > 0) {
                    count.setVisible(true);
                    count.setText(nb + (nb == 1 ? " mot découvert" : " mots découverts"));
                }
            }

            proposition.setStyle("-fx-border-color: green; -fx-border-style: solid; -fx-border-width: 3px;");
        } else {
            proposition.setStyle("-fx-border-color: red; -fx-border-style: solid; -fx-border-width: 3px;");
        }

        proposition.setText("");
    }

    @FXML
    private void goback() throws IOException {
        PopUp popUp = new PopUp(Alert.AlertType.CONFIRMATION, "Confirmation", "Vous êtes sûr de quitter l'exercice ?");

        if (popUp.isAccepted()) {
            Parent root = FXMLLoader.load(getClass().getResource("/etudiantAccueil.fxml"));
            Etudiant.getStage().setScene(new Scene(root));
            Etudiant.getStage().setMaximized(true);
            Etudiant.getStage().show();
        }
    }

    @FXML
    private void clavierAction(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) proposition();
    }

    @FXML
    private void solution() {
        PopUp popUp = new PopUp(Alert.AlertType.CONFIRMATION, "Confirmation", "L'exercice prendra fin un fois la solution affichée");

        if (popUp.isAccepted()) {
            solution.hide();
            AtomicInteger acc = new AtomicInteger();
            String[] motsOccult = exercice.getMotsOccult();

            transcription.getChildren().forEach(txt -> {
                Text text = (Text) txt;

                if (acc.get() == 0 || acc.get() == exercice.getTextOccult().length - 1 || motsOccult[exercice.getTextOccult().length < (acc.get() + 1) ? acc.get() + 1 : acc.get()].matches("[.?,.$£€!]")) {

                    if (text.getText().trim().matches("[" + exercice.getOccultation() + "]+")) {
                        text.setText(exercice.getMots()[acc.get()] + " ");
                        text.setStyle("-fx-fill: orange;");
                    }
                } else if (motsOccult[motsOccult.length < (acc.get() + 1) ? acc.get() + 1 : acc.get()].matches("['-]")) {
                    if (text.getText().trim().matches("[" + exercice.getOccultation() + "]+")) {
                        text.setText(exercice.getMots()[acc.get()]);
                        text.setStyle("-fx-fill: orange;");
                    }
                } else if (motsOccult[motsOccult.length < (acc.get() + 1) ? acc.get() + 1 : acc.get()].matches("[()\\[]\\/]")) {
                    if (text.getText().trim().matches("[" + exercice.getOccultation() + "]+")) {
                        text.setText(" " + exercice.getMots()[acc.get()]);
                        text.setStyle("-fx-fill: orange;");
                    }
                } else {
                    if (text.getText().trim().matches("[" + exercice.getOccultation() + "]+")) {
                        text.setText(" " + exercice.getMots()[acc.get()] + " ");
                        text.setStyle("-fx-fill: orange;");
                    }
                }
                acc.incrementAndGet();
            });
            fin();
        }
    }

    private void fin() {
        timerTask.cancel();
        timer.setText(timer.getText() + " (TERMINÉ)");
        proposition.setDisable(true);
        btnProposition.setDisable(true);
    }

    @FXML
    private void enregistrer() {
        enregistrer.hide();

        PopUpName popUpName = new PopUpName();
        Optional<Pair<String, String>> result = popUpName.getDataResult();

        if (result.isPresent()) {
            Evaluation evaluation = (Evaluation) exercice;

            final Pattern p = Pattern.compile("[0-9]+:[0-9]+");
            Matcher m = p.matcher(timer.getText());
            if (m.find()) evaluation.setFinTemps(m.group(0));

            evaluation.setEtudiant(new EtudiantData(result.get().getKey(), result.get().getValue()));

            if (evaluation.Exporter()) {
                new PopUp(Alert.AlertType.INFORMATION, "Enregistrement", "L'exercice est maintenant enregistré");
            }
        }
    }
}
