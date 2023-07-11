package pitanja;

import java.io.File;
import java.util.Scanner;

import options.Stego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public  class Kviz {//abstract jer necu da koristim objekte klase Kviz 
	
	static String tipPitanja;
    static String pitanje;
	static String ponudjeniOdgovori[] =new String[4] ;
	static String tacanOdgovor;
	static String pitanja[]=new String[] 
			{"O@Koji je hemijski simbol srebra?@Ag",
			"O@Koja je najmanja ptica na svijetu?@Pcelinji kolibri",
			"O@Koliko ima igraca u vaterpolo ekipi?@sedam",
			"O@U kojoj drzavi se nalazi Koloseum?@Italija",
			"O@Sta znaci H u H2O?@vodonik",
			"O@Koja zemlja ima najduzu obalu?@Kanada",
			"O@U kojoj drzavi se nalazi Kip slobode?@SAD",
			"O@Koji grad okruzuje Vatikan?@Rim",
			"O@Iz koje mitologije je Pandorina kutija?@Grcke",
			"O@Ko je bio BBC-jeva 'Sportska licnost godine' 2001. godine?@David Beckham",
			"I@U filmu 'Glup i Gluplji', Jim Carrey je glumio Lloyda - ko je glumio Harryja?@David Spade@Jeff Daniels@Mike Myers@Niko od navedenih@Jeff Daniels",
			"I@S kojom biljkom se pravi pesto?@Origano@Korijanden@Timljan@Bosiljak@Bosiljak",
			"I@Koje ptice mogu okrenuti glavu za cak 270 stepeni?@Sove@Golubovi@Slavuji@Nista od navedenog@Sove",
			"I@Kako se na spanskom kaze pet?@Quinto@Cinq@Quinique@Cinco@Cinco",
			"I@Koje godine je rodjen William Shakespeare?@1564@1574@1565@1572@1564",
			"I@Koje godine je bio kraj Prvog svjetskog rata?@1918@1917@1919@1915@1918",
			"I@Koje godine je bio kraj Drugog svjetskog rata?@1945@1946@1947@1948@1945",
			"I@Koje godine je potonuo Titanic?@1912@1910@1925@1911@1912",
			"I@Koji je najblizi planet Suncu?@Merkur@Venera@Jupiter@Saturn@Merkur",
			"I@Gdje je izlozena Mona Lisa?@Francuska@Italija@Srbija@Nista od navedenog@Francuska"};//tip pitanja@Pitanje@Odgovor (o daj odgovor) (I izber odg)
	static double bodovi=0.0D;
	static String uneseniOdgovor;//mogu prebrojavati broj '@'-ova umjesto da u string upisujem tip
	
	
	public static double getBodovi() {
		return bodovi;
	}

	public static void setBodovi(double bodovi) {
		Kviz.bodovi = bodovi;
	}

	public static void unesiPitanja()
	{
		for(int i=0;i<20;i++)//do 20 jer ima 20 pitanja
		{
			String nazivSlike="slika"+(i+1)+".bmp";
			//System.out.println(nazivSlike);
			//System.out.println(pitanja[i]);//Stego.encode(new File("slika"+(i+1)+".png"), pitanja[i]);
			Stego.encode(new File(nazivSlike), pitanja[i]);
			//Stego.encode(new File("png\\"+nazivSlike+"\\"), pitanja[i]);
		}
		System.out.println("Pitanja su unesena ");
	}
	
	public static void ispisiPitanja()//napraviti da moze pokusati ponovo pitanja
	{
		List<Integer> brojPitanjaList = new ArrayList<Integer>();
		Random rand = new Random();//rand.nextInt((max - min) + 1) + min;
		Integer br;
		for(int i=0;i<5;i++)//broj pitanja koje ce kviz imati
		{
			System.out.println("pitanje " + (i+1) + ":");
		do
		{
			 br=rand.nextInt((20 - 1) + 1) + 1; //trebam napraviti da se ne ponavljaju ista pitanja
			//System.out.println("br pitanja" + br);
			
		}while(brojPitanjaList.contains(br));
			brojPitanjaList.add(br);
			
		/*	for(int k=0;k<brojPitanjaList.size();k++){
			  //  System.out.println(list.get(i));
			    System.out.println("elementi liste " + brojPitanjaList.get(k));
			} 
			*/
			
			String nazivSlike="slika"+br+"_stego.bmp";//System.out.println(nazivSlike);
			String text = Stego.decode(new File(nazivSlike));//System.out.println(text);
			String[] parametar = text.split("@");
			tipPitanja=parametar[0].trim();
			char c=tipPitanja.charAt(0);
			
			
			
			if(c=='O')//daj odgovor
			{
				pitanje=parametar[1].trim();
				System.out.println("pitanje: "+pitanje);
				System.out.println("Unesite odgovor.");
				 Scanner myObj = new Scanner(System.in);
				
				 uneseniOdgovor=myObj.nextLine();
				 uneseniOdgovor=uneseniOdgovor.trim();
				tacanOdgovor=parametar[2].trim();
				provjeraOdgovora();
				
			}
			if(c=='I')//izaberi tacan odgovor
			{
				pitanje=parametar[1].trim();
				System.out.println("pitanje: "+pitanje);
				System.out.println("Ponudjeni odgovori:");
				for(int j=2;j<=5;j++)
				{
					ponudjeniOdgovori[j-2]=parametar[j].trim();
					System.out.println((j-1)+"."+" "+ponudjeniOdgovori[j-2]);
				}
				tacanOdgovor=parametar[6].trim();
				
				System.out.println("Unesite odgovor.");
				 Scanner myObj = new Scanner(System.in);
				
				 uneseniOdgovor=myObj.nextLine();
				 uneseniOdgovor=uneseniOdgovor.trim();
				 provjeraOdgovora();
				 
			
				
			}
			System.out.println();
		}
		System.out.println("Kraj kviza. ");
	}

	private static void provjeraOdgovora() {
		if(uneseniOdgovor.equalsIgnoreCase(tacanOdgovor))
		{
			System.out.println("odgovor je tacan.");
			bodovi+=1;
			System.out.println("bodovi= "+bodovi);
		}
		else {
			System.out.println("odgovor nije tacan.");
			bodovi-=0.25;
			System.out.println("bodovi= "+bodovi);
		}
	}
	
}
