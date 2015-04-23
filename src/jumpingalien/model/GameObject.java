package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;

import jumpingalien.util.Sprite;
import be.kuleuven.cs.som.annotate.*;

/**
 * @author Rugen & Menno
 *
 * An abstract class representing an object in the game world.
 * This class provides common features of all the object such as health, etc.
 * 
 * @invar The health of the game object is never higher than the max health, nor is it negative.
 * 			| this.isValidHealth(this.getHealth())
 */
public abstract class GameObject implements Collidable {

	/**
	 * An array containing the sprites of this game object.
	 */
	private final Sprite[] sprites;
	
	/**
	 * The current sprite of this game object.
	 */
	private Sprite currentSprite;
	
	/**
	 * A motion object containing the position, speed
	 * and acceleration of this game object.
	 */
	private Motion motion;
	
	/**
	 * The facing of this game object. 1 means it's
	 * facing right, -1 means it's facing left.
	 */
	private double facing;
	
	
	/**
	 * A boolean value indicating whether this game
	 * object is passable.
	 */
	private final boolean passable;

	
	/**
	 * The maximum number of hitpoints this game object can have.
	 */
	private final int maxHealth;
	
	/**
	 * The number of hitpoints this game object has.
	 */
	private int health;
	
	
	/**
	 * How long the health of this game object has been zero.
	 */
	private double deathTime = 0.0;
	
	
	/**
	 * The world this game object is in.
	 */
	private World world;
	
	
	/**
	 * Creates a new game object with the given health, maxHealth, position and sprites.
	 * 
	 * @param health
	 * 			The health of the game object.
	 * 
	 * @param maxHealth
	 * 			The max health of the game object.
	 * 
	 * @param position
	 * 			The position of the game object in meters.
	 * 
	 * @param sprites
	 * 			The set of sprites of the game object.
	 * 
	 * @effect GameObject(health, maxHealth, position, sprites, false)
	 * 
	 * @post Passable will be set to false.
	 * 			| new.isPassable() == false
	 * 
	 * @throws IllegalArgumentException
	 */
	protected GameObject(int health, int maxHealth, Vector<Double> position, Sprite[] sprites)
			throws IllegalArgumentException {
		this(health, maxHealth, position, sprites, false);
	}
	
	
	/**
	 * Creates a new game object with the given health, maxHealth, position, sprites and passable.
	 * 
	 * @param health
	 * 			The health to set.
	 * 
	 * @param maxHealth
	 * 			The max health to set.
	 * 
	 * @param position
	 * 			The position to set.
	 * 
	 * @param sprites
	 * 			The sprites to set.
	 * 
	 * @param passable
	 * 			Whether or not the game object is passable.
	 * 
	 * @post The facing will be set to 1.
	 * 			| new.getFacing() == 1
	 * 
	 * @post Passable will be set to passable
	 * 			| new.isPassable() == passable
	 * 
	 * @post Max health will be set to maxHealth
	 * 			| new.getMaximumHealth() == maxHealth
	 * 
	 * @effect setHealth(health)
	 * 
	 * @post The sprites will be set to sprites.
	 * 			| new.getSprites() == sprites
	 * 
	 * @post The current sprite will be set to the first sprite.
	 * 			| new.getCurrentSprite() == sprites[0]
	 * 
	 * @post The position will be set to the position.
	 * 			| new.getPosition() == position
	 * 
	 * @post The speed will be set to zero.
	 * 			| new.getSpeed() == new Vector<Double>(0.0, 0.0)
	 * 
	 * @post The acceleration will be set to zero.
	 * 			| new.getAcceleration() == new Vector<Double>(0.0, 0.0)
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException when the given position is not valid.
	 * 			| !isValidPosition(position)
	 */
	protected GameObject(int health, int maxHealth, Vector<Double> position, Sprite[] sprites, boolean passable)
				throws IllegalArgumentException {
		
		if (!this.isValidPosition(position)){
			throw new IllegalArgumentException("Position is not valid.");
		}
		
		// maxHealth has to be set before setHealth because it uses maxHealth.
		this.setFacing(1);
		this.passable = passable;
		this.maxHealth = maxHealth;
		this.health = 1;
		this.setHealth(health);
		this.sprites = sprites;
		this.setCurrentSprite(sprites[0]);
		this.motion = new Motion(this, position, new Vector<Double>(0.0, 0.0), new Vector<Double>(0.0, 0.0));
	}
	
	
	/**
	 * Returns whether this game object can have the given world as it's world.
	 * 
	 * @param world
	 * 			The world to check.
	 * 
	 * @return true if this game object can have the given world as it's game world.
	 * 			The given world can not be null.
	 * 			| world != null
	 */
	public boolean canHaveAsWorld(World world) {
		return world != null;
	}
	
	/**
	 * Returns whether this game object has a proper world.
	 * 
	 * @return true if this game object has a proper world.
	 * 			This game object needs to be able to have it's current world as it's game world.
	 * 			| this.canHaveAsWorld()
	 * 			This game object's world needs to contain this game object.
	 * 			| this.getWorld().containsGameObject(this)
	 */
	public boolean hasProperWorld() {
		return this.canHaveAsWorld(this.getWorld()) && this.getWorld().containsGameObject(this);
	}
	
	/**
	 * Returns this game object's world.
	 * 
	 * @return This game object's game world.
	 */
	@Basic
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Sets the given world as this game object's game world.
	 * 
	 * @param world
	 * 			The world to set.
	 * 
	 * @post The given world is registered as this game object's game world.
	 * 			| new.getWorld() == world
	 * 
	 * @post The given world contains this game object.
	 * 			| (new world).containsGameObject(new)
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException if this game object can not have the given world as it's game world.
	 * 			| !this.canHaveAsWorld(world)
	 */
	public void setWorld(World world) throws IllegalArgumentException {
		if (!this.canHaveAsWorld(world)) {
			throw new IllegalArgumentException("This game object can't have the given world as it's game world.");
		}
		this.world = world;
		if (!world.containsGameObject(this)) {
			world.addGameObject(this);
		}
	}
	
	/**
	 * Returns whether this game object belongs to a game world.
	 * 
	 * @return true if a world contains this game object.
	 * 			| this.getWorld() != null && this.getWorld().containsGameObject(this)
	 */
	public boolean inWorld() {
		return this.getWorld() != null && this.getWorld().containsGameObject(this);
	}
	
	/**
	 * Removes this game object from it's world if it's contained by one.
	 * 
	 * @post The GameObject will have no references to the old world and
	 * 			neither will the old world have a reference to this.
	 * 			| new.getWorld() == null && old.getWorld().containsGameObject(new) == false
	 * 			| !new.inWorld()
	 */
	public void removeFromWorld() {
		if (this.inWorld()) {
			this.getWorld().removeGameObject(this);
			this.world = null;
		}
	}
	
	
	/**
	 * Returns whether other game objects shouldn't collide with this game object.
	 * 
	 * @return true if this game object is passable.
	 */
	@Override
	@Basic @Immutable
	public boolean isPassable() {
		return this.passable;
	}
	
	
	/**
	 * Returns the maximum amount of hitpoints this game object can have.
	 * 
	 * @return The maximum amount of hitpoints this game object can have.
	 */
	@Basic @Immutable
	public int getMaximumHealth(){
		return this.maxHealth;
	}

	
	/**
	 * Returns the number of hitpoints this game object has.
	 * 
	 * @return The number of hitpoints this game object has.
	 */
	@Basic
	public int getHealth(){
		return this.health;
	}
	

	/**
	 * Returns whether the object is alive, this means the object should still be
	 * visible in the game world.
	 * 
	 * @return Whether the object is alive or not. The object is considered dead when it's health
	 * 		   is zero and has been zero for longer than Constants.deathTime.
	 * 			| !((this.isHealthZero()) && (this.deathTime > Constants.deathTime))
	 */
	public boolean isAlive() {
		return !(this.isHealthZero() && (this.deathTime >= Constants.deathTime));
	}
	
	
	/**
	 * Returns whether this object's health is equal to zero.
	 * 
	 * @return Whether this object's health is equal to zero.
	 * 			| this.getHealth() == 0
	 */
	public boolean isHealthZero(){
		return this.getHealth() == 0;
	}
	
	
	/**
	 * Increases the health of this game object by the given amount.
	 * Negative numbers are allowed. The health will be kept in the 
	 * zero to maximumHealth range.
	 * 
	 * @param diff
	 * 			The amount with which to increase health.
	 * 
	 * @effect Sets the health to the sum of the current health and diff.
	 * 			| this.setHealth(this.getHealth() + diff)
	 */
	public void increaseHealth(int diff) {
		this.setHealth(this.getHealth() + diff);
	}
	
	
	/**
	 * Returns whether the given health is a valid health for this game object.
	 * 
	 * @param health
	 * 			The health to check for validity.
	 * 
	 * @return Whether the provided health does not exceed the maximum allowed health
	 * 			and is bigger or equal to zero.
	 * 			| (health <= this.maxHealth) && (health >= 0)
	 */
	public boolean isValidHealth(int health){
		return (health <= this.maxHealth) && (health >= 0);
	}
	
	
	/**
	 * Sets the health of this game object to the given health. The health will be kept
	 * in the zero to maximumHealth range.
	 * 
	 * @param health
	 * 			The suggested health for this object.
	 * 
	 * @post Set this object's health to health if health is smaller than the maximum allowed health
	 * 		 otherwise it sets it to the maximum allowed amount. If the health of this object is equal to zero
	 * 		 then setHealth does nothing.
	 * 			| if (!this.isValidHealth(health) && (this.getHealth() != 0))
	 * 			| then new.getHealth() == health
	 * 			| else if (health > this.getMaximumHealth()
	 * 			|      then new.getHealth() == this.getMaximumHealth()
	 * 			|	   else new.getHealth() == 0
	 */
	public void setHealth(int health){
		if (! this.isHealthZero()){
			this.health = Utilities.clipInRange(0, this.getMaximumHealth(), health);
		}
	}
	

	/**
	 * Returns the position of this game object in meters.
	 * 
	 * @return This GameObject's position as a 2D vector in meters.
	 */
	@Basic
	public Vector<Double> getPositionInMeters() {
		return this.motion.getPosition();
	}


	/**
	 * Returns the position of this game object in pixels.
	 * 
	 * @return This game object's position in pixels.
	 */
	@Basic
	public Vector<Integer> getPositionInPixels() {
		return Utilities.metersVectorToPixels(this.getPositionInMeters());
	}


	/**
	 * Sets this game object's position to the given position in meters.
	 * 
	 * @param position
	 * 			The position to set.
	 * 
	 * @throws NullPointerException
	 * 			Throws a NullPointerException when the position is null.
	 * 			| position == null
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException when the position is not valid. See isValidPosition.
	 * 			| !isValidPosition(position)
	 * 
	 * @post The new position will be equal to position
	 * 			| new.getPositionInMeters() == position
	 */
	@Basic
	public void setPositionInMeters(Vector<Double> position) throws NullPointerException,
			IllegalArgumentException {
		if (position == null) {
			throw new NullPointerException("The position can not be null.");
		} else if (!isValidPosition(position)) {
			if (position.y < 0 && position.x >= 0 &&
					((this.hasProperWorld() && position.x < this.getWorld().getSizeInMeters().x)
							|| !this.hasProperWorld())) {
				this.setHealth(0);
			} else {
				throw new IllegalArgumentException("The given position is not valid, see isValidPosition.");
			}
		}
				
		this.motion.setPosition(position);
	}
	
	
	/**
	 * Returns whether the given position is a valid position for this game object.
	 * This means the position lies inside the game world.
	 * 
	 * @param pos
	 * 			The position to check
	 * 
	 * @return Whether pos is valid
	 * 			When this game object has a proper world, the position has to lie in the game world.
	 * 			| (pos.x >= 0) && (pos.x <= bounds.x)
	 *			 && (pos.y >= 0) && (pos.y <= bounds.y)
	 *			Otherwise the position has to be positive
	 *			| (pos.x >= 0) && (pos.y >= 0)
	 */
	public boolean isValidPosition(Vector<Double> pos) {
		if (this.hasProperWorld()) {
			return (pos.x >= 0) && pos.x < this.getWorld().getSizeInMeters().x
					&& (pos.y >= 0) && pos.y < this.getWorld().getSizeInMeters().y;
		}
		return (pos.x >= 0) && (pos.y >= 0);
	}
	
	
	/**
	 * Returns the location of the top right pixel of this game object.
	 * 
	 * @return The location of the top right pixel of this game object.
	 * 			| Vector.add(this.getPositionInPixels(), this.getSize())
	 */
	public Vector<Integer> getTopRightPixel() {
		return Vector.add(this.getPositionInPixels(), this.getSize());
	}
	
	
	/**
	 * Returns the position of the center of this game object in pixels based on it's position
	 * and it's dimensions. If the calculated pixel value isn't an integer, the x and y
	 * components are floored.
	 * 
	 * @return The position of the center of this GameObject.
	 * 			| size = this.getSize()
	 * 			| Vector.add(this.getPositionInPixels(),
	 * 				new Vector<Integer>((int)(size.x * 0.5), (int)(size.y * 0.5)))
	 */
	public Vector<Integer> getCenterInPixels(){
		Vector<Integer> size = this.getSize();
		return Vector.add(this.getPositionInPixels(), new Vector<Integer>((int)(size.x * 0.5), (int)(size.y * 0.5)));
	}
	
	
	/**
	 * Returns whether this game object is standing on impassable terrain or
	 * on an impassable game object.
	 * 
	 * @return Whether this game object is standing on impassable terrain or
	 * 			on an impassable game object.
	 */
	public boolean onGround() {
		
		//TODO: Test this method
		
		Set<Tile> collidingTiles = this.getWorld().getTilesCollidingWithObject(this);
		
		for (Tile tile : collidingTiles) {
			if (!tile.isPassable()) {
				Vector<Integer> overlap = this.getKindOfOverlapWith(tile);
				if (overlap.y > 0) {
					return true;
				}
			}
		}
		
		Set<GameObject> collidingObjects = this.getWorld().getObjectsCollidingWithObject(this);
		
		for (GameObject object : collidingObjects) {
			if (!object.isPassable()) {
				Vector<Integer> overlap = this.getKindOfOverlapWith(object);
				if (overlap.y > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * Returns a set of classes with which the game object can collide.
	 * 
	 * @return A set of classes with which the game object can collide.
	 */
	@Immutable
	protected abstract Set<Class<? extends GameObject>> getCollidableObjectClasses();
	
	
	/**
	 * Returns whether collisions for this game object should be detected
	 * for the given object class.
	 * 
	 * @param object
	 * 			The object to check.
	 * 
	 * @return Whether or not the object should collide with the given object class.
	 * 			| this.getCollidableObjectClasses().contains(objectClass)
	 */
	@Basic
	public boolean collidesWithGameObjectClass(Class<? extends GameObject> objectClass) {
		return this.getCollidableObjectClasses().contains(objectClass);
	}
	
	
	/**
	 * Returns a set of tile types the game object can collide with.
	 * 
	 * @return A set of tile types the game object can collide with.
	 */
	@Immutable
	protected abstract Set<TileType> getCollidableTileTypes();
	
	
	/**
	 * Returns whether collisions for this game object should be detected
	 * for the given tile type.
	 * 
	 * @param type
	 * 			The tile type to check.
	 * 
	 * @return Whether or not the object should collide with the given tile type.
	 * 			| this.getCollidableTileTypes().contains(type)
	 */
	@Basic
	public boolean collidesWithTileType(TileType type) {
		return this.getCollidableTileTypes().contains(type);
	}
	
	
	/**
	 * Returns the speed of this game object in m/s.
	 * 
	 * @return The speed of this game object in m/s.
	 */
	@Basic
	public Vector<Double> getSpeed() {
		return this.motion.getSpeed();
	}
	
	
	/**
	 * Sets the speed of this game object.
	 * 
	 * @param speed
	 * 			The speed to set.
	 * 
	 * @post The speed of this game object will be the given speed.
	 * 			| new.getSpeed() == speed
	 */
	@Basic
	protected void setSpeed(Vector<Double> speed) {
		this.motion.setSpeed(speed);
	}
	
	
	/**
	 * Returns the acceleration of this game object in m/(s^2).
	 * 
	 * @return The acceleration of this game object in m/(s^2).
	 */
	@Basic
	public Vector<Double> getAcceleration() {
		
		return this.motion.getAcceleration();
	}
	
	
	/**
	 * Sets the acceleration of this game object.
	 * 
	 * @param acceleration
	 * 			The acceleration to set.
	 * 
	 * @post The acceleration of this game object will be the given acceleration.
	 * 			| new.getAcceleration() == acceleration
	 */
	@Basic
	public void setAcceleration(Vector<Double> acceleration) {
		
		this.motion.setAcceleration(acceleration);
	}


	/**
	 * Returns the facing of this game object.
	 * 
	 * @return The facing of this game object This is either 1.0 if it's facing right or -1.0 if it's facing left.
	 */
	@Basic
	public double getFacing() {
		return this.facing;
	}


	/**
	 * Sets this GameObject's facing.
	 * 
	 * @param facing
	 * 			The facing to set. This should be either 1 for right facing or -1 for left facing.
	 * 
	 * @pre		The facing should be valid;
	 * 			| GameObject.isValidDirection(facing)
	 * 
	 * @post	The facing will be set to the new facing
	 * 			| new.getFacing() == facing
	 */
	@Basic
	protected void setFacing(double facing) {
		assert GameObject.isValidDirection(facing);
		this.facing = facing;
	}
	
	
	/**
	 * Returns whether the given direction is valid (either 1 or -1).
	 * 
	 * @param direction
	 * 			The direction to check
	 * 
	 * @return Whether the direction is valid (either 1 or -1)
	 * 			| direction == 1 || direction == -1
	 */
	public static boolean isValidDirection(double direction) {
		return direction == 1 || direction == -1;
	}
	

	/**
	 * Returns the sprites list of this game object.
	 * 
	 * @return The sprites list of this game object.
	 */
	@Basic
	protected Sprite[] getSprites(){
		return this.sprites;
	}


	/**
	 * Returns this game object's current sprite.
	 * 
	 * @return This game object's current sprite.
	 */
	@Basic
	public Sprite getCurrentSprite() {
		return this.currentSprite;
	}
	
	
	/**
	 * Set the current sprite of this game object.
	 * 
	 * @param s
	 * 			The sprite to set.
	 * 
	 * @post This game object will have the given sprite as it's current sprite.
	 * 			| new.getCurrentSprite() == s
	 */
	@Basic
	protected void setCurrentSprite(Sprite s){
		this.currentSprite = s;
	}
	
	
	/**
	 * Determines the new currentSprite of this game object.
	 * The standard implementation takes sprite 0 for left facing and
	 * sprite 1 for right facing.
	 * 
	 * @return The current sprite
	 */
	protected Sprite determineCurrentSprite() {
		return this.getSprites()[this.getFacing() == 1.0 ? 1 : 0];
	}
	
	
	/**
	 * Returns the size of this game object in pixels.
	 * 
	 * @return The size of this game object in pixels.
	 */
	@Basic
	public Vector<Integer> getSize() {
		Sprite sprite = this.getCurrentSprite();
		return new Vector<Integer>(sprite.getWidth(), sprite.getHeight());
	}
	
	
	/**
	 * Returns the size of this game object in meters.
	 * 
	 * @return The size of this game object in meters.
	 */
	public Vector<Double> getSizeInMeters() {
		return Utilities.pixelsVectorToMeters(this.getSize());
	}
	
	
	/**
	 * Advances the time of this game object and adjusts it's position,
	 * speed and acceleration accordingly. Small steps will be performed
	 * to handle collisions as well.
	 * 
	 * @param dt
	 * 			The time to advance.
	 * 
	 * @post All properties of this game object will be altered accordingly.
	 * 
	 * @post The current sprite will be set to the correct sprite.
	 * 			| this.getCurrentSprite() == theCorrectSprite
	 */
	public void advanceTime(double dt) {
		
		double time = 0.0;
		while (time < dt) {
			
			double stepTime = this.motion.step(dt - time);
			time += stepTime;
			
			this.handleStep(stepTime);
			
			if (this.isHealthZero()) {
				deathTime += stepTime;
			}
			
			Set<GameObject> collidingObjects = this.getWorld().getObjectsCollidingWithObject(this);
			Set<Tile> collidingTiles = this.getWorld().getTilesCollidingWithObject(this);
			
			this.handleCollisions(collidingObjects, collidingTiles);
		}
		
		this.setCurrentSprite(this.determineCurrentSprite());
	}
	
	
	/**
	 * Handles the basic collisions with terrain and impassable game objects.
	 * 
	 * @param collidingObjects
	 * 			The objects with which the game object is colliding.
	 * 
	 * @param collidingTiles
	 * 			The tiles with which the game object is colliding.
	 */
	private void handleBasicMovementCollisions(Set<GameObject> collidingObjects, Set<Tile> collidingTiles) {
		
		Set<Collidable> collidables = new HashSet<Collidable>();
		collidables.addAll(collidingObjects);
		collidables.addAll(collidingTiles);
		
		Set<Collidable> hardOnes = new HashSet<Collidable>();
		
		// Loop through all collidables
		for (Collidable collidable: collidables) {
			
			if (!collidable.isPassable()) {

				// Get the kind of overlap with the tile
				Vector<Integer> overlap = getKindOfOverlapWith(collidable);

				// If x and y overlap are zero, there is no overlap
				if (overlap.x == 0 || overlap.y == 0) {
					continue;
				}

				// If both x and y overlaps are 1, the response can't be determined yet
				// so we add it to the hard ones set
				if (Math.abs(overlap.x) == 1 && Math.abs(overlap.y) == 1) {
					hardOnes.add(collidable);
					continue;
				}

				// If the x overlap is 1 and the y is not, we need to move the game object
				// horizontally
				if (Math.abs(overlap.x) == 1) {
					// Adjust position
					this.setPositionInMeters(this.getPositionInMeters().addX(overlap.x * Constants.metersPerPixel));
					// Set horizontal speed to 0
					this.setSpeed(this.getSpeed().setX(0.0));

					// Otherwise, if the y overlap is 1 or 2 (when standing on ground) and the
					// x overlap is not, we need to move the game object vertically
				} else if (Math.abs(overlap.y) == 1 || overlap.y == 2) {
					// Calculate a correction for when the y overlap is positive
					int correction = (overlap.y > 0) ? -1 : 0;
					// Adjust position
					this.setPositionInMeters(this.getPositionInMeters().addY((overlap.y + correction) * Constants.metersPerPixel));
					// Set vertical speed to zero.
					this.setSpeed(this.getSpeed().setY(0.0));
				}
			}
		}
	}
	
	
	/**
	 * Handles a time step of dt and updates properties accordingly.
	 * 
	 * @param dt
	 * 			The length of the time step.
	 */
	protected abstract void handleStep(double dt);
	
	
	/**
	 * Handles the collisions for this game object and updates motion and other properties accordingly.
	 * 
	 * @param collidingObjects
	 * 			The objects this game object collides with.
	 * 
	 * @param collidingTiles
	 * 			The tiles this game object collides with.
	 */
	protected void handleCollisions(Set<GameObject> collidingObjects, Set<Tile> collidingTiles){
		
		this.handleBasicMovementCollisions(collidingObjects, collidingTiles);

		for (GameObject object : collidingObjects){
			//Delegate the collision to both parties involved
			//Each party only has to worry about it's own state changes
			handleCollision(object);
			object.handleCollision(this);
		}
	}
	
	/**
	 * Handle the collision of a single game object.
	 * 
	 * @param object
	 * 			The object with which this one collides.
	 */
	protected abstract void handleCollision(GameObject object);
	
	
	/**
	 * Returns whether or not this game object is currently in contact
	 * with a tile of the given type.
	 * 
	 * @param type
	 * 			The tile type to check contact with.
	 * 
	 * @return true if this game object is in contact with a tile of
	 * 			the given type.
	 */
	protected boolean inContactWithTileOfType(TileType type) {
		
		Set<Tile> collidingTiles = this.getWorld().getTilesCollidingWithObject(this);
		
		for (Tile tile : collidingTiles) {
			if (tile.getType() == type) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Returns the kind of overlap with the given collidable.
	 * 
	 * @param collidable
	 * 			| The collidable to get the kind of overlap with.
	 * 
	 * @return A 2D vector representing the kind of overlap.
	 * 			When collidable is a Tile object:
	 * 			| this.getKindOfOverlapWith((Tile) collidable)
	 * 			When collidable is a GameObject object:
	 * 			| this.getKindOfOverlapWith((GameObject) collidable)
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException when the collidable's type is unknown.
	 * 			The type should be Tile or GameObject.
	 * 			| !(collidable instanceof Tile || collidable instanceof GameObject)
	 */
	protected Vector<Integer> getKindOfOverlapWith(Collidable collidable) throws IllegalArgumentException {
		if (collidable instanceof Tile) {
			return this.getKindOfOverlapWith((Tile) collidable);
		} else if (collidable instanceof GameObject) {
			return this.getKindOfOverlapWith((GameObject) collidable);
		} else {
			throw new IllegalArgumentException("The given collidable is of an unknown type. It should be Tile or GameObject.");
		}
	}
	
	
	/**
	 * Returns a 2D vector representing the kind of overlap of the given tile
	 * with this game object. When the overlap comes from lower coördinates
	 * (on the left or on the bottom) a positive overlap value is returned.
	 * When the overlap comes from higher coördinates (on the right and on 
	 * the top) a negative value is returned.
	 * 
	 * @param tile
	 * 			The tile to get the kind of overlap with.
	 * 
	 * @return A 2D vector representing the kind of overlap.
	 */
	protected Vector<Integer> getKindOfOverlapWith(Tile tile) {
		return getKindOfOverlapWithRect(tile.getPositionInPixels(), new Vector<>(tile.getSizeInPixels(), tile.getSizeInPixels()));
	}
	
	/**
	 * Returns a 2D vector representing the kind of overlap of the given GameObject
	 * with this game object. When the overlap comes from lower coördinates
	 * (on the left or on the bottom) a positive overlap value is returned.
	 * When the overlap comes from higher coördinates (on the right and on 
	 * the top) a negative value is returned.
	 * 
	 * @param obj
	 * 			The obj to get the kind of overlap with.
	 * 
	 * @return A 2D vector representing the kind of overlap.
	 */
	protected Vector<Integer> getKindOfOverlapWith(GameObject obj){
		return getKindOfOverlapWithRect(obj.getPositionInPixels(), obj.getSize());
	}
	
	
	/**
	 * Returns a 2D vector representing the kind of overlap of the given position and size
	 * with this game object. When the overlap comes from lower coördinates
	 * (on the left or on the bottom) a positive overlap value is returned.
	 * When the overlap comes from higher coördinates (on the right and on 
	 * the top) a negative value is returned.
	 * 
	 * @param otherPos
	 * 			The position of the other object.
	 * 
	 * @param otherSize
	 * 			The size of the other object.
	 * 
	 * @return A 2D vector representing the kind of overlap.
	 */
	private Vector<Integer> getKindOfOverlapWithRect(Vector<Integer> otherPos, Vector<Integer> otherSize) {
		Vector<Integer> selfPos = this.getPositionInPixels();
		Vector<Integer> selfSize = this.getSize();
		
		Vector<Integer> overlap = new Vector<>(0, 0);
		
		if (otherPos.x <= selfPos.x) {
			overlap = overlap.setX(Math.min(selfSize.x, otherPos.x + otherSize.x - selfPos.x));
		} else {
			overlap = overlap.setX(Math.min(selfSize.x, otherPos.x - selfPos.x - selfSize.x));
		}
		
		if (otherPos.y <= selfPos.y) {
			overlap = overlap.setY(Math.min(selfSize.y, otherPos.y + otherSize.y - selfPos.y));
		} else {
			overlap = overlap.setY(Math.min(selfSize.y, otherPos.y - selfPos.y - selfSize.y));
		}
		
		return overlap;
	}
	
	
	/**
	 * Returns whether this game object overlaps with the given game object.
	 * 
	 * @param object
	 * 			The game object to check overlap with.
	 * 
	 * @return true if this game object overlaps with the given game object.
	 * 			| !(object.getPositionInPixels().x + object.getSize().x <= self.getPositionInPixels().x
				|	|| object.getPositionInPixels().x >= self.getPositionInPixels().x + self.getSize().x
				|	|| object.getPositionInPixels().y + object.getSize().y <= self.getPositionInPixels().y
				|	|| object.getPositionInPixels().y >= self.getPositionInPixels().y + self.getSize().y)
	 */
	public boolean doesOverlapWith(GameObject object) {
		return this.doesOverlapWithRect(object.getPositionInPixels(), object.getSize());
	}
	
	
	/**
	 * Returns whether this game object overlaps with the given tile.
	 * 
	 * @param tile
	 * 			The tile to check overlap with.
	 * 
	 * @return true if this game object overlaps with the given tile.
	 * 			| !(tile.getPositionInPixels().x + tile.getSize() <= self.getPositionInPixels().x
				|	|| tile.getPositionInPixels().x >= self.getPositionInPixels().x + self.getSize().x
				|	|| tile.getPositionInPixels().y + tile.getSize() <= self.getPositionInPixels().y
				|	|| tile.getPositionInPixels().y >= self.getPositionInPixels().y + self.getSize().y)
	 */
	public boolean doesOverlapWith(Tile tile) {
		return this.doesOverlapWithRect(tile.getPositionInPixels(),
				new Vector<>(tile.getSizeInPixels(), tile.getSizeInPixels()));
	}
	
	
	/**
	 * Returns whether this game object overlaps with the given rectangle.
	 * 
	 * @param pos
	 * 			The bottom left position of the rectangle.
	 * 
	 * @param size
	 * 			The size of the rectangle.
	 * 
	 * @return true if this game object and the rectangle overlap.
	 */
	private boolean doesOverlapWithRect(Vector<Integer> pos, Vector<Integer> size) {
		
		Vector<Integer> selfPos = this.getPositionInPixels();
		Vector<Integer> selfSize = this.getSize();
		
		return !(pos.x + size.x <= selfPos.x
				|| pos.x >= selfPos.x + selfSize.x
				|| pos.y + size.y <= selfPos.y
				|| pos.y >= selfPos.y + selfSize.y);
	}
}
