package chatudpcode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReaderThread implements Runnable {
    private MulticastSocket socket;
    private InetAddress group;
    private int port;

    public ReaderThread(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        // Esperamos la respuesta no mayor a 1024 bytes
        byte[] buffer = new byte[1024];
        String line;
        while (!MulticastUDPChat.finished) {
            try {
                DatagramPacket mensajeEntrada = new DatagramPacket (buffer, buffer.length, group, port);
                socket.receive (mensajeEntrada);
                
                line = new String(buffer, 0, mensajeEntrada.getLength()) ;

                if (!line.startsWith(MulticastUDPChat.username))
                    System.out.println(line);
            } catch (IOException e) {
                System.out.println();
            }
        }
    }
}
