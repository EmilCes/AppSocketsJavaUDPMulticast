package dev.cesarele23.chatudp;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ClientMessageController {
    @FXML
    private Text tUsername;
    @FXML
    private Text tMessage;

    public void setMessageData(String username, String message) {
        tUsername.setText(username);
        tMessage.setText(message);
    }
}
