/**
 * cd D:/Work/Projects/workspace/CardGamesAPI
 * javac src/test/java/nl/knikit/cardgames/training/MyThread.java
 * execute with: java -cp src/test/java nl.knikit.cardgames.training.MyThread
 * 1 Thread is class, Runnable is interface that only has run().
 * 2 override the run method, when start is called, run will be executed
 * 3/4 this will print the same counter with no. 1
 * 5/6 this will print counter 1 and 2 since you pass the same object
 * 7/8 java 1.0 (fire and forget) vs java 1.5 (returns a value)
 */
package nl.knikit.cardgames.training;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class MyThread extends Thread{ // 1
	private int counter = 0;
	@Override // overriden from Runnable, which Thread implements
	public void run(){ counter++; System.out.println("MyThread is running, counter " + counter); } // 2
	public static void main(String args[]) throws InterruptedException, ExecutionException {
		MyThread thread1   = new MyThread(); thread1.start(); Thread.sleep(1000); // 3
		MyThread thread2   = new MyThread(); thread2.start(); Thread.sleep(1000); // 4
		MyThread myThread = new MyThread();
		Thread newThread1   = new Thread(myThread); newThread1.start(); Thread.sleep(1000); // 5
		Thread newThread2   = new Thread(myThread); newThread2.start(); Thread.sleep(1000); // 6
		MyRunnable myRunnable= new MyRunnable(); myRunnable.startMe();
		// creating thread pool to execute task which implements Callable
		ExecutorService es = Executors.newFixedThreadPool(1);
		MyCallable myCallable = new MyCallable();
		// just take a task and return a Future object now or when its done
		Future future = es.submit(myCallable); // make a list to sumbit all tasks
		// get will block until result is available so use future.isDone() to check
		future.get();
	}
}
class MyRunnable implements Runnable{ // 7
	private int counter = 0;
	@Override
	public void run(){ counter++; System.out.println("MyRunnable is running, counter " + counter); }
	public void startMe(){
		MyRunnable m1= new MyRunnable(); // or incorporate this in the next line
		Thread thread  = new Thread(m1); // make your own Thread from a runnable
		thread.start(); // 4
	}
}
class MyCallable implements Callable<String>{ // 8
	private int counter = 0;
	@Override
	public String call(){ counter++; System.out.println("MyCallable is called, counter " + counter);
						return String.valueOf(counter); }
}