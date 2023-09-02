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
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ChangeRoadWeatherDialog extends JDialog implements TrafficSimObserver{


	Controller ctrl;

    private final int WIDTH = 500;
    private final int HEIGHT = 200;

    private final String TEXT = "   Schedule an event to change the weather of a road after given \n" + "   number of simulation ticks from now.";
    private final String TITLE = "Change Road Weather";

    
    private JTextArea text;
    
    private JToolBar tb; 
    private JLabel roadLabel,weatherLabel,ticksLabel;
    private JComboBox<String>roadBox;
    private JComboBox<Weather> weatherBox;
    private JSpinner ticksSpinner;
    
    private JPanel buttonPanel;
    private JButton cancel, ok;

	private int time;

    public ChangeRoadWeatherDialog(Controller ctrl){
         this.ctrl = ctrl;
         this.ctrl.addObserver(this);
         initGUI();
    }

    private void initGUI(){
    	setTitle(TITLE);
    	setModal(true);
    	
    	initContentPane();

        textConfiguration();
        toolBarConfiguration();
        buttonsConfiguration();

        add(text);
        add(tb);
        add(buttonPanel);
    }
    
    private void initContentPane() {
        setLayout(new GridLayout(3,0));
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
    }

    private void textConfiguration(){
        text = new JTextArea(TEXT); 
        text.setBackground(Color.WHITE);
        text.setEditable(false);
    }
    
    private void toolBarConfiguration(){
		tb = new JToolBar();
		tb.setFloatable(false);
		tb.setBackground(Color.WHITE);
		tb.setBorderPainted(false);
		tb.setBorder(new EmptyBorder(10,10,10,10));
		
        roadLabel = new JLabel("Road: ");
        weatherLabel = new JLabel("Weather: ");
        ticksLabel = new JLabel("Ticks: ");
               
        weatherBox = new JComboBox<Weather>(Weather.values());
        
        SpinnerNumberModel tickModel = new SpinnerNumberModel(1, 0, 100, 1);
        ticksSpinner = new JSpinner(tickModel);
        
        tb.add(roadLabel);
        tb.add(roadBox);
        tb.addSeparator();
        tb.add(weatherLabel);
        tb.add(weatherBox);
        tb.addSeparator();
        tb.add(ticksLabel);
        tb.add(ticksSpinner);
    }

    private void buttonsConfiguration(){
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        cancel = new JButton("Cancel");
        ok = new JButton("OK");

        actions();
        
        buttonPanel.add(cancel);
        buttonPanel.add(new JSeparator());
        buttonPanel.add(new JSeparator());
        buttonPanel.add(ok);
    }
    
    private void actions() {
        cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                 ChangeRoadWeatherDialog.this.dispose();
			}
        });

        ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<Pair<String,Weather>> weatherList = new ArrayList<>();				
					weatherList.add(new Pair<String,Weather>(roadBox.getSelectedItem().toString(),(Weather) weatherBox.getSelectedItem()));
					ctrl.addEvent(new SetWeatherEvent((Integer)ticksSpinner.getValue()+time,weatherList));
				}
				catch (NullPointerException iae) {
							JOptionPane.showMessageDialog(null, "Complete each field", "Error", JOptionPane.DEFAULT_OPTION);
				}
				ChangeRoadWeatherDialog.this.dispose();
			}
        });
    }

    private void update(RoadMap map) { 
		String[] roadsList = new String[map.getRoads().size()];
		List<Road> roads = map.getRoads();
		  for (int i = 0; i < roads.size(); ++i) {
	            roadsList[i] = roads.get(i).getId();
	      }
	
		roadBox = new JComboBox<String>();
		roadBox.setModel(new DefaultComboBoxModel<String>(roadsList));
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