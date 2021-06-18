package reconstitution.model;

import java.io.*;
import java.nio.file.Files;

public class MediaExercice implements Serializable {

    private final String CACHE_DIRECTORY = System.getProperty("java.io.tmpdir") + ".reconstitution";
    private final byte[] image;
    private final boolean isAudio;
    private final String ressourceName;
    private final byte[] ressource;
    private final String imageName;

    public MediaExercice(File ressource, File image, boolean isAudio) {
        this.isAudio = isAudio;

        byte[] imgByte = new byte[2048];
        byte[] ressourceByte = new byte[2048];

        String imageName = "";
        String ressourceName = "";

        System.out.println("test: " + ressource.toPath());

        if (isAudio && image != null) {
            try {
                imgByte = Files.readAllBytes(image.toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            imageName = image.getName();
            ressourceName = ressource.getName();
        } else {
            try {
                ressourceByte = Files.readAllBytes(ressource.toPath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            ressourceName = ressource.getName();
        }
        this.ressource = ressourceByte;
        this.ressourceName = ressourceName;
        this.image = imgByte;
        this.imageName = imageName;
    }

    public File getRessource() {
        return getFromTemp(ressource, ressourceName);
    }

    public File getImage() {
        return getFromTemp(image, imageName);
    }

    public boolean isAudio() {
        return isAudio;
    }

    private File getFromTemp(byte[] el, String name) {
        new File(CACHE_DIRECTORY).mkdir();

        try {
            Files.createTempFile(name, "");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        File file = new File(CACHE_DIRECTORY + File.separator + name);

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(el);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return file;
    }
}
