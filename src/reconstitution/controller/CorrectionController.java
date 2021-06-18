package reconstitution.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import reconstitution.Enseignant;
import reconstitution.model.Evaluation;
import reconstitution.model.Exercice;
import reconstitution.utils.PopUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CorrectionController implements Initializable {
    private final ObservableList<Evaluation> evaluationObservableList = FXCollections.observableArrayList();

    @FXML
    private TableView<Evaluation> examTable;

    @FXML
    private TableColumn<Evaluation, String> nomColumn, prenomColumn, ratioColumn, tempsColumn;

    @FXML
    private Text previewTitle, preview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        evaluationObservableList.addAll(Exercice.ImporterMult());

        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtudiant().getNom()));
        prenomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtudiant().getPrenom()));
        ratioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Math.round((double) cellData.getValue().getNbNombreDecouv() / cellData.getValue().getMots().length * 100) + "%"));
        tempsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getFinTemps())));

        examTable.setItems(evaluationObservableList);

        examTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPreview(newValue));
    }

    private void showPreview(Evaluation evaluation) {
        if (!previewTitle.isVisible()) previewTitle.setVisible(true);
        if (!preview.isVisible()) preview.setVisible(true);

        preview.setText(evaluation.getTextOccultJoined());
    }

    @FXML
    private void goback() throws IOException {
        PopUp popUp = new PopUp(Alert.AlertType.CONFIRMATION, "Confirmation", "Vous êtes sûr de quitter la correction ?");
        if (popUp.isAccepted()) Enseignant.changeScene("/enseignantAccueil.fxml");
    }

}
