import java.awt.Dimension;

import javax.swing.ImageIcon;

public class Settings {
	private Settings() {}
	
	public static final ImageIcon paperImage = new ImageIcon("Media\\paper.png");
	public static final ImageIcon rockImage = new ImageIcon("Media\\rock.png");
	public static final ImageIcon scissorsImage = new ImageIcon("Media\\scissors.png");
	public static final ImageIcon icon = new ImageIcon("Media\\RPSicon.png");
	
	public static final int moveDelay = 20;			//minimum milliseconds between every update. The actual time will be max(moveDelay, updateTime)
	public static final int MAX_PIXELS_FOR_MOVE = 4;//the more pixels range the more diversity the more chaos the more unpredictability
	
	public static final int INITIAL_ROCKS = 200;
	public static final int INITIAL_PAPERS = 200;
	public static final int INITIAL_SCISSORS = 200;
	
	public static final Dimension fieldDimensions = new Dimension(1050,520);	//Only modify this. Other Dimensions will adapt.
	public static final Dimension statsDimensions = new Dimension(fieldDimensions.width, 40);
	public static final int FRAME_WIDTH = fieldDimensions.width + 17;
	public static final int FRAME_HEIGHT = fieldDimensions.height+statsDimensions.height +35;
}
