package com.hhoss.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public abstract class EventBusFactory {
	 private static final ConcurrentHashMap<String, EventBusInitiator> CONTAINER = new ConcurrentHashMap<String, EventBusInitiator>();
	 private static final ConcurrentHashMap<String, EventBusInitiator> SYNC_CONTAINER = new ConcurrentHashMap<String, EventBusInitiator>();
	 public static EventBusInitiator getInstance(final String name) {
        if (SYNC_CONTAINER.containsKey(name)) {
            return SYNC_CONTAINER.get(name);
        }
        SYNC_CONTAINER.putIfAbsent(name, new EventBusInitiator());
        return SYNC_CONTAINER.get(name);
	 }
	 public static EventBusInitiator getInstance(final String name,Executor executor) {
        if (CONTAINER.containsKey(name)) {
            return CONTAINER.get(name);
        }
        CONTAINER.putIfAbsent(name, new EventBusInitiator(executor));
	    return CONTAINER.get(name);
	}
	 public static void post(String name,final Object event) {
		 if(SYNC_CONTAINER.containsKey(name)){
		   SYNC_CONTAINER.get(name).post(event);
		 }
		 if(CONTAINER.containsKey(name)){
	       CONTAINER.get(name).post(event);
	    }
	 }
}
