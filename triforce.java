import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {
    public static void triangleEquiM(int n,int m,int trans, int decal) {
		
		for (int j = 0; j < n; j++) {
			for(int k=0;k<m;k++) {
				for (int i =(k==0?n-j+1:1); i <2*(n-j) + (k>0?0:decal)+trans; i++) {
					System.out.print(" ");
				}
				System.out.print("*");
				for (int i = 0; i < 2 * j; i++) {
				        System.out.print("*");
	
				}
				
				
			}
			System.out.println("");
		}
	
    }
    
    public static void triangleEqui(int n, int decal) {
		for (int j = 0; j < n; j++) {
		    int start=0;
			if(j==0){
			    start=1;
			    System.out.print(".");
			}
			for (int i = start+1; i < n - j + decal; i++) {
				System.out.print(" ");
			}
			System.out.print("*");
			for (int i = 0; i < 2 * j; i++) {
			    System.out.print("*");
			
			}
			System.out.println("");
		}
    }
    
    public static void tricForce(int N){
        triangleEqui(N,N);
        triangleEquiM(N,2,0,0);
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        tricForce(N);
  
    }
}
