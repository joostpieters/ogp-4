package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public class Mazub {
	private Transform transform;
	private Sprite[] sprites;
	private Sprite currentSprite;
	
	private final double vxInit;
	private final double vxMax;
	private final double vxMaxDuck = 1;
	private static final double ax = 0.9;
	
	boolean isMoving = false, isJumping = false, isDucking = false;
	double currentTime = 0;
	double endMovingTime = 0;
	
	// Speed and position use meters.
	private Vector2D<Double> speed, position;
	
	public Mazub(Sprite[] sprites, double vxInit, double vxMax){
		this.sprites = sprites;
		this.currentSprite = sprites[0];
		this.vxInit = vxInit;
		this.vxMax = vxMax;
	}
	
	@Basic
	public Sprite getCurrentSprite(){
		return currentSprite;
	}
	
	@Basic
	public Vector2D getCurrentSpeed(){
		return this.speed;
	}
	
	@Basic
	public Vector2D getCurrentPosition(){
		return this.position;
	}
	
	
	/**
	 * @param dt
	 * 			The time that has passed in the game world since last calling this method.
	 * @throws	IllegalArgumentException
	 * 			| (dt < 0) || (dt > 0.2)
	 */
	public void advanceTime(double dt) throws IllegalArgumentException{
		if (dt > 0.2){
			throw new IllegalArgumentException("Delta time may not exceed 0.2s.");
		}
		if (dt < 0){
			throw new IllegalArgumentException("Delta time has to be non-negative.");
		}
		//TODO Account for overflow somehow?
		this.currentTime += dt;
		//TODO Add other time related stuff in here
		
		//currentSprite gets determined AFTER Mazub's state has been updated
		this.determineCurrentSprite();
	}

	private void determineCurrentSprite() {
		//TODO Check of 'm' klopt
		int m = (sprites.length - 9) / 2;
		double timeSinceMoving = this.currentTime - this.endMovingTime;
		boolean recentlyMoved = timeSinceMoving < 1;
		if (!(isMoving || isDucking)){
			if (!recentlyMoved){
				this.currentSprite = this.sprites[0];
			}
			else{
				this.currentSprite = this.transform.facing == Transform.Direction.RIGHT ?
									 this.sprites[2] : this.sprites[3];
			}
		}
		if (!isMoving && isDucking && (!recentlyMoved)){
			this.currentSprite = this.sprites[1];
		}
		if (isMoving && isJumping && !isDucking){
			this.currentSprite = this.transform.facing == Transform.Direction.RIGHT ?
								 this.sprites[4] : this.sprites[5];
		}
		if (isDucking && (isMoving || recentlyMoved)){
			this.currentSprite = this.transform.facing == Transform.Direction.RIGHT ?
								 this.sprites[6] : this.sprites[7];
		}
		if (!(isJumping || isDucking) && isMoving){
			//TODO Implement the animation
			this.currentSprite = this.transform.facing == Transform.Direction.RIGHT ?
								 this.sprites[8] : this.sprites[9+m];
		}
	}
	
	
	/**
	 * Starts this Mazub's movement in the given direction.
	 * Nominally
	 * 
	 * @param direction
	 * 			The direction to start moving in.
	 */
	public void startMove(Transform.Direction direction) {
		
		
	}
	
	/**
	 * Ends this Mazub's movement.
	 */
	public void endMove() {
		
		
	}
	
	
	/**
	 * Starts the jump of this Mazub.
	 * Defensively
	 */
	public void startJump() {
		
		
	}
	
	/**
	 * Ends the jump of this Mazub.
	 * Defensively
	 */
	public void endJump() {
		
		
	}
	
	
	/**
	 * Defensively
	 */
	public void startDuck() {
		
		
	}
	
	/**
	 * Defensively
	 */
	public void endDuck() {
		
		
	}
}