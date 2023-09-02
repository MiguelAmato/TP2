package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{

	List<Pair<String,Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws == null)
			throw new IllegalArgumentException("Invalid weather list\n");
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		 for(Pair<String,Weather> w: ws) {
			 if(map.getRoad(w.getFirst()) == null)
				 throw new IllegalArgumentException("Invalid road\n");
			 map.getRoad(w.getFirst()).setWeather(w.getSecond());
		 }
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Change Weather: [");
		for(int i = 0; i < ws.size();i++) {
			sb.append("(" + ws.get(i).getFirst() + "," + ws.get(i).getSecond() + ")");
			if(i < ws.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
