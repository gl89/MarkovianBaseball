/* 
Gabriel Loterena
Markovian Runs Expected
6/16/2016

All MLB statistics are from http://www.baseball-reference.com/ & https://www.teamrankings.com
*/

import Jama.Matrix; 
import java.text.*;

public class MarkovianB{

      public static double RError(double a, double b){
         return ((Math.abs(b-a)/b)*100);
      }  
      
      public static void printM(double m[][]){
         DecimalFormat df = new DecimalFormat("0.000");
	      DecimalFormat rf = new DecimalFormat("#");
         for (int i = 0; i < 24; i++) {
            System.out.print("{ ");
            for (int j = 0; j < 24; j++) {
               System.out.print(df.format(m[i][j]) + " ");
            }
         System.out.print("}" + "\n");
         }
         System.out.println();
      }
      
	   public static void main(String[] args){
	   
	   DecimalFormat df = new DecimalFormat("0.000");
	   DecimalFormat rf = new DecimalFormat("#");
      
	   double RS = 19761;//actual runs scored in the season 2014
	   
	   //Can take these stats straight from baseballreference.com. Traditional batting stats.
	   //If I wrote this properly this should be the onloy part of the code you need adjust.  
	   double fp = 15672; //free pass = walk + hit by pitch, hbpbb seemed too annoying
	   double sgl = 28423; //singles hits - doubles - triples - homers
	   double dbl = 8137; //doubles
	   double trp = 849;  //triples
	   double hr = 4186; //homers *Unsure why, but home runs really messes with the numbers.
	   double out = 124019;//outs at bats - hits
	   double total = sgl + dbl + trp + hr + fp + out;
	   double innings = 43613; //If you input players data, this is his Plate Appearance 
	  
	   //These are the percent chance of above stats. Self-Explanatory
	   double f =  fp/total; 
	   double s = sgl/total; 
	   double d = dbl/total;
	   double t = trp/total; 
	   double h = hr/total; 
	   double o =out/total; 
	   
	   //28x28 T Matrix of all the possible bases load configuration, with 0,1,2 outs and an absorbing state of 3 outs.
	   double [] [] TMatrix = //Works, but the transition percentages are slightly off.
	   {       //   0,0/ 1,0/  2,0/ 3,0 /12,0/ 13,0 / 23,0/ 123,0/ 0,1 ect
	      /*0,0*/   {h,f+s,d,t,0,0,0,0,o,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//Definitely correct
	      /*1,0*/   {h,0,d/2,t,f+s/2,s/2,d/2,0,0,o,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	      /*2,0*/   {h,0,d,t,f,s/4,0,0,0,0,o,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  
	      /*3,0*/   {h,s,d,t,0,f,0,0,0,0,0,o,0,0,0,0,0,0,0,0,0,0,0,0,0},//Definitely correct
	      /*12,0*/  {h,0,d/2,t,s/4,s/2,d/2,f+s/4,0,0,0,0,o,0,0,0,0,0,0,0,0,0,0,0,0},
	      /*13,0*/  {h,0,d/2,t,s,s/2,d/2,f,0,0,0,0,0,o,0,0,0,0,0,0,0,0,0,0,0},
	      /*23,0*/  {h,s/2,d,t,0,s/2,0,f,0,0,0,0,0,0,o,0,0,0,0,0,0,0,0,0,0},
	      /*123,0*/ {h,0,d/2,t,s/2,s/2,d/2,f+s,0,0,0,0,0,0,0,o,0,0,0,0,0,0,0,0,0},    
	      /*0,1*/   {0,0,0,0,0,0,0,0,h,f+s,d,t,0,0,0,0,o,0,0,0,0,0,0,0,0},    
	      /*1,1*/   {0,0,0,0,0,0,0,0,h,0,d/2,t,f+s/2,s/2,d/2,0,0,o,0,0,0,0,0,0,0},        
	      /*2,1*/   {0,0,0,0,0,0,0,0,h,0,d,t,f,s/4,0,0,0,0,o,0,0,0,0,0,0,0,0},      
	      /*3,1*/   {0,0,0,0,0,0,0,0,h,s,d,t,0,f,0,0,0,0,0,o,0,0,0,0,0},        
	      /*12,1*/  {0,0,0,0,0,0,0,0,h,0,d/2,t,s/4,s/2,d/2,f+s/4,0,0,0,0,o,0,0,0,0},
	      /*13,1*/  {0,0,0,0,0,0,0,0,h,0,d/2,t,s,s/2,d/2,f,0,0,0,0,0,o,0,0,0},     
	      /*23,1*/  {0,0,0,0,0,0,0,0,h,s/2,d,t,0,s/2,0,f,0,0,0,0,0,0,o,0,0},    
	      /*123,1*/ {0,0,0,0,0,0,0,0,h,0,d/2,t,s/2,s/2,d/2,f+s,0,0,0,0,0,0,0,o,0},         
	      /*0,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,f+s,d,t,0,0,0,0,},//
	      /*1,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,0,d/2,t,f+s/2,s/2,d/2,0,o},  
	      /*2,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,0,d,t,f,s/4,0,0,o},    
	      /*3,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,s,d,t,0,f,0,0,o},
	      /*12,2*/  {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,0,d/2,t,s/4,s/2,d/2,f+s/4,o},
	      /*13,2*/  {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,0,d/2,t,s,s/2,d/2,f,o},
	      /*23,2*/  {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,s/2,d,t,0,s/2,0,f,o},      
	      /*123,2*/ {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,h,0,d/2,t,s/2,s/2,d/2,f+s,o},     
	      /*0,3*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},
	      /*1,3*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},
	      /*2,3*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},
	      /*3,3*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}
	      };
         
      double [] [] RMatrix = 
	   {       //   0,0/ 1,0/  2,0/ 3,0 /12,0/ 13,0 / 23,0/ 123,0/ 0,1 ect
	      /*0,0*/   {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//Definitely correct
	      /*1,0*/   {2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	      /*2,0*/   {2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  
	      /*3,0*/   {2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//Definitely correct
	      /*12,0*/  {3,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	      /*13,0*/  {3,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	      /*23,0*/  {3,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
	      /*123,0*/ {4,3,3,3,2,2,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    
	      /*0,1*/   {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},    
	      /*1,1*/   {0,0,0,0,0,0,0,0,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},        
	      /*2,1*/   {0,0,0,0,0,0,0,0,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},      
	      /*3,1*/   {0,0,0,0,0,0,0,0,2,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},        
	      /*12,1*/  {0,0,0,0,0,0,0,0,3,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0},
	      /*13,1*/  {0,0,0,0,0,0,0,0,3,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0},     
	      /*23,1*/  {0,0,0,0,0,0,0,0,3,2,2,2,1,1,1,0,0,0,0,0,0,0,0,0,0},    
	      /*123,1*/ {0,0,0,0,0,0,0,0,4,3,3,3,2,2,2,1,0,0,0,0,0,0,0,0,0},         
	      /*0,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},//
	      /*1,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,1,1,0,0,0,0,0},  
	      /*2,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,1,1,0,0,0,0,0},    
	      /*3,2*/   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,1,1,0,0,0,0,0},
	      /*12,2*/  {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,2,2,1,1,1,0,0},
	      /*13,2*/  {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,2,2,1,1,1,0,0},
	      /*23,2*/  {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,2,2,1,1,1,0,0},      
	      /*123,2*/ {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,3,3,3,2,2,2,1,0},     
	      };
	    
	   //This just creates the I Matrix //Works 
	   double [] [] IMatrix = new double [24][24];
	   
	   for (int row = 0; row < 24; row ++ )
	     for (int column = 0; column < 24; column ++){
	        if(row == column)
	           IMatrix [row] [column] = 1;
	        else   
	           IMatrix [row] [column] = 0;                                                                                      
	   }
	   
	   //Q Matrix is the t x t Transient Matrix of all transient states from the T Matrix 
	   double [] [] QMatrix = new double [24][24]; //Works
	     
	   for (int row = 0; row < 24; row ++ )
	     for (int column = 0; column < 24; column ++){
	        QMatrix [row][column] = TMatrix [row][column];                                                                                    
	   }
	   
	    //I-Q Matrix, this is a set up for the N matrix
	   double [] [] IQMatrix = new double [24][24]; //Works
	   
	   for (int row = 0; row < 24; row ++ )
	     for (int column = 0; column < 24; column ++){
	       IQMatrix [row][column] = IMatrix [row][column] - QMatrix [row][column];                                                                                    
	   } 
	        
	   //Fundamental Matrix this is where we get the important stuff from. 
	   Matrix GetInverse = new Matrix(IQMatrix);
      double NMatrix[][] = GetInverse.inverse().getArray();
      
      /*
      System.out.println("Transition Matrix");
      printM(TMatrix);
      System.out.println("Run Expected Matrix");
      printM(RMatrix);
      System.out.println("Fundemantal Matrix (each row of matrix E represents the number of visits to all the states given a state)");
      printM(NMatrix);
      */
      
      System.out.println();
      System.out.println("   0,0   1,0   2,0   3,0   12,0  13,0  23,0 123,0  0,1   1,1   2,1   3,1  12,1  13,1  23,1  123,1  0,2   1,2   2,2   3,2  12,2  13,2   23,2  123,2");
      System.out.println("Fundemantal Markov Matrix (Expected number of batters starting from a state)");
      
      System.out.print("{ ");
      for (int i = 0; i < 24; i++){
         double EB = 0;
         for (int j = 0; j < 24; j++)
            EB += NMatrix[i][j];
               System.out.print(df.format(EB) + " ");   
      }
      System.out.print("}" + "\n");       
     
      //Expected number of runs that can be scored from one play
      double [] [] REMatrix = new double [24][1]; 
        
      System.out.println();
      System.out.println("Expected number of runs scored from one play");
      System.out.print("{ ");
      for (int i = 0; i < 24; i++){
         double RE = 0;
         for (int j = 0; j < 24; j++)
            RE += TMatrix[i][j] * RMatrix[i][j];
            System.out.print(df.format(RE) + " ");
            REMatrix[i][0] = RE;
      }
      System.out.println(" } ");
      
      double [] [] RSMatrix = new double [24][1]; 
      for (int i=0; i<24; i++)
         for (int j=0; j<1; j++)
            for (int k=0; k<24; k++){
               RSMatrix[i][j] += NMatrix[i][k] * REMatrix[k][j];  
      }
      System.out.println();
      //number of runs scored in the remainder of the inning starting from each of the 24 starting states
      System.out.print("Expected number of runs scored from a given state");
      System.out.println();
      System.out.print("{ ");
      for (int i = 0; i < 24; i++) {
         for (int j = 0; j < 1; j++) {
            System.out.print(df.format(RSMatrix[i][j]) + " ");
         } 
      }  
      System.out.print("} ");
      double ERS = RSMatrix[0][0]*innings;
      System.out.println();
      System.out.println();
      System.out.println("Expected number of runs scored for the season " + (int)ERS); 
      System.out.println("Actual number of runs scored for the season " + (int)RS); 
      System.out.println("Relative Error " + df.format(RError(ERS,RS))); 
      }  
      
    
      	   
}

     //refactor this***
      /*
      System.out.println("Transition Matrix");
      
      //Prints out Transition Matrix 
      for (int i = 0; i < 24; i++) {
         System.out.print("{ ");
         for (int j = 0; j < 24; j++) {
            System.out.print(df.format(TMatrix[i][j]) + " ");
      }
      System.out.print("}" + "\n");
      } 
     
      System.out.println();
      
    
      //Prints out the Run Expected Matrix
      System.out.println("Run Expected Matrix");
      
      for (int i = 0; i < 24; i++) {
         System.out.print("{ ");
         for (int j = 0; j < 24; j++) {
            System.out.print(rf.format(RMatrix[i][j]) + " ");
      }
      System.out.print("}" + "\n");
      } 
      
      System.out.println();
      
      System.out.println("Fundemantal Matrix (each row of matrix E represents the number of visits to all the states given a state)");
      
      //Prints out the NMatrix contains the expected times that the 
      //inning will be in each state before the inning is over (transient state is absorbed
      //stariting with each of the possible 24 beginning states
      for (int i = 0; i < 24; i++) {
         System.out.print("{ ");
         for (int j = 0; j < 24; j++) {

      System.out.print(df.format(NMatrix[i][j]) + " ");
      }
      System.out.print("}" + "\n");
      } 
      */