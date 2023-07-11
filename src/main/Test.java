package main;

//import org.unibl.etf.stego.Steganography;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JPasswordField;


import options.Sertifikati;
import options.Stego;
import pitanja.Kviz;
import players.Korisnik;


public class Test {

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		//Kviz.unesiPitanja();
		//Kviz.ispisiPitanja();
		Korisnik k=new Korisnik();
		Scanner myObj = new Scanner(System.in);	
    	

		 String pom=null;
         int opcija=0;// Sertifikati.PrikaziRezultate();
           do
           {
        	   System.out.println("1) Registracija ");
        	   System.out.println("2) Login ");
        	   System.out.println("3) Prestanak rada ");
        	   pom=myObj.nextLine();
        	   opcija = Integer.parseInt(pom);
        	   
        	   
        	   
        	   if(opcija==1)
        	   {
        		   k.registracija();
        		  
        	   }
        	   else if(opcija==2)
        	   {
        		   if(k.login())
        		   {
        			   
        			   System.out.println("Za prikaz rezultata unesite 4 ");
        			   pom=myObj.nextLine();
        			   opcija = Integer.parseInt(pom);
        			   if(opcija==4)
        			   {
        				   Sertifikati.PrikaziRezultate();
        			   }
        		   }
        		   else
        		   {
        			   continue;
        		   }
        		   
        		   
        	   }
        	   else if(opcija!=3)
        	   {
        		   System.out.println("Neispravan unos. ");
        	   }
        	   
        	   
           }while(opcija!=3);
		 
	}
	

}

