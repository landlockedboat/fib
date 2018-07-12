#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <sys/resource.h>
#include <sys/time.h>


main (int argc, char *argv[])
{
  int connectionFD;
  int ret;
  int r;
  char buff[80];
  char buff2[80];
  char *hostname;
  int port;
  int i,num_it;
  int msec_elapsed;
  struct timeval init_t, end_t;

  gettimeofday(&init_t, NULL);

  if (argc != 4)
    {
      sprintf (buff, "Usage: %s num_it hostname port\n", argv[0]);
      write (2, buff, strlen (buff));
      exit (1);
    }

  num_it = atoi(argv[1]);
  hostname = argv[2];
  port = atoi (argv[3]);
  connectionFD = clientConnection (hostname, port);
  if (connectionFD < 0)
    {
      perror ("Error establishing connection\n");
      exit (1);
    }

  for (i=0; i<num_it; i++) {
	  ret = write (connectionFD, "hola ",5);
	  if (ret < 0)
	  {
		  perror ("Error writing to connection\n");
		  exit (1);
	  }

	  ret = read (connectionFD, buff, sizeof (buff));
	  if (ret < 0)
	  {
		  perror ("Error reading on connection\n");
		  exit (1);
	  }

	  buff[ret] = '\0';
	  sprintf (buff2, "Client [%d] received: %s\n",getpid(), buff);
	  write(1,buff2,strlen(buff2)); 

  }

  gettimeofday(&end_t, NULL);

  sprintf (buff2, "\nClient [%d] finishes\n", getpid());
  write(1,buff2,strlen(buff2));
  struct timeval res_t;
  timersub(&end_t, &init_t, &res_t);
  msec_elapsed = (res_t.tv_sec*1000)+(res_t.tv_usec/1000);
  sprintf(buff2, "Time %d msec\n",msec_elapsed);
  write(2,buff2,strlen(buff2));
  deleteSocket (connectionFD);

}
