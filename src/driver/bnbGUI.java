package driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Created by Johnson Lai - A GUI interface for a MySQL database (CSCC43) 2022
 */
public class bnbGUI {

  // Login menu
  public static String MenuLogin() {
    return ("\n--- LOGIN MENU ---\n[1] Create New User \n[2] Login \n[3] Delete User\n"
        + "[0] Exit program\n");
  }

  // Options Menu
  public static String MenuMain() {
    return ("\n\n--- MAIN MENU ---\n[1] View Listings\n[2] Create Listing\n[3] Update "
        + "Listing (Host)\n[4] Delete Listing (Host)\n[5] New Booking (Renter)\n[6] Delete Booking "
        + "\n[7] Write Review \n[8] Reports \n[9] Database Tables \n[0] Back to Login \n");
  }

  // View Menu
  public static String MenuView() {
    return ("\n--- VIEW MENU ---\n[1] View Listings DB \n[2] View Users DB \n"
        + "[3] View Reviews DB \n[4] View Calendar DB \n[5] View Renter History DB \n"
        + "[6] View Host's Listings DB \n[7] View House History DB \n"
        + "[0] Back to Main menu \n");
  }

  // View Menu
  public static String MenuReports() {
    return ("\n--- REPORTS MENU ---\n[1] Total bookings by city \n"
        + "[2] Total bookings by postal code in a city \n"
        + "[3] Total listings per country \n[4] Total listings per country & city \n"
        + "[5] Total listings per country, city & postal code \n"
        + "[6] Host rank by listings per country \n[7] Host rank by city \n"
        + "[8] Hosts with >10% listings by city \n [9] Hosts with >10% listings by country \n"
        + "[10] Renter bookings rank \n" + "[11] Renter bookings rank per cities (2+ bookings) \n"
        + "[12] Largest cancellations \n"
        + "[13] Nouns popular on listings\n[0] Back to Main menu \n");
  }

  // Declaring JShell variable(s)
  public static String promptUser() {
    return ("--> Choose your query: ");
  }

  // Output a Result Set
  public static String outputRS(ResultSet rs) throws Exception {
    String output = "";
    try {
      ResultSetMetaData meta = rs.getMetaData();
      int columnNo = meta.getColumnCount();

      int count = 0;
      while (rs.next()) {
        count++;
        for (int i = 1; i <= columnNo; i++)
          output += (rs.getString(i) + " ");
        output += "\n";
      }
      // Strip the extra newline character
      if (count > 0) {
        output = output.substring(0, output.length() - 2);
      }
    } catch (Exception e) {
      output += "err.";
    }
    return output;
  }

  /**
   * Main method. Runs a loop prompting user input that is parsed and calls appropriate commands.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    // Database
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:mysql://localhost:3306/bnbase";
    String username = "root";
    String password = "8d6hr4uu";

    // Declaring variables
    Scanner sc = new Scanner(System.in); // Scanner for parsing user input
    String sql = "";
    String userInput;
    String userSIN = "0";
    String userType = "r";
    boolean consoleOn = true; // Controls shell loop
    boolean loggedIn = false;
    boolean reports = false;
    boolean viewing = false;

    // Connect to DB
    try {
      // Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, username, password);
      stmt = conn.createStatement();
      System.out.println("Connection to active database secured!");
    } catch (SQLException se) {
      System.out.println(se);
    } catch (Exception e) {
      System.out.println("Class not found.");
    }

    // Main Loop
    while (consoleOn) {

      // Initial sign in Menu - to login, or create user profile
      while (!loggedIn) {
        try {
          System.out.print(MenuLogin());
          System.out.print(promptUser());
          userInput = sc.nextLine();
          if (userInput.equals("0")) {
            loggedIn = true;
            consoleOn = false;
          } else if (userInput.equals("1")) {

            // If Create User

            System.out.println("Answer the following prompts:");
            System.out.print("Renter [r] or Owner [o]? ");
            userType = sc.nextLine();
            // Error Protection. O is default user (no credit card)
            if (!userType.equals("r") || !userType.equals("o")) {
              userType = "o";
            }
            System.out.print("Social Insurance Number? ");
            userSIN = sc.nextLine();
            System.out.print("Name? ");
            String tempName = sc.nextLine();
            System.out.print("Date of Birth? Answer in YYYY-MM-DD: ");
            String tempDate = sc.nextLine();
            System.out.print("Address? ");
            String tempAddr = sc.nextLine();
            System.out.print("Occupation? ");
            String tempJob = sc.nextLine();
            System.out.println(
                userSIN + "," + tempName + "," + tempJob + "," + tempAddr + "," + tempDate);
            sql = "INSERT INTO USER VALUES (" + userSIN + ",\"" + tempName + "\",\"" + tempJob
                + "\",\"" + tempAddr + "\",\"" + tempDate + "\");";
            stmt.executeUpdate(sql);
            if (userType == "r") { // Type Renter (r) means we need a credit card.
              System.out.println("Credit Card Number? (Up to 4 digits)");
              String tempCard = sc.nextLine();
              sql = "INSERT INTO RENTER VALUES (" + userSIN + "," + tempCard + ");";
              stmt.executeUpdate(sql);
            }

          } else if (userInput.equals("2")) {

            // If Login

            System.out.print("To login, enter your SIN: ");
            userSIN = sc.nextLine();
            sql = "SELECT UNAME FROM USER WHERE SIN = " + userSIN + ";";
            rs = stmt.executeQuery(sql);
            if (outputRS(rs).equals("")) {
              System.out.println("Login failed! Not a user.");
            } else {
              loggedIn = true;
              sql = "SELECT UNAME FROM USER WHERE SIN = " + userSIN + ";";
              rs = stmt.executeQuery(sql);
              System.out.println("Login successful as " + outputRS(rs));
              // Determine the user type
              sql = "SELECT SIN FROM RENTER WHERE SIN = " + userSIN + ";";
              rs = stmt.executeQuery(sql);
              if (!outputRS(rs).equals("")) {
                userType = "r";
              } else {
                userType = "o";
              }
            }

          } else if (userInput.equals("3")) {

            // If Delete user

            System.out.print("To delete, enter your SIN: ");
            userInput = sc.nextLine();
            try {
              sql = "DELETE FROM USER WHERE SIN LIKE " + userInput + ";";
              if (stmt.executeUpdate(sql) == 0) {
                System.out.print("No such user.");
              } else {
                System.out.print("Successfully purged.");
              }
            } catch (SQLException se) {
              System.out.println("User does not exist.");
            }

          } else {
            System.out.println("Please provide a valid query..");
          }
        } catch (SQLException se) {
          System.out.println(se);
        } catch (Exception e) {
          System.out.println("Error in input.");
        }

      }

      // Reports Menu -----------------------------------------------------------------------------
      while (reports) {
        System.out.print(MenuReports());
        System.out.print(promptUser());
        userInput = sc.nextLine();

        try {
          if (userInput.equals("0")) { // Exit Case
            reports = false;
            break;
          } else if (userInput.equals("1")) { // Bookings by City
            System.out.print("Enter a city: ");
            String city = sc.nextLine();
            sql = "SELECT COUNT(LISTNO) FROM BOOKING NATURAL JOIN LISTINGS WHERE CITY = '" + city
                + "';";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("2")) { // Bookings by City, PC
            System.out.print("Enter a city: ");
            String city = sc.nextLine();
            System.out.print("Enter a postal code (5 digits): ");
            String postalCode = sc.nextLine();
            sql = "SELECT COUNT(LISTNO) FROM BOOKING NATURAL JOIN LISTINGS WHERE CITY = '" + city
                + "' AND POSTAL = '" + postalCode + "';";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("3")) { // Listings by country
            sql = "select country, count(listno) from listings group by country";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("4")) { // Listings by country, city
            sql = "select city, country, count(listno) from listings group by country, city";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("5")) { // Total listings by country, city, PC
            sql = "select postal, city, country, count(listno) from listings group by country, "
                + "city, postal";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("6")) { // Host rank by country
            System.out.print("Enter a country: ");
            String country = sc.nextLine();
            sql = "select uname, ownership.SIN, count(ownership.sin), country "
                + "from listings JOIN OWNERSHIP JOIN User where country = '" + country
                + "' AND listings.listno = ownership.listno AND user.sin = ownership.sin "
                + "GROUP BY country, ownership.sin ORDER BY COUNT(ownership.sin) DESC";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("7")) { // Host rank by city
            System.out.print("Enter a country: ");
            String country = sc.nextLine();
            System.out.print("Enter a city: ");
            String city = sc.nextLine();
            sql = "select uname, ownership.SIN, count(ownership.sin), city, country "
                + "from listings JOIN OWNERSHIP JOIN User where country = '" + country
                + "' AND city = '" + city
                + "' AND listings.listno = ownership.listno AND user.sin = ownership.sin "
                + "GROUP BY country, ownership.sin, city ORDER BY COUNT(ownership.sin) DESC";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("8")) { // Hosts with >10% by city & country
            System.out.print("Enter a country: ");
            String country = sc.nextLine();
            System.out.print("Enter a city: ");
            String city = sc.nextLine();
            sql = "select uname, ownership.SIN, count(ownership.sin), city, country"
                + " from listings JOIN OWNERSHIP JOIN User WHERE country = '" + country
                + "' AND CITY like '" + city + "' "
                + " AND listings.listno = ownership.listno AND user.sin = ownership.sin"
                + " GROUP BY ownership.sin, country HAVING (10 * COUNT(ownership.sin) > ("
                + " select count(listno) from listings b natural JOIN ownership c "
                + " WHERE country like '" + country + "' AND CITY like '" + city
                + "' group by b.country)) ORDER BY COUNT(ownership.sin) DESC;";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("9")) { // Hosts with >10% by country
            System.out.print("Enter a country: ");
            String country = sc.nextLine();
            sql = "select uname, ownership.SIN, count(ownership.sin), country"
                + " from listings JOIN OWNERSHIP JOIN User WHERE country like '" + country
                + "' AND listings.listno = ownership.listno AND user.sin = ownership.sin"
                + " GROUP BY ownership.sin, country" + " HAVING (10 * COUNT(ownership.sin) > ("
                + "select count(listno) from listings b natural JOIN ownership c "
                + " WHERE country like '" + country + "' group by b.country))"
                + " ORDER BY COUNT(ownership.sin) DESC;";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("10")) { // Renter number of bookings
            System.out.print("Enter a start date (YYYY-MM-DD): ");
            String start = sc.nextLine();
            System.out.print("Enter an end date (YYYY-MM-DD): ");
            String end = sc.nextLine();
            sql = "select uname, renter.sin, count(renter.sin)"
                + " from renter join booking on renter.sin = booking.rentersin"
                + " join user on renter.sin = user.sin" + " where date_start >= '" + start
                + "' and date_end <= '" + end + "'"
                + " group by renter.sin order by count(renter.sin) DESC;";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("11")) { // Renter number of booking in a city (2+)
            System.out.print("Enter a start date (YYYY-MM-DD): ");
            String start = sc.nextLine();
            System.out.print("Enter an end date (YYYY-MM-DD): ");
            String end = sc.nextLine();
            System.out.print("Enter the city: ");
            String city = sc.nextLine();
            sql = "select uname, renter.sin, count(renter.sin), city "
                + " from listings join booking on booking.listno = listings.listno "
                + " join renter on renter.sin = booking.rentersin join user "
                + "on renter.sin = user.SIN where date_start >= '" + start + "' and date_end <= '"
                + end + "' and city = '" + city + "' " + "group by renter.sin "
                + "having (count(renter.sin) >= 2)  " + " order by count(renter.sin) DESC;";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("12")) { // Most Cancels in Bookings (renters)
            System.out.println("Most cancellations by renters: ");
            sql = "select uname, b.rentersin, count(b.rentersin) "
                + " from listings join (select * from booking where cancel like \"yes\") "
                + " as b on listings.listno = b.listno join renter on renter.sin = b.rentersin "
                + " join user on user.sin = renter.sin"
                + " where date_start >= \"2021-8-8\" group by b.rentersin"
                + " order by count(b.rentersin) desc;";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
            // Host cancellations are determined by
            // how many times their listings have been cancelled.
            System.out.println("Most cancellations by hosts: "); // Host cancellations
            sql = "select uname, ownership.sin, count(ownership.sin)"
                + " from user natural join ownership "
                + " join (select * from booking where cancel like \"yes\") "
                + " as b on ownership.listno = b.listno where date_start >= \"2021-8-8\""
                + " group by ownership.sin order by count(ownership.sin) desc;";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
          } else if (userInput.equals("13")) { // Max nouns per comments
            // Probably requires the use of an Index.
            // Currently not working.
            String[] nounList = {"color", "books", "house", "lake", "garden"};
            System.out.print("Enter a listing number to view it's popular nouns: ");
            String listno = sc.nextLine();
            sql = "SELECT comments FROM listing_comments where listno =" + listno + ";";
            rs = stmt.executeQuery(sql);
            System.out.println(outputRS(rs));
            String allText = "";
            while (rs.next()) {
              allText += rs.getString("comments");
            }
            // Call Noun Detection Method
            // Call Sorting Algorithm
            // For Loop on resulting list, sorted using prevalence high to low
          } else {
            System.out.println("Please select a valid number.");
          }
        } catch (SQLException se) {
          System.out.println(se);
        } catch (Exception e) {
          System.out.println("Error in input.");
        }

      }

      // Viewing Menu
      while (viewing) {
        System.out.print(MenuView());
        System.out.print(promptUser());
        userInput = sc.nextLine();

        try {
          if (userInput.equals("0")) { // Exit Case
            viewing = false;
          } else if (userInput.equals("1")) { // LISTINGS
            System.out.println("Format: #, Type, Long, Lat, Addr, Postal, City, Country");
            sql = "SELECT * FROM LISTINGS;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          } else if (userInput.equals("2")) { // USERS
            System.out.println("Format: SIN, name, job, address, birthdate");
            sql = "SELECT * FROM USERS;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          } else if (userInput.equals("3")) { // Reviews
            System.out.println("Format: Reviewer, Reviewee, STARS, Comment");
            sql = "SELECT reviewer, reviewee, rating, comment FROM Reviews;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          } else if (userInput.equals("4")) { // Calendar
            System.out.println("Format: Listing #, Owner SIN, Start, End, Price");
            sql = "SELECT * FROM CALENDAR;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          } else if (userInput.equals("5")) { // Rent History
            System.out.println("Format: Renter SIN, Listing #, Booking time");
            sql = "SELECT * FROM RENTER_HISTORY;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          } else if (userInput.equals("6")) { // Host Ownership
            System.out.println("Format: Owner SIN, Listing #");
            sql = "SELECT * FROM OWNERSHIP;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          } else if (userInput.equals("7")) { // House History
            System.out.println("Format: Listing #, Renter SIN");
            sql = "SELECT * FROM HOUSE_HISTORY;";
            rs = stmt.executeQuery(sql);
            System.out.print(outputRS(rs));
          }
        } catch (SQLException se) {
          System.out.println(se);
        } catch (Exception e) {
          System.out.println("Error in input.");
        }
      }

      // Check if exit in login menu
      if (!consoleOn) {
        System.out.println("\nExiting program.. ");
        break;
      }

      // Main Menu ---------------------------------------------------------------------------------
      // System.out.print("Logged in as " + userSIN + ": " + userType);
      System.out.print(MenuMain());
      System.out.print(promptUser());
      userInput = sc.nextLine();

      try {
        if (userInput.equalsIgnoreCase("exit")) { // Exit Case
          consoleOn = false;
          break;
        } else if (userInput.equals("1")) {

          // View Listings
          String filters = "";

          System.out.println("--- Listings search ---");

          // filters
          System.out.print("Do have a price range? (1 = yes, other = no) : ");
          userInput = sc.nextLine();
          if (userInput.equals("1")) {
            System.out.print("Lowest Price: ");
            String vLow = sc.nextLine();
            System.out.print("Highest Price: ");
            String vHigh = sc.nextLine();
            filters += " AND PRICE >= " + vLow + " AND PRICE <= " + vHigh;
          }
          System.out.print("Do you have a date range? (1 = yes, other = no) : ");
          userInput = sc.nextLine();
          if (userInput.equals("1")) {
            System.out.print("Start Date (YYYY-MM-DD): ");
            String vStart = sc.nextLine();
            System.out.print("End Date (YYYY-MM-DD): ");
            String vEnd = sc.nextLine();
            filters += " AND DATE_START <= '" + vStart + "' AND DATE_END >= '" + vEnd;
          }
          System.out.print("Do you want specific amenities? (1 = yes, other = no) : ");
          userInput = sc.nextLine();
          if (userInput.equals("1")) {
            System.out.print("Do you want air conditioning? (1 = yes, 0 = no): ");
            String amenA = sc.nextLine();
            System.out.print("Do you want heat? (1 = yes, 0 = no): ");
            String amenH = sc.nextLine();
            System.out.print("Should it be beside a lake? (1 = yes, 0 = no): ");
            String amenL = sc.nextLine();
            System.out.print("Do you have pets? (1 = yes, 0 = no): ");
            String amenP = sc.nextLine();
            System.out.print("Do you want internet? (1 = yes, 0 = no): ");
            String amenI = sc.nextLine();
            filters += " and ac = " + amenA + " and heat = " + amenH + " and lake = " + amenL
                + " and pets = " + amenP + " and internet = " + amenI;
          }

          // Location
          System.out.println("\nHow do you want to find the listing? ");
          System.out.println("[1] Near a geolocation \n[2] Near a postal code\n[3] Address Lookup");
          System.out.print("--> Select an option: ");
          userInput = sc.nextLine();
          if (userInput.equals("1")) { // Near a LONG/LAT in KM
            System.out.print("What Longtitude? ");
            String tempLong = sc.nextLine();
            System.out.print("What Latitude? ");
            String tempLat = sc.nextLine();
            System.out.print("Search radius? Default is 5km, press enter for default: ");
            userInput = sc.nextLine();
            String radius = "5";
            if (!userInput.equals("")) {
              radius = userInput;
            } else {
              radius = "5";
            }
            System.out.print("View order? (1 for Ascending, 2 for Descending): ");
            String vAsc = sc.nextLine();
            if (vAsc.equals("1")) {
              vAsc = "ASC";
            } else {
              vAsc = "DESC";
            }
            // Pythagoras theorem for radius of a circle with a queried column of Radius^2
            sql = "SELECT listno, housetype,longtitude,latitude, address, postal, city, country, "
                + "date_start, date_end, price, (POWER(LONGTITUDE - " + tempLong
                + ",2) + POWER(LATITUDE - " + tempLat + ",2)) as 'Distance'"
                + " FROM AMENITIES NATURAL JOIN LISTINGS " + "NATURAL join CALENDAR WHERE +"
                + "(POWER(LONGTITUDE - " + tempLong + ",2) + POWER(LATITUDE - " + tempLat
                + ",2) <= POWER(" + radius + ", 2)) " + filters + " ORDER BY Distance " + vAsc
                + ";";
          } else if (userInput.equals("2")) { // Near an American postal code
            System.out.print("Enter a 5-digit postal code: ");
            userInput = sc.nextLine();
            sql = "SELECT listno, housetype,longtitude,latitude, address, postal, city, country,"
                + "date_start, date_end, price"
                + " FROM AMENITIES NATURAL JOIN LISTINGS NATURAL JOIN CALENDAR "
                + "WHERE ABS(POSTAL - " + userInput + ") <= 8000 " + filters;
            // I have determined 8000 to be the postal code adjacent distance
          } else if (userInput.equals("3")) {
            System.out.print("Enter address number and street: ");
            String tempAddr = sc.nextLine();
            sql = "SELECT listno, housetype,longtitude,latitude, address, postal, city, country, "
                + "date_start, date_end, price FROM AMENITIES NATURAL JOIN LISTINGS "
                + "NATURAL JOIN CALENDAR WHERE ADDRESS = '" + tempAddr + "'" + filters + ";";
          } else {
            sql = "<Bad Input.. please try again>";
          }

          rs = stmt.executeQuery(sql);
          System.out.print(outputRS(rs));

        } else if (userInput.equals("2")) {

          // Create Listings

          System.out.println("Follow the prompts to create a new listing. ");
          System.out.print("What is the listing number? ");
          String listno = sc.nextLine();
          System.out.print("Housing Type? ");
          String listtype = sc.nextLine();
          System.out.print("Longitude? ");
          String listlong = sc.nextLine();
          System.out.print("Latitude? ");
          String listlat = sc.nextLine();
          System.out.print("Postal Code? ");
          String listpost = sc.nextLine();
          System.out.print("City? ");
          String listcity = sc.nextLine();
          System.out.print("Country? ");
          String listcn = sc.nextLine();

          try {
            sql = "INSERT INTO LISTINGS VALUES (" + listno + ",'" + listtype + "','" + listlong
                + "','" + listlat + "','" + listpost + "','" + listcity + "','" + listcn + "');";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO OWNERSHIP VALUES (" + listno + "," + userSIN + ");";
            stmt.executeUpdate(sql);
          } catch (SQLException se) {
            System.out.println(se);
          }

          // Host Toolkit - Calculates price difference due to a lack of amenities
          System.out.println("Host Toolkit for MyBnB:");
          System.out.println(
              "What amenities does this place have? For the following, 1 = Yes, other = No.");
          System.out.print("Does it have air conditioning?");
          String amenA = sc.nextLine();
          System.out.print("Does it have heat?");
          String amenH = sc.nextLine();
          System.out.print("Is it beside a lake?");
          String amenL = sc.nextLine();
          System.out.print("Are pets allowed?");
          String amenP = sc.nextLine();
          System.out.print("Does it supply internet?");
          String amenI = sc.nextLine();

          System.out.print("Calculating amenities.. ");
          int amenCalculation = 0;
          String[] amenResponse = {};
          int arSize = 0;
          if (!amenA.equals("1")) {
            amenCalculation += 100;
            arSize++;
            amenResponse[arSize] = "Air Conditioning";
          }
          if (!amenH.equals("1")) {
            amenCalculation += 105;
            arSize++;
            amenResponse[arSize] = "Heat";
          }
          if (!amenL.equals("1")) {
            amenCalculation += 300;
            arSize++;
            amenResponse[arSize] = "Lake";
          }
          if (!amenP.equals("1")) {
            amenCalculation += 75;
            arSize++;
            amenResponse[arSize] = "Pets";
          }
          if (!amenI.equals("1")) {
            amenCalculation += 60;
            arSize++;
            amenResponse[arSize] = "Internet";
          }
          System.out.println("With these amenities: ");
          for (int i = 0; i < arSize; i++) {
            System.out.println("[" + (i + 1) + "]" + amenResponse[i]);
          }
          System.out.println("You could be earning $" + amenCalculation + " more.");

          System.out
              .print("Would you like to place the listing on the market? (y = Yes, other = No)");
          userInput = sc.nextLine();
          if (userInput.equalsIgnoreCase("y")) {
            sql = "SELECT AVERAGE(PRICE) FROM CALENDAR;";
          }

        } else if (userInput.equals("3")) {

          // Update Listings

          System.out.println("Follow the prompts to update a listing. ");
          System.out.print("Enter the listing number: ");
          String listno = sc.nextLine();

          sql = "SELECT LISTNO FROM CALENDAR WHERE OWNER_SIN = " + userSIN + ";";
          rs = stmt.executeQuery(sql);
          if (!outputRS(rs).equals(listno)) {
            System.out.println("Unfortunately, you do not own this listing.");
            break;
          }

          // Date Change

          System.out.print("Would you like to change the listing dates? (1 = yes, other = no) :");
          userInput = sc.nextLine();
          if (userInput.equals("1")) {
            System.out.print("Set new listing days.\nStart Date (YYYY-MM-DD): ");
            String lists = sc.nextLine();
            System.out.print("End Date: ");
            String liste = sc.nextLine();

            sql = "SELECT LISTNO FROM BOOKING WHERE ((DATE_END <= '" + lists
                + "') OR (DATE_START >= '" + liste + "')) AND LISTNO = " + listno + ";";
            rs = stmt.executeQuery(sql);
            if (!outputRS(rs).equals(listno)) {
              System.out.println("Unfortunately, this listing cannot be updated.");
              break;
            }
            sql = "UPDATE CALENDAR SET DATE_START = '" + lists + "', DATE_END = '" + liste
                + "' WHERE LISTNO = " + listno + ";";
            stmt.executeUpdate(sql);
          }

          // Price Change

          System.out.print("Would you like to change the price? (1 = yes, other = no) :");
          userInput = sc.nextLine();
          if (userInput.equals("1")) {
            System.out.print("Enter a new price: ");
            userInput = sc.nextLine();
            sql = "UPDATE CALENDAR SET PRICE = " + userInput + ", WHERE LISTNO = " + listno + ";";
            stmt.executeUpdate(sql);
          }


        } else if (userInput.equals("4")) {

          // Delete listings from BNB

          System.out.println("Which listing do you want to delete? ");
          userInput = sc.nextLine();
          sql = "SELECT LISTNO FROM OWNERSHIP WHERE SIN = " + userSIN + " and listno = " + userInput
              + ";";
          String CanDeleteListNumber = outputRS(stmt.executeQuery(sql));
          if (CanDeleteListNumber == "") {
            System.out.println("Only the owner can delete the listing.");
            break;
          }
          sql = "DELETE FROM LISTINGS WHERE LISTNO = " + CanDeleteListNumber + ";";
          int success = stmt.executeUpdate(sql);
          if (success != 0) {
            System.out.println("Listing no. " + userInput + " has been removed.");
          } else {
            System.out.println("Action failed.");
          }

        } else if (userInput.equals("5")) {

          // Book Listings (new booking)

          System.out.println("Follow the prompts to book a new listing. ");
          System.out.println("Checking if you have a credit card on file... ");

          try {
            sql = "SELECT cc_no FROM RENTER WHERE SIN = " + userSIN + ";";
            String ccOnFile = outputRS(stmt.executeQuery(sql));
            if (ccOnFile != "") {
              System.out.println("Credit Card found: " + ccOnFile);
            } else {
              System.out.print("Please enter your credit card number (4 digits): ");
              ccOnFile = sc.nextLine();
              sql = "INSERT INTO RENTER VALUES (" + userSIN + "," + ccOnFile + ");";
            }
            System.out.print("You wish to book a BNB. What is the listing number? ");
            String listno = sc.nextLine();
            System.out.print("Enter a start date (YYYY-MM-DD): ");
            String lists = sc.nextLine();
            System.out.print("Enter an end date (YYYY-MM-DD): ");
            String liste = sc.nextLine();
            sql = "SELECT price FROM calendar WHERE listno = " + listno + ";";
            String listPrice = outputRS(stmt.executeQuery(sql));
            System.out.println("The BNB will cost $" + listPrice);
            System.out.println("Checking the listing's availiability... ");
            // Only allow someone to rent if their Start and End days fall inside availability.
            sql = "SELECT listno FROM calendar WHERE listno = " + listno + " and"
                + " DATE_START <= '" + lists + "' and DATE_END >= '" + liste + "';";
            rs = stmt.executeQuery(sql);
            if (outputRS(rs).equals(listno)) {
              System.out.println("Your credit card is being charged.. ");
              sql = "INSERT INTO BOOKING VALUES (" + listno + "," + userSIN + ",'" + lists + "','"
                  + liste + "','no');";
              stmt.executeUpdate(sql);
              System.out.println("Thank you for booking a listing.");
            } else {
              System.out.println("Unfortunately, the listing is not available.");
            }

          } catch (SQLException se) {
            System.out.print("Invalid input.");
          }

        } else if (userInput.equals("6")) {

          // Delete a booking

          System.out.println("Follow the prompts to delete a booking. ");
          System.out.println("What was the listing number?");
          String listno = sc.nextLine();
          System.out.println("Thank you. Checking if we can remove your booking... ");

          sql = "SELECT listno FROM BOOKING JOIN RENTER ON BOOKING.renterSIN = RENTER.SIN "
              + "WHERE listno = " + listno + ";";
          String canDeleteBookingNumber = outputRS(stmt.executeQuery(sql));
          if (canDeleteBookingNumber.equals(listno)) {
            System.out.println("The booking has been removed.");
            sql = "UPDATE BOOKING SET cancel = \"yes\" WHERE LISTNO = " + listno
                + " and renterSIN = " + userSIN + ";";
            stmt.executeUpdate(sql);
          } else {
            System.out.println("Sorry, this is not one of your bookings.");
          }

        } else if (userInput.equals("7")) {

          // Create a review

          System.out.println("To create a review, answer the prompts. ");
          System.out.print("I want to review a [1] BNB, [2] Renter, [3] Owner: ");
          String reviewType = sc.nextLine();
          System.out.print("The SIN or Listing number is: ");
          String reviewID = sc.nextLine();
          System.out.print("STARS (1 to 5): ");
          String reviewRate = sc.nextLine();
          System.out.print("Comments? ");
          String reviewComment = sc.nextLine();
          if (reviewType.equals("1") && userType == "r") { // Renter reviews house
            sql = "select listno from renter_history where listno = " + reviewID + "and sin = "
                + userSIN + ";";
            if (outputRS(stmt.executeQuery(sql)) == "") {
              System.out.println("You haven't rented this house before.");
              break;
            }
            sql = "INSERT INTO REVIEWS VALUES (" + userSIN + "," + reviewID + "," + reviewRate
                + ",'" + reviewComment + "', \"BNB Review\");";
            stmt.executeUpdate(sql);
          } else if (reviewType.equals("3") && userType == "r") { // Renter reviews Owner
            sql = "select ownership.sin from renter_history join ownership on ownership.listno "
                + " = renter_history.listno " + " where renter_history.sin = " + userSIN
                + " and ownership.sin =" + reviewID + ";";
            if (outputRS(stmt.executeQuery(sql)) == "") {
              System.out.println("You haven't rented from this owner.");
              break;
            }
            sql = "INSERT INTO REVIEWS VALUES (" + userSIN + "," + reviewID + "," + reviewRate
                + ",'" + reviewComment + "', \"Host Review\");";
            stmt.executeUpdate(sql);
          } else if (reviewType.equals("2") && userType == "o") { // Owner reviews renter
            sql = "select house_history.sin from house_history join ownership on ownership.listno "
                + " = house_history.listno where house_history.sin = " + reviewID
                + " and ownership.sin =" + userSIN + ";";
            if (outputRS(stmt.executeQuery(sql)) == "") {
              System.out.println("You haven't rented to this renter.");
              break;
            }
            sql = "INSERT INTO REVIEWS VALUES (" + userSIN + "," + reviewID + "," + reviewRate
                + ",'" + reviewComment + "', \"Renter Review\");";
            stmt.executeUpdate(sql);
          } else {
            System.out.println("Invalid input, back to main menu.");
          }

        } else if (userInput.equals("8")) {
          reports = true;
        } else if (userInput.equals("9")) {
          viewing = true;
        } else if (userInput.equals("0")) {
          System.out.println("Returning to login menu.");
          loggedIn = false;
        } else {
          System.out.println("Please input a proper query number.");
        }
      } catch (SQLException se) {
        System.out.println(se);
      } catch (Exception e) {
        System.out.println("Error in input, try again.");
      }

    }
    sc.close();
  }

}
