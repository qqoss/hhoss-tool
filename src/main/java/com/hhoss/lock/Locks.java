package com.hhoss.lock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import com.hhoss.Logger;
import com.hhoss.aspi.Factory;
import com.hhoss.aspi.SPIManager;
import com.hhoss.boot.App;

public class Locks {
	private static final Logger logger = Logger.get();
	private static Factory<String,Lock> factory ;
	private Lock lock;

	/**
	 * @param caller 获取到锁后的可执行任务
	 * @return value from caller
	 * @throws Exception
	 */
	public <T> T doInLock(Callable<T> caller) throws Exception{
		T result = null;
		if (lock.tryLock())try {
			result = caller.call();
		} finally {
			lock.unlock();
		}
		return result;
	}
	/**
	 * @param rank 锁分类类型
	 * @param code 不可并发的任务，唯一编码
	 */
	public static Locks get(Lock lock) {
		Locks locks = new Locks();
		locks.lock = lock;
		return locks;
	}
	
	public static Locks get(int rank, String code) {
		//Lock lock = new RowLock(rank, code);		
		Lock lock = createLock(rank, code);	
		return get(lock);
	}

	public static <T> T doInLock(Lock lock, Callable<T> caller) throws Exception{
		return get(lock).doInLock(caller);
	}

	/**
	 * @param rank 锁分类类型
	 * @param code 不可并发的任务，唯一编码
	 * @param caller 获取到锁后的可执行任务
	 * @return value from caller
	 * @throws Exception
	 */
	public static <T> T run(int rank, String code, Callable<T> caller) throws Exception {
		return get(rank,code).doInLock(caller);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Lock createLock(Object... params){
		//Lock lock = new RowLock(rank, code);		
		if(factory==null)try{
			String spiName = App.getProperty("res.app.hhoss.tool", "api.locker.factory.name");
			if(spiName==null){spiName="spi.locker.dbms.rowlock";}
			for(Entry<Factory,Collection> ent :SPIManager.getFactories().entrySet() ){
				if(ent.getValue().contains(spiName)){
					factory = ent.getKey();
					return factory.get(spiName, params);
				}
			}
			logger.warn("can't get Factory[{}].",spiName);
		}catch(Exception e){
			logger.warn("Fail when get Locker Provider for[{}].",Arrays.toString(params),e);
		}
		return null;
	}
}
