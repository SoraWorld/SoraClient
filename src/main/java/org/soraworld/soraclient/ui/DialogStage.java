/*******************************************************************************
 * Created by Himmelt on 2016/9/24.
 * Copyright (c) 2015-2016. Himmelt All rights reserved.
 * https://opensource.org/licenses/MIT
 ******************************************************************************/

package org.soraworld.soraclient.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DialogStage extends Stage {

    private static Double PosX, PosY;

    public DialogStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            initStyle(StageStyle.TRANSPARENT);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("assets.lang.Language", Locale.getDefault());
            Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/DialogStage.fxml"), resourceBundle);
            Button button = (Button) root.lookup("#confirm");
            button.setOnAction(e -> close());
            root.setOnMouseDragged(event -> {
                setX(event.getScreenX() - PosX);
                setY(event.getScreenY() - PosY);
            });
            root.setOnMousePressed(event -> {
                PosX = event.getScreenX() - getX();
                PosY = event.getScreenY() - getY();
            });
            Scene scene = new Scene(root, 400, 200);
            scene.setFill(null);
            setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
