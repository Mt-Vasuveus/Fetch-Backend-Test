
// Methods
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileInputStream;

// Exceptions
import java.util.NoSuchElementException;
import java.text.ParseException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class creates an Object to hold a given customer's transactions as well
 * as the points they have with each payer
 * 
 * @author Vasu Janjrukia
 */
public class Customer {

	public ArrayList<Transaction> transactions;
	public ArrayList<Payer> payers;

	/**
	 * Constructor to create a new Customer object.
	 * This is mostly here to make it easier to extend this program to multiple
	 * instances of Customer like a database of customers
	 */
	public Customer() {
		this.transactions = new ArrayList<Transaction>();
		this.payers = new ArrayList<Payer>();
	}

	/**
	 * This method takes a path to a CSV file and puts the data in each row into a
	 * new Transaction object.
	 * 
	 * @param csvFilePath path to the csvFile
	 * @throws IOException
	 * @throws ParseException
	 */
	public void parseCSV(String csvFilePath) throws IOException, ParseException {

		FileInputStream fileByteStream = null;
		Scanner inFS = null;
		// Date format
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		try {
			// set up Java to read filename
			File filename = new File(csvFilePath);
			fileByteStream = new FileInputStream(filename);
			inFS = new Scanner(fileByteStream).useDelimiter(",|\n");

			// Skip first line in CSB with name of columns ("payer","points","timestamp")
			inFS.nextLine();

			// While there is still text in the file
			while (inFS.hasNextLine()) {

				// Get each of the columns of a given row
				String payer = inFS.next().replaceAll("\"", "");
				int points = Integer.parseInt(inFS.next());

				// Get the timestamp and convert it into a Timestamp object
				String timestampString = inFS.next().replaceAll("\"", "");
				Date timestamp = f.parse(timestampString);

				// Save the data as a Transaction
				transactions.add(new Transaction(payer, points, timestamp));

				// Go to the next row
				inFS.nextLine();
			}
		} catch (FileNotFoundException e0) {
			throw new FileNotFoundException();
		} catch (NoSuchElementException e1) {
			throw new NoSuchElementException();
		} finally {
			// Close the scanner
			if (inFS != null) {
				inFS.close();
			}
			// Close the fileInputStream
			if (fileByteStream != null) {
				fileByteStream.close();
			}
		}
	}

	/**
	 * Takes the customer's list of transactions and adds them to an Object relating
	 * to their respective Payers
	 */
	public void transactionToPayers() {
		for (Transaction transaction : transactions) {
			boolean matchFound = false;
			String curPayer = transaction.getPayer();

			// Check if a given Payer is in the ArrayList
			for (Payer payer : payers) {
				if (payer.getName().equals(curPayer)) {
					payer.addPoints(transaction);
					matchFound = true;
					break;
				}
			}
			// If the Payer doesn't have an entry in the list of payers, add it
			if (!matchFound && transaction.getPayer().equals(curPayer)) {
				payers.add(new Payer(curPayer, transaction.getPoints()));
			}
		}
	}

	/**
	 * Determines the logic for spending points
	 * 
	 * @param pointsToSpend The number of points to spend
	 * @return A CSV of the remaining points as a String
	 */
	public String spendPoints(int pointsToSpend) {
		int pointsRemaining = pointsToSpend;

		// Loops until the necessary points have been spent
		while (pointsRemaining > 0) {

			// Find information about the oldest entry in the CSV
			int oldestPoints = this.getOldestTransaction().getPoints();
			String oldestPayer = this.getOldestTransaction().getPayer();
			Date oldestTransactionDate = this.getOldestTransaction().getTimestamp();

			// If a payer has more available points than needed, spend the remaining points
			if (oldestPoints > pointsRemaining) {
				for (Payer i : payers) {
					if (i.getName().equals(oldestPayer)) {
						i.removePoints(pointsRemaining);
						pointsRemaining = 0;
					}
				}
				break;
			}

			// Use up a payer's remaining points
			for (Payer i : payers) {
				if (i.getName().equals(oldestPayer)) {
					i.removePoints(oldestPoints);
					// update running count of points needed to be spent
					pointsRemaining -= oldestPoints;
					// remove value from the original CSV so it doesn't get counted multiple times
					transactions.remove(findTransaction(oldestPayer, oldestPoints, oldestTransactionDate));
				}
			}
		}
		// A string with the CSV values
		return csvPayerPoints();
	}

	private String csvPayerPoints() {
		// The String that contains the CSV
		String csv = new String();
		csv += "{";
		int lastIndex = this.payers.size() - 1;
		for (int i = 0; i < lastIndex; i++) {
			csv += "\t\"" + this.payers.get(i).getName() + "\": ";
			csv += this.payers.get(i).getTotalPoints() + ",\n";
		}
		csv += "\t\"" + this.payers.get(lastIndex).getName() + "\": ";
		csv += this.payers.get(lastIndex).getTotalPoints() + "\n}";

		return csv;
	}

	/**
	 * Finds the oldest transaction in the CSV. This can change if entries are
	 * removed from the CSV
	 * 
	 * @return the oldest Transaction
	 */
	public Transaction getOldestTransaction() {
		// Find the oldest timestamp
		Transaction oldest = transactions.get(0);
		for (Transaction i : transactions) {
			if (i.getTimestamp().before(oldest.getTimestamp())) {
				oldest = i;
			}
		}
		return oldest;
	}

	/**
	 * Find the index of a specific value in the transaction ArrayList
	 * 
	 * @param payer     the name of the payer
	 * @param points    the number of points
	 * @param timestamp the time of the transaction
	 * @return The specified transaction or null if the transaction isn't present
	 */
	public Transaction findTransaction(String payer, int points, Date timestamp) {
		for (Transaction i : transactions) {
			if (i.getPayer().equals(payer) && i.getTimestamp().equals(timestamp) && i.getPoints() == points) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Combines all of the functions to provide a simple interface to spend points
	 * 
	 * @param customer      the customer who's transactions are being processed
	 * @param csvFile       the path to the CSV file
	 * @param pointsToSpend the number of points to spend
	 * @return The CSV with the points spent accordingly
	 */
	private static String processTransactions(Customer customer, String csvFile, int pointsToSpend) {
		String finalCSV = null;
		try {
			customer.parseCSV(csvFile);
			customer.transactionToPayers();
			customer.csvPayerPoints();
			finalCSV = customer.spendPoints(pointsToSpend);
			return finalCSV;
		} catch (FileNotFoundException e0) {
			System.out.println("Error: File could not be found");
		} catch (IOException e1) {
			System.out.println("Error: IOException");
		} catch (ParseException e2) {
			System.out.println("Error: CSV isn't formatted correctly");
		} catch (NoSuchElementException e3) {
			System.out.println("Error: CSV is empty");
		} catch (NumberFormatException e4) {
			System.out.println("Error: CSV isn't formatted correctly");
		} catch (Exception e) {
			System.out.println("Error: Unspecified Exception");
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println("Transaction processor by Vasu Janjrukia.");

		if (args.length == 0) {
			System.out.println("Please use the following syntax: java Customer [\"pathToCsvFile\"] [pointsToSpend]");
			return;
		}

		if (args.length == 1) {
			System.out.println("Please provide the number of points to spend");
			return;
		}

		if (args.length > 2) {
			System.out.println("Too many arguments");
			return;
		}

		Customer customer = new Customer();

		String csvFile = args[0];
		System.out.println(processTransactions(customer, csvFile, Integer.parseInt(args[1])));
	}
}
