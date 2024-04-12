package dev.cesarele23.chatudp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField tfUsername;
    @FXML
    private Text tErrorMessage;

    @FXML
    protected void clickBtnLogin() {
        login();
    }

    public void initialize() {
        sendWithEnter();
    }

    private void goToChat() {
        Stage mainScene = (Stage) tfUsername.getScene().getWindow();
        mainScene.setScene(startScene());
        centerScene(mainScene);
        mainScene.setTitle("Chat");
        mainScene.show();
    }

    private Scene startScene() {
        Scene scene = null;

        try {
            FXMLLoader loader = new FXMLLoader(ChatUDPApplication.class.getResource("FXMLChat.fxml"));
            Parent root = loader.load();
            ChatController controller = loader.getController();
            controller.setUsername(tfUsername.getText());
            scene = new Scene(root);
        } catch (IOException ex) {
            System.err.println("ERROR: " + ex.getMessage());
        }

        return scene;
    }

    private static void centerScene(Stage scene) {
        Rectangle2D limits = Screen.getPrimary().getVisualBounds();
        scene.setX((limits.getWidth() - limits.getWidth()) / 2);
        scene.setY((limits.getHeight() - limits.getHeight()) / 2);
    }

    private void sendWithEnter() {
        tfUsername.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
    }

    private void login() {
        if (tfUsername.getText().isEmpty()) {
            tErrorMessage.setVisible(true);
        } else {
            goToChat();
        }
    }
}
