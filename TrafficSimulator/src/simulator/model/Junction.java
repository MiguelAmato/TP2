package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> inRoads;
	private Map<Junction,Road> outRoads;
	private List<List<Vehicle>> queueList;
	private Map<Road,List<Vehicle>>queueRoad;
	private int currGreen;
	private int lastSwitchingTime = 0;
	private LightSwitchingStrategy lightStrat;
	private DequeuingStrategy dequeStrat;
	private int x;
	private int y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		if(lsStrategy == null)
			throw new IllegalArgumentException("lsStrategy is null\n");
		if(dqStrategy == null)
			throw new IllegalArgumentException("dqStrategy is null\n");
		if(xCoor < 0)
			throw new IllegalArgumentException("xCoor is negative\n");
		if(yCoor < 0)
			throw new IllegalArgumentException("yCoor is negative\n");
		
		inRoads = new ArrayList<>();
		outRoads = new HashMap<Junction, Road>();
		queueList = new ArrayList<List<Vehicle>>();
		queueRoad = new HashMap<Road, List<Vehicle>>();
		currGreen = -1;
		this.lightStrat = lsStrategy;
		this.dequeStrat = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
	}


	@Override
	void advance(int time) {
		List<Vehicle> dequeueList;
		if(currGreen != -1) {
			dequeueList = dequeStrat.dequeue(queueList.get(currGreen));
			for(Vehicle v: dequeueList) {
				v.moveToNextRoad();
				queueList.get(currGreen).remove(v);
			}
		}
 		int nextGreen = lightStrat.chooseNextGreen(inRoads, queueList, currGreen, lastSwitchingTime, time);  
		if(currGreen != nextGreen) {
			currGreen = nextGreen;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {  
		JSONObject jsonJunc = new JSONObject();
		jsonJunc.put("id", _id);
		if(currGreen == -1) {
			jsonJunc.put("green", "none");
		}
		else {
			jsonJunc.put("green", inRoads.get(currGreen).getId()); 
			} 
		JSONArray ja = new JSONArray();
		
		for (Road r : inRoads) {
			JSONObject jo = new JSONObject();
			jo.put("road" , r.getId());
			int i = inRoads.indexOf(r);
			JSONArray jav = new JSONArray();
			for ( Vehicle v : queueList.get(i)) {
				jav.put(v.getId());
			}
			jo.put("vehicles", jav);
			ja.put(jo);
		}
		jsonJunc.put("queues",ja);
		return jsonJunc;
	}
	void addIncommingRoad(Road r) {
		if(!(r.getDest() == this))
			throw new IllegalArgumentException("Invalid road\n");
		inRoads.add(r);
		List<Vehicle> queue = new LinkedList<Vehicle>();	 
		queueList.add(queue);
		queueRoad.put(r, queue);
	}
	void addOutGoingRoad(Road r) {    
		if(!(r.getSrc() == this)) {
			throw new IllegalArgumentException("Invalid road\n");
		}
		if((outRoads.containsKey(r.getDest()))){
			throw new IllegalArgumentException("Invalid road\n");
		}
		outRoads.put(r.getDest(),r);
	}
	void enter(Vehicle v) {
		queueRoad.get(v.getRoad()).add(v);
 	}
	Road roadTo(Junction j) {
		return outRoads.get(j);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getGreenLightIndex() {
		return currGreen;
	}
	
	public Road getGreenLightRoad() { // ESTE METODO LO HEMOS CREADO AL HACER LA VISTA
		if(currGreen == -1)
			return null;
		return inRoads.get(currGreen);
	}

	public String getQueueString(){
	
		StringBuilder str = new StringBuilder();
		for(Road r: inRoads) {
			str.append(r.getId() + ":");
			str.append(queueRoad.get(r) + " ");
			
		}
		return str.toString();
	}

	public List<Road> getInRoads() {
		 return inRoads;
	}
}
