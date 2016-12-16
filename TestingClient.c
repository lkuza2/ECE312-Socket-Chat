#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <pthread.h>
#include <unistd.h>

int sockfd, portno, n;
char buffer[256];
char readbuffer[256];
char name[30];
char message[286];
void error(char *msg) {
	perror(msg);
	exit(0);
}

void *thread_runner(void *x) {
	while(1) {
		bzero(readbuffer, 256);
		n = read(sockfd, readbuffer, 255);
		if (n < 0) {
			error("ERROR reading from socket");
		}
		printf("\n%s\n", readbuffer);
		fflush(stdout);
		if (strcmp("exit", readbuffer)==0){
			exit(0);
		}
		
	}
}

int main(int argc, char *argv[])
{
	
	struct sockaddr_in serv_addr;
	struct hostent *server;
	
	pthread_t thread0;
	if (argc < 3) {
		fprintf(stderr, "usage %s hostname port\n", argv[0]);
		exit(0);
	}
	portno = atoi(argv[2]);
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0)
		error("ERROR opening socket");
	server = gethostbyname(argv[1]);
	if (server == NULL) {
		fprintf(stderr, "ERROR, no such host\n");
		exit(0);
	}
	bzero((char *) &serv_addr, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	bcopy((char *)server->h_addr,
		(char *)&serv_addr.sin_addr.s_addr,
		server->h_length);
	serv_addr.sin_port = htons(portno);
	if (connect(sockfd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
		error("ERROR connecting");
	
	
	if(pthread_create(&thread0, NULL, thread_runner, NULL)) {
		fprintf(stderr, "Error creating a thread\n");
		return 1;
	}
	
	printf("Please enter your name: ");
	scanf("%s", name);
	
	while(1) {
		bzero(buffer, 256);
		fgets(buffer, 255, stdin);
		bzero(message, 286);
		strcat(message, "<");
		strcat(message,name);
		strcat(message, "> ");
		strcat(message, buffer);
		n = write(sockfd, message, strlen(message));
		printf("%s", message);
		if (n < 0)
			error("ERROR writing to socket");
		bzero(buffer, 256);
	}
	
	return 0;
}
	