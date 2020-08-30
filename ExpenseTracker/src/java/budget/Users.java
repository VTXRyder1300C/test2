package budget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;//format double
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    private double total = 0.00;
    private double totalMoney = 0.00;
    private boolean userExists = false;
    private boolean loginAttempted = false;
    private boolean passwordMismatch = false;
    private boolean insertSuccess = false;
    private boolean expenseError = false;
    private boolean expenseRemoved = false;
    private boolean incomeRemoved = false;
    private boolean incomeError = false;
    private boolean searchIncomeEmpty = false;
    private boolean searchExpenseEmpty = false;
    private boolean amountEmpty = false;
    private boolean typeEmpty = false;
    private boolean amountNotNumber = false;
    private String username;
    private String password;
    private String passwordCheck;
    private String name;
    private String incomeSearch1;
    private String incomeSearch2;
    private String expenseSearch1;
    private String expenseSearch2;
    String pattern = "yyyy/MM/dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String date = simpleDateFormat.format(new Date());
    String date2 = simpleDateFormat.format(new Date());
    private String type;
    private String amount;
    private double income;
    private String notes;
    private String email;
    private String userID = "0";//default for testing only
    private String expenseID;
    private String incomeID;
    private static String table = "";
    private static String[] data2 = new String[10000];
    private static int[] allExpenseIDs = new int[10000];
    private static int[] allIncomeIDs = new int[10000];
    private String[] expenseTableFromDatabase = new String[10000];
    private String[] incomeTableFromDatabase = new String[10000];
    private static int count = 0;
    private static int expenseIDCount = 0;
    private static int incomeIDCount = 0;
    private static String dbURL = "jdbc:derby://localhost:1527/contact;create=true;user=nbuser;password=nbuser";
    private static String usersTable = "APP.USERS";
    private static String incomeTable = "APP.INCOME";
    private static String expenseTable = "APP.EXPENSES";
//    private static String dbURL = "jdbc:mysql://budgetdatabase.c9nvdtjgpraj.us-west-1.rds.amazonaws.com:3306/finances?user=admin&password=Umgc495Pr0j3ct&useSSL=false";
//    private static String usersTable = "finances.USERS";
//    private static String incomeTable = "finances.INCOME";
//    private static String expenseTable = "finances.EXPENSES";
    private static int x;
    private Integer userCounter;//count number of users 
    private Integer incomeCounter = 0;//count rows of income
    private Integer expenseCounter = 0;//count rows of expenses
    private String test = "lets try something";
    DecimalFormat df = new DecimalFormat("#.##");
    DecimalFormat df2 = new DecimalFormat("#.00");

    // Chart vars
    private int categoryCount;
    private static String[] categoryNames = new String[10000];
    private static double[] categoryTotals = new double[10000];
    private int totalCounter;
    private int nameCounter;
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    private int countE;
    private int countI;
    private int i = 0;
    private int e = 0;
    //information sent from the page to update the java page

    public int getExpenseCategoryCount() {
        System.out.println("Getting expense Category count and resetting arrays/counter.");//test only see glassfish server output
        categoryCount = 0;
        categoryTotals = new double[10000];
        totalCounter = 0;
        categoryNames = new String[10000];
        nameCounter = 0;
        double totalForCategory = 0;
        DBC();
        try {//connect to the database
            System.out.println("Connecting to database for expense chart");//test only see glassfish server output
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("Select * from " + expenseTable + " order by type");
            //ResultSet results = stmt.executeQuery("Select type, Sum(cast(amount as decimal)) as amount FROM "+expenseTable+" WHERE USERID ="+userID+" GROUP BY type ORDER BY amount desc");
            while (results.next()) {
                String userID = results.getString(2);
                String type = results.getString(4);
                double amount = results.getDouble(5);
                System.out.println("Verifying username...");
                if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in or try and validate this information later 
                    System.out.println("Match! " + userID);
                    if (categoryCount == 0) {//ensure array does not go out of bounds
                        System.out.println("array, location " + categoryCount);
                        totalForCategory += amount;
                        categoryNames[categoryCount] = type;
                        categoryTotals[categoryCount] = totalForCategory;
                        categoryCount++;//Prepare next space...
                        System.out.println("First expense data loaded to chart arrays...");
                        System.out.println("Checking data if " + categoryNames[categoryCount - 1] + " is a match for " + type);
                    } //                    else if (categoryCount >= 20) {
                    //                        System.out.println("Over 20 categories, lumping smallest types into other");
                    //                        Arrays.sort(categoryTotals);
                    //                        categoryTotals[categoryCount] = totalForCategory;
                    //                        totalForCategory += amount;
                    //                    } 
                    else if (categoryNames[categoryCount - 1].equalsIgnoreCase(type)) {//matches type at previous location
                        System.out.println("Matching type found, array is at " + (categoryCount - 1));
                        System.out.println("array, location " + categoryCount);
                        totalForCategory += amount;
                        categoryTotals[categoryCount - 1] = totalForCategory;
                        System.out.println("array, " + (categoryCount - 1) + " loaded with " + categoryTotals[categoryCount - 1]);
                    } else {//no matches, store another type
                        System.out.println("No match for " + categoryNames[categoryCount - 1] + " and " + type);
                        totalForCategory = 0;//reset to 0
                        totalForCategory += amount;// add initial amount
                        categoryNames[categoryCount] = type;
                        categoryTotals[categoryCount] = totalForCategory;
                        categoryCount++;
                    }
                }
            }
            results.close();
            stmt.close();
            System.out.println("Expense chart data stored");
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return categoryCount;
    }

    public double getExpenseCategoryTotals() {
        totalCounter++;
        return categoryTotals[totalCounter - 1];
    }

    public String getExpenseCategoryNames() {
        nameCounter++;
        return categoryNames[nameCounter - 1];
    }

    public int getIncomeCategoryCount() {
        System.out.println("Getting Income Category count and resetting arrays/counter.");//test only see glassfish server output
        categoryCount = 0;
        categoryTotals = new double[10000];
        totalCounter = 0;
        categoryNames = new String[10000];
        nameCounter = 0;
        double totalForCategory = 0;
        DBC();
        try {//connect to the database
            System.out.println("Connecting to database for income chart");//test only see glassfish server output
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("Select * from " + incomeTable + " order by type");
            while (results.next()) {
                String userID = results.getString(2);
                String type = results.getString(4);
                double amount = results.getDouble(5);
                System.out.println("Verifying username...");
                if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in or try and validate this information later 
                    System.out.println("Match! " + userID);
                    if (categoryCount == 0) {//ensure array does not go out of bounds
                        System.out.println("array, location " + categoryCount);
                        totalForCategory += amount;
                        categoryNames[categoryCount] = type;
                        categoryTotals[categoryCount] = totalForCategory;
                        categoryCount++;//Prepare next space...
                        System.out.println("First income data loaded to chart arrays...");
                        System.out.println("Checking data if " + categoryNames[categoryCount - 1] + " is a match for " + type);
                    } //                    else if (categoryCount >= 20) {
                    //                        System.out.println("Over 20 categories, lumping smallest types into other");
                    //                        Arrays.sort(categoryTotals);
                    //                        categoryTotals[categoryCount] = totalForCategory;
                    //                        totalForCategory += amount;
                    //                    } 
                    else if (categoryNames[categoryCount - 1].equalsIgnoreCase(type)) {//matches type at previous location
                        System.out.println("Matching type found, array is at " + (categoryCount - 1));
                        System.out.println("array, location " + categoryCount);
                        totalForCategory += amount;
                        categoryTotals[categoryCount - 1] = totalForCategory;
                        System.out.println("array, " + (categoryCount - 1) + " loaded with " + categoryTotals[categoryCount - 1]);
                    } else {//no matches, store another type
                        System.out.println("No match for " + categoryNames[categoryCount - 1] + " and " + type);
                        totalForCategory = 0;//reset to 0
                        totalForCategory += amount;// add initial amount
                        categoryNames[categoryCount] = type;
                        categoryTotals[categoryCount] = totalForCategory;
                        categoryCount++;
                    }
                }
            }
            results.close();
            stmt.close();
            System.out.println("Income chart data stored");
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return categoryCount;
    }

    public double getIncomeCategoryTotals() {
        totalCounter++;
        return categoryTotals[totalCounter - 1];
    }

    public String getIncomeCategoryNames() {
        nameCounter++;
        return categoryNames[nameCounter - 1];
    }

    public void DBC() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
//            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setExpenseID(String expenseID) {
        this.expenseID = expenseID;
    }

    public void setIncomeID(String incomeID) {
        this.incomeID = incomeID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //transactions
    public void setDate(String date) {
        this.date = date;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setIncomeSearch1(String incomeSearch1) {
        this.incomeSearch1 = incomeSearch1;
    }

    public void setIncomeSearch2(String incomeSearch2) {
        this.incomeSearch2 = incomeSearch2;
    }

    public void setExpenseSearch1(String expenseSearch1) {
        this.expenseSearch1 = expenseSearch1;
    }

    public void setExpenseSearch2(String expenseSearch2) {
        this.expenseSearch2 = expenseSearch2;
    }

    //Requests for the information from the HMTL page
    public String getData2() {
        x++;
        return data2[x - 1];
    }

    public String getTest() {
        //return data[0];
        return test;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    //transactions
    public String getDate() {
        return date;
    }

    public String getDate2() {
        return date2;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }

    public double getIncome() {
        return income;
    }

    public String getNotes() {
        return notes;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    public Boolean getPasswordMismatch() {
        return passwordMismatch;
    }

    public Boolean getSearchIncomeEmpty() {
        return searchIncomeEmpty;
    }

    public Boolean getSearchExpenseEmpty() {
        return searchExpenseEmpty;
    }

    public String getEmail() {
        return email;
    }

    public boolean getloginAttempted() {
        return loginAttempted;
    }

    public boolean getuserExists() {
        return userExists;
    }

    public boolean getExpenseError() {
        return expenseError;
    }

    public boolean getIncomeError() {
        return incomeError;
    }

    public boolean getIncomeRemoved() {
        return incomeRemoved;
    }

    public boolean getExpenseRemoved() {
        return expenseRemoved;
    }

    public String getTable() {
        return table;
    }

    public String getIncomeID() {
        return incomeID;
    }

    public String getExpenseID() {
        return expenseID;
    }

    public String getIncomeSearch1() {
        return incomeSearch1;
    }

    public String getIncomeSearch2() {
        return incomeSearch2;
    }

    public String getExpenseSearch1() {
        return expenseSearch1;
    }

    public String getExpenseSearch2() {
        return expenseSearch2;
    }

    public Boolean getInsertSuccess() {
        return insertSuccess;
    }

    public Boolean getTypeEmpty() {
        return typeEmpty;
    }

    public Boolean getAmountEmpty() {
        return amountEmpty;
    }

    public Boolean getAmountNotNumber() {
        return amountNotNumber;
    }

    public String getTotal() {
        //format the total to only 2 digits...some how the system sees "total" as a string?
        String formatTotal = String.format("%.2f", total);
        double formattedTotal = Double.valueOf(formatTotal);
        String s = df2.format(formattedTotal);
        return s;
    }

    public String getTotalMoney() {
        this.totalMoney = 0.00;
        DBC();
        try {//connect to the database
            System.out.println("Connecting to database to get total income for getTotalMoney() method...");//test only see glassfish server output
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + incomeTable + " order by INCOMEID ASC");
            while (results.next()) {
                String userID = results.getString(2);
                double amount = results.getDouble(5);
                if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in or try and validate this information later
                    this.totalMoney += amount;
                }
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        DBC();
        try {//connect to the database
            System.out.println("Connecting to database to get total expense for getTotalMoney() method...");//test only see glassfish server output
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + expenseTable + " order by EXPENSEID ASC");
            while (results.next()) {
                String userID = results.getString(2);
                double amount = results.getDouble(5);
                if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in 
                    this.totalMoney -= amount;
                }
            }
            results.close();
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        String formatTotal = String.format("%.2f", totalMoney);
        double formattedTotal = Double.valueOf(formatTotal);
        String s = df2.format(formattedTotal);
        return s;
    }

    //lets try and build a better table by getting each object by itself...
    public int getExpenseObjectCount() {
        this.x = 0;
        this.e = 0;
        int countE = 0;
        allExpenseIDs = new int[10000];
        expenseIDCount = 0;
        DBC();
        try {//connect to the database
            System.out.println("Connecting to database for getExpenseObjectCount() method...");//test only see glassfish server output
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + expenseTable + " order by EXPENSEID ASC");
            while (results.next()) {
                String expenseID = results.getString(1);
                String userID = results.getString(2);
                String theDate = results.getString(3);
                String type = results.getString(4);
                double amount = results.getDouble(5);
                String formatTotal = String.format("%.2f", amount);
                double formattedTotal = Double.valueOf(formatTotal);
                String notes = results.getString(6);
                if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in or try and validate this information later
                    expenseTableFromDatabase[countE] = expenseID;
                    allExpenseIDs[expenseIDCount] = Integer.valueOf(expenseID);
                    countE++;
                    expenseTableFromDatabase[countE] = theDate;
                    countE++;
                    expenseTableFromDatabase[countE] = type;
                    countE++;
                    expenseTableFromDatabase[countE] = "" + df2.format(formattedTotal);
                    countE++;
                    expenseTableFromDatabase[countE] = notes;
                    countE++;
                    expenseIDCount++;

                }
            }
            this.countE = countE;
            results.close();
            stmt.close();;
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return countE;
    }

    public int getCountE() {
        return (countE / 5);
    }

    public int getIncomeIDCount() {
        return incomeIDCount;
    }

    public int getAllIncomeIDs() {
        i++;
        return allIncomeIDs[i - 1];
    }

    public int getAllExpenseIDs() {
        e++;
        return allExpenseIDs[e - 1];
    }

    public int getExpenseIDCount() {
        return expenseIDCount;
    }

    public String getExpenseTableData() {
        x++;
        return expenseTableFromDatabase[x - 1];
    }

    public int getIncomeObjectCount() {
        this.i = 0;
        this.x = 0;
        int countI = 0;
        allIncomeIDs = new int[10000];
        incomeIDCount = 0;
        DBC();
        try {//connect to the database
            System.out.println("Connecting to database for getIncomeObjectCount() method...");//test only see glassfish server output
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + incomeTable + " order by INCOMEID ASC");
            while (results.next()) {
                String incomeID = results.getString(1);
                String userID = results.getString(2);
                String theDate = results.getString(3);
                String type = results.getString(4);
                double amount = results.getDouble(5);
                String formatTotal = String.format("%.2f", amount);
                double formattedTotal = Double.valueOf(formatTotal);
                String notes = results.getString(6);
                if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in or try and validate this information later
                    incomeTableFromDatabase[countI] = incomeID;
                    allIncomeIDs[incomeIDCount] = Integer.valueOf(incomeID);
                    countI++;
                    incomeTableFromDatabase[countI] = theDate;
                    countI++;
                    incomeTableFromDatabase[countI] = type;
                    countI++;
                    incomeTableFromDatabase[countI] = "" + df2.format(formattedTotal);
                    countI++;
                    incomeTableFromDatabase[countI] = notes;
                    countI++;
                    incomeIDCount++;
                }
            }
            this.countI = countI;
            results.close();
            stmt.close();;
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
        return countI;
    }

    public int getCountI() {
        return (countI / 5);
    }

    public String getIncomeTableData() {
        x++;
        return incomeTableFromDatabase[x - 1];
    }

    boolean isAmountDouble(String str) {//check is input is actually a double
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getCount() {
        return count;
    }

    public int getRowCount() {
        return count / 5;
    }

    public void setTable(String t) {//t holds the value that is passed in
        if ("login".equalsIgnoreCase(t)) {
            System.out.println("Logging in...");//test only see glassfish server output
            allIncomeIDs = new int[10000];
            incomeIDCount = 0;
            allExpenseIDs = new int[10000];
            expenseIDCount = 0;
            this.loginAttempted = true;
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();
            try {//connect to the database
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + usersTable);
                while (results.next()) {
                    String userID = results.getString(1);
                    String username = results.getString(2);
                    String password = results.getString(3);
                    String name = results.getString(4);
                    if (this.username.equalsIgnoreCase(username) && this.password.equals(password)) {
                        this.userID = userID;
                        this.username = username;
                        this.name = name;
                        System.out.println("User exists and passwords match!");//test only see glassfish server output
                        break;//kill the loop after a valid user is found
                    } else if (this.username.equalsIgnoreCase(username)) {
                        System.out.println("User exists, but password is wrong...");//test only see glassfish server output
                    }
                    count++;
                }
                results.close();
                stmt.close();
                System.out.println("Setting loggedIn to true");
                stmt = conn.createStatement();
                //stmt.execute("Update "+usersTable+" SET loggedIn = true WHERE USERID =" + userID + "");
                System.out.println("User is logged in and loggedIn is set to true");
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        } else if ("logout".equalsIgnoreCase(t)) {
            System.out.println("Logging out...");//test only see glassfish server output
            DBC();
            try {//connect to the database
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                stmt.execute("Update " + usersTable + " SET loggedIn = false WHERE USERID =" + userID + "");
                stmt.close();
                System.out.println("User is logged out and loggedIn is set to false");
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        } else if ("create".equalsIgnoreCase(t)) {
            System.out.println("Creating user...");//test only see glassfish server output
            allIncomeIDs = new int[10000];
            incomeIDCount = 0;
            allExpenseIDs = new int[10000];
            expenseIDCount = 0;
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();

            try {//connect to the database
                System.out.println("Connecting to database for: " + t + " check");//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + usersTable);
                while (results.next()) {
                    String username = results.getString(2);
                    if (this.username.equalsIgnoreCase(username)) {
                        System.out.println("Username taken...");
                        this.userExists = true;
                        this.userID = "0";
                        break;//kill the loop after a valid user is found
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
            if (!userExists) {//check if the user exists

                //checking new user data
                System.out.println("No other user exist with that username...");
                System.out.println("Checking matching passwords..." + password + " " + passwordCheck);

                if (!password.equals(passwordCheck)) {//check if passwords match
                    passwordMismatch = true;
                    //password matches
                    System.out.println("Passwords do not match..." + password + " " + passwordCheck);
                    this.userID = "0";
                } else {
                    System.out.println("Passwords match!");
                    //END OF TESTING
                    // Getting row count
                    System.out.println("Getting unique userID...");//For TESTING...See Glassfish Server for outputs

                    DBC();
                    try {//connect to the database
                        System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                        stmt = conn.createStatement();
                        ResultSet results = stmt.executeQuery("SELECT COUNT(*) AS rowcount from " + usersTable);
                        results.next();
                        this.userCounter = results.getInt("rowcount");//set the user counter to the row count
                        results.close();
                        stmt.close();
                    } catch (SQLException sqlExcept) {
                        sqlExcept.printStackTrace();
                    }
                    count = 0;//reset counter for array
                    x = 0;//reset counter in get
                    try {
                        userCounter++;//add a new row and ensure a unique ID
                        this.userID = userCounter.toString();
                        DBC();
                        conn = DriverManager.getConnection(dbURL);
                        stmt = conn.createStatement();
                        //add a test statement and do not use the data passed into the page (TESTING ONLY)
                        //stmt.execute("insert into " + usersTable + " values (" + "123" + ",'" + t + "','" + t + "'," + 5 + ",'" + t + ")");
                        stmt.execute("insert into " + usersTable + " values ('" + userCounter + "','" + username + "','" + password + "','" + name + "','" + email + "','" + 1 + "')");
                        stmt.close();
                        System.out.println("Success in creating user...");//For TESTING...See Glassfish Server for outputs
                        //set the IDs and login the user
                        stmt.close();
                    } catch (Exception sqlExcept) {
                        System.out.println("Failure to create user");//For TESTING...See Glassfish Server for outputs
                        sqlExcept.printStackTrace();
                    }
                }
            }
        } else if ("income".equalsIgnoreCase(t)) {
            System.out.println("Getting income...");//test only see glassfish server output
            allIncomeIDs = new int[10000];
            incomeIDCount = 0;
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();
            try {//connect to the database
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + incomeTable + " order by INCOMEID ASC");
                int onlyOnce = 0;//only load the object array once
                while (results.next()) {
                    String userID = results.getString(2);
                    double amount = results.getDouble(5);

                    if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in or try and validate this information later
                        if (onlyOnce == 0) {
                            onlyOnce++;
                            System.out.println("Loading income array...");//test only see glassfish server output
                            getIncomeObjectCount();
                        }
                        this.total += amount;
                    }
                    count++;
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        } else if ("expense".equalsIgnoreCase(t)) {
            System.out.println("Getting Expenses...");//test only see glassfish server output
            allExpenseIDs = new int[10000];
            expenseIDCount = 0;
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();
            try {//connect to the database
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + expenseTable + " order by EXPENSEID ASC");
                int onlyOnce = 0;
                while (results.next()) {
                    String userID = results.getString(2);
                    double amount = results.getDouble(5);
                    if (userID.equalsIgnoreCase(this.userID)) {//only display for the USERID logged in 
                        if (onlyOnce == 0) {
                            onlyOnce++;
                            System.out.println("Loading expense array..");//test only see glassfish server output
                            getExpenseObjectCount();
                        }
                        this.total -= amount;
                    }
                    count++;
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        } else if ("insertIncome".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to insert income...");//test only see glassfish server output
            if (type.isEmpty() || amount.isEmpty() || !isAmountDouble(amount)) {
                if (type.isEmpty()) {
                    System.out.println("type is empty...exiting!");
                    typeEmpty = true;
                }
                if (amount.isEmpty()) {
                    System.out.println("amount is empty...exiting! ");
                    amountEmpty = true;
                }
                if (!isAmountDouble(amount)) {
                    System.out.println("amount is not a number...exiting! ");
                    amountNotNumber = true;
                }
            } else {
                System.out.println("insert income data valid...");//test only see glassfish server output
                count = 0;//reset counter for array
                x = 0;//reset counter in get
                try {//connect to the database
                    System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                    stmt = conn.createStatement();
                    //determine unique ID
                    ResultSet results = stmt.executeQuery("select incomeID from " + incomeTable + " order by INCOMEID ASC");
                    while (results.next()) {
                        int incomeID = results.getInt(1);
                        if (incomeCounter == incomeID) {
                            this.incomeCounter++;
                        }
                    }
                    results.close();
                    stmt.close();
                } catch (SQLException sqlExcept) {
                    sqlExcept.printStackTrace();
                }
                count = 0;//reset counter for array
                x = 0;//reset counter in get
                try {
                    DBC();
                    conn = DriverManager.getConnection(dbURL);
                    stmt = conn.createStatement();
                    //remove any extra 0's
                    double d = Double.parseDouble(amount);
                    if (d < 0) {//no negate inputs allowed
                        d *= -1;
                    }
                    System.out.println("Inserting income...");//test only see glassfish server output
                    stmt.execute("insert into " + incomeTable + " values (" + incomeCounter + ",'" + userID + "','" + date + "','" + type + "','" + df.format(d) + "','" + notes + "')");
                    stmt.close();
                    System.out.println("income data inserted!");//test only see glassfish server output
                    insertSuccess = true;
                    this.amount = "";
                    this.type = "";
                    this.notes = "";
                } catch (Exception sqlExcept) {
                    System.out.println("Failure..." + sqlExcept);//test only see glassfish server output
                    sqlExcept.printStackTrace();
                }
            }
        } else if ("insertExpense".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to insert expense...");//test only see glassfish server output
            if (type.isEmpty() || amount.isEmpty() || !isAmountDouble(amount)) {
                if (type.isEmpty()) {
                    System.out.println("type is empty... exiting! ");
                    typeEmpty = true;
                }
                if (amount.isEmpty()) {
                    System.out.println("amount is empty... exiting! ");
                    amountEmpty = true;
                }
                if (!isAmountDouble(amount)) {
                    System.out.println("amount is not a number... exiting! ");
                    amountNotNumber = true;
                }
            } else {
                System.out.println("expense data is valid...");//test only see glassfish server output
                count = 0;//reset counter for array
                x = 0;//reset counter in get
                try {//connect to the database
                    System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                    stmt = conn.createStatement();
                    //determine unique ID
                    ResultSet results = stmt.executeQuery("select expenseID from " + expenseTable + " order by EXPENSEID ASC");
                    while (results.next()) {
                        int expenseID = results.getInt(1);
                        if (expenseCounter == expenseID) {
                            this.expenseCounter++;
                        }
                    }
                    results.close();
                    stmt.close();
                } catch (SQLException sqlExcept) {
                    sqlExcept.printStackTrace();
                }
                count = 0;//reset counter for array
                x = 0;//reset counter in get
                try {
                    DBC();
                    conn = DriverManager.getConnection(dbURL);
                    stmt = conn.createStatement();
                    double d = Double.parseDouble(amount);
                    if (d < 0) {
                        d *= -1;
                    }
                    System.out.println("Inserting expense..");//test only see glassfish server output
                    stmt.execute("insert into " + expenseTable + " values (" + expenseCounter + ",'" + userID + "','" + date + "','" + type + "','" + df.format(d) + "','" + notes + "')");
                    stmt.close();
                    System.out.println("insert expense successful!");//test only see glassfish server output
                    insertSuccess = true;
                    this.amount = "";
                    this.type = "";
                    this.notes = "";
                } catch (Exception sqlExcept) {
                    System.out.println("Failure: " + sqlExcept);//test only see glassfish server output
                    sqlExcept.printStackTrace();
                }
            }
        } else if ("removeExpense".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to remove expense...");//test only see glassfish server output
            allExpenseIDs = new int[10000];
            expenseIDCount = 0;
            x = 0;
            DBC();
            try {//connect to the database
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + expenseTable + " order by EXPENSEID ASC");
                while (results.next()) {
                    String expenseID = results.getString(1);
                    String userID = results.getString(2);
                    expenseError = true;//true if nothing is found
                    if (userID.equalsIgnoreCase(this.userID)) {
                        if (expenseID.equalsIgnoreCase(this.expenseID)) {
                            System.out.println("Found expense to remove!");//test only see glassfish server output
                            expenseError = false;
                            expenseRemoved = true;
                            stmt.execute("Delete from " + expenseTable + " where expenseID = " + expenseID);
                            System.out.println("Expense with expense ID " + expenseID + " deleted!");//test only see glassfish server output
                            break;//A. no need to keep going B. While loop flags an error (because there is no next if deleting last row)
                        } else {
                        }
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        } else if ("removeIncome".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to remove income...");//test only see glassfish server output
            allIncomeIDs = new int[10000];
            incomeIDCount = 0;
            x = 0;
            DBC();
            try {//connect to the database
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + incomeTable + " order by INCOMEID ASC");
                while (results.next()) {
                    String incomeID = results.getString(1);
                    String userID = results.getString(2);
                    incomeError = true;//true if nothing is found
                    if (userID.equalsIgnoreCase(this.userID)) {
                        if (incomeID.equalsIgnoreCase(this.incomeID)) {
                            System.out.println("Found income to remove...");//test only see glassfish server output
                            incomeError = false;
                            incomeRemoved = true;
                            stmt.execute("Delete from " + incomeTable + " where incomeID = " + incomeID);
                            System.out.println("Income with incomeID " + incomeID + " removed!");//test only see glassfish server output
                            break;//A. no need to keep going B. While loop flags an error (because there is no next if deleting last row)
                        } else {
                        }
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                sqlExcept.printStackTrace();
            }
        } else if ("incomeSearch".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to search income...");//test only see glassfish server output
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();
            try {
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + incomeTable + " WHERE " + incomeSearch1 + " = '" + incomeSearch2 + "' order by INCOMEID ASC");
                searchIncomeEmpty = true;
                System.out.println("Getting results from database and loading array...");//test only see glassfish server output
                while (results.next()) {
                    String incomeID = results.getString(1);
                    String userID = results.getString(2);
                    String theDate = results.getString(3);
                    String type = results.getString(4);
                    double amount = results.getDouble(5);
                    String formatTotal = String.format("%.2f", amount);
                    double formattedTotal = Double.valueOf(formatTotal);
                    String notes = results.getString(6);
                    if (userID.equalsIgnoreCase(this.userID)) {
                        searchIncomeEmpty = false;
                        data2[count] = incomeID;
                        count++;
                        data2[count] = theDate;
                        count++;
                        data2[count] = type;
                        count++;
                        data2[count] = "" + df2.format(formattedTotal);
                        count++;
                        data2[count] = notes;
                        count++;
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                System.out.println("Faulure:  " + sqlExcept);//test only see glassfish server output
                sqlExcept.printStackTrace();
            }
        } else if ("expenseSearch".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to search expenses");//test only see glassfish server output
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();
            try {
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + expenseTable + " WHERE " + expenseSearch1 + " = '" + expenseSearch2 + "' order by EXPENSEID ASC");
                searchExpenseEmpty = true;
                System.out.println("Getting results from database and loading array...");//test only see glassfish server output
                while (results.next()) {
                    String expenseID = results.getString(1);
                    String userID = results.getString(2);
                    String theDate = results.getString(3);
                    String type = results.getString(4);
                    double amount = results.getDouble(5);
                    String formatTotal = String.format("%.2f", amount);
                    double formattedTotal = Double.valueOf(formatTotal);
                    String notes = results.getString(6);
                    if (userID.equalsIgnoreCase(this.userID)) {
                        searchExpenseEmpty = false;
                        data2[count] = expenseID;
                        count++;
                        data2[count] = theDate;
                        count++;
                        data2[count] = type;
                        count++;
                        data2[count] = "" + df2.format(formattedTotal);
                        count++;
                        data2[count] = notes;
                        count++;
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                System.out.println("Failure: " + sqlExcept);//test only see glassfish server output
                sqlExcept.printStackTrace();
            }
        } else if ("expenseDateSearch".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to search expense by date...");//test only see glassfish server output
            data2 = new String[1000];//clear entire array
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            DBC();
            System.out.println("Connecting to database for: " + t);//test only see glassfish server output

            try {
                stmt = conn.createStatement();
                System.out.println("Expense Date Search: " + date + " and date 2 " + date2);
                ResultSet results = stmt.executeQuery("select * from " + expenseTable + " WHERE  (date between '" + date + "' and '" + date2 + "') order by EXPENSEID ASC");
                searchExpenseEmpty = true;
                System.out.println("Getting results from database and loading array...");//test only see glassfish server output
                while (results.next()) {
                    String expenseID = results.getString(1);
                    String userID = results.getString(2);
                    String theDate = results.getString(3);
                    String type = results.getString(4);
                    double amount = results.getDouble(5);
                    String formatTotal = String.format("%.2f", amount);
                    double formattedTotal = Double.valueOf(formatTotal);
                    String notes = results.getString(6);
                    if (userID.equalsIgnoreCase(this.userID)) {
                        searchExpenseEmpty = false;
                        data2[count] = expenseID;
                        count++;
                        data2[count] = theDate;
                        count++;
                        data2[count] = type;
                        count++;
                        data2[count] = "" + df2.format(formattedTotal);
                        count++;
                        data2[count] = notes;
                        count++;
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                System.out.println("Failure: " + sqlExcept);//test only see glassfish server output
                sqlExcept.printStackTrace();
            }
        } else if ("incomeDateSearch".equalsIgnoreCase(t)) {
            System.out.println("Getting ready to search income by : " + date + " and date 2 " + date2);
            count = 0;//reset counter for array
            x = 0;//reset counter in get
            //data[0] = "Nothing matches your selection, please try again!\n";
            DBC();
            try {
                System.out.println("Connecting to database for: " + t);//test only see glassfish server output
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery("select * from " + incomeTable + " WHERE  (date between '" + date + "' and '" + date2 + "') order by INCOMEID ASC");
                searchIncomeEmpty = true;
                System.out.println("Getting results from database and loading array...");//test only see glassfish server output
                while (results.next()) {
                    String incomeID = results.getString(1);
                    String userID = results.getString(2);
                    String theDate = results.getString(3);
                    String type = results.getString(4);
                    double amount = results.getDouble(5);
                    String formatTotal = String.format("%.2f", amount);
                    double formattedTotal = Double.valueOf(formatTotal);
                    String notes = results.getString(6);
                    if (userID.equalsIgnoreCase(this.userID)) {
                        searchIncomeEmpty = false;
                        data2[count] = incomeID;
                        count++;
                        data2[count] = theDate;
                        count++;
                        data2[count] = type;
                        count++;
                        data2[count] = "" + df2.format(formattedTotal);
                        count++;
                        data2[count] = notes;
                        count++;
                    }
                }
                results.close();
                stmt.close();
            } catch (SQLException sqlExcept) {
                System.out.println("Failure: " + sqlExcept);//test only see glassfish server output
                sqlExcept.printStackTrace();
            }
        } else {
            System.out.println("Error... nothing caught in setTable... Please ensure proper case of operation.  Such as(insertExpense..etc");//test only see glassfish server output
        }
    }
}
