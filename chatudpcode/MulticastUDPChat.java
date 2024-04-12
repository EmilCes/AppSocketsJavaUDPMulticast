package chatudpcode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

public class MulticastUDPChat {

    static volatile boolean finished = false;
    static String username = "";

    @SuppressWarnings("deprecation")

    public static void main(String[] args) {

        try {
            // Establecemos un puerto acordado para multicast
            int port = 8080;
            // Establecemos la IP para multicas
            InetAddress group = InetAddress.getByName("224.0.0.0");
            // Creamos el socket multicast
            MulticastSocket socket = new MulticastSocket(port);

            Scanner scanner = new Scanner(System.in);
            String line = "";

            byte[] buffer = new byte[1024];

            System.out.println("Ingresa tu nombre: ");
            username = scanner.nextLine();

            // Se une al grupo
            socket.joinGroup(group);

            Thread thread = new Thread(new ReaderThread(socket, group, port));
            thread.start();

            System.out.println("Puede comenzar a escribir mensajes en el grupo...");

            while (true) {
                line = scanner.nextLine();

                if (line.equalsIgnoreCase("Adios")) {
                    finished = true;

                    line = username + ": Ha terminado la conexión.";
                    buffer = line.getBytes();
                    DatagramPacket outputMessage = new DatagramPacket(buffer, buffer.length, group, port);
                    socket.send(outputMessage);

                    socket.leaveGroup(group);
                    socket.close();
                    break;
                }

                line = username + ": " + line;
                buffer = line.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, group, port);
                socket.send(datagramPacket);
            }

            scanner.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}




