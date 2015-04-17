package jumpingalien.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jumpingalien.util.ModelException;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class representing a rectangular game world, also responsible of managing all game objects it contains.
 * 
 * @author Rugen and Menno 
 *
 */
public class World {
	
	private boolean isTerminated;

	private final int tileSize;
	private final Vector<Integer> nbTiles;
	private Vector<Integer> visibleWindowBottomLeft;
	private Vector<Integer> visibleWindowTopRight;
	private final Vector<Integer> targetTilePosition;
	
	private TileType[][] tiles;
	
	private Mazub mazub;
	private Set<GameObject> objects = new HashSet<>();
	
	@Raw
	public World(int tileSize, int nbTilesX, int nbTilesY,
			int visibleWindowWidth, int visibleWindowHeight, int targetTileX,
			int targetTileY) {
		
		this.tileSize = tileSize;
		this.nbTiles = new Vector<>(nbTilesX, nbTilesY);
		this.visibleWindowBottomLeft = new Vector<>(0, 0);
		this.visibleWindowTopRight = new Vector<>(visibleWindowWidth, visibleWindowHeight);
		this.targetTilePosition = new Vector<>(targetTileX, targetTileY);
		this.tiles = new TileType[nbTilesX][nbTilesY];
		
		for (int x = 0; x < nbTilesX; x++) {
			for (int y = 0; y < nbTilesY; y++) {
				this.tiles[x][y] = TileType.AIR;
			}
		}
	}
	
	
	/**
	 * Terminates this game world.
	 * 
	 * @post The world will be terminated
	 * 			| new.isTerminated() == true
	 */
	public void terminate() {
		//TODO: Inverse connections?
		this.mazub = null;
		this.objects = null;
		this.isTerminated = true;
	}
	
	
	/**
	 * @return true if this game world is terminated.
	 */
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	
	/**
	 * @return The size of one tile in the game world in pixels.
	 */
	@Basic
	@Immutable
	public int getTileSize() {
		return this.tileSize;
	}
	
	
	/**
	 * @return The size of one tile in the game world in meters.
	 */
	@Immutable
	public double getTileSizeInMeters() {
		return Utilities.pixelsToMeters(this.getTileSize());
	}
	
	
	/**
	 * @return A 2D vector containing the number of tiles in the x, respectively y directions.
	 */
	@Basic
	@Immutable
	public Vector<Integer> getNumberOfTiles() {
		return new Vector<>(this.nbTiles);
	}
	
	
	/**
	 * @return A 2D vector representing the size of the game world in pixels.
	 */
	@Immutable
	public Vector<Integer> getSizeInPixels() {
		return new Vector<>(this.getNumberOfTiles().x * this.getTileSize(), this.getNumberOfTiles().y * this.getTileSize());
	}
	
	
	/**
	 * @return A 2D vector representing the size of the game world in meters.
	 */
	@Immutable
	public Vector<Double> getSizeInMeters() {
		return Utilities.pixelsVectorToMeters(this.getSizeInPixels());
	}
	
	
	/**
	 * @param pixel
	 * 			The pixel to check.
	 * 
	 * @return true if the pixel lies in the game world.
	 */
	public boolean pixelInWorld(Vector<Integer> pixel) {
		Vector<Integer> worldSize = this.getSizeInPixels();
		return pixel.x >= 0 && pixel.x < worldSize.x
				&& pixel.y >= 0 && pixel.y < worldSize.y;
	}
	
	
	/**
	 * Returns whether the given tile position lies in the game world.
	 * 
	 * @param tile
	 * 			The tile position to check.
	 * 
	 * @return true if the tile lies in the game world.
	 */
	public boolean tilePositionInWorld(Vector<Integer> tile) {
		Vector<Integer> numberOfTiles = this.getNumberOfTiles();
		return tile.x >= 0 && tile.x < numberOfTiles.x
				&& tile.y >= 0 && tile.y < numberOfTiles.y;
	}
	
	
	/**
	 * Returns the position of the bottom left pixel of the tile at the given tile position.
	 * 
	 * @param tileX
	 * 			The x index of the specified tile.
	 * 
	 * @param tileY
	 * 			The y index of the specified tile.
	 * 
	 * @return A 2D vector representing the bottom left pixel position of the specified tile.
	 * 
	 * @pre The given tile must lie in the game world.
	 * 			| this.tileInWorld(tile)
	 * 
	 * geen stijl gespecifieerd -> nominally
	 */
	public Vector<Integer> getBottomLeftPixelOfTile(Vector<Integer> tile) {
		assert this.tilePositionInWorld(tile);
		return new Vector<>(tile.x * this.getTileSize(), tile.y * this.getTileSize());
	}
	
	
	/**
	 * @param pixel
	 * 			The pixel to get the containing tile of.
	 * 
	 * @return A 2D vector representing the position of the tile containing the given pixel.
	 * 
	 * @pre The given pixel must lie in the game world.
	 * 			| this.pixelInWorld(pixel)
	 * 
	 * geen stijl gespecifieerd -> nominally
	 */
	public Vector<Integer> getTileContainingPixel(Vector<Integer> pixel) {
		assert this.pixelInWorld(pixel);
		return new Vector<>(pixel.x / this.getTileSize(), pixel.y / this.getTileSize());
	}
	
	
	/**
	 * Returns a list containing the positions of the tiles the given rectangle intersects with.
	 * 
	 * @param bottomLeftPixel
	 * 			The position of the bottom left pixel of the rectangle.
	 * 
	 * @param topRightPixel
	 * 			The position of the top right pixel of the rectangle.
	 * 
	 * @return An array of 2D arrays representing the positions of the tiles intersecting with the
	 * 			specified rectangle in pixels.
	 * 
	 * @pre The bottom left pixel must lie inside the game world.
	 * 			| this.pixelInWorld(bottomLeftPixel)
	 */
	public ArrayList<Vector<Integer>> getTilePositionsInRectangle(Vector<Integer> bottomLeftPixel, Vector<Integer> topRightPixel) {
		assert this.pixelInWorld(bottomLeftPixel);
		
		Vector<Integer> bottomLeftTile = this.getTileContainingPixel(bottomLeftPixel);
		Vector<Integer> topRightTile = this.pixelInWorld(topRightPixel) ?
				this.getTileContainingPixel(topRightPixel) :
				this.getTileContainingPixel(Utilities.clipVectorInRange(new Vector<>(0, 0), this.getSizeInPixels(), topRightPixel));
		
		int blockWidth = topRightTile.x - bottomLeftTile.x + 1;
		int blockHeight = topRightTile.y - bottomLeftTile.y + 1;
		int numberOfTiles = blockWidth * blockHeight;
		
		ArrayList<Vector<Integer>> positions = new ArrayList<Vector<Integer>>(numberOfTiles);
		
		for (int x = bottomLeftTile.x; x <= topRightTile.x; x++) {
			for (int y = bottomLeftTile.y; y <= topRightTile.y; y++) {
				positions.add(new Vector<Integer>(x, y));
			}
		}
		
		return positions;
	}
	
	
	/**
	 * @return An array representing the location of the bottom left and top right corners of the visible window in pixels.
	 * 		   The array uses the following format {bottomLeft.x, bottomLeft.y, topRight.x, topRight.y};
	 */
	@Basic
	@Immutable
	public int[] getVisibleWindow() {
		Vector<Integer> size = new Vector<>(visibleWindowTopRight.x - visibleWindowBottomLeft.x,
											visibleWindowTopRight.y - visibleWindowBottomLeft.y);
		Vector<Integer> pos = new Vector<>(mazub.getCenterInPixels().x - size.x / 2,
										   mazub.getCenterInPixels().y - size.y / 2);
		//Correction for the edges of the map
		pos = Utilities.clipVectorInRange(new Vector<>(0, 0), Vector.add(Vector.add(this.getSizeInPixels(), Vector.scale(size, -1)), new Vector<>(-1, -1)), pos);
		
		Vector<Integer> topRight = Vector.add(pos, size);
		int[] windowArray = {pos.x, pos.y, topRight.x, topRight.y};
		return windowArray;
	}
	
	
	/**
	 * @return A 2D vector representing the position of the target tile.
	 */
	@Basic
	@Immutable
	public Vector<Integer> getTargetTilePosition() {
		return new Vector<>(this.targetTilePosition);
	}
	
	
	/**
	 * Starts the game in this game world.
	 */
	public void startGame() {
		//TODO: Implement this method
	}
	
	
	/**
	 * @return true if the game has ended. This means the player is dead or the player reached the target tile.
	 */
	public boolean isGameOver() {
		return this.getMazub().isHealthZero() || this.didPlayerWin();
	}
	
	
	/**
	 * @return true if the player won the game.
	 */
	public boolean didPlayerWin() {
		ArrayList<Vector<Integer>> tilePositions = this.getTilePositionsInRectangle(mazub.getPosition(), Vector.add(mazub.getPosition(), mazub.getSize()));
		Vector<Integer> targetTilePos = this.getTargetTilePosition();
		for (Vector<Integer> pos : tilePositions){
			if ((pos.x == targetTilePos.x) && (pos.y == targetTilePosition.y)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @return A 2D array representing the game world's tiles' types.
	 */
	@Basic
	public TileType[][] getTileTypes() {
		return this.tiles.clone();
	}
	
	
	/**
	 * Returns the type of the tile that contains the given pixel.
	 * 
	 * @param pixel
	 * 			A 2D vector representing the position of the pixel to get the tile type of.
	 * 
	 * @return The type of the tile that contains the given pixel.
	 * 
	 * @throws ModelException
	 * 			Throws a ModelException when the given pixel does not lie in the game world.
	 * 			| !this.pixelInWorld(pixel)
	 */
	public TileType getTileTypeOfPixel(Vector<Integer> pixel) throws ModelException {
		if (!this.pixelInWorld(pixel)) {
			throw new ModelException("The pixel has to lie in the game world.");
		}
		Vector<Integer> tilePosition = this.getTileContainingPixel(pixel);
		return this.getTileTypes()[tilePosition.x][tilePosition.y];
	}
	
	
	/**
	 * Returns the type of the tile at the given tile position.
	 * 
	 * @param position
	 * 			The position of the tile.
	 * 
	 * @return The type of the tile at the given tile position.
	 * 
	 * @throws ModelException
	 * 			Throws a model exception when the given tile position
	 * 			does not lie in the game world.
	 * 			| !this.tilePositionInWorld(position)
	 */
	public TileType getTileType(Vector<Integer> position) throws ModelException {
		if (!this.tilePositionInWorld(position)) {
			throw new ModelException("The tile position has to lie in the game world.");
		}
		return this.getTileTypes()[position.x][position.y];
	}
	
	
	/**
	 * Sets the tile of the given tile position.
	 * 
	 * @param position
	 * 			The position of the tile to set.
	 * 
	 * @param tile
	 * 			The tile to set.
	 * 
	 * @pre The given tile position must lie in the game world.
	 * 			| this.tilePositionInWorld(position)
	 * 
	 * @post The tile at the given tile position will be the given tile.
	 * 			| new.getTile(new.getBottomLeftPixelOfTile(position)) == tile
	 */
	@Basic
	public void setTileType(Vector<Integer> position, TileType type) {
		assert this.tilePositionInWorld(position);
		this.tiles[position.x][position.y] = type;
	}
	
	
	/**
	 * @param gameObject
	 * 			The game object to check.
	 * 
	 * @return true if this game world contains the given game object.
	 */
	public boolean containsGameObject(GameObject gameObject) {
		if (gameObject instanceof Mazub) {
			return this.getMazub() == mazub;
		}
		return objects.contains(gameObject);
	}
	
	
	/**
	 * Removes the given game object from this game world.
	 * 
	 * @param gameObject
	 * 			The game object to remove.
	 * 
	 * @post This world will no longer hold a reference to gameObject
	 * 			| new.containsGameObject(gameObject) == false
	 * no style specified -> totally
	 */
	public void removeGameObject(GameObject gameObject) {
		if ((gameObject instanceof Mazub) && this.getMazub() == gameObject) {
			this.mazub = null;
		}
		if (this.objects.contains(gameObject)) {
			this.objects.remove(gameObject);
		}
	}
	
	
	/**
	 * @return This game world's mazub.
	 */
	public Mazub getMazub() {
		return this.mazub;
	}
	
	
	/**
	 * Sets this game world's mazub to the given mazub.
	 * 
	 * @param mazub
	 * 			The mazub to set.
	 * 
	 * @post The given mazub is registered as this game world's mazub.
	 * 			| new.getMazub() == mazub
	 * 
	 * @post This game world is registered as the given mazub's game world.
	 * 			| (new mazub).getWorld() == new
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException when this game world can not have the given mazub as it's mazub.
	 * 			| !this.canHaveAsMazub(mazub)
	 */
	public void setMazub(@Raw Mazub mazub) throws IllegalArgumentException {
		if (!this.canHaveAsMazub(mazub)) {
			throw new IllegalArgumentException("This game world can not have the given mazub as it's mazub");
		}
		this.mazub = mazub;
		mazub.setWorld(this);
	}
	
	
	/**
	 * @param mazub
	 * 			The mazub to check.
	 * 
	 * @return true if this game world can have the given mazub as it's mazub.
	 * 			This game world can not be terminated.
	 * 			| !this.isTerminated()
	 * 			The given mazub can not be null.
	 * 			| mazub != null
	 * 			The given mazub can not be terminated.
	 * 			| !mazub.isTerminated()
	 */
	public boolean canHaveAsMazub(@Raw Mazub mazub) {
		if (this.isTerminated() || (mazub == null) || mazub.isTerminated()) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * @return true if this game world has a proper mazub.
	 * 			This game world needs to be able to have it's current mazub as it's mazub.
	 * 			| this.canHaveAsMazub(this.getMazub())
	 * 			This game world's mazub has to have this game world as it's game world.
	 * 			| this.getMazub().getWorld() == this
	 */
	public boolean hasProperMazub() {
		return this.canHaveAsMazub(this.getMazub()) && this.getMazub().getWorld() == this;
	}
	
	
	/**
	 * @param cls
	 * 			The class of which to return GameObjects
	 * 
	 * @return The collection of all object with class "type" in this game world.
	 */
	public <T extends GameObject> Set<T> getGameObjectWithClass(Class<T> cls) {
		Set<T> objects = new HashSet<T>();
		for (GameObject obj : this.objects){
			if (obj.getClass() == cls){
				objects.add(cls.cast(obj));
			}
		}
		return objects;
	}
	
	
	/**
	 * Adds the given GameObject to the collection of GameObjects in this game world.
	 * 
	 * @param object
	 * 			The GameObject to add.
	 * 
	 * @post The given GameObject will be added to the collection of GameObjects in this game world.
	 * 			| new.containsGameObject(object)
	 * 
	 * @throw IllegalArgumentException
	 * 			Throws an IllegalArgumentException if the given GameObject is null or terminated.
	 * 			| object == null || object.isTerminated()
	 * @post The world will contain a reference to object
	 * 			| new.containsGameObject(object)
	 */
	public void addGameObject(GameObject object) throws IllegalArgumentException {
		if (object == null || object.isTerminated()) {
			throw new IllegalArgumentException("The GameObject can't be null or terminated.");
		}
		this.objects.add(object);
		object.setWorld(this);
	}
	
	
	/**
	 * Returns whether two game objects overlap in the game world.
	 * 
	 * @param first
	 * 			The first game object.
	 * 
	 * @param second
	 * 			The second game object.
	 * 
	 * @return true if the two given game objects overlap.
	 */
	public boolean objectsOverlap(GameObject first, GameObject second) {
		
		Vector<Integer> pos1 = first.getPosition();
		Vector<Integer> size1 = first.getSize();
		Vector<Integer> pos2 = second.getPosition();
		Vector<Integer> size2 = second.getSize();
		
		return !(pos1.x + size1.x - 1 < pos2.x
				|| pos2.x + size2.x - 1 < pos1.x
				|| pos1.y + size1.y - 1 < pos2.y
				|| pos2.y + size2.y - 1 < pos1.y);
	}
	
	
	/**
	 * Returns a set of all game objects colliding with the given game object.
	 * 
	 * @param object
	 * 			The object to get colliding objects with.
	 * 
	 * @return A set containing all objects the given object collides with.
	 */
	public Set<GameObject> getObjectsCollidingWithObject(GameObject object) {
		
		HashSet<GameObject> collidingObjects = new HashSet<GameObject>();
		
		for (GameObject obj : this.objects) {
			
			if (obj != object && object.collidesWithGameObjectClass(obj.getClass())
					&& this.objectsOverlap(object, obj)) {
				
				collidingObjects.add(obj);
			}
		}
		
		return collidingObjects;
	}
	
	
	/**
	 * Returns a set of all tiles colliding with the given game object.
	 * 
	 * @param object
	 * 			The object to get colliding tiles with.
	 * 
	 * @return A set containing all tiles the given object collides with.
	 */
	public Set<Tile> getTilesCollidingWithObject(GameObject object) {
		
		Set<Tile> collidingTiles = new HashSet<Tile>();
		
		ArrayList<Vector<Integer>> positions = this.getTilePositionsInRectangle(object.getPosition(),
				Vector.add(object.getPosition(), object.getSize()));
		
		for (Vector<Integer> position : positions) {
			TileType type = this.getTileType(position);
			if (object.collidesWithTileType(type)) {
				Tile tile = new Tile(position, this.getTileSize(), type);
				collidingTiles.add(tile);
			}
		}
		
		return collidingTiles;
	}
	
	
	/**
	 * Returns a vector representing the overlap directions in the x and y directions.
	 * 
	 * @param bottomLeftPixel
	 * 			The position of the bottom left pixel of the object that is testing the collision.
	 * 
	 * @param topRightPixel
	 * 			The position of the top right pixel of the object that is testing the collision.
	 * 
	 * @param collidingBottomLeftPixel
	 * 			The position of the bottom left pixel of the colliding object.
	 * 
	 * @param collidingTopRightPixel
	 * 			The position of the top right pixel of the colliding object.
	 * 
	 * @return The x and y overlap directions.
	 */
	public Vector<OverlapDirection> getKindOfOverlap(Vector<Integer> bottomLeftPixel,
			Vector<Integer> topRightPixel, Vector<Integer> collidingBottomLeftPixel,
			Vector<Integer> collidingTopRightPixel) {
		
		Vector<OverlapDirection> overlapDir = new Vector<>(OverlapDirection.NONE, OverlapDirection.NONE);
		
		//TODO: check < and <=
		
		if (collidingBottomLeftPixel.x <= bottomLeftPixel.x) {
			if (collidingTopRightPixel.x <= bottomLeftPixel.x) {
				overlapDir = overlapDir.setX(OverlapDirection.NONE);
			} else if (collidingTopRightPixel.x < topRightPixel.x) {
				overlapDir = overlapDir.setX(OverlapDirection.LOW);
			} else {
				overlapDir = overlapDir.setX(OverlapDirection.FULL);
			}
		} else if (collidingBottomLeftPixel.x < topRightPixel.x) {
			if (collidingTopRightPixel.x < topRightPixel.x) {
				overlapDir = overlapDir.setX(OverlapDirection.MIDDLE);
			} else {
				overlapDir = overlapDir.setX(OverlapDirection.HIGH);
			}
		}
		
		if (collidingBottomLeftPixel.y <= bottomLeftPixel.y) {
			if (collidingTopRightPixel.y <= bottomLeftPixel.y) {
				overlapDir = overlapDir.setY(OverlapDirection.NONE);
			} else if (collidingTopRightPixel.y < topRightPixel.y) {
				overlapDir = overlapDir.setY(OverlapDirection.LOW);
			} else {
				overlapDir = overlapDir.setY(OverlapDirection.FULL);
			}
		} else if (collidingBottomLeftPixel.y < topRightPixel.y) {
			if (collidingTopRightPixel.y < topRightPixel.y) {
				overlapDir = overlapDir.setY(OverlapDirection.MIDDLE);
			} else {
				overlapDir = overlapDir.setY(OverlapDirection.HIGH);
			}
		}
		
		return overlapDir;
	}
	
	
	/**
	 * Advances the time in this game world with the given time interval and updates
	 * position, speed and acceleration of all game objects in this game world.
	 * 
	 * @param dt
	 * 			The time that has passed in the game world since last calling this method.
	 * 
	 * @throws	IllegalArgumentException
	 * 			| (dt < 0) || (dt > Constants.maxTimeInterval) || dt.isNan()
	 */
	public void advanceTime(double dt) {
		
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
		
		this.getMazub().advanceTime(dt);
		for (GameObject object : this.objects) {
			object.advanceTime(dt);
		}
		
		//Remove dead objects from the world
		Set<GameObject> deaths = new HashSet<>();
		for (GameObject object : this.objects) {
			if (!object.isAlive()){
				deaths.add(object);
			}
		}
		this.objects.removeAll(deaths);
	}
}
