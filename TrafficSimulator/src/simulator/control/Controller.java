package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if (sim == null) {
			throw new IllegalArgumentException("Invalid Traffic Simulator\n");
		}
		if (eventsFactory == null) {
			throw new IllegalArgumentException("Invalid Events Factory\n");
		}
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if (!jo.has("events")) {
			throw new IllegalArgumentException("Invalid input\n");
		}
		JSONArray ja = jo.getJSONArray("events");  
		for (int i = 0; i < ja.length(); i++) {
			sim.addEvent(eventsFactory.createInstance(ja.getJSONObject(i)));
		}
		try {
			in.close();
		} catch (IOException e) {
 			e.printStackTrace();
		}
	}
	
	public void run(int n, OutputStream out) {
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		for (int i = 0; i < n; i++) {
			sim.advance();
			ja.put(sim.report());
		}
		jo.put("states", ja);
		 PrintStream p = new PrintStream(out);
		 p.print(jo.toString());  
	}
	
	public void run(int n) {
		for (int i = 0; i < n; i++) {
			sim.advance();
		}
	}
	
	public void reset() {
		sim.reset();  
	}
	
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}

	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}

	public void addEvent(Event e) {
		sim.addEvent(e);
	}

	
	
}
