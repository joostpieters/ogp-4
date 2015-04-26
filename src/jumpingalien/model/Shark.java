package jumpingalien.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jumpingalien.model.reactions.GameObjectCollisionDamager;
import jumpingalien.model.reactions.TerrainCollisionDamager;
import jumpingalien.model.reactions.TerrainDamageInfo;
import jumpingalien.util.Sprite;

/**
 * @author Rugen en Menno
 * 
 * A public class representing a shark game object.
 * 
 * @invar The speed of a shark will never be bigger than it's maximum allowed
 * 			horizontal speed.
 * 			| Math.abs(this.getSpeed().x) <= Constants.sharkMaxHorizontalSpeed 
 * 
 * @invar See GameObject.
 */
public class Shark extends GameObject {
	
	/**
	 * How much time there is left in the current movement period.
	 */
	private double moveTimeLeft = 0;
	
	/**
	 * The number of movement periods without jumping there have been
	 * since the last jumping period.
	 */
	private double movePeriodCount = 0;
	
	/**
	 * Whether or not the shark is jumping.
	 */
	private boolean jumping = false;
	
	
	/**
	 * Creates a shark with the given position and sprites.
	 * 
	 * @param position
	 * 			The position in the game world in pixels.
	 * 
	 * @param sprites
	 * 			The sprite list.
	 * 
	 * @effect Calling GameObject's constructor.
	 * 			| super(Constants.sharkBeginHealth, Constants.sharkMaxHealth, position, sprites)
	 */
	public Shark(Vector<Double> position, Sprite[] sprites) {
		super(Constants.sharkBeginHealth, Constants.sharkMaxHealth, position, sprites);
		
		Collection<TerrainDamageInfo> terrainInfos= new HashSet<>();
		terrainInfos.add(new TerrainDamageInfo(TileType.MAGMA, Constants.magmaDamage, 0));
		terrainInfos.add(new TerrainDamageInfo(TileType.AIR, Constants.sharkAirDamage, Constants.terrainDamageInterval));
		this.addCollisionDamager(new TerrainCollisionDamager(this, Constants.terrainDamageInterval, terrainInfos));

		Collection<Class<? extends GameObject>> damageClasses = new HashSet<Class<? extends GameObject>>();
		damageClasses.add(Mazub.class);
		damageClasses.add(Slime.class);
		this.addCollisionDamager(new GameObjectCollisionDamager(this, Constants.sharkEnemyDamage, Constants.enemyDamageInterval, damageClasses));
	}
	
	
	@Override
	protected Set<Class<? extends GameObject>> getCollidableObjectClasses() {
		
		HashSet<Class<? extends GameObject>> collidables = new HashSet<Class<? extends GameObject>>();
		collidables.add(Mazub.class);
		collidables.add(Slime.class);
		collidables.add(Shark.class);
		
		return collidables;
	}
	
	
	@Override
	protected Set<TileType> getCollidableTileTypes() {
		
		HashSet<TileType> collidables = new HashSet<TileType>();
		collidables.add(TileType.GROUND);
		collidables.add(TileType.AIR);
		collidables.add(TileType.WATER);
		collidables.add(TileType.MAGMA);
		
		return collidables;
	}
	
	
	/**
	 * Overrides the setSpeed method of gameObject to clip the speed within the allowed range.
	 * 
	 * @post The horizontal speed of this shark (in absolute value) will not be bigger than the
	 * 			maximum allowed horizontal speed.
	 * 			| Math.abs(new.getSpeed().x) <= Constants.sharkMaxHorizontalSpeed
	 */
	@Override
	public void setSpeed(Vector<Double> speed) {
		
		super.setSpeed(new Vector<Double>(Utilities.clipInRange(-Constants.sharkMaxHorizontalSpeed,
											Constants.sharkMaxHorizontalSpeed,
											speed.x), speed.y));
	}
	
	
	/**
	 * Returns whether this shark's top perimeter overlaps with water.
	 * 
	 * @return true if this shark's top perimeter overlaps with water.
	 */
	private boolean topInWater() {
		
		Set<Tile> collidingTiles = this.getWorld().getTilesCollidingWithObject(this);
		
		for (Tile tile : collidingTiles) {
			
			if (tile.getType() == TileType.WATER) {
				
				Vector<Integer> overlap = this.getKindOfOverlapWith(tile);
				
				if (overlap.y < 0 || overlap.y == this.getSizeInPixels().y) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	/**
	 * Returns whether this shark's bottom perimeter overlaps with water.
	 * 
	 * @return true if this shark's bottom perimeter overlaps with water.
	 */
	private boolean bottomInWater() {
		
		Set<Tile> collidingTiles = this.getWorld().getTilesCollidingWithObject(this);
		
		for (Tile tile : collidingTiles) {
			
			if (tile.getType() == TileType.WATER) {
				
				Vector<Integer> overlap = this.getKindOfOverlapWith(tile);
				
				if (overlap.y > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	@Override
	protected void handleStep(double dt) {
		
		if (moveTimeLeft <= 0) {
			
			this.stopMove();
			
			double direction = Math.rint(Math.random()) == 0 ? -1.0 : 1.0;
			this.startMove(direction);
			
		} else {
			
			moveTimeLeft -= dt;
		}
	}	
	
	
	@Override
	protected void handleCollisions(Set<GameObject> collidingObjects,
			Set<Tile> collidingTiles) {
		super.handleCollisions(collidingObjects, collidingTiles);
		
		if (!(this.onGround() || this.topInWater())) {
			this.setAcceleration(this.getAcceleration().setY(Constants.gravityAcceleration));
		} else if (this.getAcceleration().y == Constants.gravityAcceleration) {
			this.setSpeed(this.getSpeed().setY(0.0));
			this.setAcceleration(this.getAcceleration().setY(0.0));
		}
	}
	
	
	/**
	 * Starts moving in the given direction.
	 * 
	 * @param direction
	 * 			The direction to start moving in.
	 * 			1.0 means to the right, -1.0 means to the left.
	 */
	private void startMove(double direction) {
		
		this.setFacing(direction);
		this.setAcceleration(this.getAcceleration().setX(Constants.sharkHorizontalAcceleration * direction));
		moveTimeLeft = Constants.sharkMinMoveTime + Math.random() *
				(Constants.sharkMaxMoveTime - Constants.sharkMinMoveTime);
		
		// If there have been 4 move periods, jump 50% of the times
		if (movePeriodCount > 4 && Math.rint(Math.random()) == 0
				&& (this.onGround() || this.bottomInWater())) {
			
			this.startJump();
		
		// Otherwise move up or down
		} else {
			
			double vertAccDir = Math.rint(Math.random()) == 0 ? -1.0 : 1.0;
			this.startMoveVertical(vertAccDir);
		}
	}
	
	
	/**
	 * Stops the movement of this shark.
	 */
	private void stopMove() {
		
		this.setSpeed(this.getSpeed().setX(0.0));
		this.stopMoveVertical();
		this.stopJump();
	}
	
	
	/**
	 * Starts the jump of this shark.
	 */
	private void startJump() {
		
		this.setSpeed(this.getSpeed().setY(Constants.sharkInitialJumpSpeed));
		this.jumping = true;
		movePeriodCount = 0;
	}
	
	
	/**
	 * Stops the jump of this shark. This means the vertical speed is set
	 * to zero if the vertical speed is bigger than zero (when it's not
	 * falling).
	 */
	private void stopJump() {
		
		if (this.getSpeed().y > 0) {
			this.setSpeed(this.getSpeed().setY(0.0));
		}
		this.jumping = false;
	}
	
	
	/**
	 * Starts the vertical movement of this shark in the given direction.
	 * 
	 * @param direction
	 * 			The direction to start the vertical movement in.
	 * 			1.0 means up, -1.0 means down.
	 */
	private void startMoveVertical(double direction) {
		
		this.setAcceleration(this.getAcceleration().setY(Constants.sharkVerticalAcceleration * direction));
		movePeriodCount += 1;
	}
	
	
	/**
	 * Stops the vertical movement of this shark.
	 */
	private void stopMoveVertical() {
		
		if (!this.jumping) {
			this.setSpeed(this.getSpeed().setY(0.0));
		}
		this.setAcceleration(this.getAcceleration().setY(0.0));
	}
}
