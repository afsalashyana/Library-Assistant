package library.assistant.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LibraryAssistantUtil {
    private static final String IMAGE_LOC = "/resources/icon.png";
    
    public static void setStageIcon(Stage stage)
    {
        stage.getIcons().add(new Image(IMAGE_LOC));
    }
}
