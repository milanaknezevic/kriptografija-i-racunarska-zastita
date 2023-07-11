package options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Sertifikati {
	
	  public  static String GenerateHash(String password)
      {
		  try {
	            MessageDigest md = MessageDigest.getInstance("SHA-512");
	            byte[] messageDigest = md.digest(password.getBytes());
	            BigInteger no = new BigInteger(1, messageDigest);
	            String hashedpassword = no.toString(16);
	            while (hashedpassword.length() < 32)
	            {
	                hashedpassword = "0" + hashedpassword;
	            }
	  
	            return hashedpassword;
	        }
		  catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
      }
	  
	  public static void createBazaKorisnika(String username, String password, int br,String CaTijelo)// upisuje korisnike u bazu
	  {
		  try
		  { //int br=0;
			  Boolean korisnikPostoji=checkIfKorisnikExistsInBazaKorisnika(username, password);
			   
				  if(korisnikPostoji==false ) {
					  
					  String filename= "C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\bazaKorisnika.txt";
					  FileWriter fw = new FileWriter(filename,true); //the true will append the new data
					  // fw.write(username + " " + password + "\n");//appends the string to the file
					  fw.write(username + " " + password + " " + br + " " + CaTijelo + "\n");
					  fw.close();
					  
					 // System.out.println("Korisnik je dodan u bazu");
				  }
				  /*else   {
					  System.out.println("Korisnik vec postoji");
				  
			  }*/
		  }
		  catch(IOException ioe)
		  {
		      System.err.println("IOException: " + ioe.getMessage());
		  }
	  }
	  
	  public static Boolean checkIfKorisnikExistsInBazaKorisnika(String username, String password)
	  {
		  try {
			  username=username.toUpperCase();
			 // System.out.println("username "+ username);
			  
			  String filename= "C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\bazaKorisnika.txt";
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      int br=0;
		      while (myReader.hasNextLine()) 
		      {
		        String data = myReader.nextLine();
		        if(data.equals(""))
		        {
		        	break;
		        }
		        String[] parts = data.split(" ");
		        parts[0]=parts[0].toUpperCase();
		       // System.out.println("parts[0]"+ parts[0]);
		       // if(username.equals(parts[0]) ||  password.equals(parts[1]) )//ne treba provjeravati ya sifru to smije da imaju dva korisnika isto???? 
		        if(username.equals(parts[0]))
		        {
		        	br++;
		        	//System.out.println("Korisnik vec postoji");
		        }
		      }
		      myReader.close();
		      if(br!=0)
		      {
		    	  return true;
		      }
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		  return false;
	  }
	  
	  public static  Boolean checkCertificateV(String username,String caTijelo)//provjerava dal je certifikat validan i da li je vrijeme isteklo
	     {
	    	
	    	  try {
				  String filename="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + caTijelo + "\\index.txt";//System.out.println("filename "+ filename);
			      File myObj = new File(filename);
			      Scanner myReader = new Scanner(myObj);
			      while (myReader.hasNextLine()) 
			      {
			        String line = myReader.nextLine();//System.out.println("line "+ line);//System.out.println("/CN=Ivona??? "+ "CN=" + username);
			        if( line.contains("CN=" + username))
			        {//
			        	//System.out.println("line u if  "+ line);
			        	 String[] parts = line.split("	");
			        	 if(parts[0].equals("V"))
			        	 {	
			        		 
			        		 SimpleDateFormat dt = new SimpleDateFormat("yyMMddHHmmss");  
			        		    Date date = new Date();  
			        		   // 
			        		  //  System.out.println("datum trenutni " + dt.format(date));
			        		    String datumIzFajla = parts[1].substring(0, parts[1].length() - 1);
			        		   // 
			        		  //  System.out.println("datum iy fajla bey Z " + datumIzFajla);
			        		    if(dt.format(date).compareTo(datumIzFajla) < 0)  
			        		    {
			        		    //	System.out.println("Sertifikat valjaaaaaa" );
			        		    	return true;
			        		    }
			        		    else {
			        		    	return false;
			        		    }

			        	 }
			        	 else {
			        		 return false;
			        	 }
					        
			      
			        }
			        /*else {
		        		 return false;
		        	 }*/
			      }
			      myReader.close();
			      
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
	    	  return false;
			
	    	 
	    	 
	     }
	  
	  public static  void revokeCertifikat(String username,String br) throws IOException, InterruptedException //povlaci certifikat i iydaje crl listu
	  {//POVLACENJE CERTIFIKATA
		  
		 try
		 {
			 ProcessBuilder pr=new ProcessBuilder();//dodati komandi ya kriptovanje 3des alg i vidjeti kako dekriptovati 
			 List<String> komandaZaPovlacenjeCertifikata =new ArrayList<String>();
			 komandaZaPovlacenjeCertifikata.add("openssl");
			 komandaZaPovlacenjeCertifikata.add("ca");
			 komandaZaPovlacenjeCertifikata.add("-revoke");
			 komandaZaPovlacenjeCertifikata.add("certs/cert" + username + ".crt");//generisace se kljuc npr private/privateMilana4096.key
			 komandaZaPovlacenjeCertifikata.add("-crl_reason");
			 komandaZaPovlacenjeCertifikata.add("cessationOfOperation");
			 komandaZaPovlacenjeCertifikata.add("-config");
			 komandaZaPovlacenjeCertifikata.add("openssl"+ br + ".cnf");
			 komandaZaPovlacenjeCertifikata.add("-gencrl");
			 komandaZaPovlacenjeCertifikata.add("-out");
			 komandaZaPovlacenjeCertifikata.add("crls/crl" + username + ".crl");
			 
			// System.out.println("povlacenje certifikata" + komandaZaPovlacenjeCertifikata);
			 
			 String path="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\cert" + br;//
			// System.out.println("path "+path);
			 pr.directory(new File(path));  //
			 
			// System.out.println("trenutni direktorij" + pr.directory());
			 pr.command(komandaZaPovlacenjeCertifikata);
			 Process ppr=pr.start();
			 Thread.sleep(5000);
			// System.out.println("DONE ");
		 }
		 catch (IOException e) {
		        // handle e
		    }
	  }

	  public static String radSaKljucevima()
{
	String kljuc="";
	 try
	 {
		 //DEKRIPTOVANJE FAJLA U KOM JE SIMETRICNI KLJUC
		ProcessBuilder pr=new ProcessBuilder();
		 List<String> komandaZaDekripcijuSimetricnogKljuca =new ArrayList<String>();
		 //openssl rsautl -decrypt -in sifratJavnim.key -out sifratSigurnost.key -inkey kljucZaZastitu.key
		 komandaZaDekripcijuSimetricnogKljuca.add("openssl");
		 komandaZaDekripcijuSimetricnogKljuca.add("rsautl");
		 komandaZaDekripcijuSimetricnogKljuca.add("-decrypt");
		 komandaZaDekripcijuSimetricnogKljuca.add("-in");
		 komandaZaDekripcijuSimetricnogKljuca.add("sifratKljuc.txt");
		 komandaZaDekripcijuSimetricnogKljuca.add("-out");
		 komandaZaDekripcijuSimetricnogKljuca.add("simetricniKljuc.txt");
		 komandaZaDekripcijuSimetricnogKljuca.add("-inkey");
		 komandaZaDekripcijuSimetricnogKljuca.add("asimetricniKljuc.key");
		 
		 
		// System.out.println("komanda ya dekripciju simetricnog kljuca " + komandaZaDekripcijuSimetricnogKljuca);
		 
		 String path="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate";//
		// System.out.println("path "+path);
		 pr.directory(new File(path));  //
		 
		// System.out.println("trenutni direktorij" + pr.directory());
		 pr.command(komandaZaDekripcijuSimetricnogKljuca);
		 Process ppr=pr.start();
		 Thread.sleep(2000);
		 // TREBA PROCITATI IZ TOG FAJLA I OBRISATI GA
		 
		 
		  String filename= "C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\simetricniKljuc.txt";
	      File myObj = new File(filename);
	      Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) 
	      {
	        kljuc = myReader.nextLine();
	     
	      }
	      myReader.close();
		 
	   //   System.out.println("kljuc " + kljuc);
	      //OBRISATI DEKRIPTOVANI FAJL
		 
	      File file
	        = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\simetricniKljuc.txt");

	 /*   if (file.delete()) {
	        System.out.println("file obrisan");
	    }
	    else {
	        System.out.println("fajl nije obrisan");
	    }
	*/  file.delete();
		
		 
	  //  System.out.println("DONE ");
		 
		 //return kljuc;
		 
	 }
	 catch (IOException e) {
	        // handle e
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return kljuc;
}
	  
	  public static void Dekriptuj() throws IOException, InterruptedException {
		  try { //Dekriptovati FAJL SA REZULTATIMA
		  
		  String kljuc=radSaKljucevima();
 		// System.out.println("kljuc " + kljuc);
 		 
 		 System.out.println();
 		 ProcessBuilder pr=new ProcessBuilder();//dodati komandi ya kriptovanje 3des alg i vidjeti kako dekriptovati 
 		 List<String> komandaZaDekripcijufajlaRezultati =new ArrayList<String>();
 		 //openssl aes-128-ecb -in dekriptovano.txt -out enkriptovano.txt -k sigurnost -base64 komanda ze enkripciju
 		 //openssl aes-128-ecb -d -in enkriptovano.txt -out plainText.txt -k sigurnost -base64 ya dekripciju
 		 komandaZaDekripcijufajlaRezultati.add("openssl");
 		 komandaZaDekripcijufajlaRezultati.add("aes-128-ecb");
 		 komandaZaDekripcijufajlaRezultati.add("-d");
 		 komandaZaDekripcijufajlaRezultati.add("-in");
 		 komandaZaDekripcijufajlaRezultati.add("enkriptovano.txt");
 		 komandaZaDekripcijufajlaRezultati.add("-out");
 		 komandaZaDekripcijufajlaRezultati.add("dekriptovano.txt");
 		 komandaZaDekripcijufajlaRezultati.add("-k");
 		 komandaZaDekripcijufajlaRezultati.add(kljuc);
 		 komandaZaDekripcijufajlaRezultati.add("-base64");
 		 
 		 
 	//	 System.out.println("komanda Za dekripciju fajla Rezultati " + komandaZaDekripcijufajlaRezultati);
 		 
 		 String path="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate";//
 	//	 System.out.println("path "+path);
 		 pr.directory(new File(path));  //
 		 
 		// System.out.println("trenutni direktorij" + pr.directory());
 		 pr.command(komandaZaDekripcijufajlaRezultati);
 		 Process ppr=pr.start();
 		 Thread.sleep(2000);
 		// System.out.println("DONE");
		  } catch (IOException e) {
	 	        // handle e
	 	    }
	  }
	  
	  
	  public static void Enkriptuj() throws IOException, InterruptedException {
		  try { //Dekriptovati FAJL SA REZULTATIMA
		  
		  String kljuc=radSaKljucevima();
 		// System.out.println("kljuc " + kljuc);
 		 
 		// System.out.println();
 		 ProcessBuilder pr=new ProcessBuilder();//dodati komandi ya kriptovanje 3des alg i vidjeti kako dekriptovati 
 		 List<String> komandaZaEnkripcijufajlaRezultati =new ArrayList<String>();
 		 //openssl aes-128-ecb -in dekriptovano.txt -out enkriptovano.txt -k sigurnost -base64 komanda ze enkripciju
 		 //openssl aes-128-ecb -d -in enkriptovano.txt -out plainText.txt -k sigurnost -base64 ya dekripciju
 		 komandaZaEnkripcijufajlaRezultati.add("openssl");
 		komandaZaEnkripcijufajlaRezultati.add("aes-128-ecb");
 		komandaZaEnkripcijufajlaRezultati.add("-in");
 		komandaZaEnkripcijufajlaRezultati.add("dekriptovano.txt");
 		komandaZaEnkripcijufajlaRezultati.add("-out");
 		komandaZaEnkripcijufajlaRezultati.add("enkriptovano.txt");
 		komandaZaEnkripcijufajlaRezultati.add("-k");
 		komandaZaEnkripcijufajlaRezultati.add(kljuc);
 		komandaZaEnkripcijufajlaRezultati.add("-base64");
 		 
 		 
 	//	 System.out.println("komanda Za enkripciju fajla Rezultati " + komandaZaEnkripcijufajlaRezultati );
 		 
 		 String path="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate";//
 		// System.out.println("path "+path);
 		 pr.directory(new File(path));  //
 		 
 	//	 System.out.println("trenutni direktorij" + pr.directory());
 		 pr.command(komandaZaEnkripcijufajlaRezultati);
 		 Process ppr=pr.start();
 		 Thread.sleep(2000);
 		// System.out.println("DONE");
		  } catch (IOException e) {
	 	        // handle e
	 	    }
	  }
	  
	  
	  public static void PrikaziRezultate() throws IOException, InterruptedException
	     { //DEKRIPTUJ FAJL ENKRIPTOVANO.TXT
		  
		  System.out.println("waiting... ");
	    	Dekriptuj();
	    	//SADA JE POTREBNO CITATI IY FAJLA DEKRIPTOVANO.TXT
	    	//NAKON TOGA OBRISATI FAJL DEKRIPTOVANO.TXT
	    	  try {
	    		  File file = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt");//treba biti baya korisnika
			    
	  	    	BufferedReader reader = new BufferedReader(new FileReader(file));
	  	        String line = "", text = "";
	  	        while((line = reader.readLine()) != null) 
	  	        {
	  	        	 text += line + "\r\n";
	  	        }
	  	        reader.close();//System.out.println("za zamjenu sa =  " + zaZamjenu);
	  	        
	  	        System.out.println("Rezultati ");
	  	   //   System.out.println("username	vrijeme	bodovi");
	  	    System.out.println(text);
	  	    
	  	    //SADA OBRISATI DEKRIPTOVANO.TXT
	  	    
	  	  File file1
	        = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt");

	  	  file1.delete();
	  /*  if (file1.delete()) {
	        System.out.println("file obrisan");
	    }
	    else {
	        System.out.println("fajl nije obrisan");
	    }*/
	  	        
	  	    } catch (IOException e) {
	  	        // handle e
	  	    }
		  
		  
		  
	     }
	  
	  
	  public static boolean checkVerification(String username,String caTijelo) throws InterruptedException
	  {
		  //openssl verify -CAfile rootVerify.pem -verbose certs/certAndrea.crt > verification/username.txt
		  Boolean verification=false;
		  try
			 {
				 ProcessBuilder pr=new ProcessBuilder();//dodati komandi ya kriptovanje 3des alg i vidjeti kako dekriptovati 
				 List<String> komandaZaVerifikacijuCertifikata =new ArrayList<String>();
				 komandaZaVerifikacijuCertifikata.add("openssl");
				 komandaZaVerifikacijuCertifikata.add("verify");
				 komandaZaVerifikacijuCertifikata.add("-CAfile");
				 komandaZaVerifikacijuCertifikata.add("rootVerify.pem");
				 komandaZaVerifikacijuCertifikata.add("-verbose");
				 komandaZaVerifikacijuCertifikata.add("certs/cert" + username + ".crt");
				 komandaZaVerifikacijuCertifikata.add(">");
				 komandaZaVerifikacijuCertifikata.add("verification/" + username + ".txt");
				 System.out.println("verifikacija" + komandaZaVerifikacijuCertifikata);
				 
				 String path="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + caTijelo;//
				// System.out.println("path "+path);
				 pr.directory(new File(path));  //
				 
				 System.out.println("trenutni direktorij" + pr.directory());
				 pr.command(komandaZaVerifikacijuCertifikata);
				 Process ppr=pr.start();
				 Thread.sleep(5000);
				 System.out.println("DONE ");
				 
				 //SADA TREBA CITATI IY TOG FAJLA
				 
				 
			
			  String filename= "C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + caTijelo + "\\verification\\" + username + ".txt";
			  System.out.println("citam iz " + filename);
			  File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) 
		      {
		        String data = myReader.nextLine();
		        if(data.equals(data.contains("OK")))
		        {
		        	verification=true;
		        	break;
		        }
		        
		      }
		      myReader.close();
				  
				  
				 
			 }
			 catch (IOException e) {
			        // handle e
			    }
		return verification;
	  }
	  

	/*public static boolean checkVerification(String username, String caTijelo) {
		
		
		// TODO Auto-generated method stub
		return false;
	}
	  
	*/  
	  
	  
}
