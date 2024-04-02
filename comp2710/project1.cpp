/* Matthew Rogers
* To run the program:
 * Compile using g++ in the following command: g++ project1.cpp
 * Run the generated executable using the command: ./a.out
 * Enter a positive loan amount, a non-negative interest, and a positive monthly payment.
 * The program will calculate how many months it will take to pay off the loan.
* Sources:
* I used the Project1 hints PDF to help with formatting the output in columns.
* I used StackOverflow.com to learn how to declare and instantiate multiple variables on one line.
*   https://stackoverflow.com/questions/6838408/how-can-i-declare-and-define-multiple-variables-in-one-line-using-c
* I used StackOverflow.com to learn the syntax for ternary operators in C++.
*   https://stackoverflow.com/questions/392932/how-do-i-use-the-conditional-ternary-operator
*/

#include <iostream>
using namespace std;

int main() {
    double loan = -1, rate = -1, monthly = -1, pRate = -1, interest = -1, interestPaid = 0, principle = -1;

    cout.setf(ios::fixed);
    cout.setf(ios::showpoint);
    cout.precision(2);

    while (monthly <= pRate * loan) { // Monthly payments must be larger than monthly interest
        loan = rate = monthly = pRate = -1;
        while (loan <= 0){ // Loan must be positive
            cout << "\nLoan Amount: ";
            cin >> loan;
        }

        while (rate < 0){ // Rate must not be negative
            cout << "\nInterest Rate (% per year): ";
            cin >> rate;
        }
        rate /= 12;
        pRate = rate / 100;

        while (monthly <= 0) { // Monthly payments must be positive
            cout << "\nMonthly Payments: ";
            cin >> monthly;
        }

        if(monthly <= pRate * loan){
            cout << "\nWarning: Please ensure that regular payments are larger than any monthly interest.\n";
        }
    }

    // Header
    cout << "\n*****************************************************************\n"
         << "\tAmortization Table\n"
         << "*****************************************************************\n"
         << "Month\tBalance\t\tPayment\tRate\tInterest\tPrincipal\n";

    int month = 0;
    string tabs;
    while (loan > 0){ // Paying of the loan
        if(month == 0){ // Initial month, no interest or payments
            tabs = (loan >= 1000 ? "\t$" : "\t\t$");
            cout << month << "\t$" << loan << tabs << "N/A\tN/A\tN/A\t\tN/A\n";
        }
        else { // Months with interest and payments
            interest = pRate * loan;
            interestPaid += interest;
            loan += interest;
            if (loan >= monthly){ // Normal payment
                principle = monthly - interest;
                loan = loan - monthly;
            }
            else { // Odd last payment
                monthly = loan;
                principle = monthly - interest;
                loan = 0;
            }
            // Formatting and output
            tabs = (loan >= 1000 ? "\t$" : "\t\t$");
            cout << month << "\t$" << loan << tabs << monthly << "\t" << rate << "\t$" << interest << "\t\t$" << principle << "\n";
        }
        month++;
    }
    // Output summary
    cout << "*****************************************************************\n";
    cout << "\nIt takes " << --month << " months to pay off "
         << "the loan.\n"
         << "Total interest paid is: $" << interestPaid;
    cout << "\n\n";


    return 0;
}
