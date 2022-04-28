package com.company;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.lang.String.join;

public class Biletomat {
    private final int MAX_TRANSAKCJI=50;
    private final String [][]historiaTransakcji= new String[MAX_TRANSAKCJI][4];
    private int iloscTansakcji;
    private final Bilet []bilety=new Bilet[50];
    private int nrBiletu;
    private int []saldo;
    Transakcja transakcje;
    String lokalizacja;
    Rodzaje typ;
    Scanner in;
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
        //data : rodzaj biletu : liczba biletów : dochód
        String historia="";
        for(int i=0;i<iloscTansakcji;i++){
            historia+= String.join(" : ",historiaTransakcji[i][0],historiaTransakcji[i][1],historiaTransakcji[i][2],historiaTransakcji[i][3]);
            historia+='\n';
        }
        return historia;
    }

    public void wydrukujTransakcje(LocalDate date){
        for(int i=0;i<iloscTansakcji;i++){
            if(date.toString().equals(historiaTransakcji[i][0])){
                System.out.println(String.join(":",historiaTransakcji[i][0],historiaTransakcji[i][1],historiaTransakcji[i][2],historiaTransakcji[i][3]));
            }
        }
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
        System.out.println("Dostepne rodzaje biletow:");
        System.out.println("1. "+Rodzaje.values()[0]);
        System.out.println("2. "+Rodzaje.values()[1]);
        System.out.println("3. "+Rodzaje.values()[2]);

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
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
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

