#include <chrono>
#include <fstream>
#include <iostream>
#include <random>
#include <sstream>
#include <string>
#include <vector>

using namespace std;
using namespace std::chrono;

// Adds integers from input file to vector
void read_input(vector<int> &numbers, string filename) {
  ifstream File;
  File.open(filename);
  string line;
  getline(File, line);
  stringstream ss(line);
  for (int i; ss >> i;) {
    numbers.push_back(i);
    if (ss.peek() == ',')
      ss.ignore();
  }
  File.close();
}

void write_output(vector<vector<int>> output, string filename){
    ofstream outFile(filename);
    string header = "algorithm-1,algorithm-2,algorithm-3,algorithm-4,T1(n),T2(n),T3(n),T4(n)";
    outFile << header << "\n";

    for(int i = 0; i < output.size(); i++){
        vector<int> row = output[i];
        for(int j = 0; j < row.size(); j++){
            outFile << row[j] << ",";
        }
        outFile << "\n";
    }
    outFile.close();
}

int algorithm1(vector<int> X) {
  int P = 0, Q = X.size() - 1; // setting start,end indices
  int maxSoFar = 0;
  for (int L = P; L < Q; L++) {   // for L = P to Q
    for (int U = L; U < Q; U++) { // for U = L to Q
      int sum = 0;
      for (int I = L; I < U; I++) { // for I = L to U
        sum = sum + X[I];
        /* sum now contains the sum of X[L..U] */
      }
      maxSoFar = max(maxSoFar, sum);
    }
  }
  return maxSoFar;
}

int algorithm2(vector<int> X) {
  int P = 0, Q = X.size(); // setting start,end indices
  int maxSoFar = 0;
  for (int L = P; L < Q; L++) {
    int sum = 0;
    for (int U = L; U < Q; U++) {
      sum = sum + X[U];
      /* sum now contains the sum of X[L..U] */
      maxSoFar = max(maxSoFar, sum);
    }
  }
  return maxSoFar;
}

int MaxSum(vector<int> X, int L, int U) {

  if (L > U) {
    return 0; /* zero-element vector */
  }
  if (L == U) {
    return max(0, X[L]); /* one-element vector */
  }
  double M = (L + U) / 2; /* A is X[L..M], B is X[M+1..U] */

  /* Find max crossing to left */
  int sum = 0, maxToLeft = 0;
  for (int I = M; I >= L; I--) { // for I = M downto L do
    sum = sum + X[I];
    maxToLeft = max(maxToLeft, sum);
  }

  /* Find max crossing to left */
  sum = 0;
  int maxToRight = 0;
  for (int I = (M + 1); I < U; I++) { // for I = M+1 to U
    sum = sum + X[I];
    maxToRight = max(maxToRight, sum);
  }

  int maxCrossing = maxToLeft + maxToRight;
  int maxInA = MaxSum(X, L, M);
  int maxInB = MaxSum(X, (M + 1), U);
  return max(max(maxCrossing, maxInA), maxInB);
}
int algorithm3(vector<int> X) { return MaxSum(X, 0, (X.size() - 1)); }

int algorithm4(vector<int> X) {
  int P = 0, Q = X.size(); // setting start,end indices
  int maxSoFar = 0, maxEndingHere = 0;
  for (int I = P; I < Q; I++) { // for I = P to Q
    maxEndingHere = max(0, (maxEndingHere + X[I]));
    maxSoFar = max(maxSoFar, maxEndingHere);
  }
  return maxSoFar;
}

void generate_test_data(vector<vector<int>> &data) {
  int j = 0;
  for (int s = 10; s <= 100; s += 5) {

    vector<int> nums(s);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(-10, 10);
    for (int i = 0; i < s; i++) {
      nums[i] = dis(gen);
    }
    data[j] = nums;
    j++;
  }
}

// Returns average completion time of trials for one input case for one function
int trial_average(int n, vector<int> input, int (*algo)(vector<int>)) {
  vector<int> times(n);
  for (int i = 0; i < n; i++) {
    // Start clock
    auto start_point = high_resolution_clock::now();

    // Run algorithm
    int x = algo(input);

    // End clock
    auto end_point = high_resolution_clock::now();
    auto duration = duration_cast<nanoseconds>(end_point - start_point);
    times[i] = duration.count();
  }

  // Calculate and return average of times
  int sum = 0;
  for (int k = 0; k < times.size(); k++) {
    sum += times[k];
  }
  int average = sum / times.size();
  return average;
}

// Returns list of avg completion times for many input cases, for one algorithm
vector<int> algorithm_completion_averages(int n, vector<vector<int>> inputs,
                                          int (*algo)(vector<int>)) {
  vector<int> averages(19);
  for (int i = 0; i < inputs.size(); i++) { // For each input in list of inputs
    vector<int> input = inputs[i];
    averages[i] = trial_average(n, input, algo);
  }
  return averages;
}

int main() {
  // Read in 10 ints from phw_input, add to vector
  vector<int> input;
  read_input(input, "phw_input.txt");

  // Run 4 algorithms using input data
  int a1 = algorithm1(input);
  int a2 = algorithm2(input);
  int a3 = MaxSum(input, 0, input.size() - 1);
  int a4 = algorithm4(input);
  // Print to console
  cout << "algorithm-1: " << a1 << "; algorithm-2: " << a2
       << "; algorithm-3: " << a3 << "; algorithm-4: " << a4 << ";";

  // Generate 19 cases of test data, each case a with size 10,15,20...95,100
  vector<vector<int>> integer_sequences(19);
  generate_test_data(integer_sequences);

  vector<vector<int>> solution_matrix(19, vector<int>(8));

  // Run trials for each algorithm
  int num_trials = 100;
  for(int i = 0; i < 19; i++){
      vector<int> data = integer_sequences[i];
      int n = data.size();
      // Run trial for this sequence and add average to matrix cell
      solution_matrix[i][0] = trial_average(num_trials, data, &algorithm1);
      solution_matrix[i][1] = trial_average(num_trials, data, &algorithm2);
      solution_matrix[i][2] = trial_average(num_trials, data, &algorithm3);
      solution_matrix[i][3] = trial_average(num_trials, data, &algorithm4);

      // Add calculated T(n) for each input data size
      solution_matrix[i][4] = (2*(n*n*n)+(14*(n*n))+(21*n)+4-(5*((n*(n+1))/2)))/4; // scaling
      solution_matrix[i][5] = (13*(n*n)-((13*(n*n))/2)+(28*n)+((13*n)/2)+5)/4;   // scaling
      solution_matrix[i][6] = (13*n*log2(n)+12*n);
      solution_matrix[i][7] = ((13*n) + 5);
  }

    write_output(solution_matrix, "matthew_rogers_phw_output.txt");


  return 0;
}
