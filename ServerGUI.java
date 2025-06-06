import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ServerGUI extends JFrame {
    private JTextArea logArea;

    public ServerGUI() {
        setTitle("Server");
        setSize(400, 300);
        logArea = new JTextArea();
        add(new JScrollPane(logArea));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        startServer();
    }

    private void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(5000)) {
                logArea.append("Server started on port 5000\n");
                while (true) {
                    Socket client = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                    String msg = in.readLine();
                    logArea.append("Received: " + msg + "\n");

                    // Auto-reply logic, handle special characters
                    String reply = processMessage(msg);

                    out.println(reply);
                    logArea.append("Replied: " + reply + "\n");

                    client.close();
                }
            } catch (IOException e) {
                logArea.append("Error: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private String processMessage(String msg) {
        if (msg == null) return "Invalid message";

        // Example: If message contains $, reply differently
        if (msg.contains("$") || msg.contains("#") || msg.contains("%") || 
    msg.contains("*") || msg.contains("@") || msg.contains("!")) {
    return "You sent invalid request, Try again.";
} else {
    return "Server received: " + msg;
}

    }

    public static void main(String[] args) {
        new ServerGUI();
    }
}

