package flex;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Flex extends Application {

    Scene scene, scene2;
    Stage stageSwitch;

    @Override
    public void start(final Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        scene = new Scene(root);
        final Delta dragDelta = new Delta();
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        stageSwitch = stage;
        stage.setScene(scene);
        stage.show();
    }

    public void switchScreen() {
        stageSwitch.setScene(scene2);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
