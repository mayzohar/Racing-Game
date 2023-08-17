package game.arenas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import game.arenas.exceptions.RacerLimitException;
import game.arenas.exceptions.RacerTypeException;
import game.racers.Racer;
import utilities.Observable;
import utilities.Observer;
import utilities.Point;

/**
 *The Arena class is an abstract class that represents a generic arena in which races take place.
 *It contains the fields and methods for managing and running a race.
 * @author Sapir Ovadya, id: 318258274
 * 			May Zohar, id : 315199810
 * @see AerialArena, LandArena, NavalArena,Racer
 * @version 1.0
 */
public abstract class Arena implements Observer{

	private ArrayList<Racer> activeRacers;
	private ArrayList<Racer> completedRacers;
	private ArrayList<Racer> brokenRacers;
	private ArrayList<Racer> disabledRacers;
	private final double FRICTION;
	private final int MAX_RACERS;
	private final static int MIN_Y_GAP = 10;
	private double length;
    private boolean hasFinish=false;
    private boolean activeGame = true;

/**
 * Constructor for the Arena class.
 * @param length The length of the arena.
 * @param maxRacers The maximum number of racers allowed in the arena.
 * @param friction The friction of the arena.
 */
	public Arena(double length, int maxRacers, double friction) {
		
		this.FRICTION = friction;
		this.MAX_RACERS = maxRacers;
		setLength(length);
		setCompletedRacers(new ArrayList<>());
		setActiveRacers(new ArrayList<>());	
		setBrokenRacers(new ArrayList<Racer>());
		setDisabledRacers(new ArrayList<Racer>());
	}
	
	 public final double getMIN_Y_GAP(){return MIN_Y_GAP;}
	
	/**
	 * Get method for the activeRacers field.
	 * @return The ArrayList of all active racers in the arena.
	 */
	public ArrayList <Racer> getActiveRacers(){
		return activeRacers;
	}
	
	/**
	 * Set method for the activeRacers field.
	 * @param activeRacers The ArrayList of all active racers in the arena.
	 */
	public Boolean setActiveRacers(ArrayList <Racer> activeRacers)
	{
		if (activeRacers != null){
			this.activeRacers = activeRacers;
			return true;
		}
		return false;
		
	}
	
	/**
	 * Get method for the completedRacers field.
	 * @return The ArrayList of all completed racers in the arena.
	 */
	public ArrayList <Racer> getCompletedRacers(){
		return completedRacers;
	}
	
	/**
	 * Set method for the completedRacers field.
	 * @param completedRacers The ArrayList of all completed racers in the arena.
	 */
	public boolean setCompletedRacers(ArrayList <Racer> completedRacers)
	{
		
		this.completedRacers = completedRacers;
		return true;
	}
	
	/**
	 * Set method for the broken racers.
	 * @param brokenRacers The ArrayList of Racer objects representing the broken racers.
	 */
	public void setBrokenRacers(ArrayList<Racer> brokenRacers) {
		this.brokenRacers = brokenRacers;
	}
	
	/**
	 * Get method for the broken racers.
	 * @return The ArrayList of Racer objects representing the broken racers.
	 */
	public ArrayList<Racer> getBrokenRacers() {
		
			return brokenRacers;
		
	}
	
	/**
	* Set method for the disabled racers.
	* @param disabledRacers The ArrayList of Racer objects representing the disabled racers.
	*/
	public void setDisabledRacers(ArrayList<Racer> disabledRacers) {
		this.disabledRacers = disabledRacers;
	}


	/**
	 * Get method for the disabled racers.
	 * @return The ArrayList of Racer objects representing the disabled racers.
	 */
	public ArrayList<Racer> getDisabledRacers() {
			return disabledRacers;
		
	}
	
	/**
	 * Set method for the length field.
	 * @param len The length of the arena.
	 */
	public boolean setLength(double len) {
		this.length = len;
		return true;
	}
	
	/**
	 * get method for the length field.
	 * @return lenght
	 */
	public double getLength() {
		return length;
	}
	
	/**
	 * Returns the friction value of the arena.
	 * @return FRICTION the friction value of the arena.
	 */
	public double getFriction() {
		return FRICTION;
	}
	
	/**
	 * Returns the maximum number of racers allowed in the arena.
	 * @return MAX_RACERS
	 */
	public int getMaxRacers() {
		return MAX_RACERS;
	}
	

	/**
	 * Adds a new racer to the arena.
	 * @param newRacer the new racer to be added.
	 * @throws RacerTypeException if the type of the racer is not supported by the arena.
	 * @throws RacerLimitException if the maximum number of racers allowed in the arena has been reached.
	 */
	public void addRacer(Racer newRacer) throws RacerTypeException, RacerLimitException {	  
		
	    if (activeRacers.size() >= getMaxRacers()) {
	    	throw new RacerLimitException(newRacer.getSerialNumber(),getMaxRacers());
	     }
	    activeRacers.add(newRacer);
  }
	
	/**
	 * Initializes the race by setting the start and finish points for each racer in the arena.
	 */
	public void initRace() {
  	
      int y = 0;
      for (Racer racer : activeRacers) {
      	Point start = new Point (0,y);
      	Point finish = new Point (getLength(),y);
          racer.initRace(this,start,finish);
          y += MIN_Y_GAP;
      }
      
  }
  
  /**
   * Checks if there are any active racers left in the arena
   * @return boolean type
   */
  public boolean hasActiveRacers() {
  	return (!activeRacers.isEmpty());
  }
  
  /**
   * Plays a turn of the game by moving each active racer in the arena and removing any racer that has crossed the finish line.
   */
  public void playTurn() {
	  
      ExecutorService exe = Executors.newFixedThreadPool(this.activeRacers.size());
      for (Racer racer : activeRacers) {
          exe.execute(racer);
      }
      exe.shutdown();
   
  }
  
  /**
   * Add the racer that as crossed the finish line to the completedRacers list and removing him from the activeRacers list.
   * @param racer  the racer that has crossed the finish line.
   */
  public synchronized void crossFinishLine(Racer racer) {
  	completedRacers.add(racer);
  	activeRacers.remove(racer);

  }
  
  /**
   * This method displays the results of completed racers.
   */
  public void showResults() {
  	
  	int i=0;
  	
  	for(Racer racer : getCompletedRacers() ) {
  		
  		System.out.print("#" + i + "-> ");
  		
  		System.out.println(racer.describeRacer() + racer.describeSpecific());
  		
  		i++;
  	}
  	
  }
  
  /**
   * Updates the game based on changes in the Observable objects.
   * @param racer the Observable object representing the game state changes
   */
  @Override
  public synchronized void update(Racer racer) {
	  
      if (racer.hasFinished()) {
          crossFinishLine(racer);
      }
      if (racer.getState().equals("Failed")) {
          disabledRacers.add(racer);
          activeRacers.remove(racer);
      }
      if (racer.getState().equals("Broken")) {
          brokenRacers.add(racer);
          activeRacers.remove(racer);
      }
      if (racer.getState().equals("Active")) {
          activeRacers.add(racer);
          brokenRacers.remove(racer);
      }

      

  }
  
  /**
  Starts the race by registering active racers and initiating the turn.
  */
  public void startRace() {

      for(Racer racer:this.getActiveRacers()) {
          racer.register(this);
      }
      this.playTurn();

  }
  
  
	/**
	 * Creates and returns a JTable containing information about the racers in the arena.
	 * Each racer's information is added as a row in the table, with the corresponding values for each column.
	 * @return JTable containing the racer information
	 */
  public JTable InfoOfTheRacer() {
      
	  int counter = 0;
      Object[][] data = new Object[getActiveRacers().size() + getCompletedRacers().size() + getDisabledRacers().size()][6];
      Object[] racerInfo = null; 
      String[] columnNames = {"Racer name", "Current speed", "Max speed", "Current X location", "Finished", "State"}; 
      
      for (Racer racer : this.getCompletedRacers()) {
        	racerInfo = new String[columnNames.length];
        	racerInfo[0] = racer.getName();
        	racerInfo[1] = Double.toString(racer.getcurrentSpeed());
        	racerInfo[2] = Double.toString(racer.getMaxSpeed());
            racerInfo[3] = Double.toString(racer.getCurrentLocation().getX());
            racerInfo[4] = Boolean.toString(racer.hasFinished());
            racerInfo[5] = racer.getState();
            data[counter++] = racerInfo;
        	
        }
      
      for (Racer racer : this.getActiveRacers()) {
    	  
	      racerInfo = new String[columnNames.length];  
	      racerInfo[0] = racer.getName();
	      racerInfo[1] = Double.toString(racer.getcurrentSpeed());
	      racerInfo[2] = Double.toString(racer.getMaxSpeed());
	      racerInfo[3] = Double.toString(racer.getCurrentLocation().getX());
          racerInfo[4] = Boolean.toString(racer.hasFinished());
          racerInfo[5] = racer.getState();
	      data[counter++] = racerInfo;
      }
      
      for (int i = this.getDisabledRacers().size()-1; i >=0;) { //to go backwards
    	  
	      racerInfo = new String[columnNames.length];  
	      racerInfo[0] = this.getDisabledRacers().get(i).getName();
	      racerInfo[1] = Double.toString(this.getDisabledRacers().get(i).getcurrentSpeed());
	      racerInfo[2] = Double.toString(this.getDisabledRacers().get(i).getMaxSpeed());
	      racerInfo[3] = Double.toString(this.getDisabledRacers().get(i).getCurrentLocation().getX());
          racerInfo[4] = Boolean.toString(this.getDisabledRacers().get(i).hasFinished());
          racerInfo[5] = this.getDisabledRacers().get(i).getState();
	      data[counter++] = racerInfo;
	      i--;
          }
      

      TableModel tableModel = new DefaultTableModel(data, columnNames);
      JTable tableInfo = new JTable(tableModel);
      return tableInfo;

  }
/**
 * Sets the game state
 * @param bool boolean value indicating whether the game is active or not
 */
  public void setActiveGame(boolean bool){
	  activeGame=bool;
  }
  
  
  /**
   * checks if the game is finished
   * @return hasFinish boolean value
   */
  public boolean getHasFinish() {
	  return hasFinish;
  }
  

}

