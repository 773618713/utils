package com.scy.utils.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h1>线程池的应用</h1>
 *
 * <h1>ThreadPoolExecutor 参数说明</h1>
 * <h2>corePoolSize:</h2>
 * 核心线程池的大小。当提交一个任务到线程池时，核心线程池会创建一个核心线程来执行任务，即使其他核心线程能够执行新任务也会创建线程，等到需要执行的任务数大于核心线程池基本大小时就不再创建。
 * 如果调用了线程池的prestartAllCoreThreads() 方法，核心线程池会提前创建并启动所有核心线程。
 *
 * <h2>maximumPoolSize:</h2>
 * 线程池允许创建的最大线程数。如果队列也满了，并且已创建的线程数小于最大线程数，则线程池会再创建新的空闲线程执行任务。
 * 值得注意的是，如果使用了无界的任务队列则这个参数不起作用。
 *
 * <h2>keepAliveTime:</h2>
 * 当线程池中的线程数大于 corePoolSize 时，
 * keepAliveTime为多余的空闲线程等待新任务的最长时间，超过这个时间后多余的线程将被终止。所以，如果任务很多，并且每个任务执行的时间比较短，可以调大时间，提高线程的利用率。值得注意的是，如果使用了无界的任务队列则这个参数不起作用。
 *
 * <h2>TimeUnit:</h2>
 * 线程活动保持时间的单位。
 *
 * <h2>workQueue:</h2>
 * 任务队列。当核心线程池中没有线程时，所提交的任务会被暂存在队列中。Java 提供了多种阻塞队列
 * <https://www.cnblogs.com/jmcui/p/11442616.html>。
 *
 * @author sun
 */
public class ThreadPool {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(50));

		for (int i = 0; i < 10; i++) {
			MyTask myTask = new MyTask(i);
			executor.execute(myTask);
			MyTask2 myTask2 = new MyTask2(i);
			executor.execute(myTask2);
			
			System.out.println("线程池中线程数目：" + executor.getPoolSize() + 
					"，队列中等待执行的任务数目：" + executor.getQueue().size()
					+ "，已执行完别的任务数目：" + executor.getCompletedTaskCount());
		}
		executor.shutdown();
	}

}

class MyTask implements Runnable {
	private int taskNum;

	public MyTask(int num) {
		this.taskNum = num;
	}

	@Override
	public void run() {
		System.out.println("正在执行task " + taskNum);
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("task " + taskNum + "执行完毕");
	}
}


class MyTask2 implements Runnable {
	private int taskNum;

	public MyTask2(int num) {
		this.taskNum = num;
	}

	@Override
	public void run() {
		System.out.println("正在执行task2 " + taskNum);
		try {
			Thread.currentThread().sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("task2 " + taskNum + "执行完毕");
	}
}