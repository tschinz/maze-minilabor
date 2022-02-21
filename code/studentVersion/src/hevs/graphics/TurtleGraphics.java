package hevs.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;

/**
 * Graphics class that emulates the tortoise in the Logo programming language
 * 
 * The turtle starts looking up with a black color, pen up 
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Turtle_graphics">Wikipedia description of Turtle Graphics</a>
 * 
 * @author Pierre Roduit (pierre.roduit@hevs.ch)
 * @author <a href='mailto:pandre.mudry&#64;hevs.ch'> Pierre-André Mudry</a>
 * @version 1.2
 */
public class TurtleGraphics extends SimpleGraphics {
		
	private int x = frameWidth / 2;
	private int y = frameHeight / 2;
	private boolean isWriting = false;
	private double angle = - Math.PI / 2.0; // Current rotation
	private Color color = Color.black;
	
	static final boolean HIGH_QUALITY_RENDERING = true;
	
	public TurtleGraphics(int width, int height, String windowName){
		super(width, height, windowName, HIGH_QUALITY_RENDERING);
		this.checkBorders = false;
	}
	
	public TurtleGraphics(int width, int height) {
		this(width, height, null);	
	}
	
	/**
	 * Start the writing
	 */
	public void penDown() {
		isWriting = true;
		// Write the pixel corresponding to the position
		Color oldColor = display.g2d.getColor();
		setColor(color);
		setPixel(x, y);
		setColor(oldColor);
		repaint();
	}

	/**
	 * Change the color of writing
	 * 
	 * @param color
	 */
	public void changeColor(Color color) {
		this.color = color;
	}

	/**
	 * Stop the writing
	 */
	public void penUp() {
		isWriting = false;
	}

	/**
	 * Go forward the specified distance (writing if the pen is down)
	 * 
	 * @param distance
	 *            The distance to move
	 */
	public void forward(double distance) {
		// Compute new position
		int newX = x + (int) Math.round(Math.cos(angle) * distance);
		int newY = y + (int) Math.round(Math.sin(angle) * distance);
		
		// Write if the pen is down
		if (isWriting) {
			Color oldColor = this.display.g2d.getColor();
			setColor(color);
			drawLine(x, y, newX, newY);
			setColor(oldColor);
			repaint();
		}
		x = newX;
		y = newY;
	}

	/**
	 * Jump to a specific location (without writing)
	 * 
	 * @param x
	 *            X coordinate of the destination
	 * @param y
	 *            Y coordinate of the destination
	 */
	public void jump(int x, int y) {
		this.x = x;
		this.y = y;
		
		// If the pen is down, draw a point at destination
		if (isWriting) {
			Color oldColor = display.g2d.getColor();
			setColor(color);
			setPixel(x, y);
			setColor(oldColor);
			repaint();
		}
	}

	/**
	 * @return The location of the turtle
	 */
	public Point getPosition(){
		return new Point(x, y);
	}
	
	/**
	 * Sets the width of the pen
	 * @param w
	 */
	public void setWidth(float w){
		this.getGraphics().setStroke(new BasicStroke(w));
	}
	
	/**
	 * @return The current turtle angle (in degrees)
	 */
	public double getTurtleAngle(){
		return (this.angle * 180.0 / Math.PI);
	}
	
	/**
	 * Turn the direction of writing with the specified angle
	 * 
	 * @param angle
	 *            specified angle in degrees
	 */
	public void turn(double angle) {
		this.angle += angle * Math.PI / 180;
	}

	/**
	 * Set the direction of writing to the specified angle
	 * 
	 * @param angle
	 *            specified angle in degrees
	 */
	public void setAngle(double angle) {
		this.angle = angle * Math.PI / 180;
	}

	/**
	 * Turn the direction of writing with the specified angle
	 * 
	 * @param angle specified angle in radians
	 */
	public void turnRad(double angle) {
		this.angle += angle;
	}

	/**
	 * Set the direction of writing to the specified angle
	 * 
	 * @param angle specified angle in radians
	 */
	public void setAngleRad(double angle) {
		this.angle = angle;
	}

	public double getTurtleAngleRad(){
		return this.angle;
	}
	
}