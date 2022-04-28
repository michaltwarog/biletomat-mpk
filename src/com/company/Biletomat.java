package com.company;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.lang.String.join;

/**
 * klasa do sprzedaży biletow
 */
public class Biletomat {
    /** maksymalna liczba transakcji  */
    private final int MAX_TRANSAKCJI=50;
    private final String [][]historiaTransakcji= new String[MAX_TRANSAKCJI][4];
    /** ilość wykonanych już transakcji */
    private int iloscTansakcji;
    /**  kompozycja */
    private final Bilet []bilety=new Bilet[50];
    /** ilość wydanych biletów */
    private int nrBiletu;
    /** ilość monet dla każdego nominału */
    private int []saldo;
    /** kompozycja */
    private Transakcja transakcje;
    /** lokalzacja biletomatu */
    private String lokalizacja;
    private Rodzaje typ;
    private Scanner in;
    Biletomat(int s,String l){
        saldo=new int[7];
        for(int i=0;i<7;i++) {
            saldo[i]=s;
        }
        transakcje=new Transakcja(saldo);
        lokalizacja=l;
    }
    @Override
    public String toString() {
        String historia="";
        for(int i=0;i<iloscTansakcji;i++){
            historia+= String.join(" : ",historiaTransakcji[i][0],historiaTransakcji[i][1],historiaTransakcji[i][2],historiaTransakcji[i][3]);
            historia+='\n';
        }
        return historia;
    }

    /**
     * funkcja zwracajaca wszystkie transakcje z danego dnia
     * @param date dzien z ktorego chcemy uzyskac transakcje
     */
    public void wydrukujTransakcje(LocalDate date){
        for(int i=0;i<iloscTansakcji;i++){
            if(date.toString().equals(historiaTransakcji[i][0])){
                System.out.println(String.join(" : ",historiaTransakcji[i][0],historiaTransakcji[i][1],historiaTransakcji[i][2],historiaTransakcji[i][3]));
            }
        }
    }

    enum Rodzaje{NORMALNY,ULGOWY,GRUPOWY};
    enum Normalny{
        _20MINUT(4, "20 minut"),_60MINUT(6, "60 minut"),_90MINUT(8,"90 minut"),_24GODZINY(22,"24 godziny"),_48GODZIN(35,"48 godzin");
        private double cena;
        private String nazwa;
        Normalny(double i, String n){
            this.cena=i;
            this.nazwa=n;
        }
        public double zwrocCene(){
            return cena;
        }
        @Override
        public String toString() {
            return nazwa;
        }
    }

    enum Ulgowy{
        _20MINUT(2,"20 minut"), _60MINUT(3,"60 minut"),_90MINUT(4,"90 minut"),_24GODZINY(11,"24 godziny"),_48GODZIN(17.5,"48 godzin");
        private double cena;
        private String nazwa;
        Ulgowy(double i, String n){
            this.cena=i;
            this.nazwa=n;
        }
        public double zwrocCene(){
            return cena;
        }
        @Override
        public String toString() {
            return nazwa;
        }
    }
    enum Grupowy{
        NORMALNY(60,"normalny"),ULGOWY(30,"ulgowy");
        private double cena;
        private String nazwa;
        Grupowy(double i, String n){
            this.cena=i;
            this.nazwa=n;
        }
        public double zwrocCene(){
            return cena;
        }
        @Override
        public String toString() {
            return nazwa;
        }

    }

    /**
     * wypisuje dostępne rodzaje biletów
     */
    private void dostepneRodzaje(){
        System.out.println("Dostepne rodzaje biletow:");
        for(int i=0;i<Rodzaje.values().length;i++){
            System.out.println((i+1)+". "+Rodzaje.values()[i]);
        }
    }
    private void wybierzPodrodzaj(){
        Normalny nor;
        Ulgowy ul;
        Grupowy grup;
        int i;
        switch(typ){
            case NORMALNY:
                System.out.println("Wybierz jego długość:");
                for(int j=0;j<Normalny.values().length;j++)
                    System.out.println((j+1)+". "+Normalny.values()[j]);
                i=in.nextInt();
                nor= Normalny.values()[i-1];
                bilety[nrBiletu++]=new Bilet(nor.toString(),nor.zwrocCene());
                break;
            case ULGOWY:
                System.out.println("Wybierz jego długość:");
                for(int j=0;j<Ulgowy.values().length;j++)
                    System.out.println((j+1)+". "+Ulgowy.values()[j]);
                i=in.nextInt();
                ul=Ulgowy.values()[i-1];
                bilety[nrBiletu++]=new Bilet(ul.toString(),ul.zwrocCene());
                break;
            case GRUPOWY:
                System.out.println("Wybierz rodzaj biletu grupowego:");
                for(int j=0;j<Grupowy.values().length;j++)
                    System.out.println((j+1)+". "+Grupowy.values()[j]);
                i=in.nextInt();
                grup=Grupowy.values()[i-1];
                bilety[nrBiletu++]=new Bilet(grup.toString(),grup.zwrocCene());
        }
    }

    public void sprzedazBiletow(){
        double reszta;
        in=new Scanner(System.in);
        Transakcja tran;
        int o=0;
        while(o!=4){
            System.out.println("1.Kup bilet.\n2.Wypisz historię biletomatu.\n3.Wypisz historię z danego dnia.\n4.Zakończ program");
            o=in.nextInt();
            System.out.println();
            if(o==1){
                System.out.println("Wybierz rodzaj biletu:");
                dostepneRodzaje();
                int i = in.nextInt();
                typ = Rodzaje.values()[i - 1];
                wybierzPodrodzaj();
                System.out.print("Podaj ilość:");
                i = in.nextInt();
                if (i > 0) {
                    if (transakcje.platnosc(bilety[nrBiletu - 1].cena * i)) {
                        historiaTransakcji[iloscTansakcji][0] = LocalDate.now().toString();
                        historiaTransakcji[iloscTansakcji][1] = bilety[nrBiletu - 1].getRodzaj();
                        historiaTransakcji[iloscTansakcji][2] = Integer.toString(i);
                        historiaTransakcji[iloscTansakcji][3] = Integer.toString((int) bilety[nrBiletu - 1].cena * i);
                        System.out.println("Dokonano zakupu:\n"+String.join(" : ",historiaTransakcji[iloscTansakcji][0],historiaTransakcji[iloscTansakcji][1],historiaTransakcji[iloscTansakcji][2],historiaTransakcji[iloscTansakcji][3]));
                    } else {
                        System.out.println("Transakcja nie powiodła się.");
                    }
                }
                iloscTansakcji++;
            }
            else if(o==2){
                System.out.println(this.toString());
            }
            else if(o==3){
                System.out.println("Podaj datę (yy-MM-dd)");
                String n=in.next();
                LocalDate d=LocalDate.parse(n);
                this.wydrukujTransakcje(d);
            }
        }

    }
    private abstract class BiletRodzaj{
        public String rodzaj;
        public double cena;

        public String getRodzaj() {
            return rodzaj;
        }

        public void setRodzaj(String rodzaj) {
            this.rodzaj = rodzaj;
        }

        public double getCena() {
            return cena;
        }

        public void setCena(double cena) {
            this.cena = cena;
        }

    }
    /** dziedziczenie */
   private class Bilet extends BiletRodzaj{
        private LocalDate data;
        public Bilet(String r, double c){
            super.setCena(c);
            super.setRodzaj(r);
            data=LocalDate.now();
        }

        @Override
        public String toString() {
            return "Zakupiono bilet: " + super.getRodzaj() + ", za " + super.getCena()+ "zł, dnia "+data;
        }

    }
}

