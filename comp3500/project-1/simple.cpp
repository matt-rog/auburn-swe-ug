/* First Last
* user@auburn.edu
* simple.cpp
* Program Description:
    * Accepts an n-length array of positive integers, calculates n!, and calculates the std dev of the integers.
* Resources:
    * I used this StackOverflow answer to refresh on string->int conversions and type checking: https://stackoverflow.com/questions/40031543/how-do-i-validate-int-input-using-stdstoi-in-c
    * I modified the provided standard deviation and factorial methods from COMP 3500 canvas course.
*/

#include <bits/stdc++.h>
#include <iostream>
#include <cmath>
#include <vector>
#include <sstream>
using namespace std;


float calculateSD(vector<int> data)
{
    float sum = 0.0, mean, standardDeviation = 0.0;
    int dataSize = data.size();
    int i;

    for(i = 0; i < dataSize; ++i)
    {
        sum += data[i];
    }

    mean = sum/dataSize;

    for(i = 0; i < dataSize; ++i)
        standardDeviation += pow(data[i] - mean, 2);

    return sqrt(standardDeviation / dataSize);
}

int factorial(int n)
{
    int i, factorial = 1;

    for (i = 1; i <= n; ++i) {
    factorial *= i;
    }
    return factorial;
}

int main() {

    cout << "Enter positive integers seperated by commas:" << endl;
    string input;
    cin >> input;
    vector<int> data;

    istringstream ss(input);
    int i;

    while (ss.good()) {
        string substr;
        getline(ss, substr, ',');
        try {
            int temp = atoi(substr.c_str());
            if(temp >= 0){ // Negative check
                data.push_back(temp);
            }
        } catch(...) {
            // Just skip the invalid input
        }
    }
    int dataSize = data.size();

    cout << endl << dataSize << " positive integers provided.";
    cout << endl << "Factorial of " << dataSize << " = " << factorial(dataSize);
    cout << endl << "Standard Deviation = " << calculateSD(data);

    return 0;
}

