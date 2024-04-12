package chatudpcode;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

public class MulticastUDP {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        try{

            // Establecemos un puerto acordado para multicast
            int puerto = 8080;
            // Establecemos la IP para multicas
            InetAddress grupo = InetAddress.getByName("224.0.0.0");
            // Creamos el socket multicast
            MulticastSocket socket = new MulticastSocket(puerto);

            // Se une al grupo
            socket.joinGroup(grupo);

            // Inicia un envío de datos al grupo multicast
            Scanner scanner = new Scanner(System.in);
            System.out.println("Envíe un mensaje al grupo: ");
            String msj = scanner.nextLine();

            // Envía un mensaje por UDP al grupo
            byte[] m = msj.getBytes();
            DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupo, puerto);
            socket.send(mensajeSalida);

            // Esperamos la respuesta no mayor a 1024 bytes
            byte[] buffer = new byte[1024];
            String linea;

            // Se queda a la espera de mensajes al grupo,
            // hasta recibir "Adios" por parte de algun nuevo miembro
            while (true) {
                // Leemos los mensajes UDP que lleguen
                DatagramPacket mensajeEntrada = new DatagramPacket(buffer, buffer.length);
                socket.receive(mensajeEntrada);

                // Convierte el mensaje a cadena
                linea = new String(mensajeEntrada.getData(), 0, mensajeEntrada.getLength());
                System.out.println("Recibido: " + linea);

                // Si alguien envía "Adios", termina las conexiones
                if (linea.equalsIgnoreCase("Adios")) {
                    // Recibio "Adios" abandona el grupo
                    socket.leaveGroup(grupo);
                    break;
                }
            }

            scanner.close();
            socket.close();

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch(IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

}

