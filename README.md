# ECE312-Socket-Chat

This branch of the ECE312 project contains the server code written in Java.  The server code utilizes Java SDK 8, and the Netty.IO sockets API.  Netty an asynchronous event-driven network application framework, allowing socket connection to occur without blocking the main thread.

The application itself is threaded, with the main thread controlling the user interface on the command line, and the second thread managing the server connections.  The second thread has a data structure that contains all of the currently connected clients to the server.  When a client sends a message to the server, the server reads that data, and then relays it to every client in the client array (excluding the client that sent the message), allowing all of the other clients connected to see it.  This allows group chatting to work.

The server also recognizes the kill command "exit".  If a client, or the server itself, sends a message only containing the word "exit", all of the clients and server will be disconnected and gracefully shut down.

# Compiling

Compiling is simple with Maven.  You will need Maven and the Java SDK 8 or above. From there, compiling to a runnable "executable" is as simple as "mvn compiler:compile package".  A file will be created called socket-chat-server-1.0-SNAPSHOT-jar-with-dependencies.jar.

# Running
To run, simply execute the JAR in the command line.  If you downloaded the release packages, "run-windows.bat" will work in windows, and "run-linux.sh" will work on Linux.  Linux users need to type "sh run-linux.sh".

If you compiled the project yourself, simply type "java -jar target.jar" in the command line to run the program.
