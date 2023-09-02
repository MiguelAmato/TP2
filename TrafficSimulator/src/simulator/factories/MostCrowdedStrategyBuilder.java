package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	private final static String TYPE = "most_crowded_lss";
	
	public MostCrowdedStrategyBuilder() {
		super(TYPE);
 	}

 	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot;
		if(!data.has("timeslot"))
			timeslot = 1;
		else 
			timeslot = data.getInt("timeslot");
		
 		return new MostCrowdedStrategy(timeslot);
 	}

}
