package jumpingalien.model;

import java.util.Set;

import jumpingalien.util.Sprite;

/**
 * A class to represent a Slime object in a certain school.
 * 
 * @author Rugen en Menno
 * 
 * @invar The slime must always have a valid school.
 * 			| this.hasProperSchool()
 */
public class Slime extends GameObject {
	
	private School school;
	
	
	/**
	 * Creates a new slime with the given positions, sprites and school.
	 * 
	 * @param position
	 * 			The position of the slime.
	 * 
	 * @param sprites
	 * 			The sprites of the slime.
	 * 
	 * @param school
	 * 			The school of the slime.
	 * 
	 * TODO: dit nakijken 
	 * @effect this.setSchool(school)
	 */
	public Slime(Vector<Double> position, Sprite[] sprites, School school) throws IllegalArgumentException {
		
		// GameObject
		super(100, 100, position, sprites);
		
		this.setSchool(school);
	}
	
	
	/**
	 * @return The school of this slime.
	 */
	public School getSchool() {
		return this.school;
	}
	
	
	/**
	 * @param school
	 * 			The school to check.
	 * 
	 * @return Whether this slime can have the given school as it's school.
	 * 			| !(school == null)
	 */
	public static boolean canHaveAsSchool(School school) {
		return !(school == null) /*&& !school.isTerminated()*/;
		//TODO: Check exact definition of isTerminated
	}
	
	
	/**
	 * @return Whether this slime has a proper school.
	 * 
	 * @effect this.canHaveAsSchool(this.getSchool())
	 */
	public boolean hasProperSchool() {
		return canHaveAsSchool(this.getSchool());
	}
	
	
	/**
	 * Sets the school of this slime.
	 * 
	 * @param school
	 * 			The school to set.
	 * 
	 * @post This slime will have the given school as it's school.
	 * 			| new.getSchool() == (new school)
	 * 
	 * @throws IllegalArgumentException
	 * 			Throws an IllegalArgumentException when this slime can't have the given school as school.
	 */
	public void setSchool(School school) throws IllegalArgumentException {
		if (!canHaveAsSchool(school)) {
			throw new IllegalArgumentException("Invalid school provided.");
		}
		
		this.school = school;
	}
	
	
	@Override
	protected Set<Class<? extends GameObject>> getCollidables() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void advanceTime(double dt) {
		// TODO Auto-generated method stub
		this.setPosition(Vector.add(this.getPositionInMeters(), new Vector<Double>(0.005, 0.0)));
	}

}
