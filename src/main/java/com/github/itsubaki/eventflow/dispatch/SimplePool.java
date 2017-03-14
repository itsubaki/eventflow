package com.github.itsubaki.eventflow.dispatch;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimplePool {
	protected final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();
	private SimpleThread[] thread;
	private AtomicBoolean close = new AtomicBoolean(false);

	public SimplePool(int size) {
		thread = new SimpleThread[size];
	}

	public void execute(Runnable task) {
		tasks.offer(task);
	}

	public void start() {
		for (int i = 0; i < thread.length; i++) {
			thread[i] = new SimpleThread();
			thread[i].start();
		}
	}

	public void close() {
		close.set(true);
	}

	public boolean isClosed() {
		return close.get();
	}

	public class SimpleThread extends Thread {

		@Override
		public void run() {
			while (!isClosed()) {
				Runnable task = tasks.poll();
				if (task == null) {
					continue;
				}

				task.run();
			}
		}

	}

}