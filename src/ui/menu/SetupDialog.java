package ui.menu;

import javax.swing.*;
import java.awt.*;

public class SetupDialog extends JDialog {
    private JTextField nicknameField;
    private JTextField serverField;
    private boolean confirmed = false;

    public SetupDialog(Frame owner) {
        super(owner, "Setup", true);
        setupUI();
    }

    private void setupUI() {
        setSize(400, 150);
        setLocationRelativeTo(getOwner());
        setLayout(new GridLayout(3, 2, 10, 10));

        nicknameField = new JTextField();
        serverField = new JTextField("localhost:3000");

        add(new JLabel("Nickname:"));
        add(nicknameField);
        add(new JLabel("Server Address:"));
        add(serverField);
        JButton connectButton = new JButton("OK");
        connectButton.addActionListener(e -> {
            if (!nicknameField.getText().trim().isEmpty() && !serverField.getText().trim().isEmpty()) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(new JLabel());
        add(connectButton);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getNickname() {
        return nicknameField.getText().trim();
    }

    public String getServerAddress() {
        return serverField.getText().trim();
    }
}
