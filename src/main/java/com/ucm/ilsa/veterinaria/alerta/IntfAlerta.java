package com.ucm.ilsa.veterinaria.alerta;

import com.ucm.ilsa.veterinaria.event.Event;
import com.ucm.ilsa.veterinaria.event.IntfEventListener;

public interface IntfAlerta<T extends Event> extends IntfEventListener {
	public void responseToEvent(T event);
}
