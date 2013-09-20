package org.ecberkeley.css.lang;

public class BreakContinue {
	
	public static void main(String[] args) {
		int i=0, j=0;
		outer: while(j < 100){
			inner: while (i < 100){
				if (i % 2 == 0) continue;// inner; //same as simply saying break;
				if (i>5) break outer; 
				i++;
			}
			j++;
		}

	}

}
