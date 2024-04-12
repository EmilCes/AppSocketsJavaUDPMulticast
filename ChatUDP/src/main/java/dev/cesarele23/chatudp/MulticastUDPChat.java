package dev.cesarele23.chatudp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastUDPChat extends Thread {

    private static final int PORT = 8080;
    private static final String GROUP_ADDRESS = "224.0.0.0";

    private volatile boolean running = true;

    private final String username;
    private final VBox vbMessages;
    private MulticastSocket socket;
    private InetAddress group;

    public MulticastUDPChat(String username, VBox messageContainer) {
        this.username = username;
        this.vbMessages = messageContainer;
    }

    public void stopRunning() {
        running = false;
    }

    @Override
    public void run() {
        try {
            group = InetAddress.getByName(GROUP_ADDRESS);
            socket = new MulticastSocket(PORT);
            socket.joinGroup(group);

            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                String[] receivedMessageDivided = receivedMessage.split(":");
                String messageUsername = receivedMessageDivided[0].trim();
                String message = receivedMessageDivided[1].trim();

                if (!messageUsername.equals(username)) {
                    Platform.runLater(() -> {
                        FXMLLoader loadClientMessage = new FXMLLoader(getClass().getResource("FXMLServerMessage.fxml"));
                        Pane serverMessage = null;

                        try {
                            serverMessage = loadClientMessage.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ServerMessageController serverMessageController = loadClientMessage.getController();
                        serverMessageController.setMessageData(messageUsername, message);
                        vbMessages.getChildren().add(serverMessage);
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!running) {
                socket.close();
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        String formattedMessage = username + ": " + message;
        byte[] buffer = formattedMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
        socket.send(packet);
    }

    public void disconnect() {
        try {
            String logoutMessage = username + ": ha salido de la sesión.";
            byte[] buffer = logoutMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);
            socket.leaveGroup(group);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopRunning();
        }
    }
}
