# AcmePayments
Acme Payments Exercise part of IOET recruitment process.

This project was made with Java using Maven, according to the restrictions of this exercise the only external library used is JUnit for testing purposes.

Before I begin detailing the process, I will list the initial assumptions that were taken into account when creating the solution.

Assumptions:
1) Daily rates will be uploaded in a text file called rates.txt with the specified format in the example file in the resources folder.
2) 00:00 will be considered as the start of day and 23:59 as end of day.
3) The employees work hours will be uploaded in a text file called workForm.txt following the format specified in the example file in the resources folder.
4) Employees will work complete hours with no remaining minutes.

Solution:

To solve the stated exercise, I decided to create 3 Object classes; Payment, Range and Rates.
These classes were created to maintain order and stability throughout the process.

The Payment class was created to store the data of the payment for each employee inputted in the txt file.  This class stores the employee name and a list of  daily rates. This list serves maintains control of each range the employee worked and separates each range with its respective hourly amount. This way if an employee worked through multiple ranges, each amount will be stored individually with its range.

The Rate class was created to store the data of each daily rate, this class serves two purposes: to establish the daily rates applied to each employee and to store the rates each employee worked. This solution guaranteed a great way to recycle the class and keep the amount of objects to a minimum.

The Range class was created to store the data of each range inputted by the user. In this object, the start and end times are stored along with an hourly amount. I found this class to be a very clean way to manipulate and work with this data. This class also allows the solution to be more flexible by allowing the user to establish their own ranges in the text file.

In the main class, I created an assortment of methods that execute everything needed to obtain the payment amount for each employee. Due to the small size of  the project, I decided to create these methods there, because I saw it kind of as an overkill to create Util classes or other classes with static methods for these functionalities.

The first thing created in the main class is a list of rates, with this list is that the payment amount is determined. After creating the list, the algorithm begins to deconstruct the data in the workForm file and creates a list of Payments. While creating this list, it validates that the ranges are correctly inputted and that everything is in order; I added validations to each possible error I saw as essential.

I began the solution applying TDD, meaning that I created failed tests and developed the solution in order to pass these tests. Due to the size of the project, I kept the tests short and to the point. Once these tests passed I knew my solution was ready to go.

The execution can be done by opening the files on an IDE and executing it or by creating a package using maven "maven clean package" and executing the generated jar (should be ACMEPayments-1.0-SNAPSHOT.jar) using the command "java - jar jarName".