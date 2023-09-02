package simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject  {
	private Junction srcJunc, destJunc;
	private int length;
	protected int maxSpeed;
	protected int curSpeedLimit; // constructor
	protected int contLimit;
	protected Weather weather;
	protected int totalPol;
	private List<Vehicle> vehicles;
	
	
	public Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);

		
		if (!(maxSpeed > 0)) 
			throw new IllegalArgumentException("Invalid max speed: " + maxSpeed+"\n");
		if (!(contLimit >= 0)) 
			throw new IllegalArgumentException("Invalid contamination limit: " + contLimit+"\n");
		if (!(length > 0)) 
			throw new IllegalArgumentException("Invalid length: " + length+"\n");
		if (srcJunc == null)
			throw new IllegalArgumentException("Source Junction can't be null\n");
		if (destJunc == null)
			throw new IllegalArgumentException("Destination Junction can't be null\n");
		if (weather == null) 
			throw new IllegalArgumentException("Weather can't be null\n");

		
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.srcJunc.addOutGoingRoad(this);
		this.destJunc.addIncommingRoad(this);
		this.maxSpeed = maxSpeed;
		this.contLimit = contLimit;
		this.length = length;
		this.weather = weather;
		curSpeedLimit = maxSpeed;  
		vehicles = new SortedArrayList<Vehicle>();
	}
	
	void enter(Vehicle v) {
		if (!(v.getLocation() == 0 && v.getSpeed() == 0)) 
			throw new IllegalArgumentException("Not stopped vehicle\n");
		
		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void addContamination(int c) {
		if(c < 0)
			throw new IllegalArgumentException("Negative contamination\n");
		totalPol += c;
	}
	
	void setWeather(Weather w) {
		if (w == null) 
			throw new IllegalArgumentException("Weather can't be null\n");
		weather = w;
	}
	
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(Vehicle i: vehicles) {
			i.setSpeed(calculateVehicleSpeed(i));
			i.advance(time);
		}
		vehicles.sort((Vehicle v1,Vehicle v2) -> v1.compareTo(v2)); 
	}
	
	public JSONObject report() {
		JSONObject jsonRoad = new JSONObject();
		jsonRoad.put("id",_id);
		jsonRoad.put("speedlimit",curSpeedLimit);
		jsonRoad.put("weather",weather.toString());
		jsonRoad.put("co2", totalPol);
		
		JSONArray ja = new JSONArray();
		for(Vehicle i: vehicles) {
			ja.put(i.getId());  				  
		}								 
		jsonRoad.put("vehicles", ja);  
		return jsonRoad;
	}
	
	abstract void reduceTotalContamination(); 
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	public int getLength() {
		return length;
	}
	public Junction getDest() {
		return destJunc;
	}
	public Junction getSrc() {
		return srcJunc;
	}
	public Weather getWeather() {
		return weather;
	}
	public int getContLimit() {
		return contLimit;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public int getTotalCO2() {
		return totalPol;
	}

	public int getSpeedLimit() {
		return curSpeedLimit;
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicles);    
	}
	
}
