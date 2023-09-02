package simulator.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog implements TrafficSimObserver{
	
	private final int WIDTH = 500;
	private final int HEIGHT = 200;
	
	private int time;
	
	private final String TEXT = "   Schedule an event to change the CO2 class of a vehicle after given \n   number of simulation ticks from now.";
	private final String TITLE = "Change CO2 Class";
	
	private Controller ctrl;
	
	private JTextArea text;
	
	private JToolBar tb;
	private JLabel vehicleLabel, CO2Label, ticksLabel;
	private JComboBox<String> vehiclesComboBox; 
	private JComboBox<Integer> CO2ComboBox;
	private JSpinner ticksSpinner;  
	
	private JPanel panel;
	private JButton cancel, ok;
	
	public ChangeCO2ClassDialog(Controller ctrl) {
		 this.ctrl = ctrl;
		 ctrl.addObserver(this);
		 initGUI();
	}

	private void initGUI() {
		
		setTitle(TITLE);
		setModal(true);
		
		initContentPane();
		
		textConfiguration();
		toolBarConfiguration();
		buttonsConfiguration();
		
		add(text);
		add(tb);
		add(panel);
	}
	
	private void initContentPane() {
		setLayout(new GridLayout(3, 0));
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
	}
	
	private void textConfiguration() {
		text = new JTextArea(TEXT);
		text.setEditable(false);
	}
	 
	private void toolBarConfiguration() {
		tb = new JToolBar();
		tb.setFloatable(false);
		tb.setBackground(Color.WHITE);
		tb.setBorderPainted(false);
		tb.setBorder(new EmptyBorder(10,10,10,10));
		
		vehicleLabel = new JLabel("Vehicle: ");
		CO2Label = new JLabel("CO2 Class: ");
		ticksLabel = new JLabel("Ticks: ");	
		
  		
		Integer co2List[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; 
		CO2ComboBox = new JComboBox<Integer>(co2List);
		
 		SpinnerNumberModel tickModel = new SpinnerNumberModel(1, 0, 100, 1);
		ticksSpinner = new JSpinner(tickModel);
		
		tb.add(vehicleLabel);
		tb.add(vehiclesComboBox);
		tb.addSeparator();
		tb.add(CO2Label);
		tb.add(CO2ComboBox);
		tb.addSeparator();
		tb.add(ticksLabel);
		tb.add(ticksSpinner);
	}
	
	private void buttonsConfiguration() {
		panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		cancel = new JButton("Cancel");
		
		ok = new JButton("Ok");
		
		actions();
		
		panel.add(cancel);
		panel.add(new JSeparator());
		panel.add(new JSeparator());
		panel.add(ok);
	}
	
	private void actions() {
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.dispose();
			}
		});
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
	 				List<Pair<String,Integer>> csList = new ArrayList<>();				
					csList.add(new Pair<String,Integer>(vehiclesComboBox.getSelectedItem().toString(),(Integer)CO2ComboBox.getSelectedItem()));
					ctrl.addEvent(new NewSetContClassEvent((Integer)ticksSpinner.getValue()+time,csList));
				}
				catch (NullPointerException iae) {
  						JOptionPane.showMessageDialog(null, "Complete each field", "Error", JOptionPane.DEFAULT_OPTION);
				}
				ChangeCO2ClassDialog.this.dispose();
			}
		});
	}

	private void update(RoadMap map) { 
 		String[] vehicleList = new String[map.getVehicles().size()];
		List<Vehicle> vehicles = map.getVehicles();
		  for (int i = 0; i < vehicles.size(); ++i) {
	            vehicleList[i] = vehicles.get(i).getId();
	      }
	
		vehiclesComboBox = new JComboBox<String>();
		vehiclesComboBox.setModel(new DefaultComboBoxModel<String>(vehicleList));
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);	
		this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
		this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
		this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);	
		this.time = time;
	}

	@Override
	public void onError(String err) {}
	
}