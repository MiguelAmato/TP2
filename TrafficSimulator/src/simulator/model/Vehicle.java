package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle> {
		
	private List<Junction> itinerary;
	private int maxS;
	private int curS;
	private VehicleStatus state;
	private Road road = null;
	private int location;
	private int contClass; 
	private int totalPol;
	private int distance;
	private int lastJunct;
	
	
	Vehicle(String id,int maxSpeed,int contClass,List<Junction> itinerary) {
		super(id);
 		if(!(maxSpeed > 0))
			throw new IllegalArgumentException("Invalid Speed: "+maxSpeed + "\n");
		else if(!(contClass >= 0 && contClass <= 10))
			throw new IllegalArgumentException("Invalid contamination: "+ contClass + "\n");
		else if((itinerary.size() < 2))
			throw new IllegalArgumentException("Invalid size of itinerary: "+ itinerary.size() + "\n");
		maxS = maxSpeed;
		this.contClass = contClass;
		this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		lastJunct = 0;  
		state = VehicleStatus.PENDING;
	}
	
	@Override
	void advance(int time) {
		if(VehicleStatus.TRAVELING.equals(state)) {
			int c;
			int prevLoc = location;
			if(location + curS < road.getLength()) {
 				location = location + curS;
 			}
			else {
				location = road.getLength();	
			}
			distance = distance + (location - prevLoc);
			
			
			c = contClass * (location - prevLoc);
 			totalPol = totalPol + c;
			road.addContamination(c);
			 
			if(location >= road.getLength()) {
 				state = VehicleStatus.WAITING;
 				curS = 0;
 				itinerary.get(itinerary.indexOf(road.getDest())).enter(this); 
 			}	
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jsonVehicle = new JSONObject();
		
		jsonVehicle.put("id",_id);
		jsonVehicle.put("speed",curS);
		jsonVehicle.put("distance",distance);
		jsonVehicle.put("co2",totalPol);	
		jsonVehicle.put("class",contClass);
		jsonVehicle.put("status",state.toString());
		if(VehicleStatus.TRAVELING.equals(state) || VehicleStatus.WAITING.equals(state)) {
			jsonVehicle.put("road",road.getId());
			jsonVehicle.put("location", location);
		}
		
		return jsonVehicle;
	}

	
	void setSpeed(int s) {
		if(s < 0)
			throw new IllegalArgumentException("Invalid speed " + s + "\n");
		if(VehicleStatus.TRAVELING.equals(state)) {
			if(s < maxS)
				curS = s;
			else 
				curS = maxS;
		}
	}
	
	void setContClass(int c) {
		if(!(c >= 0 && c <= 10))
			throw new IllegalArgumentException("Invalid contamination: "+ c + "\n");
		contClass = c;
	}
	
	void moveToNextRoad() {  
		if(!(VehicleStatus.WAITING.equals(state) || VehicleStatus.PENDING.equals(state)))
			throw new IllegalArgumentException("Invalid vehicle state: "+ state.toString()+ "\n");
	
		if (VehicleStatus.WAITING.equals(state)) {
			road.exit(this);
		}
		if(lastJunct < itinerary.size() - 1) {
			location = 0;
			road = itinerary.get(lastJunct).roadTo(itinerary.get(lastJunct+1));
			road.enter(this);
			state = VehicleStatus.TRAVELING;
		}
		else {
			state = VehicleStatus.ARRIVED;
		}
		lastJunct++;
	}
	
	public int getSpeed() {
		return curS;
	}
	
	public int getLocation() {
		return location;
	}
	public int getMaxSpeed() {
		return maxS;
	}
	public int getContClass() {
		return contClass;
	}
	public VehicleStatus getStatus() {
		return state;
	}
	public int getTotalCO2() {
		return totalPol;
	}
	public List<Junction> getItinerary(){
		return itinerary;
	}
	public Road getRoad() {
		return road;
	}

	public Junction getActualJunction() {
		return road.getDest();
	}
	
	
	
	@Override
	public int compareTo(Vehicle o) {
		 if(this.location < o.location)
			 return 1;
		 else if(this.location == o.location)
			 return 0;
		 else
			 return -1;
	}

	public int getDistance() {
 		return distance;
	}
}
