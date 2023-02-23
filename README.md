# Point Spender

## Description

This project takes a CSV file with information about a customer's rewards points and returns a CSV with a specified number of points spent.

The CSV file must be in the following format:

```csv
"payer","points","timestamp"
"Company Name",11037,"yyyy-MM-dd'T'HH:mm:ssX"
```

A sample CSV in included in this repository.

## Dependencies, Installation, and How To Run

This project requires an installation of Java. It is recommended to version 8+.

Clone this repository and compile the `Customer.java` file by running in the command line:

```cmd
javac Customer.java
```

Then run the file with:

```cmd
java Customer [pathToCSV] [numPoints]
```

Where `[pathToCSV]` is the path to the CSV file and `[numPoints]` is the number of points to remove.

## Rational

I used Java to write the program because I was familiar with how to write programs in it. Additionally, Java is built around object-oriented design, which influenced the way that I designed this class by making objects out of the CSV rows.

I used ArrayList, Date, Scanner, SimpleDateFormat, File, FileInputStream, in my implementation because they were all built into Java by default. Additionally, using methods like SimpleDateFormat made dealing with the timestamps significantly easier.

## Advantages / Disadvantages

### Advantages

This project has the benefit of being easily modifiable. If more columns are added to the CSV, they can be accommodated by adding new variables to the methods. Additionally, the object oriented structure makes it easier for this class to be implemented into a larger, object-oriented project.

### Disadvantages

One of the issues with this implementation is the performance. This current implementation relies on working memory, which might slow down for larger data sets with more unique payers. A potential solution would be to use a different data structure to store the elements. A hash table could also be used to speed up look-up times.

## Other information

### What has been a favorite school/personal project thus far? What about it that challenged you?

My favorite school project is one that I am currently working on where I am creating a minesweeper game from scratch. I like it because it gave me a chance to learn about algorithms hands on by thinking them through and implementing them. 

One notable example is the code required to open all neighboring cells with 0 mines. This required me to implement a flood-fill algorithm to check each of the neighboring squares while ignoring flagged tiles.

I hope to slowly add more quality of life features to the game as I work on it because in it's current state, it is very simplistic.