package com.ucm.ilsa.veterinaria.business.tratamiento;

import com.ucm.ilsa.veterinaria.business.event.Event;
import com.ucm.ilsa.veterinaria.business.event.IntfEventListener;

public interface IntfTratamiento<T extends Event> extends IntfEventListener {
	public void responseToEvent(T event);
	
}
