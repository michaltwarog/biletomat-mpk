package com.company;

import java.time.LocalDate;
import java.util.Scanner;

public class Biletomat {
    private final String [][]historiaTransakcji= new String[50][4];
    private final Bilet []bilety=new Bilet[50];
    private int nrBiletu;
    //wypelnianie automatu
    private int []saldo;
    Transakcja transakcje;
    //String [][]rodzajBiletow;
    //String [][]cenaBiletów;
    //wydawanie reszty
    String lokalizacja;
    //obiekt klasy bilet
   // Bilet pojedynczyBilet;
    Rodzaje typ;
    Scanner in;
    Biletomat(int s,String l){
        saldo=new int[7];
        for(int i=0;i<7;i++) {
            for (int j = 0; j < s; j++) {
                ++saldo[i];
            }
        }
        transakcje=new Transakcja(saldo);
        lokalizacja=l;
    }
    @Override
    public String toString() {
        //data : rodzaj biletu : liczba biletów : dochód aka historia transakcji sumowanie!!!
        return super.toString();
    }

    /**
     * wypisuje transakcje z podanego dnia
     * @param date
     */
    public void wydrukujTransakcje(LocalDate date){

    }

    enum Rodzaje{NORMALNY,ULGOWY,GRUPOWY};
    enum Normalny{
        _20MINUT(4, "20 minut"),_60MINUT(6, "60 minut");
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
        _20MINUT(2,"20 minut"),
        _60MINUT(3,"60 minut");
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
    private void dostepneRodzaje(){
        System.out.println("Dostepne rodzaje biletow:\n");
        System.out.println("1. "+Rodzaje.values()[0]);
        System.out.println("2. "+Rodzaje.values()[1]);
        System.out.println("3. "+Rodzaje.values()[2]);

    }
    private void wybierzPodrodzaj(){
        // String rodzaj;
       // Double cena;
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
       // while(!"2".equals(in)) {
            //Start lub tryb ...
        System.out.println("Wybierz rodzaj biletu:");
        dostepneRodzaje();

        int i = in.nextInt();
        typ = Rodzaje.values()[i - 1];
        wybierzPodrodzaj();
        //ilosc biletow
        System.out.print("Podaj ilość:");
        i=in.nextInt();
        if(i>0) {
            transakcje.platnosc(bilety[nrBiletu - 1].cena * i);
        }
        //}//

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
   private class Bilet extends BiletRodzaj{
        private LocalDate data;
        public Bilet(String r, double c){
            super.setCena(c);
            super.setRodzaj(r);
            data=LocalDate.now();
        }

        @Override
        public String toString() {
            return "Bilet{" + "rodzaj='" + super.getRodzaj() + '\'' + ", cena=" + super.getCena()+ "data='"+data+'\''+'}';
        }

    }
}

