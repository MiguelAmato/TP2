package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap roadMap;
	private List<Event> events;  
	private int time;
		
	private List<TrafficSimObserver> observers;
	
	public TrafficSimulator() {
		events = new SortedArrayList<Event>();
		roadMap = new RoadMap();
		observers = new ArrayList<>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
 		events.sort((Event e1,Event e2)-> e1.compareTo(e2));
 		
 		for(TrafficSimObserver o: observers){
 			o.onEventAdded(roadMap, events, e, time);
 		}	
	}
	
	public void advance(){

		time++;
		for(TrafficSimObserver o: observers){
			o.onAdvanceStart(roadMap, events, time);
		}
		List<Event> aux = new SortedArrayList<Event>();
		try {
			for(Event e: events) {
				if(e.getTime() == time) {
					e.execute(roadMap);
				}
				else {
					aux.add(e);
				}		
			}
			events = aux;
			
			for(Junction j: roadMap.getJunctions()) {
				j.advance(time);
			}
			for(Road r: roadMap.getRoads()){
				r.advance(time);
			}
		}
		catch(Exception e) {
			for(TrafficSimObserver o: observers){
				o.onError(e.getMessage());
			}
			throw e;
 		}
		
		finally {
			for(TrafficSimObserver o: observers){
				o.onAdvanceEnd(roadMap, events, time);
			}
		}
	}

	public void reset() {
		roadMap.reset();
		time = 0;
		events.clear();
		for(TrafficSimObserver o: observers){
			o.onReset(roadMap, events, time);
		}
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		jo.put("time",time);
		jo.put("state", roadMap.report());
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		for(TrafficSimObserver ob: observers){
			ob.onRegister(roadMap, events, time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	} 
}
