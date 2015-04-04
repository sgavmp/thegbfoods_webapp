package com.ucm.ilsa.veterinaria.business.alerta;

import com.ucm.ilsa.veterinaria.business.event.Event;
import com.ucm.ilsa.veterinaria.business.event.IntfEventListener;

public interface IntfAlerta<T extends Event> extends IntfEventListener {
	public void responseToEvent(T event);
}
