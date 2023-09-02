package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
 

public class MapByRoadComponent extends JComponent implements TrafficSimObserver{

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final int _JRADIUS = 10;
	
 	private RoadMap map;
	private Controller ctrl;
	
	private int x1;
	private int x2;
	
	 MapByRoadComponent(Controller ctrl) {
		this.ctrl = ctrl;
		this.ctrl.addObserver(this);
		setPreferredSize (new Dimension (300, 200));	
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (map == null || map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		 
		for(int i = 0; i < map.getRoads().size();i++) {
			maxH += 40;
		}
	
		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}
	
	private void drawMap(Graphics g) {
		x1 = 50;
		x2 = getWidth()-100;
		
		for(int i = 0; i < map.getRoads().size();i++) { 
			
			Road actualRoad = map.getRoads().get(i);
			
			int y =  (i+1)*50;
			
			drawRoads(g,actualRoad,y);
			
			drawJunction(g,actualRoad,y);
			
			drawVehicles(g,actualRoad,y);
			
			drawWeather(g,actualRoad,y);
			
			drawCont(g,actualRoad,y);
			
		}
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			this.map = map;
			repaint();
		});
	}
 	
	private void drawRoads(Graphics g,Road actualRoad,int y) {
		g.setColor(Color.black);
		g.drawLine(x1, y, x2, y);
		g.setColor(Color.BLACK);
		g.drawString(actualRoad.getId(), x1-20, y + 4);
	}
	
	private void drawJunction(Graphics g, Road actualRoad, int y) {
		Junction js =  actualRoad.getSrc();
		Junction jd =  actualRoad.getDest();
		
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
	
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(js.getId(),x1 - 4, y - 10);
		
		Color jdColor = _GREEN_LIGHT_COLOR;
		
		if(jd.getGreenLightRoad() == null ||actualRoad != jd.getGreenLightRoad()) {
			jdColor = _RED_LIGHT_COLOR;
		}
		
		g.setColor(jdColor);
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS/ 2, _JRADIUS, _JRADIUS);
		
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(jd.getId(), x2 - 4 , y - 10);
	}
	
	private void drawVehicles(Graphics g,Road actualRoad,int y) {
		for(Vehicle v: actualRoad.getVehicles()) {
			Image car = loadImage("car.png");
			int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) actualRoad.getLength()));
			g.drawImage(car, x, y - 12, 16, 16, this);
			g.setColor(Color.GREEN);
			g.drawString(v.getId(), x + 1, y - 10);
		}
	}
	
	private void drawWeather(Graphics g,Road actualRoad,int y) {
		Image weather = null;
		switch(actualRoad.getWeather()) {
		case SUNNY:
			weather = loadImage("sun.png");
			break;
		case CLOUDY:
			weather = loadImage("cloud.png");
			break;
		case RAINY: 
			weather = loadImage("rain.png");
			break;
		case WINDY: 
			weather = loadImage("wind.png");
			break;
		case STORM:
			weather = loadImage("storm.png");
			break;				
		}
		g.drawImage(weather, x2 + 10, y - 16, 32, 32, this);
	}
	
	private void drawCont(Graphics g,Road actualRoad,int y) {
		Image cont;
		int c = (int) Math.floor(Math.min((double) actualRoad.getTotalCO2()/(1.0 + (double) actualRoad.getContLimit()),1.0) / 0.19);

		cont = 	loadImage("cont_" + c + ".png");
		g.drawImage(cont, x2 + 50, y - 16, 32, 32, this);
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);		
	}

	@Override
	public void onError(String err) {}
	

}
