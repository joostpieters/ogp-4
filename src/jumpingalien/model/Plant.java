package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;

import jumpingalien.model.Reactions.GameObjectCollisionDamager;
import jumpingalien.model.Reactions.PlantMazubCollisionDamager;
import jumpingalien.util.Sprite;

public class Plant extends GameObject {
	
	private double directionTime = Constants.plantMoveTime;
	
	/**
	 * Creates a plant with the given position and sprites.
	 * 
	 * @param position
	 * 			The position of the plant in meters.
	 * 
	 * @param sprites
	 * 			The sprites of the plant.
	 * 
	 * @effect The GameObject constructor will be called.
	 * 			| super(1, 1, position, sprites, true);
	 */
	public Plant(Vector<Double> position, Sprite[] sprites){
		
		super(1, 1, position, sprites, true);
		this.setSpeed(this.getSpeed().setX(Constants.plantSpeed));
		
		this.addCollisionDamager(new PlantMazubCollisionDamager(this, -1, 0));
	}
	
	
	@Override
	protected Set<Class<? extends GameObject>> getCollidableObjectClasses() {
		
		//TODO: Check whether it does not give problems returning an empty set here
		//		because the interaction of a plant with Mazub will be handled by Mazub.
		
		return new HashSet<Class<? extends GameObject>>();
	}
	
	
	@Override
	protected Set<TileType> getCollidableTileTypes() {
		
		HashSet<TileType> collidables = new HashSet<TileType>();
		collidables.add(TileType.GROUND);
		
		return collidables;
	}
	
	
	@Override
	protected void handleStep(double dt) {
		
		double timeLeft = Constants.plantMoveTime - directionTime;
		directionTime = (directionTime + dt) % Constants.plantMoveTime;
		
		if (dt > timeLeft){
			this.setSpeed(this.getSpeed().setX(this.getFacing() * Constants.plantSpeed));
			this.setFacing(this.getFacing() * -1);
		}
	}
	
	
//	@Override
//	protected void handleCollision(GameObject object) {
//		if (this.isAlive()){
//			if ((object instanceof Mazub) && !(this.isHealthZero()) && (object.getHealth() != object.getMaximumHealth())){
//				this.setHealth(0);
//			}
//		}
		
	}
	
//	@Override
//	public void advanceTime(double dt){
//		double timeLeft = maxDirectionTime - directionTime;
//		directionTime = (directionTime + dt) % maxDirectionTime;
//		Vector<Double> newPosition = new Vector<>(this.getPositionInMeters());
//		// If the alternation of direction happens in this time interval
//		// process the part of the interval before the alternation first.
//		if (dt > timeLeft){
//			newPosition = newPosition.setX(newPosition.x + this.getFacing() * this.speed * timeLeft);
//			dt = dt - timeLeft;
//			this.setFacing(this.getFacing() * -1);
//		}
//		// Then update the position with what's left
//        newPosition = newPosition.setX(newPosition.x + this.getFacing() * this.speed * dt);
//        this.setPosition(newPosition);
//        this.setCurrentSprite(this.getSprites()[this.getFacing() < 0 ? 0 : 1]);
//	}