/*
 * libc.c 
 */

#include <libc.h>

#include <types.h>

int errno;

void itoa(int a, char *b)
{
  int i, i1;
  char c;
  
  if (a==0) { b[0]='0'; b[1]=0; return ;}
  
  i=0;
  while (a>0)
  {
    b[i]=(a%10)+'0';
    a=a/10;
    i++;
  }
  
  for (i1=0; i1<i/2; i1++)
  {
    c=b[i1];
    b[i1]=b[i-i1-1];
    b[i-i1-1]=c;
  }
  b[i]=0;
}

int strlen(char *a)
{
  int i;
  
  i=0;
  
  while (a[i]!=0) i++;
  
  return i;
}

void perror()
{
  char buffer[256];

  itoa(errno, buffer);

  write(1, buffer, strlen(buffer));
}

int write(int fd, char *buffer, int size)
{
  int result;
  
  __asm__ __volatile__ (
	"int $0x80\n\t"
	: "=a" (result)
	: "a" (4), "b" (fd), "c" (buffer), "d" (size));
  if (result<0)
  {
    errno = -result;
    return -1;
  }
  errno=0;
  return result;
}
 
int gettime()
{
  int result;
  
  __asm__ __volatile__ (
	"int $0x80\n\t"
	:"=a" (result)
	:"a" (10) );
  errno=0;
  return result;
}

int getpid()
{
  int result;
  
  __asm__ __volatile__ (
  	"int $0x80\n\t"
	:"=a" (result)
	:"a" (20) );
  errno=0;
  return result;
}

int fork()
{
  int result;
  
  __asm__ __volatile__ (
  	"int $0x80\n\t"
	:"=a" (result)
	:"a" (2) );
  if (result<0)
  {
    errno = -result;
    return -1;
  }
  errno=0;
  return result;
}

void exit(void)
{
  __asm__ __volatile__ (
  	"int $0x80\n\t"
	:
	:"a" (1) );
}

int yield()
{
  int result;
  __asm__ __volatile__ (
  	"int $0x80\n\t"
	:"=a" (result)
	:"a" (13) );
  return result;
}

int get_stats(int pid, struct stats *st)
{
  int result;
  __asm__ __volatile__ (
  	"int $0x80\n\t"
	:"=a" (result)
	:"a" (35), "b" (pid), "c" (st) );
  if (result<0)
  {
    errno = -result;
    return -1;
  }
  errno=0;
  return result;
}
