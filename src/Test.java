import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class Test extends JFrame{
    private static final long serialVersionUID = -5017436391974747097L;
	String title = "RPS Simulator";
	FieldPane simulationField;
	StatisticsPanel statistics; 
	boolean inGame = false;
	boolean playedBefore = false;
	boolean paused = false;
	
	JLabel messageLabel;
	
	SwingWorker<Void, Void> simulationWorker;
	
public Test() {
    //setup the JFrame
    this.setLayout(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(Settings.FRAME_WIDTH, Settings.FRAME_HEIGHT);
    this.setLocationRelativeTo(null);		//center the frame
    this.setIconImage(Settings.icon.getImage());
    this.setTitle(title);
    //add components
    /*simulationField =  new FieldPane();
    statistics =  new StatisticsPanel(simulationField);
    this.add(simulationField);
    this.add(statistics);
    
    messageLabel = new JLabel("Press any key to start!", SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setBounds(simulationField.getWidth()/2-75, simulationField.getHeight()/2-20, 150, 40);
    messageLabel.setBackground(this.getContentPane().getBackground());
    messageLabel.setOpaque(true);
    simulationField.add(messageLabel);
    */
    
    this.addKeyListener(new KeyAdapter (){
         public void keyPressed(KeyEvent e) {
             JTextArea ta = new JTextArea("eat my butt");
             ta.setWrapStyleWord(true);
             ta.setLineWrap(true);

         }
    });
    
    this.setVisible(true);
}
