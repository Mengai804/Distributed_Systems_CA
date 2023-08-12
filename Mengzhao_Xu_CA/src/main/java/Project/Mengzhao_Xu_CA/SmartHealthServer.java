package Project.Mengzhao_Xu_CA;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

//Server class
public class SmartHealthServer {
	//Logger and server initialization
    private static final Logger logger = Logger.getLogger(SmartHealthServer.class.getName());

    private Server server;

    private void start() throws IOException {
    	// Starts the server, adds services
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new PatientMonitoringServiceImpl())
                .addService(new PrescriptionServiceImpl())
                .addService(new EquipmentMonitoringServiceImpl())
                .build()
                .start();
        
        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            SmartHealthServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    // Stop the server
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // Wait for the server termination 
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
    
    // Implementations for gRPC service methods
    private class PatientMonitoringServiceImpl extends PatientMonitoringServiceGrpc.PatientMonitoringServiceImplBase {
        @Override
        public void getPatientStatus(PatientRequest req, StreamObserver<PatientStatus> responseObserver) {
            // For demo
            PatientStatus status = PatientStatus.newBuilder().setStatus("Stable").build();
            responseObserver.onNext(status);
            responseObserver.onCompleted();
        }
    }

    private class PrescriptionServiceImpl extends PrescriptionServiceGrpc.PrescriptionServiceImplBase {
        @Override
        public void addPrescription(Prescription req, StreamObserver<PrescriptionResponse> responseObserver) {
            // For demo
            PrescriptionResponse response = PrescriptionResponse.newBuilder().setMessage("Prescription Added!").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private class EquipmentMonitoringServiceImpl extends EquipmentMonitoringServiceGrpc.EquipmentMonitoringServiceImplBase {
        @Override
        public void getEquipmentStatus(EquipmentRequest req, StreamObserver<EquipmentStatus> responseObserver) {
            // For demo
            EquipmentStatus status = EquipmentStatus.newBuilder().setStatus("Operational").build();
            responseObserver.onNext(status);
            responseObserver.onCompleted();
        }
    }

    // Main method to start the server and keep it running
    public static void main(String[] args) throws IOException, InterruptedException {
        final SmartHealthServer server = new SmartHealthServer();
        server.start();
        server.blockUntilShutdown();
    }
}