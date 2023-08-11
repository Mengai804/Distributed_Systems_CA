package Project.CA_Mengzhao_Xu;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class healthClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        // Using the PatientService
        PatientServiceGrpc.PatientServiceBlockingStub patientService = PatientServiceGrpc.newBlockingStub(channel);
        
        PatientRequest patientRequest = PatientRequest.newBuilder().setPatientId("001").build();
        PatientDetails patientDetails = patientService.getPatientDetails(patientRequest);
        System.out.println("Patient Details: " + patientDetails.getName());

        // Using the DoctorService
        DoctorServiceGrpc.DoctorServiceBlockingStub doctorService = DoctorServiceGrpc.newBlockingStub(channel);

        DoctorRequest doctorRequest = DoctorRequest.newBuilder().setDoctorId("doc001").build();
        DoctorDetails doctorDetails = doctorService.getDoctorDetails(doctorRequest);
        System.out.println("Doctor Details: " + doctorDetails.getName());

        // Bi-directional streaming example for AppointmentService
        AppointmentServiceGrpc.AppointmentServiceStub appointmentServiceAsync = AppointmentServiceGrpc.newStub(channel);
        
        StreamObserver<AppointmentRequest> requestObserver = appointmentServiceAsync.streamAppointmentUpdates(new StreamObserver<AppointmentStatus>() {
            @Override
            public void onNext(AppointmentStatus appointmentStatus) {
                System.out.println("Received appointment status: " + appointmentStatus.getStatus());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error from server: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Server completed sending data.");
            }
        });

        // Send some sample appointment requests
        for (int i = 0; i < 3; i++) {
            requestObserver.onNext(AppointmentRequest.newBuilder().setPatientId("001").setDoctorId("doc001").setPreferredTime("10:00").build());
        }
        requestObserver.onCompleted();

        // Shutting down the channel
        channel.shutdown();
    }
}
