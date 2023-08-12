SmartHealth: gRPC-based Health Monitoring System
SmartHealth is a health monitoring system built on top of gRPC, offering services for patient monitoring,
prescription management, and equipment status checking.


Overview:

This project is structured into two main components:

1. SmartHealthServer: The server component responsible for serving client requests and handling business logic.
2. SmartHealthClient: A client application that demonstrates how to interact with the server and request services.

Features:

Patient Monitoring: Query patient statuses.
Prescription Management: Add and manage patient prescriptions.
Equipment Monitoring: Check the operational status of medical equipment.

Installation and Usage:

Prerequisites:
Ensure you have Java installed.
Ensure you have gRPC libraries and the Protocol Buffers compiler installed.

Setup:

bash:
https://github.com/Mengai804/SmartHealth.git
cd SmartHealth

Compilation:
Compile the .proto files (not provided in the sample) to generate Java gRPC code. Then, compile the Java code.

bash:
protoc --java_out=. path_to_your_proto_files/*.proto
javac -cp .:path_to_grpc_jar_files/* *.java

Run:

Start the server:
bash:
java Project.Mengzhao_Xu_CA.SmartHealthServer

In a different terminal, run the client:
bash:
java Project.Mengzhao_Xu_CA.SmartHealthClient

Error Handling:

Client-Side: Errors during RPC calls are captured as 'StatusRuntimeException' and are logged.
Server-Side: The server gracefully shuts down upon JVM shutdown, ensuring all tasks are completed.

Future Enhancements:

Implement advanced error handling techniques such as retries, timeouts, and deadlines.
Introduce load balancing for handling multiple server instances.
Enhance the system with actual business logic replacing the current placeholders.

Contributing:

Contributions are welcome! 

License:

This project is licensed under the MIT License.

Made by Mengzhao Xu