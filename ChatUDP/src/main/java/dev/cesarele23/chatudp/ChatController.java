package dev.cesarele23.chatudp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatController {

    @FXML
    private TextField tfMessage;
    @FXML
    private Text tUsername;
    @FXML
    private VBox vbMessages;

    private MulticastUDPChat chat;

    public void initialize() {
        sendWithEnter();
    }

    @FXML
    protected void clickBtnSendMessage() throws IOException {
        sendMessage();
    }

    @FXML
    protected void clickBtnLogout() throws IOException {
        chat.disconnect();
        goToLogin();
    }

    public void setUsername(String username) {
        tUsername.setText(username);
        chat = new MulticastUDPChat(tUsername.getText(), vbMessages);
        chat.start();
    }

    private void goToLogin() {
        Stage mainScene = (Stage) tfMessage.getScene().getWindow();
        mainScene.setScene(startScene());
        centerScene(mainScene);
        mainScene.setTitle("Chat");
        mainScene.show();
    }

    private Scene startScene() {
        Scene scene = null;

        try {
            FXMLLoader loader = new FXMLLoader(ChatUDPApplication.class.getResource("FXMLLogin.fxml"));
            Parent root = loader.load();
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
        tfMessage.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessage() throws IOException {
        String message = tfMessage.getText().trim();

        if (!tfMessage.getText().isEmpty()) {
            FXMLLoader loadClientMessage = new FXMLLoader(getClass().getResource("FXMLClientMessage.fxml"));
            Pane clientMessage = loadClientMessage.load();
            ClientMessageController clientMessageController = loadClientMessage.getController();
            clientMessageController.setMessageData(tUsername.getText(), message);
            vbMessages.getChildren().add(clientMessage);

            chat.sendMessage(message);
            tfMessage.setText("");
        }
    }
}
