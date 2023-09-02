package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;

public abstract class NewRoadBuilder extends Builder<Event>{

	NewRoadBuilder(String type) {
		super(type); 
	}
	
	@Override
	protected abstract Event  createTheInstance(JSONObject data); 
}
