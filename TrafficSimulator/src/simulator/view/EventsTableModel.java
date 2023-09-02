package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private Controller ctrl;
	
	private List<Event> events;
	private String[] colNames = {"Time", "Desc" };
	
	public EventsTableModel() {}
	
	public EventsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		events = null;
		ctrl.addObserver(this);
 	}
 
	@Override
	public int getRowCount() {
		return events == null ? 0 : events.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = events.get(rowIndex).getTime();
			break;
		case 1:
			s = events.get(rowIndex).toString();
			break;
		}
		return s;
	}
	
	public void update() {
		fireTableDataChanged();		
	}
	
	public void setEventsList(List<Event> events) {
		this.events = events;
		update();
	}
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setEventsList(events);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setEventsList(events);		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setEventsList(events);		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}
}
