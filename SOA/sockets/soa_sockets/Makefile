all: serverSocket clientSocket launchClient

launchClient: launchClient.c
	gcc -o launchClient launchClient.c

serverSocket: serverSocket.c socketMng.o
	gcc -pthread -o serverSocket -g serverSocket.c socketMng.o

clientSocket: clientSocket.c socketMng.o
	gcc -o clientSocket -g clientSocket.c socketMng.o

socketMng.o: socketMng.c
	gcc -c -g socketMng.c

clean: 
	rm -f serverSocket clientSocket socketMng.o launchClient
