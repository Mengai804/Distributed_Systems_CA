package Project.Mengzhao_Xu_CA;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class SmartHealthServer {
    private static final Logger logger = Logger.getLogger(SmartHealthServer.class.getName());

    private Server server;

    private void start() throws IOException {
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

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class PatientMonitoringServiceImpl extends PatientMonitoringServiceGrpc.PatientMonitoringServiceImplBase {
        @Override
        public void getPatientStatus(PatientRequest req, StreamObserver<PatientStatus> responseObserver) {
            // For demo purposes. Implement actual logic.
            PatientStatus status = PatientStatus.newBuilder().setStatus("Stable").build();
            responseObserver.onNext(status);
            responseObserver.onCompleted();
        }
    }

    private class PrescriptionServiceImpl extends PrescriptionServiceGrpc.PrescriptionServiceImplBase {
        @Override
        public void addPrescription(Prescription req, StreamObserver<PrescriptionResponse> responseObserver) {
            // For demo purposes. Implement actual logic.
            PrescriptionResponse response = PrescriptionResponse.newBuilder().setMessage("Prescription Added!").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    private class EquipmentMonitoringServiceImpl extends EquipmentMonitoringServiceGrpc.EquipmentMonitoringServiceImplBase {
        @Override
        public void getEquipmentStatus(EquipmentRequest req, StreamObserver<EquipmentStatus> responseObserver) {
            // For demo purposes. Implement actual logic.
            EquipmentStatus status = EquipmentStatus.newBuilder().setStatus("Operational").build();
            responseObserver.onNext(status);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final SmartHealthServer server = new SmartHealthServer();
        server.start();
        server.blockUntilShutdown();
    }
}