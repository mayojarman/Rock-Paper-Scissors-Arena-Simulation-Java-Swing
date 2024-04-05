import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class SimulationFrame extends JFrame{
	private static final long serialVersionUID = -5017436391974747097L;
	String title = "RPS Simulator";
	FieldPane simulationField;
	StatisticsPanel statistics; 
	boolean inGame = false;
	boolean playedBefore = false;
	boolean paused = false;
	
	JLabel messageLabel;
	
	SwingWorker<Void, Void> simulationWorker;
	
	public SimulationFrame() {
		//setup the JFrame
		this.setLayout(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(Settings.FRAME_WIDTH, Settings.FRAME_HEIGHT);
        this.setLocationRelativeTo(null);		//center the frame
        this.setIconImage(Settings.icon.getImage());
        this.setTitle(title);
        //add components
        simulationField =  new FieldPane();
		statistics =  new StatisticsPanel(simulationField);
	    this.add(simulationField);
	    this.add(statistics);
        
	    messageLabel = new JLabel("Press any key to start!", SwingConstants.CENTER);
	    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    messageLabel.setBounds(simulationField.getWidth()/2-75, simulationField.getHeight()/2-20, 150, 40);
	    messageLabel.setBackground(this.getContentPane().getBackground());
	    messageLabel.setOpaque(true);
	    simulationField.add(messageLabel);
	    
	    
	    this.addKeyListener(new KeyAdapter (){
	    	 public void keyPressed(KeyEvent e) {
                 if(!inGame) {
                	 inGame = true;
                	 if(playedBefore) reset();
                	 startSimulation();
                	 playedBefore = true;
                 }
                 else if(!paused) pause();
                 else resume();
             }
        });
	    
	    this.setVisible(true);
	}
	
	
	private void startSimulation() {
		simulationField.remove(messageLabel);
		simulationWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!simulationField.terminated()) {
                	//check if simulation was paused
                	while(paused) {
                		try {
                			synchronized(this){ wait(100); }
                		} catch(InterruptedException e){ System.out.println("Background interrupted"); }
                	}
                	//update simulation on a separate thread 
                    Thread updateThread = new Thread(() -> updateSimulation());
                    updateThread.start();
                    //during the update we can sleep
                    try {
                        Thread.sleep(Settings.moveDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //if the update didn't complete during our sleep, we wait for it
                    updateThread.join();
                }
                return null;
            }
            @Override
            protected void done() {
                inGame = false;
                messageLabel.setText("Press any key to restart");
           	 	simulationField.add(messageLabel, JLayeredPane.PALETTE_LAYER,0);
            }
        };
        simulationWorker.execute();
	}
	
	private void updateSimulation() {
		simulationField.moveAll();
		simulationField.detectCollisionsSmart();
		statistics.update();
		this.repaint();
	}
	private void pause() {
		this.setTitle("Pause. Press any key to resume.");
		paused = true;
		statistics.stopTimer();
	}
	private void resume() {
		this.setTitle(title);
		paused = false;
		statistics.startTimer();
		//this.notify(); 
	}
	
	private void reset() {
		simulationField.setStartingPosition();
		statistics.reset();
		this.repaint();
	}
}
