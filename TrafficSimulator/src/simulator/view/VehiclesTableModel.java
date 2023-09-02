package simulator.view;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import extra.jtable.EventEx;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver  {

	private Controller ctrl;
	private List<Vehicle> vehicles;
	private String[] colNames = {"Id", "Location","Itinerary","CO2 Class", "Max.Speed", "Speed","Total CO2","Distance"};

	
	public VehiclesTableModel(Controller _ctrl) {
		ctrl = _ctrl;
		vehicles = null;
		ctrl.addObserver(this);
	}
 
	public void update() {
		fireTableDataChanged();		
	}
	
	public void setVehiclesList(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		update();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public int getRowCount() {
		return vehicles == null ? 0 : vehicles.size();

	}

	@Override
	public int getColumnCount() {
		return colNames .length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = vehicles.get(rowIndex).getId();
			break;
		case 1:
			VehicleStatus stat = vehicles.get(rowIndex).getStatus();
			String showStatus = stat.toString().substring(0,1).toUpperCase() + stat.toString().substring(1).toLowerCase();
			
			if(stat.equals(VehicleStatus.TRAVELING))
				s = vehicles.get(rowIndex).getRoad().getId() +  ":" + vehicles.get(rowIndex).getLocation();
		
			else if(stat.equals(VehicleStatus.WAITING)) 
				s = showStatus + ":" + vehicles.get(rowIndex).getActualJunction();
			
			else
				s = showStatus;
			
			break;
		case 2:
			s = vehicles.get(rowIndex).getItinerary();
			break;
		case 3:
			s = vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			s = vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			s = vehicles.get(rowIndex).getTotalCO2();
			break;
		case 7:
			s = vehicles.get(rowIndex).getDistance();
			break;
		}
	
		return s;
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		setVehiclesList(map.getVehicles());	
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		setVehiclesList(map.getVehicles());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onError(String err) {}	 
}
