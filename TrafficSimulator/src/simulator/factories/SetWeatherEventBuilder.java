package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {
	
	private static final String TYPE = "set_weather";
	
	public SetWeatherEventBuilder() {
		super(TYPE);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		List<Pair<String, Weather>> ws = new ArrayList<>();
		JSONArray jav = new JSONArray();
		jav = data.getJSONArray("info");
		JSONObject jo;
		for (int i = 0; i < jav.length(); i++) {  
			jo = jav.getJSONObject(i);
			Pair<String, Weather> p = new Pair<String, Weather>(jo.getString("road"), Weather.valueOf(jo.getString("weather")));
			ws.add(p);
		}
		return new SetWeatherEvent(time, ws);
	}
	
}
