/* Matthew Rogers
* To run the program:
 * Compile using g++ in the following command: g++ project4.cpp
 * Run the generated executable using the command: ./a.out
 * Define the macros "PRODUCTION" and "UNIT_TEST" to toggle between the normal trivia game and the debugging mode.
 * PRODUCTION:
    * Create as many trivia questions as you'd like via the program input.
    * When you are ready to play the game, type "No" when it prompts you to continue.
    * Grab a friend and play the answer the trivia game you made!
 * UNIT_TEST:
    * Provide input as specified and ensure the functions are working properly.
* Sources:
 * I used the Project4 hints PDF to help me define my TriviaNode structure, as well has how to use the pointers.
    * Referenced on lines 34-49
 * I used an article from DigitalOcean to view documentation for comparing two string values (x.compares(y)).
    * https://www.digitalocean.com/community/tutorials/compare-strings-in-c-plus-plus
    * Referenced on lines 95 and 119
 * I read a discussion on StackOverflow to clarify how to handle pointers and reassignment.
    * https://stackoverflow.com/questions/60122455/pointer-problems-assigning-to-int-from-incompatible-type-int
    * Used on line 87
*/

#include <iostream>
#include <assert.h>
#include <string>
#include <cstdlib>

using namespace std;

int trivia_length = 0;

// Creates the TriviaNode structure
struct TriviaNode {
    string question;
    string answer;
    int points;
    TriviaNode* next;
};

typedef TriviaNode* PointerNode;

// Adds a list of hard-coded, default trivia questions defined by Dr. Li.
void create_default_trivia(PointerNode& node) {


    PointerNode third = new TriviaNode;
    third->question =  "What is the best-selling video game of all time? (Hint: Call of Duty or Wii Sports)?";
    third->answer = "Wii Sports";
    third->points = 20;
    trivia_length++;

    PointerNode second = new TriviaNode;
    second->question = "What was Bank of America’s original name? (Hint: Bank of Italy or Bank of Germany)?";
    second->answer = "Bank of Italy";
    second->points = 50;
    second->next = third;
    trivia_length++;

    node->question = "How long was the shortest war on record? (Hint: how many minutes)?";
    node->answer = "38";
    node->points = 100;
    node->next = second;
    trivia_length++;
}

// Adds questions to a node using user-provided input. Stops adding questions whenever the user is ready to.
void add_questions(PointerNode& node) {

    string isAdding;

    do {

        PointerNode newQuestion = new TriviaNode;

        string points_in;

        cout << "Enter a question: ";
        getline(cin, newQuestion->question);
        cin.clear();

        cout << "Enter an answer: ";
        getline(cin, newQuestion->answer);
        cin.clear();

        cout << "Enter award points: ";
        cin >> points_in;
        cin.clear();
        newQuestion->points = atoi(points_in.c_str());

        newQuestion->next = node;
        node = newQuestion;
        trivia_length++;

        cout << "Continue? (Yes/No): ";
        cin >> isAdding;
        cin.clear();
        cin.ignore();

    } while (isAdding.compare("No") != 0); // Executes until user says "No" to continuing
}

// Asks int questions from a given trivia node. Records awarded points and provides feedback to user.
int ask_questions(PointerNode trivia_node, int questions){
    if( trivia_node == NULL) {
        return 0;
    }
    if( questions < 1 ) {
        cout << "Warning - the number of trivia to be asked must equal to or be larger than 1.";
        return 1;
    }
    if( questions > trivia_length ){
        cout << "Warning - There is only " << trivia_length << " trivia in the list.";
        return 1;
    }

    int points_awarded = 0;
    for(int i = 0; i < questions; i++){

        string answer_in;
        cout << "\nQuestion: " << trivia_node->question;
        cout << "\nAnswer: ";
        cin >> answer_in;

        if(answer_in.compare(trivia_node->answer) == 0){
            cout << "Your answer is correct. You receive " << trivia_node->points << " points.";
            points_awarded += trivia_node->points;
        } else {
            cout << "Your answer is wrong. The correct answer is: " << trivia_node->answer;
        }
        cout << "\nYour total points: " << points_awarded;
        trivia_node = trivia_node->next; // Switching to next node

    }
    return 0;
}

// Comment / uncomment to switch between production version and debugging version
#define PRODUCTION
//#define UNIT_TEST

// Main function that implements both a production, custom trivia game and a debugger for the trivia game
int main() {
    PointerNode triviaNode = new TriviaNode;

#ifdef PRODUCTION
    // Production and normal version for trivia game
    cout << "*** Welcome to Matthew's trivia quiz game ***\n";

    add_questions(triviaNode);
    ask_questions(triviaNode, trivia_length);

    cout << "\n*** Thank you for playing the trivia quiz game. Goodbye! ***\n";
#endif

#ifdef UNIT_TEST
    // Test driver for trivia game

    // Adding trivia questions
    create_default_trivia(triviaNode);

    cout << "***This is a debugging version ***\n";

    // 1: No question asked
    cout << "Unit Test Case 1: Ask no question. The program should give a warning message.\n";
    int result = ask_questions(triviaNode, 0);
    assert(result == 1); // 1 is returned upon an error
    cout << "\nCase 1 passed\n\n";

    // 2.1: Ask 1 question, you enter a wrong answer
    cout << "Unit Test Case 2.1: Ask 1 question in the linked list. The tester enters an incorrect answer.";
    result = ask_questions(triviaNode, 1);
    assert(result == 0); // 0 points should be awarded for an incorrect answer
    cout << "\n\nCase 2.1 passed\n\n";

    // 2.2: Ask 1 question, you enter a right answer
    cout << "Unit Test Case 2.2: Ask 1 question in the linked list. The tester enters a correct answer.";
    result = ask_questions(triviaNode, 1);
    assert(result == 100); // 100 points are awarded for a correct answer
    cout << "\n\nCase 2.2 passed\n\n";

    // 3: Ask all 3 questions
    cout << "Unit Test Case 3: Ask all the questions of the last trivia in the linked list.";
    result = ask_questions(triviaNode, trivia_length);
    assert(result != 1); // 1 should not be returned, as all questions should be asked without problem
    cout << "\n\nCase 3 passed\n\n";

    // 4: Ask more questions than available
    cout << "Unit Test Case 4: Ask 5 questions in the linked list.\n";
    result = ask_questions(triviaNode, 5);
    assert(result == 1); // 1 Error will occur, as questions asked > questions available.
    cout << "\nCase 4 passed\n\n";

    cout << "*** End of the Debugging Version ***";
#endif
    return 0;
}
