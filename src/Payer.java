/**
 * This object holds methods relating to keeping track of the points of each
 * payer in a parsed CSV file
 * 
 * @author Vasu Janjrukia
 */
class Payer {

	private String name;
	private int totalPoints;

	/**
	 * Creates an Object to store information about a given payer
	 * 
	 * @param name   the name of the payer
	 * @param points the number of points with that payer
	 */
	public Payer(String name, int points) {
		this.name = name;
		this.totalPoints = points;
	}

	/**
	 * Adds the points for a specific payer from a transaction to the running count
	 * of points
	 * 
	 * @param transaction Rhe Object with the transaction information
	 */
	public void addPoints(Transaction transaction) {
		this.totalPoints += transaction.getPoints();
	}

	/**
	 * Removes a specified number of points from a payer's running count
	 * 
	 * @param points The number of points to remove
	 */
	public void removePoints(int points) {
		this.totalPoints -= points;
	}

	public int getTotalPoints() {
		return this.totalPoints;
	}

	public String getName() {
		return this.name;
	}
}