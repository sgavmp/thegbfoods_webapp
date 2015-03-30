package com.ucm.ilsa.veterinaria.event.config;

import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.ucm.ilsa.veterinaria.event.IntfEventListener;
import com.ucm.ilsa.veterinaria.tratamiento.IntfTratamiento;

@Component
public class EventBusFactoryBean implements FactoryBean<EventBus> {

    @Autowired
    private List<IntfEventListener> eventListeners;
    
    @Autowired
    private Executor executor;

    private static EventBus instance;

    @PostConstruct
    public void init() {

        this.instance = new AsyncEventBus(executor);
    
        
        for (IntfEventListener listener : this.eventListeners) {
            this.instance.register(listener);
        }
    }

    public EventBusFactoryBean() {

    }

    public EventBus getObject() throws Exception {
        return this.instance;
    }
    
    public static EventBus getInstance() {
    	return instance;
    }

    public Class<?> getObjectType() {
        return EventBus.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public List<IntfEventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(
            List<IntfEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

}