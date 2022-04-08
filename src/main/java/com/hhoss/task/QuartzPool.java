package com.hhoss.task;

import org.quartz.simpl.SimpleThreadPool;

import com.hhoss.util.thread.TaskCall;
import com.hhoss.util.thread.ThreadPool;

public class QuartzPool extends SimpleThreadPool {
	@Override
    public boolean runInThread(Runnable runnable) {
		//ThreadPool.run(runnable);
		return super.runInThread(TaskCall.get(runnable));
    }
}
