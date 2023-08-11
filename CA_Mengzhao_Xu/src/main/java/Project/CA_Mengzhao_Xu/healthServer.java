package Project.CA_Mengzhao_Xu;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class healthServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new PatientServiceImpl())
                .addService(new DoctorServiceImpl())
                .addService(new AppointmentServiceImpl())
                .build();

        server.start();
        System.out.println("Server started at " + server.getPort());
        server.awaitTermination();
    }

    static class PatientServiceImpl extends PatientServiceGrpc.PatientServiceImplBase {
        @Override
        public void getPatientDetails(PatientRequest request, StreamObserver<PatientDetails> responseObserver) {
            // Mock data
            responseObserver.onNext(PatientDetails.newBuilder()
                    .setPatientId(request.getPatientId())
                    .setName("John Doe")
                    .setAge("30")
                    .setGender("Male")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public void updatePatientDetails(PatientDetails request, StreamObserver<Status> responseObserver) {
            // Simply acknowledge for now
            responseObserver.onNext(Status.newBuilder().setSuccess(true).setMessage("Updated").build());
            responseObserver.onCompleted();
        }

        @Override
        public void listMedicalHistory(PatientRequest request, StreamObserver<MedicalHistory> responseObserver) {
            // Mock data
            responseObserver.onNext(MedicalHistory.newBuilder()
                    .setDisease("Cold")
                    .setTreatment("Rest")
                    .setDate("2023-08-11")
                    .build());
            responseObserver.onCompleted();
        }
    }

    static class DoctorServiceImpl extends DoctorServiceGrpc.DoctorServiceImplBase {
        @Override
        public void getDoctorDetails(DoctorRequest request, StreamObserver<DoctorDetails> responseObserver) {
            // Mock data
            responseObserver.onNext(DoctorDetails.newBuilder()
                    .setDoctorId(request.getDoctorId())
                    .setName("Dr. Smith")
                    .setSpecialty("General")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public void updateDoctorAvailability(DoctorAvailability request, StreamObserver<Status> responseObserver) {
            // Simply acknowledge for now
            responseObserver.onNext(Status.newBuilder().setSuccess(true).setMessage("Availability Updated").build());
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<DoctorAvailability> streamDoctorAvailability(StreamObserver<Status> responseObserver) {
            return new StreamObserver<DoctorAvailability>() {
                @Override
                public void onNext(DoctorAvailability doctorAvailability) {
                    System.out.println("Received availability update for doctor: " + doctorAvailability.getDoctorId());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error in streamDoctorAvailability: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    responseObserver.onNext(Status.newBuilder().setSuccess(true).setMessage("All updates received").build());
                    responseObserver.onCompleted();
                }
            };
        }
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        @Override
        public void bookAppointment(AppointmentRequest request, StreamObserver<AppointmentConfirmation> responseObserver) {
            // Mock data
            responseObserver.onNext(AppointmentConfirmation.newBuilder()
                    .setAppointmentId("12345")
                    .setPatientId(request.getPatientId())
                    .setDoctorId(request.getDoctorId())
                    .setConfirmedTime("2023-08-12T10:00")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public void getAppointmentStatus(AppointmentStatusRequest request, StreamObserver<AppointmentStatus> responseObserver) {
            // Mock data
            responseObserver.onNext(AppointmentStatus.newBuilder()
                    .setAppointmentId(request.getAppointmentId())
                    .setStatus("CONFIRMED")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<AppointmentRequest> streamAppointmentUpdates(StreamObserver<AppointmentStatus> responseObserver) {
            return new StreamObserver<AppointmentRequest>() {
                @Override
                public void onNext(AppointmentRequest appointmentRequest) {
                    System.out.println("Received appointment request for patient: " + appointmentRequest.getPatientId());
                    responseObserver.onNext(AppointmentStatus.newBuilder()
                            .setAppointmentId("12345")
                            .setStatus("PENDING")
                            .build());
                }

                @Override
                public void onError(Throwable throwable) {
                    System.err.println("Error in streamAppointmentUpdates: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
