import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class StatisticsPanel extends JPanel{
	private static final long serialVersionUID = -640705996885344206L;
	private Dimension dims;
	private Dimension labelsDim;
	
	private FieldPane field;
	
	private JLabel scissorsLabel;
	private JLabel rocksLabel;
	private JLabel papersLabel;
	private JLabel timerLabel;
	
	private Timer timer;
	private int secondsElapsed;
	
	private boolean simulationStarted = false;
	
	StatisticsPanel(FieldPane field){
		dims = Settings.statsDimensions;
		labelsDim = new Dimension(field.getWidth()/5, dims.height);
		
		this.setBounds(0, field.getHeight(), dims.width, dims.height);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		this.setSize(dims);
		
		rocksLabel = createAndAddLabel("rocks: " + field.rocks);
		papersLabel = createAndAddLabel("papers: " + field.papers);
		scissorsLabel = createAndAddLabel("scissors: " + field.scissors);
		timerLabel = createAndAddLabel("time: " + 0 +" sec");
		
		
		this.field=field;
		
		timer = new Timer(1000, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                secondsElapsed++;
	                update();
	            }
	        });
	}
	public void update() {
		if(!simulationStarted) {
			simulationStarted = true;
			timer.start();
		}
		updateObjCount();
		updateTime();
		if(field.terminated()) {
			timer.stop();
			switchToResultPanel();
		}
	}
	
	public void reset() {
		this.removeAll();
		this.add(rocksLabel);
		this.add(papersLabel);
		this.add(scissorsLabel);
		this.add(timerLabel);
		secondsElapsed = 0;
		this.repaint();
		simulationStarted = false;
		update();
	}
	
	private JLabel createAndAddLabel(String text) {
		JLabel label = new JLabel(text);
		label.setPreferredSize(labelsDim);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label);
		return label;
	}
	private void updateObjCount() {
		scissorsLabel.setText("scissors: "+ field.scissors);
		rocksLabel.setText("rocks: "+ field.rocks);
		papersLabel.setText("papers: "+ field.papers);
	}
	private void updateTime() {
		timerLabel.setText("time: " + secondsElapsed + " sec");
	}
	public void stopTimer() {
		timer.stop();
	}
	public void startTimer() {
		timer.start();
	}
	
	private void switchToResultPanel() {
		this.removeAll();
		String winners = field.winner == ObjectType.SCISSORS ? field.winner + "" : field.winner + "S" ;
		JLabel winnerLabel = new JLabel(winners + " WON in " + secondsElapsed +" seconds!");
		winnerLabel.setForeground(Color.red);
		winnerLabel.setVisible(true);
		winnerLabel.setOpaque(true);
		this.add(winnerLabel);
		this.revalidate();
		this.repaint();
	}
}
