package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
    
	private Controller _ctrl;  

	private final int WIDTH = 1000;
	private final int HEIGHT = 650;
    
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
 		
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START); 
		mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END); 

		JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);	
        	
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);

		// tables
		JTable eventsTable = new JTable(new EventsTableModel(_ctrl));
		eventsTable.setShowGrid(false);
		  
		JPanel eventsView = createViewPanel(eventsTable, "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
 		tablesPanel.add(eventsView);

 		JTable vehiclesTable = new JTable(new VehiclesTableModel(_ctrl));
		vehiclesTable.setShowGrid(false);
		 
		JPanel vehiclesView = createViewPanel(vehiclesTable ,"Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
 		tablesPanel.add(vehiclesView);
		
 		JTable roadsTable = new JTable(new RoadsTableModel(_ctrl));
		roadsTable.setShowGrid(false);
		 
		JPanel roadsView = createViewPanel(roadsTable ,"Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
 		tablesPanel.add(roadsView);
 		
 		JTable junctionsTable = new JTable(new JunctionsTableModel(_ctrl));
		junctionsTable.setShowGrid(false);
		 
		JPanel junctionView = createViewPanel(junctionsTable,"Junctions");
		junctionView.setPreferredSize(new Dimension(500, 200));
 		tablesPanel.add(junctionView);
 		
 		// maps
		JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);

		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(_ctrl), "Map by Road");
		mapByRoadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadView);
		 
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(WIDTH, HEIGHT);
		setLocation(s.width/2 - WIDTH /2, s.height/2 - HEIGHT /2);
		this.setVisible(true);
	}
    
	private JPanel createViewPanel(JComponent c, String title) {
		
		Border _defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
		
		JPanel p = new JPanel( new BorderLayout() );
 		p.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title, TitledBorder.LEFT,TitledBorder.TOP));
  		p.add(new JScrollPane(c));
		return p;
	}
}