package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{

	String id; 
	int maxSpeed;
	int contClass;
	List<String> itinerary;
	
	public NewVehicleEvent(int time,String id,int maxSpeed,int contClass,List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;	
 	}

	@Override
	void execute(RoadMap map) {
		List<Junction> list = new ArrayList<Junction>();  
		for(String s: itinerary) {
			list.add(map.getJunction(s));
		}
		Vehicle v = new Vehicle(id, maxSpeed, contClass, list);
		map.addVehicle(v);
		v.moveToNextRoad();	
	}

		@Override
	public String toString() {
		return "New Vehicle '"+ id +"'";
	}

}
