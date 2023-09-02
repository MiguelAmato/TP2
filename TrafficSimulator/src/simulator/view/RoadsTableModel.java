package simulator.view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import extra.jtable.EventEx;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private Controller ctrl;
	private List<Road> roads;
	private String[] colNames = {"Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		roads = null;
 		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return roads == null ? 0 : roads.size();
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
			s = roads.get(rowIndex).getId();
			break;
		case 1:
			s = roads.get(rowIndex).getLength();
			break;
		case 2:
			s = roads.get(rowIndex).getWeather().toString();
			break;
		case 3:
			s = roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s = roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = roads.get(rowIndex).getContLimit();
			break;
		}
		return s;
	}

	public void update() {
		fireTableDataChanged();		
	}
	
	public void setRoadsList(List<Road> roads) {
		this.roads = roads;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setRoadsList(map.getRoads());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}
}
