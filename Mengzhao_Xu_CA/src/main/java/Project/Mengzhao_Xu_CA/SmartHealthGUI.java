package Project.Mengzhao_Xu_CA;

import javax.swing.*;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import java.awt.*;
import java.awt.event.*;
import Project.Mengzhao_Xu_CA.PatientMonitoringServiceGrpc.*;
import Project.Mengzhao_Xu_CA.PrescriptionServiceGrpc.*;
import Project.Mengzhao_Xu_CA.EquipmentMonitoringServiceGrpc.*;



public class SmartHealthGUI {
    private JFrame frame;
    private JButton btnCheckPatientStatus;
    private JButton btnAddPrescription;
    private JButton btnCheckEquipmentStatus;
    private JTextArea textArea;

    private SmartHealthClient client;  // gRPC client instance

    public SmartHealthGUI() {
        // Initialize gRPC client
        try {
            String target = "localhost:50051";
            client = new SmartHealthClient(Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

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

        // Updated Event listeners
        btnCheckPatientStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            PatientRequest patientRequest = PatientRequest.newBuilder().setPatientId("123").build();
                            PatientStatus patientStatus = client.getPatientStatus(patientRequest);
                            textArea.append("Received patient status: " + patientStatus.getStatus() + "\n");
                        } catch (Exception ex) {
                            textArea.append("Error: " + ex.getMessage() + "\n");
                        }
                        return null;
                    }
                }.execute();
            }
        });

        btnAddPrescription.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            Prescription prescription = Prescription.newBuilder().setPrescriptionId("456").setMedicineName("Aspirin").setDosage(100).build();
                            PrescriptionResponse prescriptionResponse = client.addPrescription(prescription);
                            textArea.append("Received prescription response: " + prescriptionResponse.getMessage() + "\n");
                        } catch (Exception ex) {
                            textArea.append("Error: " + ex.getMessage() + "\n");
                        }
                        return null;
                    }
                }.execute();
            }
        });

        btnCheckEquipmentStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            EquipmentRequest equipmentRequest = EquipmentRequest.newBuilder().setEquipmentId("789").build();
                            EquipmentStatus equipmentStatus = client.getEquipmentStatus(equipmentRequest);
                            textArea.append("Received equipment status: " + equipmentStatus.getStatus() + "\n");
                        } catch (Exception ex) {
                            textArea.append("Error: " + ex.getMessage() + "\n");
                        }
                        return null;
                    }
                }.execute();
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