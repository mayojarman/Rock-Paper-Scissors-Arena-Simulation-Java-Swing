# Rock, Paper, Scissors: Arena Simulation in Java Swing (TikTok inspired)

## Description
Three groups of customizable size of papers, rocks, and scissors are generated in three distinct sections of the field.   
Each object is assigned a random speed within a customizable range and a random direction upon creation.   
Once the simulation begins, every object moves continuously across the field.   
When two objects of different types collide, the losing one (determined by the classic Rock, Paper, and Scissors rules) transforms and joins the winner's group.   
At the end, there will be only one group left.  

## GUI Functionalities
- Pressing a key starts a new simulation.
- Pressing a key during a simulation stops/resumes the current simulation.

## Preview
![Beginning](/Preview/beginning.png)  
![In Game](/Preview/middle.png)  
![Ending](/Preview/end.png)

## The Code
I tried to structure the code to be pretty easy to understand and modify according to personal preferences. The main aspects of the simulation, such as its speed, number of objects, and container size, are directly editable through the Settings class.
Here is a brief description of each class and the overall hierarchy, from down to top:
- **Settings:** includes static constants to set the field size, simulation speed, and the number of objects.
- **ObjectType:** enum representing the type of an object, which can be ROCK, PAPER, or SCISSORS.
- **ObjectLabel:** JLabel representing an ObjectType. Provides the following functionalities:  
   - Constructor: sets the type, gives a flexible initial position to the label (depending on the given type), and a random speed/direction
   - setType: sets the type and the corresponding image to the object 
   - move: translates the object by the initially generated speed, switching direction if edges are reached
- **FieldPane:** JLayeredPane representing the field of the simulation, containing a list of ObjectLabel
   - Constructor: generates a (pre-specified) numbers of rocks, scissors, and papers (each is an ObjectLabel) and places them on the field
   - setStartingPosition: resets the field into a starting state, waiting for a new simulation (regenerates the objects list and adds it to the field)
   - move: moves every object by one unit.
   - detectCollisionsSmart: Detects and handles every collision on board. Collision handling involves transforming the losing object into the winning object.
- **StatisticsPanel:** JPanel containing labels highlighting the state of the game (number of objects alive, time elapsed since the start..)
   - Constructor: requires a FieldPane from which informations will be extracted at every update
   - update: updates the labels with the latest informations extracted from the field. If simulation ends, it shows a result message
   - reset: resets the panel into a starting state, waiting for a new simulation
- **SimulationFrame:** JFrame of the simulation, puts the previous components and settings together and starts a new simulation when a key is pressed.
   - KeyListener: handles the start and pause mechanisms of the simulation
   - startSimulation: Executes the simulation on a SwingWorker. Called by KeyListener when a key is pressed and there isn't an active simulation.  
     simulation:= loop of the following actions until termination: move objects, detect collisions, update highlights, repaint frame  
     two iterations of this loop are separated from max(iterationTime, moveDelay), where moveDelay can be set in the Settings class.
   - pause: sets the paused flag to true, interrupting the SwingWorker loop. Called by KeyListener when a key is pressed and there is an active simulation.
   - resume: resets the paused flag to false. Called by KeyListener when a key is pressed and an active simulation was paused.
   - reset: when a previous simulation has ended and a key is pressed, prepares the frame for a new simulation. Called by KeyListener.

## Considerations
From a technical standpoint, one of the biggest challenges I faced as a beginner from this small project has been to find an efficient mechanism of collision detection, allowing for a large amount of objects to move and change continuously in the field without considerable performance sacrifices. So I took the opportunity to dive into cool data-structure and multi-threading techniques, which appear to have led to fairly smooth results, even with more than a thousand of total objects simultaneously on the board (I tested it just on a mid-laptop, lol). However, if anyone with more experience than me has any suggestion to make it even better, I'm very open and curious to learn! Thanks for the attention.

## Usage
Run the Main.java class. JDK required.
