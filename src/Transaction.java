
// Methods
import java.util.Date;

/**
 * This class contains methods for dealing with a parsed CSV file
 * 
 * @author Vasu Janjrukia
 */
class Transaction {

	private String payer;
	private int points;
	private Date timestamp;

	/**
	 * Creates an object from the contents of a row in a CSV file
	 * 
	 * @param payer  The payer
	 * @param points The number of points
	 * @param date   The timestamp in "yyyy-MM-dd'T'HH:mm:ssX" format
	 */
	public Transaction(String payer, int points, Date date) {
		this.payer = payer;
		this.points = points;
		this.timestamp = date;
	}

	/**
	 * Get the name of a payer
	 * 
	 * @return String of the payer's name
	 */
	public String getPayer() {
		return payer;
	}

	/**
	 * Get the number of points
	 * 
	 * @return Number of points
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Get the timestamp as a Date object
	 * 
	 * @return the timestamp as a Date object
	 */
	public Date getTimestamp() {
		return timestamp;
	}
}
