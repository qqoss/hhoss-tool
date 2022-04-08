package com.hhoss.event;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class EventBusInitiator {
    private final EventBus instance;
    private final ConcurrentHashMap<String,EventListener> listeners = new ConcurrentHashMap<String,EventListener>();
    public EventBusInitiator(){
    	instance = new EventBus();
    }
    public EventBusInitiator(Executor executor){
    	instance=new AsyncEventBus(executor);
    }
    public void post(final Object event) {
        if (listeners.isEmpty()) {
            return;
        }
        instance.post(event);
    }
    public void register(final EventListener listener) {
        if (null != listeners.putIfAbsent(listener.getName(), listener)) {
            return;
        }
        instance.register(listener);
    }
    public synchronized void clearListener() {
        for (EventListener each : listeners.values()) {
            instance.unregister(each);
        }
        listeners.clear();
    }
}
