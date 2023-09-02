package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{

	List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs == null)
			throw new IllegalArgumentException("Invalid contClass\n");
		this.cs = cs;
	}
	@Override
	void execute(RoadMap map) {
		for(Pair<String,Integer> c : cs) {
			if(map.getVehicle(c.getFirst()) == null)
				throw new IllegalArgumentException("Invalid contClass\n");
			map.getVehicle(c.getFirst()).setContClass(c.getSecond());
		}
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Change CO2 class: [");
		for(int i = 0; i < cs.size();i++) {
			sb.append("(" + cs.get(i).getFirst() + "," + cs.get(i).getSecond() + ")");
			if(i < cs.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
