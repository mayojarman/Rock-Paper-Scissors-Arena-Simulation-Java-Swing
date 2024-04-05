import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JLayeredPane;

public class FieldPane extends JLayeredPane{
	private static final long serialVersionUID = 6060568550783551712L;
	Dimension dimensions = Settings.fieldDimensions;
	int rocks;
	int papers;
	int scissors;
	List<ObjectLabel> list;
	ObjectType winner;
	
	Dimension subsectionDimensions = new Dimension(dimensions.width/3, dimensions.height/2);
	
	public FieldPane() {
		setStartingPosition();
		this.setBounds(0,0, dimensions.width, dimensions.height);
	}
	
	public void setStartingPosition() {
		this.removeAll();
		list = new ArrayList<>();
		rocks = Settings.INITIAL_ROCKS;
		papers = Settings.INITIAL_PAPERS;
		scissors = Settings.INITIAL_SCISSORS;
		winner = null;
		for(int i = 0; i<rocks; i++) {
			ObjectLabel rock = new ObjectLabel(ObjectType.ROCK);
			list.add(rock);
			this.add(rock);
		}
		for(int i = 0; i<papers; i++) {
			ObjectLabel paper = new ObjectLabel(ObjectType.PAPER);
			list.add(paper);
			this.add(paper);
		}
		for(int i = 0; i<scissors; i++) {
			ObjectLabel scissor = new ObjectLabel(ObjectType.SCISSORS);
			list.add(scissor);
			this.add(scissor);
		}
		this.repaint();
	}
	
	private void handleCollision(ObjectLabel ob1, ObjectLabel ob2) {
		if(ob1.getType() == ob2.getType()) return;
		//Note that every ObjectType is associated to a value:
		//ROCK = 3
		//PAPER = 1
		//SCISSORS = 2
		int max = Math.max(ob1.getType().getValue(), ob2.getType().getValue());
		int min = Math.min(ob1.getType().getValue(), ob2.getType().getValue());
		if(max-min == 2) {
			//only way this is possible is a rock (3) and a paper (1) have collided
			//we don't know who is what so we set both of them as papers
			//note that setType returns immediately when you set a type that the object already is
			ob1.setType(ObjectType.PAPER);
			ob2.setType(ObjectType.PAPER);
			papers++;
			rocks--;
		}
		else if(max == 2) {
			//if max is 2 we can exclude rock (3) and we have scissors (2) and paper (1)
			ob1.setType(ObjectType.SCISSORS);
			ob2.setType(ObjectType.SCISSORS);
			papers--;
			scissors++;
		}
		else {
			//there is only one case left
			ob1.setType(ObjectType.ROCK);
			ob2.setType(ObjectType.ROCK);
			scissors--;
			rocks++;
		}
	}
	
	public void detectCollisionsSmart() {
		//concept: divide the field into n sections, then compare each object within the same section
		Map<Rectangle, List<ObjectLabel>> subsections = new HashMap<>();
		
		//init subsections
		int currX = 0;
		int currY = 0;
		while(currY<dimensions.height-15) {			//-15 due to possible loss of size from the division. Objects.size>15 so will collide with rects anyway.
			while(currX<dimensions.width-15) {
				Rectangle subsection = new Rectangle(currX, currY, subsectionDimensions.width, subsectionDimensions.height);
				List<ObjectLabel> population = new ArrayList<>();
				subsections.put(subsection, population);
				currX+=subsectionDimensions.width;
			}
			currX=0;
			currY+=subsectionDimensions.height;
		}
		//add population to every subsection
		//NB: an object (little Rectangle) gets added to every subsection (big Rectangle) that it collides with
		//for efficiency matters we assign each subsection to a distinct thread, which will fill that section
		ExecutorService populationFillers = Executors.newCachedThreadPool();	//pool of threads
		for(Rectangle subsection : subsections.keySet()) {
			populationFillers.execute(() -> {						//assign current subsection to a new thread
				for(ObjectLabel obj : list) {
					if(obj.getBounds().intersects(subsection)) {
						subsections.get(subsection).add(obj);
					}
				}
			});
		}
		//execute threads and close the group
		populationFillers.shutdown();
		//wait for the pool of threads to be done before moving on
		boolean subsectionsFilled = false;
		try { 
			//give a maximum amount of time to the pool to accomplish their task (3 milliseconds in this case)
			subsectionsFilled = populationFillers.awaitTermination(3, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!subsectionsFilled) System.err.println("Maybe your machine's too slow.\nIncrease time limit in detectCollisionsSmart");
			
		//find collisions in every population
		for(Rectangle subsection : subsections.keySet()) {
				List<ObjectLabel> population = subsections.get(subsection);
				for(int i = 0; i<population.size()-1; i++) {
					for(int j = i+1; j<population.size(); j++) {
						if(population.get(i).getBounds().intersects(population.get(j).getBounds())) {
							handleCollision(population.get(i), population.get(j));
						}
					}
				}
		}
	}
	
	public void detectCollisions() {
		//cute and easy
		//this first implementation is obsolete and not used anymore (see detectCollisionsSmart)
		for(int i = 0; i<list.size()-1; i++) {
			for(int j = i+1; j<list.size(); j++) {
				if(list.get(i).getBounds().intersects(list.get(j).getBounds())) {
					handleCollision(list.get(i), list.get(j));
				}
			}
		}
	}
	
	public void moveAll() {
		for(ObjectLabel ob : list) {
			ob.move();
		}
		this.validate();
	}
	
	public boolean terminated() {
		int goal = list.size();
		
		if(rocks == goal) winner = ObjectType.ROCK;
		else if(papers == goal) winner = ObjectType.PAPER;	
		else if(scissors == goal) winner = ObjectType.SCISSORS;
		else return false;
		
		return true;
	}
}
