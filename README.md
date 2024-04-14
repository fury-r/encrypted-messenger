# SecureMessenger App
This code is an improvement over my old project which is private:[link](https://github.com/fury-r/messenger)

[![Workflow status](https://github.com/fury-dev/erp/actions/workflows/actions.yaml/badge.svg?branch=master)](https://github.com/fury-dev/erp/actions/workflows/actions.yaml)

![messenger](https://github.com/fury-r/encrypted-messenger/assets/79844581/66a70d5b-6978-4114-9a72-cd559901d8fd)


SecureMessenger is a secure and end-to-end encrypted (E2EE) messaging application designed to prioritize user privacy and data security. The application is written in Kotlin for the Android platform, utilizes gRPC for communication, employs Protocol Buffers for efficient data serialization, and includes middleware written in Node.js using TypeScript. The backend server is implemented in the Go programming language.

## Features

- **End-to-End Encryption (E2EE):** All communication within SecureMessenger is encrypted, ensuring that only the intended recipients can access the messages (RSA + DES).

- **Kotlin Android App:** The Android application is built using Kotlin, providing a seamless and modern user experience.

- **gRPC Communication:** The app uses gRPC for efficient and reliable communication between the client (Android app) and the server.

- **Protocol Buffers:** Protocol Buffers are employed for data serialization, optimizing data exchange between the client and the server.

- **Node.js Middleware:** Middleware, written in Node.js using TypeScript, acts as an intermediary layer between the Android app and the backend server. It handles various tasks such as authentication, authorization, and additional security measures.

- **Go Backend Server:** The backend server is implemented in the Go programming language, providing a robust and performant infrastructure for managing user accounts, storing messages, and handling various application functionalities.

## Architecture
![Architecture drawio (5)](https://github.com/fury-r/encrypted-messenger/assets/79844581/52ad568b-6c70-47e2-8a0a-9d7bdab76c7d)

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
   - Open terminal in wsl or in your linux os
   - generate the types if not you have made any change
   ``
      sh linux_proto.sh
   ``
   - Build and run the application on an emulator or physical device.

2. **Node.js Middleware:**
   - Navigate to the `middleware` directory.
   - Run `npm install` to install dependencies.
   - Start the middleware server with `npm start`.

3. **Go Backend Server:**
   - Navigate to the `backend` directory.
   - generate types by running `sh proto.sh`
   - Run    `go mod download` to download all modules.
   - Run `go run main.go` to start the backend server.
4. **RabbitMQ server**
   - Installation:[link](https://www.rabbitmq.com/docs/install-windows-manual)
   - Make sure rabbitMQ is running.
   - Configure client and admin credetials at [link](http://localhost:15672/)
4. **Protobufs**
   - Installation:[link](https://protobuf.dev/)

### End to End encryption
## How It Works

1. **User Registration and Public Key Storage**: 
    - Users register on the platform   and generates public key and private key and their public keys are securely stored in Firestore.
    - Public keys are essential for encrypting data intended for specific users.

2. **Handshake Initialization**: 
    - User1 initiates communication with User2, User1 checks in the local db if DES key is present for this communication if not.
    - User1 generates an DES key for symmetric encryption.
    - User1 saves the DES key in the local DB.
    - User1 retrieves User2's public key from Firestore.
    - User1 encrypts the DES key with User2's public key.

3. **Handshake**: 
    - User1 sends the encrypted DES key to User2.
    - User2 receives the encrypted DES key and decrypts it using their private key.
    - Stores it in the local DB.

4. **Secure Communication**:
    - User1 and User2 now share a common DES key securely.
    - Both parties can use this DES key for symmetric encryption and decryption of messages.

5. **Message Exchange**:
    - User1 encrypts their message using the shared DES key.
    - User1 sends the encrypted message to User2.
    - User2 receives the encrypted message and decrypts it using the shared DES key.


## Security Measures

- **End-to-End Encryption**: 
    - All communication between users is encrypted from end-to-end.
    - Only the intended recipient possessing the private key can decrypt the messages.

- **Public Key Storage**: 
    - Public keys are stored securely in Firestore.
    - Firestore security rules ensure that only authorized users can access these keys.

- **DES Key Exchange**: 
    - The DES key used for symmetric encryption is exchanged securely via RSA encryption.
    - This ensures that even if the public key is compromised, the messages remain secure.

## Technologies Used

- **Firestore**: 
    - Firestore is utilized for storing users' public keys securely.

- **RSA Encryption**: 
    - RSA encryption is used for asymmetric encryption of the DES key during the handshake.

- **DES Encryption**: 
    - DES encryption is used for symmetric encryption of the actual message data.



## Conclusion

This setup ensures end-to-end encryption of communication between users, providing a high level of security and privacy. By leveraging Firestore for secure storage of public keys and employing RSA for key exchange and DES for symmetric encryption, users can communicate confidentially and securely within the platform.
## Configuration

- Update the necessary configuration parameters in the Android app, middleware, and backend server to match your environment, such as API endpoints, database connections, and encryption keys.


### Further Enhancements

- Save voice messages in the firestore db.Show user that message is recieved, give a option to download the voice message so the user can decide when to download.(Types have been already declared)
- Send other forms of media
- Send otp To Device using sms

## Contact

For any inquiries or issues, please contact [link](https://github.com/fury-r)
