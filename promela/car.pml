int noUpSema =1; /* semaphore to protect mutual exclusion on numAlleyUp variable */
int noDownSema=1;  /* semaphore to protect mutual exclusion on numAlleyDown variable */
int numAlleyUp = 0;
int numAlleyDown = 0;
int alleySema = 1; /* semaphore to get access to enter alley */
int inAlleyUp = 0; /* number of cars going up in critical section (alley) */
int inAlleyDown = 0; /* number of cars going down in critical section (alley) */

pid p1,p2;
pid p5,p6;

int tNumAlleyUp=0; /*temp variable to make increment and decrement of numAlleyUp to be atomic */
int tNumAlleyDown=0; /*temp variable to make increment and decrement of numAlleyDown to be atomic */

inline P(s){
	atomic{s > 0 -> s = s - 1}
}

inline V(s){
	atomic{s=s+1}
}

init{
	atomic{
		p1=run CW();
		p2=run CW();

		p5=run CCW();
		p6=run CCW();
	}
}

proctype CW () 
{
	do
	::	skip;

entry:
		/* Entering the Alley */
		P(noUpSema);
		tNumAlleyUp=numAlleyUp;
		numAlleyUp=tNumAlleyUp+1;
		if :: numAlleyUp==1--> P(alleySema);
                             :: else --> skip;
		fi;
		V(noUpSema);
		
crit:
		inAlleyUp++;
		/*  In critical section (alley) */
		inAlleyUp--;

exit:
		/* Exiting the Alley */
		P(noUpSema);
		tNumAlleyUp=numAlleyUp;
		numAlleyUp=tNumAlleyUp-1;
		if :: numAlleyUp==0 --> V(alleySema);
                             :: else --> skip;
		fi;
		V(noUpSema);
	od  
}


proctype CCW () 
{
	do
	::	skip;

entry2:
		/* Entering the Alley */
		P(noDownSema);
		tNumAlleyDown=numAlleyDown;
		numAlleyDown=tNumAlleyDown+1;
		if :: numAlleyDown==1--> P(alleySema);
                             :: else --> skip;
		fi;
		V(noDownSema);
		
crit2:
		inAlleyDown++;
		/*  In critical section (alley) */
		inAlleyDown--;

exit2:
		/* Exiting the Alley */
		P(noDownSema);
		tNumAlleyDown=numAlleyDown;
		numAlleyDown=tNumAlleyDown-1;
		if :: numAlleyDown==0 --> V(alleySema);
                             :: else --> skip;
		fi;
		V(noDownSema);
	od  
}


/* Alley Invariant
 */
active proctype Check ()
{
	!(inAlleyUp*inAlleyDown==0)-->assert(inAlleyUp*inAlleyDown==0);
}



//ltl obl1 { [] ( ( CW[p1]@entry && [] !(CW[p2]@entry || CCW[p5]@entry2 || CCW[p6]@entry2) ) -> <> (CW[p1]@crit) )} 
//ltl res1   { [] ( (CW[p1]@entry || CW[p2]@entry || CCW[p5]@entry2 || CCW[p6]@entry2) -> <> (CW[p1]@crit || CW[p2]@crit || CCW[p5]@crit2 ||CCW[p6]@crit2) ) }
//ltl fair1 { [] ( (CW[p1]@entry) -> <>  (CW[p1]@crit) ) } 


