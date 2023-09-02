package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends Builder<Event>{

	private static final String TYPE= "new_city_road";
	
	
	public NewCityRoadEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		int time = data.getInt("time");
		String id = data.getString("id");
		String src = data.getString("src");
		String dest = data.getString("dest");
		int length = data.getInt("length");
		int co2limit = data.getInt("co2limit");
		int maxSpeed = data.getInt("maxspeed");
		Weather weather = Weather.valueOf(data.getString("weather"));
		
		return new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}

}
