package views;

/**
 * "process" elements for queue
 * @author Matthew
 *
 */
public class Process {
	int priority;
	int pid;
	
	public Process(){
	priority = 1;
	pid = 0;
	}
	
	public Process(int p, int id){
		priority = p;
		pid = id;
	}
}
