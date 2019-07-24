package com.scy.utils.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的应用
 * @author sun
 *
 */
public class ThreadPool {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(5));

		for (int i = 0; i < 7; i++) {
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
			Thread.currentThread().sleep(4000);
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