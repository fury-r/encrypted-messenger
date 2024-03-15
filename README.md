# SecureMessenger App

[![Workflow status](https://github.com/fury-dev/erp/actions/workflows/actions.yaml/badge.svg?branch=master)](https://github.com/fury-dev/erp/actions/workflows/actions.yaml)


SecureMessenger is a secure and end-to-end encrypted (E2EE) messaging application designed to prioritize user privacy and data security. The application is written in Kotlin for the Android platform, utilizes gRPC for communication, employs Protocol Buffers for efficient data serialization, and includes middleware written in Node.js using TypeScript. The backend server is implemented in the Go programming language.

## Features

- **End-to-End Encryption (E2EE):** All communication within SecureMessenger is encrypted, ensuring that only the intended recipients can access the messages.

- **Kotlin Android App:** The Android application is built using Kotlin, providing a seamless and modern user experience.

- **gRPC Communication:** The app uses gRPC for efficient and reliable communication between the client (Android app) and the server.

- **Protocol Buffers:** Protocol Buffers are employed for data serialization, optimizing data exchange between the client and the server.

- **Node.js Middleware:** Middleware, written in Node.js using TypeScript, acts as an intermediary layer between the Android app and the backend server. It handles various tasks such as authentication, authorization, and additional security measures.

- **Go Backend Server:** The backend server is implemented in the Go programming language, providing a robust and performant infrastructure for managing user accounts, storing messages, and handling various application functionalities.

## Architechure
![ArchitechureMessenger drawio](https://github.com/fury-r/messenger/assets/79844581/b7b883e2-87a6-4097-b6ff-25aee1cd5c3b)
### Description
- Client: This refers to the mobile application that users interact with to send and receive messages. Written Kotlin(Android App).
- Client Db: This represents the database used by the mobile client to store information locally on the device. Here, SQLite is being used.
- GRPC: This stands for Remote Procedure Calls and it’s a high-performance framework for communication between applications. In the diagram, it  refers to the communication protocol used between the mobile client and the middleware and backend server.
- Backend: This represents the server-side component of the application. It’s built using the Go programming language.
- Firebase: This is a backend-as-a-service (BaaS) platform developed by Google. It provides a variety of features for mobile app development, including authentication, databases, notifications, and more. In this messaging system, it likely acts as a real-time database.
- Message Broker: This is a software component that’s responsible for routing messages between applications. Here, RabbitMQ is being used.
- Middleware: This is a software layer that sits between the client and the backend server and provides additional functionality, such as security or logging.


## Getting Started

### Prerequisites

- Install the latest version of Android Studio for Android development.
- Install Node.js for running the middleware.
- Ensure Go is installed for the backend server.

### Dependecies for proto
- Backend:

``sh
 go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest
``


### Installation

1. **Android App:**
   - Open the SecureMessenger Android project in Android Studio.
   - Build and run the application on an emulator or physical device.

2. **Node.js Middleware:**
   - Navigate to the `middleware` directory.
   - Run `npm install` to install dependencies.
   - Start the middleware server with `npm start`.

3. **Go Backend Server:**
   - Navigate to the `backend` directory.
   - Run `go run main.go` to start the backend server.


## Configuration

- Update the necessary configuration parameters in the Android app, middleware, and backend server to match your environment, such as API endpoints, database connections, and encryption keys.

## Contact

For any inquiries or issues, please contact [link](https://github.com/fury-r)