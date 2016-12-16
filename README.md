# ECE312-Socket-Chat

This branch contains the client portion of the Socket-Chat program. The socket chat client is written in C, and is threaded using POSIX threads so that it can read and write to the server at the same time.  Group chat is supported, and recieving an "exit" command from any client will kill this client and all others. C11 standard C was used.

# Compiling

A Makefile is included, so on systems where C11 is the default, simply type "make" in the command line.

# Running

Execute the compiled binary file in the command line "./client"
