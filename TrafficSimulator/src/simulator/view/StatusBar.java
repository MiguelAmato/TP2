package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
 
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private Controller ctrl; 
	int ticks;
	String eventString;
	JLabel time,event;
 	
	
	public StatusBar(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		time = new JLabel("Time: " + ticks);
		
		if(eventString == null) {
			eventString = "Welcome!";
		}
		event = new JLabel(eventString);
		event.setForeground(Color.BLACK);

		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setPreferredSize(new Dimension(100, 0));
		
		add(time);
		add(separator);
		add(event);
 	}
	
	private void update(int time, Event e){
		event.setForeground(Color.BLACK);
		ticks = time;
		if(e != null)
			eventString = ("Event added (" + e.toString() + ")");
		else {
			eventString = "Welcome!";
		}
		this.time.setText("Time: " + ticks);
		event.setText(eventString);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
 		event.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.time.setText("Time: " + time);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
 		update(time,e);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
 		update(0,null);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {
 		event.setForeground(Color.red);
 		event.setText("ERROR: " + err);
 	}
}
