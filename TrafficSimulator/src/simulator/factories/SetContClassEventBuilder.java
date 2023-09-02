package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{
	
	private static final String TYPE = "set_cont_class";
	
	public SetContClassEventBuilder() {
		super(TYPE);
	}

	protected Event createTheInstance(JSONObject data) {
		int time = data.getInt("time");
		List<Pair<String, Integer>> cs = new ArrayList<>();
		JSONArray jav = new JSONArray();
		jav = data.getJSONArray("info");
		JSONObject jo;
		for (int i = 0; i < jav.length(); i++) {  
			jo = jav.getJSONObject(i);
			Pair<String, Integer> p = new Pair<String, Integer>(jo.getString("vehicle"), jo.getInt("class"));
			cs.add(p);
		}
		return new NewSetContClassEvent(time, cs);
	}

}
