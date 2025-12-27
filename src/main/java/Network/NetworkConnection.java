package Network;

import java.io.*;
import java. net. Socket;

public class NetworkConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public NetworkConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String message) {
        out.println(message);
        System.out.println("MESSAGE SENT: " + message);
    }

    public String receive() throws IOException {
        String message = in.readLine();
        System.out.println("MESSAGE RECEIVED: " + message);
        return message;
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}