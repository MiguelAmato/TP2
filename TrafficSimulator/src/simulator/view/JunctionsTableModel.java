package simulator.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private Controller ctrl;
	private List<Junction> junctions;
	private String[] colNames = {"Id", "Green","Queues"};

	public JunctionsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		ctrl.addObserver(this);
		junctions = null;
	}
 
	@Override
	public int getRowCount() {
		return junctions == null ? 0 : junctions.size();
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
			s = junctions.get(rowIndex).getId();
			break;
		case 1:
			int index = junctions.get(rowIndex).getGreenLightIndex();
			if(index == -1)
				s = "NONE";
			else 
			s = junctions.get(rowIndex).getInRoads().get(index).getId();
			break;
		case 2:  		
			s = junctions.get(rowIndex).getQueueString();
			break;
		}
		return s;
	}

	public void update() {
		fireTableDataChanged();		
	}
	
	public void setJunctionsList(List<Junction> junctions) {
		this.junctions = junctions;
		update();
	}
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setJunctionsList(map.getJunctions());		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setJunctionsList(map.getJunctions());		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}

}
