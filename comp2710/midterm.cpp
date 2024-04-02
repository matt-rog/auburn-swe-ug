/*
 *     Midterm Exam 2
 * Software Construction COMP2710
 * Instructor: Xuechao Li @ Auburn University
 */

#include<iostream>
#include<time.h>
using namespace std;

//tower of HANOI function implementation
void moveDisk(int n,char Original, char Aux,char Dest)
{
    if( n < 1){
        return;
    }

    // Moving all smaller ones to middle
    moveDisk(n-1, Original, Dest, Aux);

    cout << "Move Disk " << n << " from " << Original << " to " << Dest << "\n";

    // Moving all smaller ones form middle to final pole
    moveDisk(n-1, Aux, Original, Dest);

}

//main program
int main()
{
	int n;
        time_t start, end;
	cout<<"Enter the number of disks:";
	cin>>n;

	//calling the moveDisk
        time (&start);
		moveDisk(n,'A','B','C');
		time (&end);


        double elapse = difftime (end,start);
        cout << "The elapsed time is " << elapse << " seconds" << " for moving " << n << " disks "<<endl;
	return 0;
}
