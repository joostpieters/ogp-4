package jumpingalien.model;

import be.kuleuven.cs.som.annotate.*;
import jumpingalien.util.Sprite;
import jumpingalien.util.Util;
import jumpingalien.model.Constants;
import jumpingalien.model.Utilities;

/**
 * A class representing a single Mazub.
 * 
 * @invar Mazub's bottom-left position remains within the bounds of the world.
 * 			| Mazub.isValidPosition(this.getPosition())
 * 
 * @invar Mazub's horizontal speed does not exceed the maximum speed.
 * 			| Mazub.isValidSpeed(this.getSpeed())
 * 
 * @author Rugen Heidbuchel & Menno Vanfrachem
 * @version 1.0
 */
public class Mazub extends GameObject {
	private final double vxInit; // Initial moving speed
	private final double vxMax; // Max running speed (not ducking)
	
	private boolean isMoving = false, isDucking = false, hasMoved = false;
	private double movingTime = 0, timeSinceMoving = 0;
	
	private Vector2D<Double> speed; // speed in m/s, position in m
	// CONSTANTS
	// Speed
	public final static double vxMaxDucking = 1.0; // Max running speed while ducking
	public final static double vInitJump = 8.0; // Initial jump speed
	
	// Acceleration
	public final static Vector2D<Double> maxAcceleration = new Vector2D<>(0.9, -10.0);

	/**
	 * @param x
	 * 			The x position of this Mazub.
	 * 
	 * @param y
	 * 			The y position of this Mazub.
	 * 
	 * @param sprites
	 * 			An array of sprites containing Mazubs animations.
	 * 
	 * @param vxInit
	 * 			Mazubs initial speed when starting to walk.
	 * 
	 * @param vxMax
	 * 			Mazubs maximum speed while walking.
	 * 
	 * @param direction
	 * 			The direction Mazub is facing. -1 means he's facing left, +1 means he's facing right.
	 * 
	 * @throws NullPointerException
	 * 			Throws a NullPointerException when sprites is null.
	 * 			| sprites == null
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException when the position and/or direction are not valid.
	 * 			| !Mazub.isValidPosition(new Vector2D<>(x, y)) || !Mazub.isValidDirection(direction)
	 */
	public Mazub(Vector2D<Double> position, Sprite[] sprites, double vxInit, double vxMax, double direction) throws NullPointerException, IllegalArgumentException{
		
		// GameObject
		super(100, 500, position, sprites);
		
		if (! Mazub.isValidDirection(direction)) {
			throw new IllegalArgumentException("direction is not valid.");
		}
		if (sprites == null){
			throw new NullPointerException("Sprites can't be null.");
		}
		assert vxInit >= 1.0;
		assert vxMax >= vxInit;
		
		this.setSpeed(new Vector2D<>(0.0, 0.0));
		this.setCurrentSprite(sprites[0]);
		this.vxInit = vxInit;
		this.vxMax = vxMax;
		this.setFacing(direction);
	}
	
	@Basic @Immutable
	public static double getMaxSpeedWhileDucking() {
		return vxMaxDucking;
	}

	@Basic @Immutable
	public static double getInitialJumpSpeed() {
		return vInitJump;
	}

	@Basic @Immutable
	public static Vector2D<Double> getMaxAcceleration() {
		return maxAcceleration;
	}
	

	/**
	 * @param speed
	 * 			The speed to check
	 * 
	 * @return Whether speed.x's magnitude doesn't exceed the maximum horizontal speed.
	 * 			| Math.abs(speed.x) <= this.getMaxHorizontalSpeed()
	 */
	public boolean isValidSpeed(Vector2D<Double> speed) {
		return Math.abs(speed.x) <= this.getMaxHorizontalSpeed();
	}
	
	
	/**
	 * @return The maximum horizontal speed of this Mazub in m/s.
	 */
	private double getMaxHorizontalSpeed(){
		return this.isDucking ? Mazub.getMaxSpeedWhileDucking() : this.vxMax;
	}
	
	/**
	 * @return This Mazub's speed as a 2D vector in m/s.
	 */
	@Basic
	public Vector2D<Double> getSpeed(){
		return this.speed;
	}
	
	/**
	 * Sets this Mazub's speed to the given speed.
	 * 
	 * @param speed
	 * 			The speed to set.
	 */
	@Basic
	private void setSpeed(Vector2D<Double> speed) {
		this.speed = speed;
	}
	
	/**
	 * @return A 2-dimensional vector of this Mzaub's acceleration in m/s.
	 */
	public Vector2D<Double> getAcceleration(){
		Vector2D<Double> maxAcc = Mazub.getMaxAcceleration();
		Vector2D<Double> acc = new Vector2D<>(0.0, 0.0);
		if (isMoving){
			acc.x = this.getFacing() * maxAcc.x;
		}
		if (!onGround()){
			acc.y = maxAcc.y;
		}
		return acc;
	}
	
	/**
	 * @return	This Mazub's height in pixels.
	 */
	@Basic
	public int getHeight(){
		return this.getCurrentSprite().getHeight();
	}

	/**
	 * @return	This Mazub's width in pixels.
	 */
	@Basic
	public int getWidth(){
		return this.getCurrentSprite().getWidth();
	}
	
	/**
	 * @return Whether this Mazub is touching the ground or not.
	 * 			| if (this.getCurrentPosition().y == 0.0)
	 * 			| then true
	 * 			| else false
	 */
	public boolean onGround(){
		return Util.fuzzyEquals(this.getPosition().y, 0.0);
	}
	
	
	
	
	/**
	 * Advances the time for this Mazub with the given time interval and updates position, speed and acceleration.
	 * 
	 * @param dt
	 * 			The time that has passed in the game world since last calling this method.
	 * 
	 * @throws	IllegalArgumentException
	 * 			| (dt < 0) || (dt > Constants.maxTimeInterval)
	 */
	public void advanceTime(double dt) throws IllegalArgumentException{
		
		// check for exceptions
		if (Double.isNaN(dt)) {
			throw new IllegalArgumentException("Delta time can not be NaN.");
		}
		if (dt > Constants.maxTimeInterval){
			throw new IllegalArgumentException(String.format("Delta time may not exceed %.5fs.", Constants.maxTimeInterval));
		}
		if (dt < 0){
			throw new IllegalArgumentException("Delta time has to be non-negative.");
		}
		
		// update times
		if (isMoving) {
			this.movingTime += dt;
		} else {
			this.timeSinceMoving += dt;
		}
		
		// update movement
		this.updateMovement(dt);
		
		// determine and set the new current sprite after the time and movement have been updated
		this.determineCurrentSprite();
	}

	/**
	 * Updates this Mazub's position and speed using the given time interval.
	 * 
	 * @param dt
	 * 			The passed time interval since the last update in seconds.
	 */
	private void updateMovement(double dt) {
		
		// Set some variables so we need to write less. The variables are references because Vector2D is a class, so setting also works.
		Vector2D<Double> acc = this.getAcceleration();
		Vector2D<Double> speed = this.getSpeed();
		Vector2D<Double> position = this.getPositionInMeters();
		
		// Update x for position and speed
		position.x += speed.x * dt + acc.x * dt * dt / 2.0;
		speed.x += acc.x * dt;
		
		// Keep x position in bounds
		position.x = Utilities.clipInRange(0.0, Constants.screenSize.x/100, position.x);
		
		// Keep horizontal speed in bounds
		speed.x = Utilities.clipInRange(-this.getMaxHorizontalSpeed(),
										this.getMaxHorizontalSpeed(),
										speed.x);
		
		// Update y for position and speed
		position.y += speed.y * dt + acc.y * dt * dt / 2.0;
		speed.y += acc.y * dt;
		
		// Keep y position in bounds
		position.y = Utilities.clipInRange(0.0, Constants.screenSize.y/100, position.y);
		
		// if Mazub is on ground, then vertical speed has to be set to 0
		if (this.onGround()) {
			speed.y = 0.0;
		}
	}
	

	/**
	 * Determines and sets the new current sprite.
	 */
	private void determineCurrentSprite() {
		
		Sprite[] sprites = this.getSprites();
		Sprite currentSprite = this.getCurrentSprite();
		int m = (sprites.length - 8) / 2 - 1;
		boolean recentlyMoved = timeSinceMoving < 1 && this.hasMoved;
		if (!(isMoving || isDucking)){
			if (!recentlyMoved){
				currentSprite = sprites[0];
			}
			else{
				currentSprite = this.getFacing() == 1 ?
									 sprites[2] : sprites[3];
			}
		}
		if (!isMoving && isDucking && (!recentlyMoved)){
			currentSprite = sprites[1];
		}
		if (isMoving && ! onGround() && !isDucking){
			currentSprite = this.getFacing() == 1 ?
								 sprites[4] : sprites[5];
		}
		if (isDucking && (isMoving || recentlyMoved)){
			currentSprite = this.getFacing() == 1 ?
								 sprites[6] : sprites[7];
		}
		if (!(! onGround() || isDucking) && isMoving){
			int animationIndex = ((int)(this.movingTime/0.075)) % (m+1);
			currentSprite = this.getFacing() == 1 ?
								 sprites[8+animationIndex] : sprites[9+m+animationIndex];
		}
		this.setCurrentSprite(currentSprite);
	}
	
	/**
	 * Starts this Mazub's movement in the given direction.
	 * 
	 * @param direction
	 * 			The direction to start moving in.
	 * 
	 * @pre		Direction should be valid.
	 * 			| Mazub.isValidDirection(direction)
	 * 
	 * @post	The horizontal speed shall be set to +- vxInit.
	 * 			| new.getSpeed().x == direction * this.vxInit
	 * 
	 * @post	isMoving shall be set to true.
	 * 			| new.isMoving == true
	 */
	public void startMove(double direction) {
		assert Mazub.isValidDirection(direction);
		this.isMoving = true;
		this.setFacing(direction);
		this.getSpeed().x = direction * this.vxInit;
		this.movingTime = 0;
	}
	
	/**
	 * Ends this Mazub's movement.
	 */
	public void endMove() {
		this.isMoving = false;
		this.getSpeed().x = 0.0;
		this.hasMoved = true;
		this.timeSinceMoving = 0;
	}
	
	
	/**
	 * Starts the jump of this Mazub.
	 */
	public void startJump() {
		this.speed.y = Mazub.getInitialJumpSpeed();
	}
	
	/**
	 * Ends the jump of this Mazub.
	 * 
	 * @post	If the speed of this Mazub is bigger than 0, the vertical speed of this Mazub will be 0.
	 * 			| if (this.getSpeed().y > 0)
	 * 			|	new.getSpeed().y == 0
	 * 
	 * @post	If the speed of this Mazub is not bigger than 0, the vertical speed will remain the same.
	 * 			| if (this.getSpeed().y <= 0)
	 * 			|	new.getSpeed().y == this.getSpeed().y
	 */
	public void endJump() {
		if (this.getSpeed().y > 0) {
			this.getSpeed().y = 0.0;
		}
	}
	
	
	/**
	 * Starts the duck of this Mazub.
	 */
	public void startDuck() {
		this.isDucking = true;
	}
	
	/**
	 * Ends the duck of this Mazub.
	 */
	public void endDuck() {
		this.isDucking = false;
	}
}
