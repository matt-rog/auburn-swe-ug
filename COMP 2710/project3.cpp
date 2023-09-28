/* Matthew Rogers
* To run the program:
 * Compile using g++ in the following command: g++ project3.cpp
 * Run the generated executable using the command: ./a.out
 * Enter file names as the program prompts you for input/output locations.
 * The program will merge two sets of numbers, sort the numbers, and write them to a new file.
* Sources:
 * I used the Project3 hints PDF to get an idea of how to delegate tasks to different functions.
 * I used an article from GeeksForGeeks.org to view documentation for vector functions.
    * https://www.geeksforgeeks.org/vector-in-cpp-stl/
    * Referenced on lines 73, 113, 133
 * I used another article from GeeksForGeeks.org to remind myself how to implement an insertion-sort algorithm.
    * https://www.geeksforgeeks.org/insertion-sort/
    * Referenced on lines 113-121
 * I read a discussion on StackOverflow to learn how to use vector.insert() to combine multiple vectors.
    * https://stackoverflow.com/questions/201718/concatenating-two-stdvectors
    * Used on line 178
*/
#include <fstream>
#include <iostream>
#include <vector>

using namespace std;

/*
 * Input: (1) Reference to a filename variable that will be received and verified in this function
 *        (2) A phrase indicating what file number is being opened for formatting purposes
 * Output: Returns a boolean value indicating whether the file could be successfully opened
 * */
bool check_file(string &filename, string attemptNumber){
    ifstream inStream;

    // Receiving filename input
    cout << "Enter the " << attemptNumber << " input file name:";
    cin >> filename;

    // Attempting to open file
    inStream.open((char*)filename.c_str());
    if (inStream.fail()) {
        cout << "Input file opening failed. Try again" << endl;
        inStream.close();
        return false;
    }

    inStream.close();
    return true;
}

/*
 * Input: Filename to be opened and parsed
 * Output: A vector containing the integers from the text file
 * */
vector<int> read_file(string filename){

    // Variable declaration
    ifstream inStream;
    vector<int> output;
    int current_data;

    // Opening file
    inStream.open((char*)filename.c_str());

    // Iterating through file data and adding it to the output vector
    inStream >> current_data;
    while (!inStream.eof()) {
        output.push_back(current_data);
        inStream >> current_data;
    }

    // Printing vector content and information to the console
    cout << "The list of " << output.size() << " numbers in file " << filename << " is:\n";
    for (int i = 0; i < output.size(); i++){
        cout << output[i] << "\n";
    }
    cout << "\n";

    inStream.close();
    return output;
}

/*
 * Input: (1) New filename to be written to
 *        (2) The vector containing the results that will be written to the new file
 * Output: None
 * */
void write_file(string filename, vector<int> output_vector){

    ofstream outStream;

    outStream.open((char*)filename.c_str());

    // Writing vector content to txt file
    for (int i = 0; i < output_vector.size(); i++){
        outStream << output_vector[i] << "\n";
    }
    outStream.close();

}

/*
 * Input: The unsorted vector of integers
 * Output: The sorted vector of integers that was sorted using the insertion algorithm
 * */
vector<int> insertion_sort(vector<int> unsorted_vector){

    vector<int> sorted_output = unsorted_vector;

    int temp_min;


    for (int i = 1; i < sorted_output.size(); i++){
        temp_min = sorted_output[i];
        int j = i - 1;

        for (j = j; j >= 0 && sorted_output[j] > temp_min; j--){
            sorted_output[j+1] = sorted_output[j];
        }
        sorted_output[j+1] = temp_min;
    }

    return sorted_output;
}

/*
 * Input: The sorted vector of numbers to be printed to console
 * Output: None
 * */
void to_string(vector<int> sorted_numbers){

    // Iterate through vector and add each element onto statement
    cout << "The sorted list of " << sorted_numbers.size() << " is: ";
    for (int i = 0; i < sorted_numbers.size(); i++){
        cout << sorted_numbers[i] << " ";
    }
}

/*
 * Input: None
 * Output: Exit successful code 0
 * Description: Controller for Project 3. Instantiates variables, checks for valid file input names,
 * merges integers from files, sorts integers, prints results, and writes results to a new file.
 * */
int main() {

    /* Variable declaration */
    // File names
    string filename_1;
    string filename_2;
    string filename_out;

    // Vectors
    vector<int> numbers_1;
    vector<int> numbers_2;
    vector<int> merged_data;
    vector<int> sorted_data;

    cout << "*** Welcome to Matthew's sorting program ***\n";

    // Checking first file
    bool valid_name = false;
    while (!valid_name) {
        valid_name = check_file(filename_1, "first");
    }
    // Reading first file
    numbers_1 = read_file(filename_1);

    // Checking second file
    valid_name = false;
    while (!valid_name) {
        valid_name = check_file(filename_2, "second");
    }
    // Reading second file
    numbers_2 = read_file(filename_2);

    // Merging the two vectors using vector.insert()
    numbers_1.insert(numbers_1.end(), numbers_2.begin(), numbers_2.end());
    merged_data = numbers_1;

    // Sort the merged data using insertion sort
    sorted_data = insertion_sort(merged_data);

    // Output the sorted data
    to_string(sorted_data);

    // Writing sorted number data to an output file
    cout << "\nEnter the output file name:";
    cin >> filename_out;
    write_file(filename_out, sorted_data);

    cout << "*** Please check the new file - " << filename_out  << " ***\n*** Goodbye. ***\n\n";
    return 0;
}
