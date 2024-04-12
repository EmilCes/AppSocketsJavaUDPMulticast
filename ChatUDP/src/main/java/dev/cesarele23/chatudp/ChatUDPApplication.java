package dev.cesarele23.chatudp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatUDPApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatUDPApplication.class.getResource("FXMLLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat UDP");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}