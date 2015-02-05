/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Start;

import UI.UI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Luming Wu
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        UI _ui = new UI(primaryStage);
        StackPane rootroot = new StackPane();
        rootroot.getChildren().addAll(_ui.getBackEffects(), _ui.getRoot(),_ui.getFrontEffects());
        Scene scene = new Scene(rootroot, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

        primaryStage.setTitle("24 - Updated Classic");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
