/*
Gabriel Loterena
7/30/2106
Finally adding some more functionality!!!
*/

import java.io.*;
import java.util.Arrays;

/* 
Gabriel Loterena
Markovian Runs Expected
6/16/2016

Reads a csv file and calculates expected values of runs scored of a team
via Markov States.
*/

public class Reader{

    // == is the devil
    public static boolean equals(String str){ 
      if(str==null) return false;
      if(!(str instanceof String)) return false; 
      String y=(String)str; 
      if(!(str.hashCode()==y.hashCode())) return false; 
      return true; 
   }
   
   public static double[] toDBL(String[] line){
         int n = line.length;
         double[] dblArray = new double[n];
         for(int i=1;i<n;i++){
            dblArray[i]=Double.parseDouble(line[i]);
         }
         
         return dblArray;
   }

   public static void main(String[] args) throws Exception {

      try{
         // open input stream textfile.txt for reading purpose.
         BufferedReader br1 = new BufferedReader(new FileReader("2015.csv"));
         BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
         
         //Ask the user which team/player they want.
         //This is actually terrible, but we want functionality. 
         System.out.print("Which team would you like to calculate (i.e ARI, BOS): ");
         
         String team = br2.readLine();
         
         //System.out.println(team);
         
         br1.readLine();//first line is unimportant
         String line = null;
         Markovian markov = new Markovian();
         
         while ((line = br1.readLine()) != null) {
            
            String[] stat = line.split(",");
            //System.out.println(stat[0]);//team names
            double[] stats = toDBL(stat);
            //System.out.println(Arrays.toString(stats));
            if(team.equals(stat[0])){
               System.out.println(stat[0]);
               markov.Markov(stats[15]+stats[24],stats[8],stats[9],stats[10],stats[11],stats[6]-stats[8],stats[7]);
               break;
            }
            
         }
      }catch(Exception e){
         e.printStackTrace();
      }
   }
}
