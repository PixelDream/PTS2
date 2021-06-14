package reconstitution.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import reconstitution.Enseignant;
import reconstitution.model.MediaExercice;
import reconstitution.utils.PopUp;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MediaController {

    private File ressource = null, image = null;
    private boolean audio = false;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button openVideo, openImage;

    @FXML
    private ImageView playBtn, pauseBtn, stopBtn, imageVideo;

    @FXML
    private AnchorPane control_action, control_sound, control_progress;

    @FXML
    private Slider volumeBtn;

    @FXML
    private ProgressBar progressVideo;

    @FXML
    private Text timerVideo;

    @FXML
    private void openImage() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image", "*.png", "*.jpg", "*.jpeg"));
            fileChooser.setTitle("Ouvrir une image");
            image = fileChooser.showOpenDialog(Enseignant.getStage());
            Image img = new Image(image.toURI().toString());

            if (image != null) {
                imageVideo.setImage(img);
                imageVideo.setVisible(true);
                imageVideo.setOnMouseClicked(this::mediaView);
                openImage.setVisible(false);
            }
        } catch (IllegalArgumentException iae) {
            new PopUp(Alert.AlertType.ERROR, "Erreur Fichier", "Le fichier n'a pas été trouvé.", image.getAbsolutePath());
        }
    }

    @FXML
    private void openVideo() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video/Audio", "*.mp4", "*.mp3"));
            fileChooser.setTitle("Ouvrir une ressource");
            ressource = fileChooser.showOpenDialog(Enseignant.getStage());

            if (ressource != null) {
                Media media = new Media(ressource.toURI().toString());

                MediaPlayer mediaPlayer = new MediaPlayer(media);

                List<String> fileNameExt = Arrays.asList(ressource.getName().split("\\."));

                if (fileNameExt.get(fileNameExt.size() - 1).matches("mp3")) {
                    audio = true;
                    mediaPlayer.setOnReady(() -> {
                        Image image = (Image) mediaPlayer.getMedia().getMetadata().get("image");
                        if (image != null) {
                            imageVideo.setImage(image);
                            imageVideo.setVisible(true);
                            imageVideo.setOnMouseClicked(this::mediaView);
                            imageVideo.setFitHeight(image.getHeight());
                            imageVideo.setPreserveRatio(true);
                        } else {
                            openImage.setVisible(true);
                        }
                    });
                }

                openVideo.setVisible(false);
                mediaView.setMediaPlayer(mediaPlayer);
                control_sound.setVisible(true);
                control_action.setVisible(true);
                control_progress.setVisible(true);
                volumeBtn.setValue(50);
                mediaPlayer.setVolume(0.5);

                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    Date date = new Date((long) newValue.toMillis());
                    progressVideo.setProgress(newValue.toMillis() / media.getDuration().toMillis());
                    timerVideo.setText(date.getMinutes() + ":" + date.getSeconds());
                });
            }
        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        }

    }

    @FXML
    private void playBtn() {
        mediaView.getMediaPlayer().play();
        playBtn.setVisible(false);
        pauseBtn.setVisible(true);
        stopBtn.setVisible(true);
        timerVideo.setVisible(true);
    }

    @FXML
    private void pauseBtn() {
        mediaView.getMediaPlayer().pause();
        pauseBtn.setVisible(false);
        playBtn.setVisible(true);
    }

    @FXML
    private void stopBtn() {
        mediaView.getMediaPlayer().stop();
        stopBtn.setVisible(false);
        pauseBtn.setVisible(false);
        playBtn.setVisible(true);
        timerVideo.setVisible(false);
    }

    @FXML
    private void volumeBtn() {
        mediaView.getMediaPlayer().setVolume(volumeBtn.getValue() / 100);
    }

    @FXML
    private void progressVideo(MouseEvent event) {
        double x = event.getX();
        double sizeX = progressVideo.getWidth();
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        Duration duration = mediaPlayer.getMedia().getDuration();

        if (x < 0) {
            progressVideo.setProgress(0);
            mediaPlayer.seek(new Duration(0));
            timerVideo.setVisible(true);
        } else if (x > sizeX) {
            progressVideo.setProgress(duration.toMillis());
            mediaPlayer.seek(duration);
            timerVideo.setVisible(false);
            timerVideo.setText(duration.toMinutes() + " min");
        } else {
            progressVideo.setProgress(x / sizeX);
            mediaPlayer.seek(new Duration(duration.toMillis() / sizeX * x));
            timerVideo.setVisible(true);
        }
    }

    @FXML
    private void mediaView(MouseEvent event) {
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        if (mediaPlayer == null) return;
        if (mediaPlayer.getStatus().name().equalsIgnoreCase("PLAYING")) {
            mediaPlayer.pause();
            pauseBtn.setVisible(false);
            playBtn.setVisible(true);
        } else {
            mediaPlayer.play();
            playBtn.setVisible(false);
            pauseBtn.setVisible(true);
            stopBtn.setVisible(true);
            timerVideo.setVisible(true);
        }
    }

    public void playWith(MediaExercice mediaExercice) {
        if (mediaExercice.isAudio()) {
            Image image = new Image(mediaExercice.getImage().toURI().toString());

            imageVideo.setImage(image);
            imageVideo.setVisible(true);
            imageVideo.setOnMouseClicked(this::mediaView);
        }

        System.out.println(mediaExercice.getRessource().toURI().toString());
        Media media = new Media(mediaExercice.getRessource().toURI().toString());


        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        control_sound.setVisible(true);
        control_action.setVisible(true);
        control_progress.setVisible(true);
        openVideo.setVisible(false);
        volumeBtn.setValue(50);
        mediaPlayer.setVolume(0.5);

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            Date date = new Date((long) newValue.toMillis());
            progressVideo.setProgress(newValue.toMillis() / media.getDuration().toMillis());
            timerVideo.setText(date.getMinutes() + ":" + date.getSeconds());
        });
    }

    public File getRessource() {
        return ressource;
    }

    public File getImage() {
        return image;
    }

    public boolean isAudio() {
        return audio;
    }
}
