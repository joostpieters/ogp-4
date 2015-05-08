package jumpingalien.model.program;

import java.util.List;
import java.util.Map;

import jumpingalien.model.gameobject.GameObject;
import jumpingalien.model.program.expression.BinaryOperation;
import jumpingalien.model.program.expression.Expression;
import jumpingalien.model.program.expression.UnaryOperation;
import jumpingalien.model.program.expression.Value;
import jumpingalien.part3.programs.IProgramFactory;
import jumpingalien.part3.programs.SourceLocation;

public class ProgramFactory implements IProgramFactory<Expression<?>, Statement, Object, Program> {
	
	@Override
	public Expression<?> createReadVariable(String variableName, Object variableType,
			SourceLocation sourceLocation) {
		//TODO: Implement this
		return null;
	}

	@Override
	public Expression<Double> createDoubleConstant(double value, SourceLocation sourceLocation) {
		return new Value<>(value);
	}

	@Override
	public Expression<Boolean> createTrue(SourceLocation sourceLocation) {
		return new Value<>(true);
	}

	@Override
	public Expression<Boolean> createFalse(SourceLocation sourceLocation) {
		return new Value<>(false);
	}

	@Override
	public Expression<GameObject> createNull(SourceLocation sourceLocation) {
		return new Value<>(null);
	}

	@Override
	public Expression<GameObject> createSelf(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression<Direction> createDirectionConstant(
			Direction value,
			SourceLocation sourceLocation) {
		return new Value<>(value);
	}

	@Override
	public Expression<Double> createAddition(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Double, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> (Double)a + (Double)b);
	}

	@Override
	public Expression<Double> createSubtraction(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Double, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> (Double)a - (Double)b);
	}

	@Override
	public Expression<Double> createMultiplication(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Double, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> (Double)a * (Double)b);
	}

	@Override
	public Expression<Double> createDivision(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Double, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> (Double)a / (Double)b);
	}

	@Override
	public Expression<Double> createSqrt(Expression<?> expr, SourceLocation sourceLocation) {
		return new UnaryOperation<Double, Double>((Expression<Double>)expr, Math::sqrt);
	}

	@Override
	public Expression<Double> createRandom(Expression<?> maxValue, SourceLocation sourceLocation) {
		return new UnaryOperation<Double, Double>((Expression<Double>)maxValue, (a) -> Math.random() * a);
	}

	@Override
	public Expression<Boolean> createAnd(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Boolean, Boolean>((Expression<Boolean>)left, (Expression<Boolean>)right,
															  (a, b) -> a && b);
	}

	@Override
	public Expression<Boolean> createOr(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Boolean, Boolean>((Expression<Boolean>)left, (Expression<Boolean>)right,
															  (a, b) -> a || b);
	}

	@Override
	public Expression<Boolean> createNot(Expression<?> expr, SourceLocation sourceLocation) {
		return new UnaryOperation<Boolean, Boolean>((Expression<Boolean>)expr, (a) -> !a);
	}

	@Override
	public Expression<Boolean> createLessThan(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> a < b);
	}

	@Override
	public Expression<Boolean> createLessThanOrEqualTo(Expression<?> left, Expression<?> right,
			SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> a <= b);
	}

	@Override
	public Expression<Boolean> createGreaterThan(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> a > b);
	}

	@Override
	public Expression<Boolean> createGreaterThanOrEqualTo(Expression<?> left, Expression<?> right,
			SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Double, Double>((Expression<Double>)left, (Expression<Double>)right, (a, b) -> a >= b);
	}

	@Override
	public Expression<Boolean> createEquals(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Object, Object>((Expression<Object>)left, (Expression<Object>)right, (a, b) -> a.equals(b));
	}

	@Override
	public Expression<Boolean> createNotEquals(Expression<?> left, Expression<?> right, SourceLocation sourceLocation) {
		return new BinaryOperation<Boolean, Object, Object>((Expression<Object>)left, (Expression<Object>)right, (a, b) -> ! a.equals(b));
	}

	@Override
	public Expression createGetX(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createGetY(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createGetWidth(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createGetHeight(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createGetHitPoints(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createGetTile(Expression x, Expression y, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createSearchObject(Expression direction, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsMazub(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsShark(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsSlime(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsPlant(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsDead(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsTerrain(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsPassable(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsWater(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsMagma(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsAir(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsMoving(Expression expr, Expression direction, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsDucking(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression createIsJumping(Expression expr, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createAssignment(String variableName, Object variableType, Expression value,
			SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createWhile(Expression condition, Statement body, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createForEach(
			String variableName,
			Kind variableKind,
			Expression where,
			Expression sort,
			SortDirection sortDirection,
			Statement body, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createBreak(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createIf(Expression condition, Statement ifBody, Statement elseBody,
			SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createPrint(Expression value, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStartRun(Expression direction, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStopRun(Expression direction, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStartJump(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStopJump(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStartDuck(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createStopDuck(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createWait(Expression duration, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createSkip(SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement createSequence(List<Statement> statements, SourceLocation sourceLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDoubleType() {
		return new Double(0);
	}

	@Override
	public Boolean getBoolType() {
		return new Boolean(true);
	}

	@Override
	public GameObject getGameObjectType() {
		return null;
	}

	@Override
	public Direction getDirectionType() {
		return Direction.DOWN;
	}

	@Override
	public Program createProgram(Statement mainStatement, Map<String, Object> globalVariables) {
		return new Program(mainStatement, globalVariables);
	}

}