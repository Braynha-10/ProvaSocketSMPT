package src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port = 2525;
    private String SMTP_server_name;

    public Server() {
        this.SMTP_server_name = "Bryan_Server_SMTP";
    }

    public Server(int port, String serverName) {
        this.port = port;
        this.SMTP_server_name = serverName;
    }

    public void start() throws IOException {
        try (ServerSocket server = new ServerSocket(this.port, 2048, InetAddress.getByName("127.0.0.1"))) {
            System.out.println("Servidor iniciado na porta " + this.port);

            while (true) {
                new Thread(new ClientProcessor(server.accept(), SMTP_server_name)).start();
            }
        }
    }
}
