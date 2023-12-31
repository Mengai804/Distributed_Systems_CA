package Project.Mengzhao_Xu_CA;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

//Client class
public class SmartHealthClient {
	
	// Logger for recording activities and exceptions
    private static final Logger logger = Logger.getLogger(SmartHealthClient.class.getName());

    // Stubs for each gRPC services
    private final PatientMonitoringServiceGrpc.PatientMonitoringServiceBlockingStub patientMonitoringStub;
    private final PrescriptionServiceGrpc.PrescriptionServiceBlockingStub prescriptionServiceStub;
    private final EquipmentMonitoringServiceGrpc.EquipmentMonitoringServiceBlockingStub equipmentMonitoringStub;

    // Constructor
    public SmartHealthClient(Channel channel) {
        patientMonitoringStub = PatientMonitoringServiceGrpc.newBlockingStub(channel);
        prescriptionServiceStub = PrescriptionServiceGrpc.newBlockingStub(channel);
        equipmentMonitoringStub = EquipmentMonitoringServiceGrpc.newBlockingStub(channel);
    }

    // A sample to demonstrate gRPC calls to services
    public void runClientOperations() {
        
        try {
            PatientRequest patientRequest = PatientRequest.newBuilder().setPatientId("123").build();
            PatientStatus patientStatus = patientMonitoringStub.getPatientStatus(patientRequest);
            logger.info("Received patient status: " + patientStatus.getStatus());

            Prescription prescription = Prescription.newBuilder().setPrescriptionId("456").setMedicineName("Aspirin").setDosage(100).build();
            PrescriptionResponse prescriptionResponse = prescriptionServiceStub.addPrescription(prescription);
            logger.info("Received prescription response: " + prescriptionResponse.getMessage());

            EquipmentRequest equipmentRequest = EquipmentRequest.newBuilder().setEquipmentId("789").build();
            EquipmentStatus equipmentStatus = equipmentMonitoringStub.getEquipmentStatus(equipmentRequest);
            logger.info("Received equipment status: " + equipmentStatus.getStatus());

        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
        }
    }

    // Wrapper methods to expose client operations
    public PatientStatus getPatientStatus(PatientRequest request) {
        return patientMonitoringStub.getPatientStatus(request);
    }

    public PrescriptionResponse addPrescription(Prescription prescription) {
        return prescriptionServiceStub.addPrescription(prescription);
    }

    public EquipmentStatus getEquipmentStatus(EquipmentRequest request) {
        return equipmentMonitoringStub.getEquipmentStatus(request);
    }

    // Main method to start and test the services
    public static void main(String[] args) throws Exception {
        String target = "localhost:50051";

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        try {
            SmartHealthClient client = new SmartHealthClient(channel);
            client.runClientOperations();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}