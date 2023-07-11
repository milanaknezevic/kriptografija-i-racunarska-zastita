package players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import options.Sertifikati;
import pitanja.Kviz;

public class Korisnik {
	 private String username;
     private String password;
     private String kljucZaSimetricnuEnkripciju="sigurnost";
    // private static String kljucZaSimetricnuEnkripciju="sigurnost";
     
     public void registracija() throws IOException, InterruptedException
     {
    	 try {
    		 
    		 
    	 Scanner myObj = new Scanner(System.in);	
    	 String caNiz[]= {"cert1.crt","cert2.crt"};
    	 
    	 System.out.println("Kreiranje novog naloga");
    	 
    	Boolean odustani=false;
    	Boolean korisnikPostoji=false;
    	do {
    			System.out.print("Korisnicko ime-> ");
    			username=myObj.nextLine();
    			username=username.trim();//System.out.println("Username  "+username);
    			
    			
    			System.out.print("Lozinka-> ");// napraviti da se prikayuju yvjeydice
    			password=myObj.nextLine();
    			password=password.trim();// System.out.println("Password  "+password);
    			korisnikPostoji=Sertifikati.checkIfKorisnikExistsInBazaKorisnika(username, password);
    			if(korisnikPostoji==true)
    			{
    				System.out.println("Korisnicko ime zauzeto, pokusajte ponovo ");
    			}
    			
    	 }while(korisnikPostoji);
    	
    	System.out.println("waiting...");
    	
    	//USERNAME I PASSWORD SU UNIJETI

		 
    	
    	
		 //BIRAMO RANDOM CA TIJELO I HESUJEMO PASWORD I UNOSIMO U BAZU KORISNKA
		 	Random rand=new Random();
			int select =rand.nextInt(caNiz.length);
			int cnf=select+1;// 1 ili 2
			String ca=caNiz[select];//cert2.crt npr
			//System.out.println("ca "+ca);
			
			String caTijelo;
			//ca.substring(0, ca.lastIndexOf('.'));
			caTijelo=ca.substring(0, ca.lastIndexOf('.'));
			//System.out.println("ca Tijelo " + caTijelo);
			
			
			String opensslCnf="openssl"+ cnf+".cnf";//openssl2.cnf npr //System.out.println("ca "+ca);
			//System.out.println("openssl.cnf "+opensslCnf);
			//System.out.println("Ovaj openssl treba citati  "+opensslCnf);
			//System.out.println("ca "+ca);
			String hashedPassword=Sertifikati.GenerateHash(password);//System.out.println("Hash "+ hashedPassword);
			String[] pom = ca.split("\\."); // System.out.println("pom[0]"+ pom[0]);
			
			String putanja="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + pom[0] + "\\openssl" + cnf + ".cnf";//System.out.println("putanja "+putanja);
			changeOpenssl(username,putanja);
			
				
				//GENERISANJE  PRIVATNOG KLJUCA ZA KORINSIKIKA 
				ProcessBuilder pr=new ProcessBuilder();//dodati komandi ya kriptovanje 3des alg i vidjeti kako dekriptovati 
				List<String> komandaZaGenerisanjeKljuca =new ArrayList<String>();
				komandaZaGenerisanjeKljuca.add("openssl");
				komandaZaGenerisanjeKljuca.add("genrsa");
				komandaZaGenerisanjeKljuca.add("-out");
				komandaZaGenerisanjeKljuca.add("private/private" + username + "4096.key");//generisace se kljuc npr private/privateMilana4096.key
				komandaZaGenerisanjeKljuca.add("4096");
				//System.out.println("generisanje kljuca" + komandaZaGenerisanjeKljuca);
				String path="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + pom[0];//System.out.println("path "+path);
				pr.directory(new File(path)); // ne treba u ovaj dir al neka ya sada ice u cert1 ili 2 yavisi od random  //postavlja dir u kom ce se izvrsavati komanda//System.out.println("trenutni direktorij" + pr.directory());
				//System.out.println("trenutni direktorij" + pr.directory());
				pr.command(komandaZaGenerisanjeKljuca);
				Process ppr=pr.start();
				Thread.sleep(5000);     /*String komanda="openssl genrsa -out"+" private/private" + username + "4096.key +4096";ProcessBuilder proces=new ProcessBuilder("cmd.exe", "/c", komanda);proces.start();*/
				//KLJUC JE GENERISAN
				
				
				
				
				//SADA TREBA IZDATI ZAHTJEV
				ProcessBuilder pr1=new ProcessBuilder();
				List<String> komandaZaZahtjev =new ArrayList<String>();
				komandaZaZahtjev.add("openssl");
				komandaZaZahtjev.add("req");
				komandaZaZahtjev.add("-new");
				komandaZaZahtjev.add("-key");
				komandaZaZahtjev.add("private/private" + username + "4096.key");//taj kljuc koji je gore generisan njime se potpisuje yahtjev
				komandaZaZahtjev.add("-config");
				komandaZaZahtjev.add(opensslCnf);
				komandaZaZahtjev.add("-out");
				komandaZaZahtjev.add("requests/req" + username + ".csr");
				//komandaZaZahtjev.add("-days");
				//komandaZaZahtjev.add("365");
				komandaZaZahtjev.add("-subj");
				komandaZaZahtjev.add("/C=BA/ST=RS/O=Elektrotehnicki fakultet/OU=CA_MilanaKnezevic/CN=" + username + "/");//pr1.directory(new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati")); // ne treba u ovaj dir al neka ya sada ice u cert1 ili 2 yavisi od random  //postavlja dir u kom ce se izvrsavati komanda
				
				//System.out.println("yahtjev ya req" + komandaZaZahtjev);
				pr1.directory(new File(path));
				pr1.command(komandaZaZahtjev);
				//System.out.println("trenutni direktorij" + pr1.directory());
				Process ppr1=pr1.start();
				Thread.sleep(5000);
				//ZAHTJEV JE POSLAN
				
				
				
				
				
				//SADA JE POTREBNO POTPISATI CERTIFIKAT
				ProcessBuilder pr2=new ProcessBuilder();
				List<String> komandaZaPotpis =new ArrayList<String>();
				komandaZaPotpis.add("openssl");
				komandaZaPotpis.add("ca");
				komandaZaPotpis.add("-in");
				komandaZaPotpis.add("requests/req" + username + ".csr");
				komandaZaPotpis.add("-out");
				komandaZaPotpis.add("certs/cert" + username + ".crt");//taj kljuc koji je gore generisan njime se potpisuje yahtjev
				komandaZaPotpis.add("-config");
				komandaZaPotpis.add(opensslCnf);//bice openssl1 ili 2 yavisi od rand
				komandaZaPotpis.add("-batch");//pr2.directory(new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati")); // ne treba u ovaj dir al neka ya sada ice u cert1 ili 2 yavisi od random  //postavlja dir u kom ce se izvrsavati komanda
				//System.out.println("yahtjev ya potpis" + komandaZaPotpis);
				pr2.directory(new File(path));
				//System.out.println("trenutni direktorij" + pr2.directory());
				pr2.command(komandaZaPotpis);
				Process ppr2=pr2.start();
				Thread.sleep(5000);


				
				
				
				
				//SADA JE POTREBNO ZASTITI CERTIFIKAT I I KLJUC(POMOCU PKCS12)
			//String username="Milos";
		ProcessBuilder pr3=new ProcessBuilder();
		List<String> komandaZaPkcs12 =new ArrayList<String>();
		komandaZaPkcs12.add("openssl");
		komandaZaPkcs12.add("pkcs12");
		komandaZaPkcs12.add("-export");
		komandaZaPkcs12.add("-out");
		komandaZaPkcs12.add("PKCS12/cert" + username + ".p12");
		komandaZaPkcs12.add("-inkey");
		komandaZaPkcs12.add("private/private" + username + "4096.key");
		komandaZaPkcs12.add("-in");
		komandaZaPkcs12.add("certs/cert" + username + ".crt");
		komandaZaPkcs12.add("-certfile");
		komandaZaPkcs12.add(ca);//ide ca
		komandaZaPkcs12.add("-password");
		komandaZaPkcs12.add("pass:sigurnost");
		//openssl pkcs12 -export -out PKCS12/certJadranko.p12 -inkey private/privateJadranko4096.key -in certs/certJadranko.crt 
		// -certfile cert1.crt -password pass:sigurnost
		
		//System.out.println("yahtjev ya PKCS12 " + komandaZaPkcs12);
		String pathPkcs12="C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + caTijelo;//ide ca tijelo
	//	System.out.println("path " + pathPkcs12);
		pr3.directory(new File(pathPkcs12));
		//System.out.println("trenutni direktorij" + pr3.directory());
		pr3.command(komandaZaPkcs12);
		Process ppr3=pr3.start();
		Thread.sleep(5000);
				
				
				
		// System.out.println("waiting... ");
				
				
				//ISPISATI PUTANJU DIREKTORIJA
				Sertifikati.createBazaKorisnika(username,hashedPassword,0,caTijelo);
				System.out.println("Putanja na kom se nalazi izdati sertifikat:	C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\newcerts\\" + ca);
				System.out.println("Done");
			}catch (IOException e) {
		        // handle e
		    }
			
			
    	}
     
     public void changeOpenssl(String ime,String path)//mjenja properties openssl fajla
     {
    	 
	     
	    try {
	    	String zaZamjenu = null;
	        File file = new File(path);
	    	BufferedReader reader = new BufferedReader(new FileReader(file));
	        String line = "", oldtext = "";
	        while((line = reader.readLine()) != null) 
	        {
	        	if(line.contains("default_keyfile"))
	        	{
	        		zaZamjenu=line;//	System.out.println("za zamjenu sa =  " + zaZamjenu);
	        		
	        	}
	        	
	            oldtext += line + "\r\n";
	        }
	        reader.close();//System.out.println("za zamjenu sa =  " + zaZamjenu);
	        String phase  = oldtext.replaceAll(zaZamjenu, "default_keyfile 	= ./private/private" + ime + ".key");
	        FileWriter writer = new FileWriter(path);
	        writer.write(phase);
	        writer.close();
	    } catch (IOException e) {
	        // handle e
	    }
	
     }
     public static void uvecajBrojLogina(String username, String password, Integer br, String caTijelo)
	  {
		  try {
		    	String zaZamjenu = null;
		        File file = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\bazaKorisnika.txt");//treba biti baya korisnika
		    	BufferedReader reader = new BufferedReader(new FileReader(file));
		        String line = "", oldtext = "",newLine="";
		        while((line = reader.readLine()) != null) 
		        {
		        	//System.out.println("line  " + line);
		        	if(line.contains(username + " " + password))
		        	{
		        		zaZamjenu=line;
		        		//System.out.println("za zamjenu sa =  " + zaZamjenu);
		        		
		        	}
		        	
		            oldtext += line + "\r\n";//text citavog fajla
		        }
		        reader.close();
		    	//System.out.println("za zamjenu sa =  " + zaZamjenu);
		       
		        String brojLogina=br.toString();
		      //  System.out.println("broj logina =  " + brojLogina);
		        
		    	newLine=username + " " + password + " " + brojLogina + " " +caTijelo ;//odvojeno je spaceom
		    	//System.out.println("newLine =  " + newLine);
		        String phase  = oldtext.replaceAll(zaZamjenu, newLine);
		        FileWriter writer = new FileWriter("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\bazaKorisnika.txt");//promjeni u bayaKorisnika
		        writer.write(phase);
		        writer.close();
		      //  System.out.println("Baza Korisnika je izmjenjana ");
		    } catch (IOException e) {
		        // handle e
		    }
	  }
	  

     
     
     
     public Boolean login() throws IOException, InterruptedException
     { Boolean ulogovan=false;
    	 try {
    	 Scanner myObj = new Scanner(System.in);	
    	
    		 
    		 System.out.print("Korisnicko ime-> ");
    		 username=myObj.nextLine();
    		 username=username.trim();//System.out.println("Username  "+username);
    		 
    		 
    		 System.out.print("Lozinka-> ");// napraviti da se prikayuju yvjeydice
    		 password=myObj.nextLine();
    		 password=password.trim();
    		 String hashedPassword=Sertifikati.GenerateHash(password);//System.out.println("Hash "+ hashedPassword);
    		 
    		 String filename= "C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\bazaKorisnika.txt";
    		  File file = new File(filename);//treba biti baya korisnika
		    	BufferedReader reader = new BufferedReader(new FileReader(file));
		        String line = "", oldtext = "",newLine="";
		        while((line = reader.readLine()) != null) 
		        {
		        	 
		        	if(line.contains(username))
		        	{
		        		//korisnik nadjen
		        	break;
		        		
		        	}
		        	
		           
		        }
		        reader.close();
		       // System.out.println("line	" + line);
		        if(line!=null)
		        {
		        	
		        	//while petlja ya ponavljanje unosa
		        	String[] parts = line.split(" ");
		        	//parts[0]=parts[0].toUpperCase();
		        	Integer brLogina=Integer.parseInt(parts[2]); 
		        	
		        	/*System.out.println("parts[0] Ime	" + parts[0]);
		        	System.out.println("parts[1] Password	" + parts[1]);
		        	System.out.println("parts[2] broj logova	" + brLogina);
		        	System.out.println("parts[3] ca tijelo	" + parts[3]);
		        	*/
		        	Boolean ime=username.equals(parts[0]);
		        	Boolean pass=hashedPassword.equals(parts[1]);
		        //	System.out.println("ime	" + ime);
		        //	System.out.println("pass	" + pass);
		        	String opensslBr=parts[3].substring(4);
		        //	System.out.println("broj " + opensslBr);
		        	//String openssl="openssl"+ opensslBr + ".cnf";
		        	//System.out.println("openssl " + openssl);
		        	if(username.equals(parts[0]) && hashedPassword.equals(parts[1]))//ako je username i pass odgovarajuci i ima pravo igranje
		        	{
		        		
		        		//if( brLogina<3 && Sertifikati.checkCertificateV(parts[0],parts[3]))
		        		Boolean verifikovano=Sertifikati.checkVerification(parts[0], parts[3]);
		        		if( brLogina<3 && Sertifikati.checkCertificateV(parts[0],parts[3]) && verifikovano==true )//provjeri validnost i verifikuj ca
		        		{
		        			//System.out.println("br logina  " + brLogina);
		        		brLogina++;
		        		//System.out.println("br logina  " + brLogina);
		        		//USPJENSO ULOGOVAN 
		        		System.out.println("Uspjesno ste se ulogovali.  ");
		        		ulogovan=true;
		        		long start1 = System.nanoTime();
		        		Kviz.ispisiPitanja();
		        		long end1 = System.nanoTime();      
		        	      long vrijeme=(end1-start1)/1000000000;
		        	      //podjeli vrijeme sa 1000000000
		        	     // System.out.println("Vrijeme u nanosekundama: "+ vrijeme);
		        	      String strVrijeme = Long.toString(vrijeme);
		        	      System.out.println("Vrijeme= "+ strVrijeme + "s");
		        		double a=Kviz.getBodovi();
		        		//System.out.println("bodovi a " + Kviz.getBodovi());
		        		//System.out.println("bodovi samo geter " + Kviz.getBodovi());
		        		String strBodovi=String.valueOf(Kviz.getBodovi());  
		        		System.out.println("Broj osvojenih bodova= " + strBodovi);
		        		strVrijeme=strVrijeme + "s";
		        		IzmjenaRezultata(username, strVrijeme, strBodovi);
		        		
		        		uvecajBrojLogina(username, hashedPassword, brLogina,parts[3]);
		        		if(brLogina>=3)
		        		{
		        			System.out.println("Broj logina je veci od 3, certifikat je povucen. ");
		        			Sertifikati.revokeCertifikat(username, opensslBr);
		        		}
		        		}
		        		else
		        		{
		        			System.out.println("Vas sertifikat nije validan ");
		        		}
		        		
		        	}else {
		        		System.out.println("Nevalidno ime ili lozinka. ");
		        	}
		        	
		        }
		        else
		        {
		        	System.out.println("Korisnik se ne nalazi u bazi");

		        }
    	 } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return ulogovan;
       }
     
     
   //  "openssl verify -CAfile " + rootCAPath + " -verbose " + userPath + " >" + path;
     
     public void IzmjenaRezultata(String username,String vrijeme,String bodovi) throws InterruptedException //mjenja reyultate korisnika koji vec postoji ili dodaje novog
     {
    	 try {
    		 
    		/*String kljuc=Sertifikati.radSaKljucevima();
    		 System.out.println("kljuc " + kljuc);
    		 
    		 System.out.println();*/
    		Sertifikati.Dekriptuj();
    		 
    		 //FAJL JE DEKRIPTOVAN I SPREMAN ZA CITANJE I IZMJENE
    		 //System.out.println("FAJL JE DEKRIPTOVAN I SPREMAN ZA CITANJE I IZMJENE");
    		Boolean korisnikVecPostoji=false;
 	    	String zaZamjenu = null;
 	        File file = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt");//odavde citam
 	
 	    	BufferedReader reader = new BufferedReader(new FileReader(file));
 	        String line = "", oldtext = "";
 	        while((line = reader.readLine()) != null) //prolazi kroz fajl
 	        {
 	        	if(line.contains(username)) //ako taj user vec postoji updajtuj bazu
 	        	{
 	        		zaZamjenu=line;//	
 	        		//System.out.println("za zamjenu sa =  " + zaZamjenu);
 	        		korisnikVecPostoji=true;
 	        		//System.out.println("Korisnik vec postoji =  " + korisnikVecPostoji);
 	        		
 	        	}
 	        	oldtext += line + "\r\n";
 	        }
 	        reader.close();//System.out.println("za zamjenu sa =  " + zaZamjenu);
 	        if(korisnikVecPostoji.equals(true))//IZMJENJAJ VRIJEME I BODOVE TJ UNESI POSLEDNJE IYMJERENE REZULTATE   ///?????
 	        {
 	        	 String phase  = oldtext.replaceAll(zaZamjenu, username + "	" + vrijeme + "	" + bodovi);
 	 	        FileWriter writer = new FileWriter("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt");
 	 	        writer.write(phase);
 	 	        writer.close();
 	        }
 	        else//DODAJ NOVE REZULTATE
 	        {
 	        	try(FileWriter fw = new FileWriter("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt", true);
 	   			    BufferedWriter bw = new BufferedWriter(fw);
 	   			    PrintWriter out = new PrintWriter(bw))
 	   			{
 	        		username=username.trim();
 	        		vrijeme=vrijeme.trim();
 	        		bodovi=bodovi.trim();
 	        		String newText=oldtext + username + "	" + vrijeme + "	" + bodovi;
 	        		String pom=oldtext.replaceAll(oldtext, newText);
 	        	    FileWriter writer = new FileWriter("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt");
 	 	 	        writer.write(pom);
 	 	 	        writer.close();
 	        		//out.print("\r\n");
 	   			  // out.print(username + "	" + vrijeme + "	" + bodovi);
 	   			    
 	   			} catch (IOException e) {
 	   			    //exception handling left as an exercise for the reader
 	   			}
 	        }
 	        //SADA JE SVE UPISANO U DEKRIPTOVANO.TXT 
 	      //TREBA OBRISATI ENKRIPTOVANO.TXT 
 	        
 	       File file1
	        = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\enkriptovano.txt");

	   /* if (file1.delete()) {
	        System.out.println("file obrisan");
	    }
	    else {
	        System.out.println("fajl nije obrisan");
	    }*/
 	       file1.delete();
 	     //TREBA ENKRIPTOVATI NOVI DEKRIPTOVANI FAJL
 	        
 	        Sertifikati.Enkriptuj();
 	        //OBRISI DEKRIPTOVANO
 	         
 	       File file2
	       = new File("C:\\Users\\Korisnik\\eclipse-workspace\\kripto2022\\certifikati\\AZaRezultate\\dekriptovano.txt");

	  /*  if (file2.delete()) {
	        System.out.println("file obrisan");
	    }
	    else {
	        System.out.println("fajl nije obrisan");
	    }
	    */
 	       file2.delete();
 	        
 	    } catch (IOException e) {
 	        // handle e
 	    }
     }
    		  
    	 
    
   

}
