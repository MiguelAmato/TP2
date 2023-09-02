package simulator.factories;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;
import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	private static final String TYPE= "new_vehicle";
	
	public NewVehicleEventBuilder() {
		super(TYPE);
	}


	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		JSONArray jav = new JSONArray();
		
		jav = data.getJSONArray("itinerary");
		List<String> itinerary = new SortedArrayList<String>();
		
		for (int i = 0; i < jav.length(); i++) {  
			itinerary.add(jav.getString(i));
		}
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}
