package reconstitution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Enseignant extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/enseignantAccueil.fxml"));
        primaryStage.getIcons().addAll(
                new Image(String.valueOf(getClass().getResource("/favicon/64x64.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/32x32.png"))),
                new Image(String.valueOf(getClass().getResource("/favicon/16x16.png")))
        );
        primaryStage.setScene(new Scene(root, 1344, 756));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


}
