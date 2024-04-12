package dev.cesarele23.chatudp;

import javafx.scene.text.Text;

public class ServerMessageController {
    @javafx.fxml.FXML
    private Text tUsername;
    @javafx.fxml.FXML
    private Text tMessage;

    public void setMessageData(String username, String message) {
        tUsername.setText(username);
        tMessage.setText(message);
    }
}
