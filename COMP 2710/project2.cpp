/* Matthew Rogers
* To run the program:
 * Compile using g++ in the following command: g++ project2.cpp
 * Run the generated executable using the command: ./a.out
 * Enter a key as the program validates and tests its functions.
 * The program will calculate which duelling strategy is better for a shooter with the worse accuracy (Aaron).
* Sources:
 * I used the Project2 hints PDF to help structuring the tests and using asserts().
*/


#include <iostream>
#include <stdlib.h>
#include <assert.h>
#include <ctime>
using namespace std;

// Constants
const double A_accuracy = 0.33333333;
const double B_accuracy = 0.5;
const double C_accuracy = 1.0;
const int DUEL_COUNT = 10000;

/* Input: A_alive indicates whether Aaron is alive */
/*        B_alive indicates whether Bob is alive */
/*        C_alive indicates whether Charlie is alive */
/* Return: true if at least two are alive */
/*         otherwise return false */
bool at_least_two_alive(bool A_alive, bool B_alive, bool C_alive) {
    if(A_alive && B_alive || B_alive && C_alive || A_alive && C_alive) {
        return true;
    }
    return false;
}

void test_at_least_two_alive(void) {
    cout << "\nUnit Testing 1: Function - at_least_two_alive()\n";

    cout << "\tCase 1: Aaron alive, Bob alive, Charlie alive\n";
    assert(at_least_two_alive(true, true, true) == true);
    cout << "\tCase passed ...\n";

    cout << "\tCase 2: Aaron dead, Bob alive, Charlie alive\n";
    assert(at_least_two_alive(false, true, true) == true);
    cout << "\tCase passed ...\n";

    cout << "\tCase 3: Aaron alive, Bob dead, Charlie alive\n";
    assert(at_least_two_alive(true, false, true) == true);
    cout << "\tCase passed ...\n";

    cout << "\tCase 4: Aaron alive, Bob alive, Charlie dead\n";
    assert(at_least_two_alive(true, true, false) == true);
    cout << "\tCase passed ...\n";

    cout << "\tCase 5: Aaron dead, Bob dead, Charlie alive\n";
    assert(at_least_two_alive(false, false, true) == false);
    cout << "\tCase passed ...\n";

    cout << "\tCase 6: Aaron dead, Bob alive, Charlie dead\n";
    assert(at_least_two_alive(false, true, false) == false);
    cout << "\tCase passed ...\n";

    cout << "\tCase 7: Aaron alive, Bob dead, Charlie dead\n";
    assert(at_least_two_alive(true, false, false) == false);
    cout << "\tCase passed ...\n";

    cout << "\tCase 8: Aaron dead, Bob dead, Charlie dead\n";
    assert(at_least_two_alive(false, false, false) == false);
    cout << "\tCase passed ...\n";
}

bool is_hit(double X_accuracy) {
    double shot_chance = ((double) rand()) / (double) RAND_MAX;
    return shot_chance <= X_accuracy;
}

/* Strategy 1: Use call by reference
 * Input: B_alive indicates whether Bob alive or dead
 *        C_alive indicates whether Charlie is alive or dead
 * Return: Change B_alive into false if Bob is killed.
 *         Change C_alive into false if Charlie is killed.
*/
int Aaron_shoots1(bool& B_alive, bool& C_alive) {
    bool hit = is_hit(A_accuracy);
    if(C_alive){
        C_alive = !hit;
        return 0;
    } else {
        if(B_alive) {
            B_alive = !hit;
            return 1;
        }
    }
    return -1;
}

void test_Aaron_shoots1(void) {
    cout << "\nUnit Testing 2: Function Aaron_shoots1(Bob_alive, Charlie_alive)\n";

    bool B_alive = true;
    bool C_alive = true;
    cout << "\tCase 1: Bob alive, Charlie alive\n";
    assert(Aaron_shoots1(B_alive, C_alive) == 0);
    cout << "\t\tAaron is shooting at Charlie\n";

    B_alive = false;
    C_alive = true;
    cout << "\tCase 2: Bob dead, Charlie alive\n";
    assert(Aaron_shoots1(B_alive, C_alive) == 0);
    cout << "\t\tAaron is shooting at Charlie\n";

    B_alive = true;
    C_alive = false;
    cout << "\tCase 3: Bob alive, Charlie dead\n";
    assert(Aaron_shoots1(B_alive, C_alive) == 1);
    cout << "\t\tAaron is shooting at Bob\n";
}

/* Call by reference
 * Input: A_alive indicates if Aaron is alive or dead
 *        C_alive indicates whether Charlie is alive or dead
 * Return: Change A_alive into false if Aaron is killed.
 *         Change C_alive into false if Charlie is killed.
*/
int Bob_shoots(bool& A_alive, bool& C_alive) {
    bool hit = is_hit(B_accuracy);
    if(C_alive){
        C_alive = !hit;
        return 0;
    } else {
        if(A_alive) {
            A_alive = !hit;
            return 1;
        }
    }
    return -1;
}

void test_Bob_shoots(void) {
    cout << "\nUnit Testing 3: Function Bob_shoots(Aaron_alive, Charlie_alive)\n";

    bool A_alive = true;
    bool C_alive = true;
    cout << "\tCase 1: Aaron alive, Charlie alive\n";
    assert(Bob_shoots(A_alive, C_alive) == 0);
    cout << "\t\tBob is shooting at Charlie\n";

    A_alive = false;
    C_alive = true;
    cout << "\tCase 2: Aaron dead, Charlie alive\n";
    assert(Bob_shoots(A_alive, C_alive) == 0);
    cout << "\t\tBob is shooting at Charlie\n";

    A_alive = true;
    C_alive = false;
    cout << "\tCase 3: Aaron alive, Charlie dead\n";
    assert(Bob_shoots(A_alive, C_alive) == 1);
    cout << "\t\tBob is shooting at Aaron\n";
}

/* Call by reference
 * Input: A_alive indicates if Aaron is alive or dead
 *        B_alive indicates whether Bob is alive or dead
 * Return: Change A_alive into false if Aaron is killed.
 *         Change B_alive into false if Bob is killed.
*/
int Charlie_shoots(bool& A_alive, bool& B_alive) {
    bool hit = is_hit(C_accuracy);
    if(B_alive){
        B_alive = !hit;
        return 0;
    } else {
        if(A_alive) {
            A_alive = !hit;
            return 1;
        }
    }
    return -1;
}

void test_Charlie_shoots(void) {
    cout << "\nUnit Testing 4: Function Charlie_shoots(Aaron_alive, Bob_alive)\n";

    bool A_alive = true;
    bool B_alive = true;
    cout << "\tCase 1: Aaron alive, Bob alive\n";
    assert(Charlie_shoots(A_alive, B_alive) == 0);
    cout << "\t\tCharlie is shooting at Bob\n";

    A_alive = false;
    B_alive = true;
    cout << "\tCase 2: Aaron dead, Bob alive\n";
    assert(Charlie_shoots(A_alive, B_alive) == 0);
    cout << "\t\tCharlie is shooting at Bob\n";

    A_alive = true;
    B_alive = false;
    cout << "\tCase 3: Aaron alive, Bob dead\n";
    assert(Charlie_shoots(A_alive, B_alive) == 1);
    cout << "\t\tCharlie is shooting at Aaron\n";
}

/* Strategy 2: Use call by reference
 * Input: B_alive indicates whether Bob alive or dead
 *        C_alive indicates whether Charlie is alive or dead
 * Return: Change B_alive into false if Bob is killed.
 *         Change C_alive into false if Charlie is killed.
*/
int Aaron_shoots2(bool& B_alive, bool& C_alive) {
    if(B_alive && C_alive){
        return 0;
    } else if (C_alive){
        bool hit = is_hit(A_accuracy);
        C_alive = !hit;
        return 1;
    } else if (B_alive){
        bool hit = is_hit(A_accuracy);
        B_alive = !hit;
        return 2;
    }
    return -1;
}

void test_Aaron_shoots2(void) {
    cout << "\nUnit Testing 5: Function Aaron_shoots2(Bob_alive, Charlie_alive)\n";

    bool B_alive = true;
    bool C_alive = true;
    cout << "\tCase 1: Bob alive, Charlie alive\n";
    assert(Aaron_shoots2(B_alive, C_alive) == 0);
    cout << "\t\tAaron intentionally misses his first shot\n";
    cout << "\t\tBoth Bob and Charlie are alive.\n";

    B_alive = false;
    C_alive = true;
    cout << "\tCase 2: Bob dead, Charlie alive\n";
    assert(Aaron_shoots2(B_alive, C_alive) == 1);
    cout << "\t\tAaron is shooting at Charlie\n";

    B_alive = true;
    C_alive = false;
    cout << "\tCase 3: Bob alive, Charlie dead\n";
    assert(Aaron_shoots2(B_alive, C_alive) == 2);
    cout << "\t\tAaron is shooting at Bob\n";
}

void Press_any_key(void) {
    cout << "Press any key to continue...\n";
    system("read -s -N 1");
}

int Duel(int strategy) {
    bool A_alive = true;
    bool B_alive = true;
    bool C_alive = true;

    while(at_least_two_alive(A_alive, B_alive, C_alive)){
        if(A_alive){
            if(strategy == 1){
                Aaron_shoots1(B_alive, C_alive);
            } else if(strategy == 2){
                Aaron_shoots2(B_alive, C_alive);
            }
        }
        if (B_alive) {
            Bob_shoots(A_alive, C_alive);
        }
        if (C_alive) {
            Charlie_shoots(A_alive, B_alive);
        }
    }
    return A_alive ? 0 : B_alive ? 1 : C_alive ? 2 : -1;
}

int Test_strategy(int strategy){
    int A_count = 0;
    int B_count = 0;
    int C_count = 0;
    int result = 0;

    for (int n = 0; n < DUEL_COUNT; n++ ){
        if(strategy == 1){
            result = Duel(1);
        } else if (strategy == 2){
            result = Duel(2);
        }
        switch (result) {
            case 0:
                A_count++;
                break;
            case 1:
                B_count++;
                break;
            case 2:
                C_count++;
                break;
        }
    }
    cout << "\nAaron won " << A_count << "/10000 duels or " << static_cast<double>(A_count) / DUEL_COUNT * 100 << "%\n"
         << "Bob won " << B_count << "/10000 duels or " << static_cast<double>(B_count) / DUEL_COUNT * 100 << "%\n"
         << "Charlie won " << C_count << "/10000 duels or " << static_cast<double>(C_count) / DUEL_COUNT * 100 << "%"
         << endl;

    return A_count;
}

int main() {
    cout << "*** Welcome to Matthew's Duel Simulator ***";
    srand(time(0));
    test_at_least_two_alive();
    Press_any_key();
    test_Aaron_shoots1();
    Press_any_key();
    test_Bob_shoots();
    Press_any_key();
    test_Charlie_shoots();
    Press_any_key();
    test_Aaron_shoots2();
    Press_any_key();

    cout << "\nReady to test strategy 1 (run 10000 times):\n";
    Press_any_key();
    int A_count_1 = Test_strategy(1);

    cout << "\nReady to test strategy 2 (run 10000 times):\n";
    Press_any_key();
    int A_count_2 = Test_strategy(2);

    cout << "\nStrategy " << (A_count_1 >= A_count_2 ? 1 : 2) << " is better than strategy " << (A_count_1 >= A_count_2 ? 2 : 1) << ".\n";
    return 0;
}