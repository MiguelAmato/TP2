package simulator.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
 
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;

public class ControlPanel extends JToolBar {
	
	private Controller ctrl;  
	private boolean stopped = false;
	
	private JButton loadEvent, changeCO2Class, changeRoadWeather, stop, execute, exit;
	private JLabel tickInfo;
	private JSpinner spinner;
	private ImageIcon loadIcon, contIcon, weatherIcon, executeIcon, stopIcon, exitIcon;
		
	public ControlPanel(Controller _ctrl) {
 		ctrl = _ctrl;
		initGUI();
	}
 	
	private void initGUI() {
		setFloatable(false);
		loadImages();
		
		// Boton para cargar un fichero de eventos
		loadEvent = new JButton(loadIcon);

		// Boton para cambiar la ContClass de un vehiculo
		changeCO2Class = new JButton(contIcon);
		
		// Boton para cambiar las condiciones atmosfericas de una carretera
		changeRoadWeather = new JButton(weatherIcon);
		
		// Boton para ejecutar
		execute = new JButton(executeIcon);	
		
		// Boton para detener la simulacion
		stop = new JButton(stopIcon);

		// Etiqueta de ticks
		tickInfo = new JLabel("  Ticks:   ");

		//Spinner para elegir ticks
		spinner = ticksSpinner();

		// Boton de salir del programa
		exit = new JButton(exitIcon);

		// Action listeners de todos los botones
		actions();

		// Add de todos los componentes
		add(loadEvent);
		addSeparator();
		add(changeCO2Class);
		add(changeRoadWeather);
		addSeparator();
		add(execute);
		add(stop);
		add(tickInfo);
		add(spinner);
		add(Box.createHorizontalGlue());
		add(exit);
	}

	private void run_sim(int n) {
 		if (n > 0 && !stopped) {
			try {
				ctrl.run(1);
			}
			catch (Exception e) {
 				stopped = true;
				enableToolBar(true);
				return;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} 
		else {
			enableToolBar(true);
			stopped = true;
		}
	}

	private void actions() {
		loadEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("resources/examples");
				jfc.setDialogTitle("Choose a directory to load your file: ");
				jfc.addChoosableFileFilter(new FileNameExtensionFilter("JSON document", "json"));
 				jfc.setAcceptAllFileFilterUsed(true);

				int returnValue = jfc.showOpenDialog(null);
 
				if (returnValue == JFileChooser.APPROVE_OPTION) {
 					InputStream selectedFile = null;
					try {
						selectedFile = new FileInputStream(jfc.getSelectedFile().getPath());
					} 
					catch (FileNotFoundException e1) {
  						JOptionPane.showMessageDialog(null, "File not found", "File Error", JOptionPane.DEFAULT_OPTION);
					}
					
					ctrl.reset();
					try {
						ctrl.loadEvents(selectedFile);
					}
					catch (IllegalArgumentException iae) {
  						JOptionPane.showMessageDialog(null, "Invalid File", "File Error", JOptionPane.DEFAULT_OPTION);
 					}
				}
				
				else if (returnValue == JFileChooser.ERROR_OPTION) {
				  JOptionPane.showMessageDialog(null, "File could not be open", "File Error", JOptionPane.DEFAULT_OPTION);
				}
			}
		});
		
		changeCO2Class.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog CO2Dialog = new ChangeCO2ClassDialog(ctrl);
				CO2Dialog.setVisible(true);
			}
		});

		changeRoadWeather.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JDialog roadWeather = new ChangeRoadWeatherDialog(ctrl);
				roadWeather.setVisible(true);
			}
		});

		execute.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				stopped = false;
 				enableToolBar(stopped);
				run_sim((Integer)spinner.getValue());
			}

		});

		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				stopped = true;
				enableToolBar(stopped);
 			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
 				int option = exitOptionDialog();
 				if(JOptionPane.YES_OPTION == option)
 					System.exit(0);
	 			else if(option == JOptionPane.NO_OPTION) {
	 				JOptionPane.getRootFrame().dispose();
	 			}
			}
		});
	}

	JSpinner ticksSpinner() {
		SpinnerNumberModel sp = new SpinnerNumberModel(0, 0, 999, 1);  
		JSpinner spinner = new JSpinner(sp); 
		spinner.setMaximumSize(new Dimension(75, 50));
		return spinner;
	}
	
	private int exitOptionDialog() {
		return JOptionPane.showConfirmDialog(null, 
				"Are you sure you want to quit?",
				"Quit",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null);
	}

 	private void enableToolBar(boolean b) {
 		loadEvent.setEnabled(b);
		changeCO2Class.setEnabled(b);
		changeRoadWeather.setEnabled(b);
		execute.setEnabled(b);
 	}
	 
	private void loadImages() {
		loadIcon = new ImageIcon("resources/icons/open.png");
		contIcon = new ImageIcon("resources/icons/co2class.png");
		weatherIcon = new ImageIcon("resources/icons/weather.png");
		executeIcon = new ImageIcon("resources/icons/run.png");
		stopIcon = new ImageIcon("resources/icons/stop.png");
		exitIcon = new ImageIcon("resources/icons/exit.png");
	} 
}
