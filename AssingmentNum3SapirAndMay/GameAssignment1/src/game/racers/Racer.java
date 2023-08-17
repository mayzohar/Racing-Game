
package game.racers;

import game.arenas.Arena;
import states.Active;
import states.Broken;
import states.Completed;
import states.Disabled;
import states.State;
import utilities.Point;
import utilities.EnumContainer.Color;
import utilities.Mishap;
import utilities.Observable;
import utilities.Observer;
import utilities.Fate;
import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * The abstract class Racer represents a racer in a race, including methods to initialize the race and move the racer.
 * @author Sapir Ovadya, id: 318258274
 * 			May Zohar, id : 315199810
 *@version 1.0
 */
public abstract class Racer implements Runnable, Observable{
	
	private int serialNumber;
	private static int IDN=1;
	private String name;
	private Point CurrentLocation;
	private Point finish;
	private Arena arena;
	private double maxSpeed;
	private double acceleration;
	private double currentSpeed;
	private double failureProbablitiy;
	private Color color;
	private Mishap mishap;
    private Observer arenaObserver;
    private State state;
    private int FlagRacer;
	private static Random rand = new Random();
    private long timeBegin;
    public void setStartTime(long startTime) {
        this.timeBegin = startTime;
    }
	
	public Racer() {
		this.setSerialNumber(IDN++);
		setName(null);
		setMaxSpeed(0);
		setAcceleration(0);
		setColor(null);
				
	}
	
	/**
	 * Constructor for the Racer class.
	 * @param name
	 * @param maxSpeed The maximum speed of the racer.
	 * @param acceleration The acceleration of the racer.
	 * @param color
	 */
	public Racer(String name,double maxSpeed, double acceleration, Color color ) {
		
		setSerialNumber(IDN++);
		setName(name);
		setMaxSpeed(maxSpeed);
		setAcceleration(acceleration);
		setColor(color);
		this.state=new Active();
	}
	
	/**
	 * Get the static IDN field.
	 * @return The static IDN field.
	 */
	public static int getIdn() {
		return IDN;
		
	}
	
	/**
	 * Set the serial number field.
	 * @param serialNumber The new serial number of the racer.
	 * @return true if the serial number was set successfully
	 */
	public boolean setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
		return true;
		
	}
	
	/**
	 * @return The serial number of the racer.
	 */
	public int getSerialNumber() {
		return serialNumber;
		
	}
	
	/**
	 * Set the name field.
	 * @param name
	 * @return true if the name was set successfully
	 */
	public boolean setName(String name) {
		this.name = name;
		return true;
	}
	

	
//	public Racer clone() {
//		return new Racer();
//	}
	
//	public Object clone() {
//		Object clone = null;
//		try {
//			clone = super.clone();
//		}
//		catch(CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		return clone;
//	}
	
	/**
	 * Set the current location field.
	 * @param CurrentLocation The new current location of the racer.
	 * @return true if the current location was set successfully
	 */
	public boolean setCurrentLocation(Point CurrentLocation) {
		if(CurrentLocation != null) {
			this.CurrentLocation= CurrentLocation;
			return true;
			}
		return false;
	}
	
	/**
	 * Runs the movement of an object within the arena.
	 */	
    public void run(){

        while (!hasFinished() ){
            this.move(this.arena.getFriction());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

        }
        
        this.setState(new Completed());
        System.out.println(this.getName()+ " compleated!"); 
        this.setCurrentLocation(new Point(arena.getLength(),CurrentLocation.getY()));
        notifyObserver();
        
    }
		
		
	/**
	 * 
	 * @param finish The new finish point of the racer.
	 * @return true if the finish point was set successfully
	 */
	public boolean setFinish(Point finish) {
		if(finish != null) {
			this.finish= finish;
			return true;
		}
		return false;
	}
	
	/**
	 * Set the arena field.
	 * @param arena The arena to the racer.
	 * @return true if the arena was set successfully
	 */
	public boolean setArena(Arena arena) {
		
		if(arena != null) {
			this.arena= arena;
			return true;
		}
		return false;
	}
	
	/**
	 * Set the max speed field.
	 * @param maxSpeed he new maximum speed of the racer.
	 * @return tue if the maximum speed was set successfully.
	 */
	public boolean setMaxSpeed(double maxSpeed) {
		this.maxSpeed= maxSpeed;
		return true;
	}
	
	/**
	 * Sets the acceleration.
	 * @param acceleration
	 * @return true if the acceleration was set successfully.
	 */
	public boolean setAcceleration(double acceleration) {
		this.acceleration= acceleration;
		return true;
	}
	
	/**
	 * Sets the current speed.
	 * @param currentSpeed
	 * @return  true if the current speed was set successfully.
	 */
	public boolean setcurrentSpeed(double currentSpeed) {
		this.currentSpeed= currentSpeed;
		return true;
	}
	
	/**
	 * Sets the failure probability
	 * @param failureProbablitiy
	 * @return  true if the failure probability was set successfully.
	 */
	public boolean setFailureProbablitiy(double failureProbablitiy) {
		this.failureProbablitiy= failureProbablitiy;
		return true;
	}
	
	/**
	 * Sets the color
	 * @param color
	 * @return  true if the color was set successfully.
	 */
	public boolean setColor(Color color) {
		this.color= color;
		return true;
	}
	
	/**
	 * Sets the mishap
	 * @param mishap
	 * @return true if the mishap was set successfully.
	 */
	public boolean setMishap(Mishap mishap) {
		this.mishap= mishap;
		return true;
	}
	
	/**
	 * Gets the mishap 
	 * @return  the mishap 
	 */
	public Mishap getMishap() {
		return mishap;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the current location of the racer
	 * @return the current location of the racer
	 */
	public Point getCurrentLocation() {
		return CurrentLocation;
	}
	
	/**
	 * Gets the finish point
	 * @return the finish point
	 */
	public Point getFinish() {
		return finish;
	}
	
	/**
	 * Gets the arena object 
	 * @return the arena object
	 */
	public Arena getArena() {
		return arena;
	}
	
	/**
	 * Gets the maximum speed
	 * @return the maximum speed
	 */
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	/**
	 * Gets the acceleration
	 * @return the acceleration 
	 */
	public double getAcceleration() {
		return acceleration;
	}
	
	/**
	 * Gets the current speed 
	 * @return  the current speed 
	 */
	public double getcurrentSpeed() {
		return currentSpeed;
	}
	
	/**
	 * Gets the failure probability 
	 * @return the failure probability 
	 */
	public double getFailureProbablitiy() {
		return failureProbablitiy;
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * nitializes the race car with an arena, start point, and finish point.
	 * @param arena the arena object
	 * @param start the start point
	 * @param finish the finish point 
	 */
	public void initRace(Arena arena, Point start, Point finish) {
		this.arena = arena;
		this.CurrentLocation = start;
		this.finish = finish;
	}
	

/**
 *Moves the racer based on the given friction value.
 * @param friction The friction value
 * @return he current location of the racer.
 */
public Point move(double friction) {
		
	
	
	  if (this.state instanceof Disabled){
          return CurrentLocation;
      }
	  
      if(this.state instanceof Broken){
          if (rand.nextInt(15) < 2){
              this.setState(new Disabled());
          }
          else{
              this.setState(new Active());
          }
      }
       
		if (!hasMishap()) {
			if(Fate.breakDown()) {
				mishap = Fate.generateMishap();
					if(getMaxSpeed() > getcurrentSpeed()) {
						currentSpeed = acceleration * mishap.getReductionFactor();
						CurrentLocation.setX(CurrentLocation.getX() + currentSpeed);
						System.out.println(this.name + " Has a new mishap! " + mishap);
						if(mishap.getFixable()) {
							this.setState(new Broken());
                            long currentTime = System.currentTimeMillis();
                            long timeBroke = currentTime - timeBegin;
                            System.out.println(this.getName()+ " time the racer broke: "+ timeBroke + " milisecond");
							mishap.nextTurn();
						}
						else {
							this.setState(new Disabled());
					          arena.getDisabledRacers().add(this);
					          arena.getActiveRacers().remove(this);
						}
						
						
					}
					else {
						currentSpeed = maxSpeed;
						CurrentLocation.setX(CurrentLocation.getX() + currentSpeed);
						if(mishap.getFixable()) {
							this.setState(new Broken());
							mishap.nextTurn();
						}
						else {
							this.setState(new Disabled());
					          arena.getDisabledRacers().add(this);
					          arena.getActiveRacers().remove(this);
						}	
					}
				}

			else {
					if(getMaxSpeed() > getcurrentSpeed()) {
						currentSpeed += acceleration * friction;
						CurrentLocation.setX(CurrentLocation.getX() + currentSpeed);
					}
					else {
						currentSpeed = maxSpeed;
						currentSpeed += acceleration * friction;
						CurrentLocation.setX(CurrentLocation.getX() + currentSpeed);
					}
				}
		}
		else {
				if(getMaxSpeed() > getcurrentSpeed()) {
					currentSpeed = acceleration * mishap.getReductionFactor();
					CurrentLocation.setX(CurrentLocation.getX() + currentSpeed);
					mishap.nextTurn();
				}
				else {
					currentSpeed = maxSpeed;
					CurrentLocation.setX(CurrentLocation.getX() + currentSpeed);
					mishap.nextTurn();
				}	
		}
		
		return getCurrentLocation();  
	}

	public abstract String describeSpecific();
	
	/**
	 * Returns a string that describes the racer
	 * @return The description of the racer.
	 */
	public String describeRacer() {
		
		return "name: " + this.name + ", " + "SerialNumber: " + serialNumber + ", " + "maxSpeed: " + this.maxSpeed + 
				", " + "acceleration: " + this.acceleration + ", " + "color: " + this.color;
	}

	public void introduce(){
	
		System.out.println("[" + className() + "] " + describeRacer() + describeSpecific());
	}
	
	
	public abstract String className();
	
	/**
	 *Return true if the racer has a mishap.
	 * @return true if the racer has a mishap.
	 */
	public boolean hasMishap() {
		
		if(mishap == null)  {
			return false;
		}
		
		if(mishap.getFixable() && mishap.getTurnsToFix()==0){
			this.setMishap(null);
			return false;
		}
		
		else
			return true;
	}
	/**
	* Checks if the racer has finished the race.
	* @return true if the racer has crossed the finish line, false otherwise.
	*/
    public boolean hasFinished(){
        if(this.getCurrentLocation().getX() < finish.getX())
            return false;
        return true;
    }

    /**
    * Registers an Observer for the racer.
    * @param ar The Observer to be registered.
    */
	  public void register(Observer ar) {
		  this.arenaObserver=ar;
	  }
	  
	/**
	* Notifies the registered observer about a change in the racer's state.
	* This method is synchronized to ensure thread-safe access to the observer.
	*/ 
    public void notifyObserver() {
        synchronized(this){
                arenaObserver.update(this);
            
        }
    }

    /**
    * Sets the state of the racer.
    * @param s The State object representing the new state of the racer.
    */
    public void setState(State s){
        this.state = s;
    }
    /**
    * Get method for the current state of the racer.
    * @return A String representing the current state of the racer.
    */
    public String getState(){
        return state.getState();
      }
    /**
    * Sets the flag for the racer.
    * @param x The value representing the flag for the racer.
    */
    public void setFlagRacer(int x) {
		this.FlagRacer = x;
    }
    /**
	* Get method flag value for the racer.
    * @return The value representing the flag for the racer.
    */
    public int getFlagRacer() {
		return FlagRacer;	
    }
    
}


