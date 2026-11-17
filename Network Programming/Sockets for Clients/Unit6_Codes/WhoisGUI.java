
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class WhoisGUI extends JFrame {

    private final JTextField domainField;
    private final JTextField serverField;
    private final JTextArea resultArea;
    private final JButton searchButton;
    private final JLabel statusLabel;

    private final WhoisClient whoisClient;

    public WhoisGUI() {

        whoisClient = new WhoisClient();

        setTitle("Java WHOIS Lookup");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input fields
        domainField = new JTextField("example.com", 20);
        serverField = new JTextField("whois.iana.org", 20);

        searchButton = new JButton("Search");

        // Result area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane scrollPane =
            new JScrollPane(resultArea);

        // Status
        statusLabel = new JLabel("Ready");

        // Input panel
        JPanel inputPanel = new JPanel(
            new GridLayout(3, 2, 8, 8)
        );

        inputPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        inputPanel.add(new JLabel("Domain:"));
        inputPanel.add(domainField);

        inputPanel.add(new JLabel("WHOIS Server:"));
        inputPanel.add(serverField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(searchButton);

        // Bottom panel
        JPanel bottomPanel = new JPanel(
            new BorderLayout()
        );

        bottomPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 10, 10, 10)
        );

        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        // Main layout
        setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Search button action
        searchButton.addActionListener(e -> searchDomain());
    }

    private void searchDomain() {

        String domain = domainField.getText().trim();
        String server = serverField.getText().trim();

        if (domain.isEmpty() || server.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Enter both domain and WHOIS server."
            );
            return;
        }

        // Prevent duplicate requests while searching
        searchButton.setEnabled(false);

        resultArea.setText("");
        statusLabel.setText("Searching...");

        // Network work happens in the background
        SwingWorker<String, Void> worker =
            new SwingWorker<String, Void>() {

                @Override
                protected String doInBackground()
                        throws Exception {

                    return whoisClient.lookup(
                        server,
                        domain
                    );
                }

                @Override
                protected void done() {

                    try {
                        String result = get();

                        resultArea.setText(
                            result.isBlank()
                                ? "No response received."
                                : result
                        );

                        statusLabel.setText("Lookup completed.");

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt();

                        statusLabel.setText(
                            "Lookup interrupted."
                        );

                    } catch (ExecutionException e) {

                        Throwable cause = e.getCause();

                        resultArea.setText(
                            "Error: " + cause.getMessage()
                        );

                        statusLabel.setText(
                            "Lookup failed."
                        );

                    } finally {
                        searchButton.setEnabled(true);
                    }
                }
            };

        worker.execute();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            WhoisGUI gui = new WhoisGUI();

            gui.setVisible(true);
        });
    }
}
