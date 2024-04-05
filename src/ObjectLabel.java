import java.awt.Dimension;
import java.security.SecureRandom;
import javax.swing.JLabel;

public class ObjectLabel extends JLabel{
	private static final long serialVersionUID = 828622724811652929L;
	private ObjectType type;
	private Dimension dimensions = new Dimension(20,20);
	private Dimension fieldDimensions = Settings.fieldDimensions;
	private int xVelocity = 0;
	private int yVelocity= 0;
	
	public ObjectLabel(ObjectType type) {
		this.setSize(dimensions);
		setType(type);
		setInitialVelocity();
		setInitialPosition();
	}
	
	public void setInitialVelocity() {
		SecureRandom random = new SecureRandom();
		  //speed
			xVelocity = random.nextInt(Settings.MAX_PIXELS_FOR_MOVE)+1;
			yVelocity = random.nextInt(Settings.MAX_PIXELS_FOR_MOVE)+1;
		  //direction
			xVelocity *= random.nextBoolean() ? 1 : -1;
			yVelocity *= random.nextBoolean() ? 1 : -1;
	}
	
	public void setInitialPosition() {
		SecureRandom random = new SecureRandom();
		int x =0;
		int y=0;
		int xTranslation = random.nextInt(fieldDimensions.width/4)-fieldDimensions.width/8;
		int yTranslation = random.nextInt(fieldDimensions.height/4) - fieldDimensions.height/8;
		switch(type) {
			case ROCK:
				//UP-LEFT
				x = fieldDimensions.width/4 + xTranslation;
				y =  fieldDimensions.height/4 + yTranslation;
				break;
			case PAPER:
				//DOWN-CENTER
				x = fieldDimensions.width/2 + xTranslation;
				y =  (fieldDimensions.height*3/4 + yTranslation);
				break;
			case SCISSORS:
				//UP-RIGHT
				x = fieldDimensions.width*3/4 + xTranslation;
				y =  fieldDimensions.height/4 + yTranslation;
				break;
		}
		setLocation(x,y);
	}
	
	public void setType(ObjectType type) {
		if(type == this.type) return;
		this.type = type;
		switch(type) {
			case ROCK:
				this.setIcon(Settings.rockImage);
				break;
			case PAPER:
				this.setIcon(Settings.paperImage);
				break;
			case SCISSORS:
				this.setIcon(Settings.scissorsImage);
				break;
			}
	}
	
	public ObjectType getType() {
		return type;
	}
	
	public void move() {
		int newX = getX()+xVelocity;
		int newY = getY()+yVelocity;
		
		if(newX <=0 || newX+this.getWidth()>=fieldDimensions.width) {
			newX = newX <= 0 ? 0 : fieldDimensions.width-this.getWidth();
			xVelocity*=-1;
		}
		if(newY <=0 || newY+this.getHeight()>=fieldDimensions.height) {
			newY = newY <= 0 ? 0 : fieldDimensions.height-this.getHeight();
			yVelocity*=-1;
		}
		
		this.setLocation(newX, newY);
	}
}
