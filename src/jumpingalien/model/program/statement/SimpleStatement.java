package jumpingalien.model.program.statement;

import java.util.Map;

/**
 * A class representing a simple Statement. A simple Statement is a 
 * statement which has finished after advanceing the time once.
 */
public abstract class SimpleStatement implements Statement {
	
	private boolean completed;
	
	/**
	 * Constructs a new uncompleted SimpleStatement.
	 * 
	 * @post This statement won't be finished.
	 * 			| new.isFinished() == false
	 */
	protected SimpleStatement() {
		this.completed = false;
	}

	@Override
	public double advanceTime(double dt, Map<String, Object> globals) {
		if (dt < Statement.defaultTime) {
			return dt;
		}
		
		this.run(globals);
		this.completed = true;
		return (dt - Statement.defaultTime);
	}

	/**
	 * Execute the statements 'body'.
	 */
	protected abstract void run(Map<String, Object> globals);

	@Override
	public boolean isFinished() {
		return completed;
	}

	@Override
	public void reset() {
		this.completed = false;
	}

}