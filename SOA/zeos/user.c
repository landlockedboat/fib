#include <libc.h>

void print_stats(int pid)
{
	char buff[24];
	struct stats st;
	int error = get_stats(pid, &st);
	if (error < 0)
		return;
	itoa(pid,buff);
	write(1, "\nStats for: ", strlen("\nStatus for: "));
	write(1,buff,strlen(buff));
	itoa(st.user_ticks, buff);
	write(1,"\nuser_ticks: ",strlen("\nuser_ticks: "));
	write(1,buff,strlen(buff));
	itoa(st.blocked_ticks, buff);
	write(1,"\nblocked_ticks: ",strlen("\nblocked_ticks: "));
	write(1,buff,strlen(buff));
	itoa(st.ready_ticks, buff);
	write(1,"\nready_ticks: ",strlen("\nready_ticks: "));
	write(1,buff,strlen(buff));
	itoa(st.system_ticks, buff);
	write(1,"\nsystem_ticks: ",strlen("\nsystem_ticks: "));
	write(1,buff,strlen(buff));
	write(1,"\n",strlen("\n"));
}

int fibonacci(int f)
{
	if (f <= 1) return 1;
	else
		return fibonacci(f-1)+fibonacci(f-2);
}

// Workload 1: Sample all CPU bursts
void workload_1()
{
	int i, pid, last_child;
	for( i = 0; i < 2; ++i )
	{
		pid = fork();
		if (pid == 0)
		{ // child	
			if(i == 1) last_child = getpid();
			break;
		}
	}

	fibonacci(35);
	print_stats(getpid());
	if(getpid() == last_child) print_stats(0);
}

// Workload 2: Starvation observation
void workload_2(void)
{
	int i, pid, last_child;
	for( i = 0; i < 2; ++i )
	{
		pid = fork();
		if (pid == 0)
		{ // child	
			if(i == 1) last_child = getpid();
			break;
		}
	}

	if (pid > 0)
	{
		fibonacci(38);
	}
	else
		fibonacci(32);

	print_stats(getpid());
	if(getpid() == last_child) print_stats(0);

}

// Workload 3: Sample all I/O bursts
void workload_3(void)
{
	int i, pid, last_child;
	for( i = 0; i < 2; ++i )
	{
		pid = fork();
		if (pid == 0)
		{
			// child processes	
			if(i == 1) last_child = getpid();
			break;
		}
	}

	read(0,0,500);
	print_stats(getpid());
	if(getpid() == last_child) print_stats(0);
}

// Workload 4: Mixed I/O - CPU bursts
void workload_4(void)
{
	int i, pid, last_child;
	for( i = 0; i < 2; ++i )
	{
		pid = fork();
		if (pid == 0)
		{
			// child processes	
			if(i == 1) last_child = getpid();
			break;
		}
	}

	read(0,0,500);
	fibonacci(33);
	print_stats(getpid());
	if(getpid() == last_child) print_stats(0);
}

// Workload 5: Mixed I/O - CPU bursts with starvation
void workload_5(void)
{
	int i, pid, last_child;
	for( i = 0; i < 2; ++i )
	{
		pid = fork();
		if (pid == 0)
		{
			// child processes	
			if(i == 1) last_child = getpid();
			break;
		}
	}

	int fib_factor = 31;
	if(pid > 0) fib_factor = 32;

	read(0,0,100);
	fibonacci(fib_factor);
	read(0,0,100);
	fibonacci(fib_factor);
	read(0,0,100);
	fibonacci(fib_factor);
	
	print_stats(getpid());
	if(getpid() == last_child) print_stats(0);
}

int __attribute__ ((__section__(".text.main")))
  main(void)
{
	// 0 -> RR
	// 1 -> FCFS
	set_sched_policy(1);
    /* Next line, tries to move value 0 to CR3 register. This register is a privileged one, and so it will raise an exception */
     /* __asm__ __volatile__ ("mov %0, %%cr3"::"r" (0) ); */
	workload_5();
	read(0,0,500000);
	while(1);
}
