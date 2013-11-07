
#define M	2	/* no. of cars going up the alley*/
#define N	2	/* no. of cars going down the alley */

int noUpSema =1; /* semaphore to protect mutual exclusion on numAlleyUp variable */
int noDownSema=1;  /* semaphore to protect mutual exclusion on numAlleyDown variable */
int numAlleyUp = 0;
int numAlleyDown = 0;
int alleySema = 1; /* semaphore to get access to enter alley */
int inAlleyUp = 0; /* number of cars going up in critical section (alley) */
int inAlleyDown = 0; /* number of cars going down in critical section (alley) */


inline P(s){
	atomic{s > 0 -> s = s - 1}
}

inline V(s){
	atomic{s=s+1}
}


active [M] proctype CW () 
{
	do
	::	
		/* Entering the Alley */
		P(noUpSema);
		numAlleyUp++;
		if :: numAlleyUp==1--> P(alleySema);
		fi;
		V(noUpSema);
		
		inAlleyUp++;
		/*  In critical section (alley) */
		inAlleyUp--;

		/* Exiting the Alley */
		P(noUpSema);
		numAlleyUp--;
		if :: numAlleyUp==0 --> V(alleySema);
		fi;
		V(noUpSema);
	od  
}


active [N] proctype CCW () 
{
	do
	::	
		/* Entering the Alley */
		P(noDownSema);
		numAlleyDown++;
		if :: numAlleyDown==1--> P(alleySema);
		fi;
		V(noDownSema);
		
		inAlleyDown++;
		/*  In critical section (alley) */
		inAlleyDown--;

		/* Exiting the Alley */
		P(noDownSema);
		numAlleyDown--;
		if :: numAlleyDown==0 --> V(alleySema);
		fi;
		V(noDownSema);
	od  
}


/* Alley Invariant
 */
active proctype Check ()
{
	do 
	::
		assert( inAlleyUp*inAlleyDown==0);
	od
}
