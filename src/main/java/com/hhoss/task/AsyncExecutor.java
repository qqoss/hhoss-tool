package com.hhoss.task;

import java.util.concurrent.Executor;

import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.hhoss.util.thread.ThreadPool;

public class AsyncExecutor extends SimpleAsyncTaskExecutor {
	private static final long serialVersionUID = 7779628666068664459L;
	private static final Executor executor = ThreadPool.get();
	private String name ;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void doExecute(Runnable task) {
		if(name==null){
			executor.execute(task);
		}else{
			ThreadPool.get(name).execute(task);
		}
	}

}
