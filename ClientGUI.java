package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame {

    private JTextArea clientArea;
    private JTextField inputField;
    private JButton sendButton;
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket socket;

    public ClientGUI() {
        setTitle("Client");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        clientArea = new JTextArea();
        clientArea.setEditable(false);
        add(new JScrollPane(clientArea), BorderLayout.CENTER);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event when Enter key is pressed
        inputField.addActionListener(e -> sendMessage());

        // Event when Send button is clicked
        sendButton.addActionListener(e -> sendMessage());

        setVisible(true);
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String msg;
                while ((msg = reader.readLine()) != null) {
                    clientArea.append("Server: " + msg + "\n");
                }

            } catch (IOException e) {
                clientArea.append("Connection failed: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private void sendMessage() {
        try {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty()) {
                writer.write(msg + "\n");
                writer.flush();
                clientArea.append("Client: " + msg + "\n");
                inputField.setText("");
            }
        } catch (IOException e) {
            clientArea.append("Failed to send: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}



    
