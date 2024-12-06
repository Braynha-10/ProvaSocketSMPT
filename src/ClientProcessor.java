package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientProcessor implements Runnable {

    private Socket socket;
    private String serverName;

    public ClientProcessor(Socket socket, String serverName) {
        this.socket = socket;
        this.serverName = serverName;
    }

    // Função para validar comandos SMTP e endereços de e-mail
    public String validarMensagem(String message) {
        String emailRegex = ("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]");
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {

            // Envia mensagem inicial de boas-vindas
            out.println("220 <" + this.serverName + "> Simple Mail Transfer Service Ready\r");

            String message_Email_Client;
            String senderEmail = null;
            String recipientEmail = null;
            boolean dataMode = false;

            while ((message_Email_Client = in.readLine()) != null) {
                System.out.println("Received: " + message_Email_Client); // Debugando output no server
               
                if (message_Email_Client.startsWith("HELO")) {
                    out.println("250 " + this.serverName + " Hello ");
                } else if (message_Email_Client.startsWith("MAIL FROM: ")) {
                    senderEmail = message_Email_Client.substring(10).trim(); // Extraindo email
                    String validSender = validarMensagem(senderEmail);
                    if (validSender != null) {
                        out.println("250 Sender <" + validSender + "> OK");
                    } else {
                        out.println("500 Syntax error, invalid email format");
                    }
                } else if (message_Email_Client.startsWith("RCPT TO: ")) {
                    recipientEmail = message_Email_Client.substring(9).trim(); // Extraindo email
                    String validRecipient = validarMensagem(recipientEmail);
                    if (validRecipient != null) {
                        out.println("250 Recipient <" + validRecipient + "> OK");
                    } else {
                        out.println("500 Syntax error, invalid recipient email format");
                    }
                } else if (message_Email_Client.equals("DATA")) {
                    out.println("354 End data with <CR><LF>.<CR><LF>");
                    dataMode = true;
                     //Lendo as linhas do corpo de data do e-mail
                        String linha;
                        while ((linha = in.readLine()) != null) {
                            if (linha.equals(".")) {
                                // O ponto em uma linha isolada indica que o corpo foi finalizado
                                out.println("250 Message accepted for delivery");
                                dataMode = false;
                                break; // Quebra o loop quando o corpo do e-mail termina
                            }
                        }
                } else if (dataMode && message_Email_Client.equals(".")) {
                    out.println("250 Message accepted for delivery");
                    dataMode = false;
                } else if (message_Email_Client.equals("QUIT")) {
                    out.println("221 " + this.serverName + " Service closing transmission channel");
                    break;
                } else {
                    out.println("500 Syntax error, command unrecognized");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
