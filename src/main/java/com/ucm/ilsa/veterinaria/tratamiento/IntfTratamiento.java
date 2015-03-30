package com.ucm.ilsa.veterinaria.tratamiento;

import com.ucm.ilsa.veterinaria.event.Event;
import com.ucm.ilsa.veterinaria.event.IntfEventListener;

public interface IntfTratamiento<T extends Event> extends IntfEventListener {
	public void responseToEvent(T event);
	
}
