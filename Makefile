CC = gcc

CFLAGS = -Wall -c -g -std=gnu11

## A list of options to pass to the linker. 
LDFLAGS = -Wall -g -pthread

## Name the executable program, list source files
PROG = TestingClient
SRCS = TestingClient.c

## Build the program from the object files (-o option)
$(PROG): TestingClient.o
	$(CC) TestingClient.o -o $(PROG) $(LDFLAGS)

TestingClient.o: TestingClient.c
	$(CC) $(CFLAGS) TestingClient.c

## Remove all the compilation and debugging files
clean: 
	rm -f core $(PROG) TestingClient.o *~
