package Project.Mengzhao_Xu_CA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SmartHealthGUI {
    private JFrame frame;
    private JButton btnCheckPatientStatus;
    private JButton btnAddPrescription;
    private JButton btnCheckEquipmentStatus;
    private JTextArea textArea;

    public SmartHealthGUI() {
        frame = new JFrame("SmartHealth Client");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Text area to display results
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        // Panel for buttons
        JPanel panel = new JPanel();
        btnCheckPatientStatus = new JButton("Check Patient Status");
        btnAddPrescription = new JButton("Add Prescription");
        btnCheckEquipmentStatus = new JButton("Check Equipment Status");
        panel.add(btnCheckPatientStatus);
        panel.add(btnAddPrescription);
        panel.add(btnCheckEquipmentStatus);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        // Event listeners
        btnCheckPatientStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call your gRPC client's method for patient status here and display the result in the text area
                textArea.append("Patient status checked\n");
            }
        });

        btnAddPrescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call your gRPC client's method for adding a prescription here and display the result in the text area
                textArea.append("Prescription added\n");
            }
        });

        btnCheckEquipmentStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call your gRPC client's method for checking equipment status here and display the result in the text area
                textArea.append("Equipment status checked\n");
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SmartHealthGUI().show();
            }
        });
    }
}
