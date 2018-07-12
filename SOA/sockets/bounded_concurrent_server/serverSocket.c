#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define MAX_CONCURRENT 4

int current_concurrent_clients = 0;
int total = 0;

doService(int fd) {
    int i = 0;
    char buff[80];
    char buff2[80];
    int ret;
    int socket_fd = (int) fd;

    ret = read(socket_fd, buff, sizeof(buff));
    while(ret > 0) {
        buff[ret]='\0';
        sprintf(buff2, "Server [%d] received: %s\n", getpid(), buff);
        write(1, buff2, strlen(buff2));
        ret = write(fd, "caracola ", 8);
        if (ret < 0) {
            perror ("Error writing to socket");
            exit(1);
        }
        ret = read(socket_fd, buff, sizeof(buff));
    }
    if (ret < 0) {
        perror ("Error reading from socket");

    }
    sprintf(buff2, "Server [%d] ends service\n", getpid());
    write(1, buff2, strlen(buff2));

}

doServiceFork(int fd) {
    int pid = fork();

    if(pid == 0)
    {
        ++current_concurrent_clients;
        doService(fd);
        --current_concurrent_clients;
        exit(0);
    }
}



main (int argc, char *argv[])
{
    int socketFD;
    int connectionFD;
    char buffer[80];
    int ret;
    int port;


    if (argc != 2)
    {
        strcpy (buffer, "Usage: ServerSocket PortNumber\n");
        write (2, buffer, strlen (buffer));
        exit (1);
    }

    port = atoi(argv[1]);
    socketFD = createServerSocket (port);
    if (socketFD < 0)
    {
        perror ("Error creating socket\n");
        exit (1);
    }

    while (1) {
        while(current_concurrent_clients >= MAX_CONCURRENT);
        connectionFD = acceptNewConnections (socketFD);
        if (connectionFD < 0)
        {
            perror ("Error establishing connection \n");
            deleteSocket(socketFD);
            exit (1);
        }

        doServiceFork(connectionFD);
    }

}
