package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;


public class RoadMap {
	
    private List<Junction> junctionList;  
    private List<Road> roadList;
    private List<Vehicle> vehicleList;
    private Map<String,Junction> junctMap;
    private Map<String,Road> roadMap;
    private Map<String,Vehicle> vehicleMap;
	
	RoadMap() {
		junctionList = new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		vehicleList = new ArrayList<Vehicle>();
		junctMap = new TreeMap<String, Junction>();
		roadMap = new TreeMap<String, Road>();
		vehicleMap = new TreeMap<String, Vehicle>();
	} 
	
	void addJunction(Junction j) {
		if(junctMap.get(j.getId()) != null) {  
			throw new IllegalArgumentException("Invalid Junction: "+ j.getId() + "\n");
		}
		junctionList.add(j);
		junctMap.put(j.getId(), j);
	}
	void addRoad(Road r) {
		if(roadMap.get(r.getId()) != null) {  
			throw new IllegalArgumentException("Invalid Road: "+ r.getId() + "\n");
		}
		if(junctMap.get(r.getDest().getId()) == null || junctMap.get(r.getSrc().getId()) == null) { 
			throw new IllegalArgumentException("Invalid Road: "+ r.getId() + "\n");
		}
		roadList.add(r);
		roadMap.put(r.getId(), r);
	}
	void addVehicle(Vehicle v) {
		if(vehicleMap.get(v.getId()) != null)
			throw new IllegalArgumentException("Invalid Vehicle "+ v.getId() + "\n");
		int i = 0;
		while(i < v.getItinerary().size() - 1) {
			if(v.getItinerary().get(i).roadTo(v.getItinerary().get(i+1)) == null) {  
				throw new IllegalArgumentException("Invalid Itinerary \n");
			}
			i++;
		}
		
		vehicleList.add(v);
		vehicleMap.put(v.getId(), v);
	}	
	
	public Junction getJunction(String id) {
		return junctMap.get(id);
	}
	
	
	public Road getRoad(String id) {
		return roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehicleMap.get(id);
	}
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionList);  
	}
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadList);  
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicleList);  
	}
	
	void reset() {
		junctionList.clear();
		roadList.clear();
		vehicleList.clear();
		junctMap.clear();
		roadMap.clear();
		vehicleMap.clear();
	}
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray jaj = new JSONArray();
		JSONArray jar = new JSONArray();
		JSONArray jav = new JSONArray();
 		
		for(Junction j : junctionList) {
			jaj.put(j.report());
		}
		
		for(Road r : roadList) {
			jar.put(r.report());
		}
		
		for(Vehicle v : vehicleList) {
			jav.put(v.report());
		}
		
		jo.put("junctions", jaj);
		jo.put("roads", jar);
		jo.put("vehicles", jav);
		
		return jo;
	}
}
