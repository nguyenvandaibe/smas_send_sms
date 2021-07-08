/*
 *	Copyright (c)  2014. All rights reserved.
 * 
 *  $Author: doanhcdm $
 */

package vn.com.viettel.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KThreadPoolExecutor {

	private static final int POOL_SIZE = 500;

	private static final int MAX_POOL_SIZE = 1000;

	private static final long KEEP_ALIVE_TIME = 10;

	/** The thread pool. */
	private static ThreadPoolExecutor threadPool;

	private static boolean isThreadEnable = true;

	static {
		threadPool = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		threadPool.allowCoreThreadTimeOut(true);
	}

	/**
	 * Run task.
	 * 
	 * @param task
	 *            the task
	 */
	public static void execute(Runnable task) {
		if (isThreadEnable) {
			threadPool.execute(task);
		} else {
			task.run();
		}
	}

	/**
	 * Shut down.
	 */
	public static void shutDown() {
		threadPool.shutdown();
	}

	public static void setThreadEnable(boolean isThreadEnable) {
		KThreadPoolExecutor.isThreadEnable = isThreadEnable;
	}
}
