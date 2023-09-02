package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	private static final String TYPE= "new_junction";
	
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super(TYPE);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
 	}
	
	protected Event createTheInstance(JSONObject data) {
		int time;
		String id;
		int coorx;
		int coory;
		LightSwitchingStrategy lss;
		DequeuingStrategy dqs;
		JSONArray coor = new JSONArray();
		
		time = data.getInt("time");
		id = data.getString("id");
		
		coor = data.getJSONArray("coor");
		coorx = coor.getInt(0);
		coory = coor.getInt(1);
		
		lss = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		dqs = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		
		return new NewJunctionEvent(time, id, lss, dqs, coorx, coory);
	}

}
