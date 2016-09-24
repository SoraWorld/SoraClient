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
    public DialogStage() {
        try {
            initModality(Modality.APPLICATION_MODAL);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("assets.lang.Language", Locale.getDefault());
            Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/DialogStage.fxml"), resourceBundle);
            setTitle("DialogStage");
            Button button = (Button) root.lookup("#close");
            button.setOnAction(e -> close());
            setScene(new Scene(root, 400, 300));
            initStyle(StageStyle.UNDECORATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DialogStage(String message) {
        try {
            initModality(Modality.APPLICATION_MODAL);
            ResourceBundle resourceBundle = ResourceBundle.getBundle("assets.lang.Language", Locale.getDefault());
            Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/DialogStage.fxml"), resourceBundle);
            setTitle(message);
            Button button = (Button) root.lookup("#close");
            button.setOnAction(e -> close());
            setScene(new Scene(root, 400, 300));
            initStyle(StageStyle.UNDECORATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
