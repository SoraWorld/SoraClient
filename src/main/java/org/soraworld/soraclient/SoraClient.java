/*******************************************************************************
 * Created by Himmelt on 2016/9/20.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Locale;
import java.util.ResourceBundle;

public class SoraClient extends Application {

    public static Stage mainStage;
    public static Double PosX, PosY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("assets.lang.Language", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/MainFrame.fxml"), resourceBundle);
        BorderPane mainFrame = (BorderPane) root.lookup("#mainFrame");
        primaryStage.setTitle("SoraClient");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        mainFrame.requestFocus();
    }
}
