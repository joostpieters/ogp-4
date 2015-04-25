package jumpingalien.model;

public class Constants {
	
	/**
	 * The maximum time allowed between two frames.
	 */
	public final static double maxTimeInterval = 0.2;
	
	/**
	 * The amount of time it takes for a dead game object to be removed from the world.
	 */
	public final static double deathTime = 0.6;
	
	/**
	 * The vertical acceleration due to gravity.
	 */
	public final static double gravityAcceleration = -10.0;
	
	/**
	 * The size of the screen in pixels.
	 */
	public final static Vector<Double> screenSize = new Vector<>(1024.0, 768.0);
	
	/**
	 * The amount of meters in one pixel.
	 */
	public final static double metersPerPixel = 0.01;
	
	/**
	 * The amount of health a game object loses when colliding with magma.
	 */
	public final static int magmaDamage = -50;
	
	/**
	 * The time interval after terrain damage in which a game object will
	 * not take additional terrain damage.
	 */
	public final static double terrainDamageInterval = 0.2;
	
	/**
	 * The time interval after enemy damage in which it will
	 * not take additional enemy damage.
	 */
	public static double enemyDamageInterval = 0.6;
	
	
	/**
	 * The amount of health someone loses when colliding with WATER terrain.
	 */
	public final static int waterDamage = -2;
	
	
	
	// MAZUB
	
	/**
	 * The amount of hitpoints a mazub begins with.
	 */
	public final static int mazubBeginHealth = 100;
	
	/**
	 * The maximum amount of hitpoints a mazub can have.
	 */
	public final static int mazubMaxHealth = 500;
	
	/**
	 * The amount of health Mazub gains when eating a plant.
	 */
	public final static int mazubPlantHealthGain = 50;
	
	/**
	 * The amount of health Mazub loses when colliding with an enemy.
	 */
	public final static int mazubEnemyDamage = -50;
	
	/**
	 * The time Mazub should stay in it's facing sprite
	 * after stopping with moving.
	 */
	public final static double mazubAfterMoveStayTime = 1;
	
	/**
	 * The initial vertical speed Mazub gets when he starts jumping.
	 */
	public final static double mazubInitialJumpSpeed = 8;
	
	/**
	 * Mazub's maximum horizontal speed while ducking.
	 */
	public final static double mazubMaxSpeedDucking = 1.0;
	
	/**
	 * Mazub's horizontal moving acceleration.
	 */
	public final static double mazubHorizontalAcceleration = 0.9;
	
	
	
	// PLANT
	
	/**
	 * The length of one movement interval of a plant.
	 */
	public final static double plantMoveTime = 0.5;
	
	/**
	 * The horizontal speed of a plant.
	 */
	public final static double plantSpeed = 0.5;
	
	
	
	// SLIME
	
	/**
	 * The amount of hitpoints a slime starts with.
	 */
	public final static int slimeBeginHealth = 100;
	
	/**
	 * The maximum amount of hitpoints a slime can have.
	 */
	public final static int slimeMaxHealth = 100;
	
	/**
	 * The horizontal acceleration of a slime.
	 */
	public final static double slimeHorizontalAcceleration = 0.7;
	
	/**
	 * The maximum horizontal speed of a slime.
	 */
	public final static double slimeMaxHorizontalSpeed = 2.5;
	
	/**
	 * The minimum duration of a slime's movement period.
	 */
	public final static double slimeMinMoveTime = 2.0;
	
	/**
	 * The maximum duration of a slime's movement period.
	 */
	public final static double slimeMaxMoveTime = 6.0;
	
	/**
	 * The amount of damage a slime receives on contact with an enemy (Mazub or Shark).
	 */
	public final static int slimeEnemyContactDamage = -50;
	
	// SHARK
	
	/**
	 * The maximum amount of health a shark can have.
	 */
	public final static int sharkMaxHealth = 100;
	
	/**
	 * The amount of health a shark has when it gets initialised.
	 */
	public final static int sharkBeginHealth = 100;
	
	/**
	 * The horizontal acceleration of a shark.
	 */
	public final static double sharkHorizontalAcceleration = 1.5;
	
	/**
	 * The maximum horizontal speed of a shark.
	 */
	public final static double sharkMaxHorizontalSpeed = 4.0;
	
	/**
	 * The initial vertical jump speed of a shark.
	 */
	public final static double sharkInitialJumpSpeed = 2.0;
	
	/**
	 * The vertical acceleration of a shark for swimming up or down.
	 */
	public final static double sharkVerticalAcceleration = 0.2;
	
	/**
	 * The minimum duration of a shark's movement interval.
	 */
	public final static double sharkMinMoveTime = 1.0;
	
	/**
	 * The maximum duration of a shark's movement interval.
	 */
	public final static double sharkMaxMoveTime = 4.0;
	
	/**
	 * The amount of health a shark loses when colliding with an enemy.
	 */
	public final static int sharkEnemyDamage = -50;
	
	/**
	 * The amount of health a shark loses when colliding with air.
	 */
	public final static int sharkAirDamage = -6;
}
