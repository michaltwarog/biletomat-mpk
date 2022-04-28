package com.company;

import java.math.BigDecimal;
import java.util.Scanner;

public class Transakcja {
    private final int MAX_NOMINALOW=7; //ilosc akceptowanych nominalow
    private final int MAX_MONET=500; //maksymalna ilosc monet dla danego nominalu
    private final int MAX_PRZELEWOW=1000;
    private int iloscNominalu[];
    private int iloscPrzelewow;
    private double doZaplaty;
    private final Gotowka [][]monety=new Gotowka[MAX_NOMINALOW][MAX_MONET]; //monety znajdujace sie w biletomacie - KOMPOZYCJA
    private final Karta[] historiaPrzelewow=new Karta[MAX_PRZELEWOW];
    Scanner in;
    public Transakcja(int []ilosc) {
        iloscNominalu=ilosc;
        in=new Scanner(System.in);
        startoweMonety();
    }
    private void startoweMonety(){
        for(int i=0;i<MAX_NOMINALOW;i++){
            for(int j=0;j<iloscNominalu[i];j++){
                monety[i][j]=new Gotowka(Gotowka.Nominaly.values()[i].zwrocCene(),Gotowka.Nominaly.values()[i].toString());
            }
        }
    }
    public boolean platnosc(double c){
        doZaplaty=c;
        System.out.println("Wybierz srodek platnosci:\n1.Bezgotowkowa\n2.Gotowkowa\nKażdy inny symbol aby anulować.");
        boolean czySiePowiodla;
        int i=in.nextInt();
        switch(i) {
            case 1: czySiePowiodla=transakcjaBezgotowkowa();
            break;
            case 2: czySiePowiodla=transakcjaGotowkowa();
            break;
            default:
                System.out.println("Wybrano zły numer!");
                czySiePowiodla=false;
        }
        return czySiePowiodla;
    }
    private abstract class Pieniadz {
       private double wartosc;
        public Pieniadz(double wartosc) {
            this.wartosc = wartosc;
        }

        public double getWartosc() {
            return wartosc;
        }

        public void setWartosc(double wartosc) {
            this.wartosc = wartosc;
        }
    }
    private boolean transakcjaGotowkowa(){
        int j=0;
        boolean czySiePowiodla=false;
        while(doZaplaty>0) {
            System.out.println("Włóż monetę. Pozostała kwota do zapłaty: " + doZaplaty);
            for(int i = 0; i< Gotowka.Nominaly.values().length; i++){
                System.out.println((i+1)+". "+ Gotowka.Nominaly.values()[i].toString());
            }
            j=in.nextInt();
            if(0<j&&j<=MAX_NOMINALOW) {
                monety[j - 1][iloscNominalu[j - 1]] = new Gotowka(Gotowka.Nominaly.values()[j - 1].zwrocCene(), Gotowka.Nominaly.values()[j - 1].toString());
                doZaplaty = (double) Math.round((doZaplaty - monety[j - 1][iloscNominalu[j - 1]].getWartosc()) * 100) / 100;
                j++;
                czySiePowiodla=true;
            }
        }
        if(doZaplaty<0){
            System.out.println("Poczekaj na wydanie reszty.");
            czySiePowiodla=wydajReszte(doZaplaty);
        }
        return czySiePowiodla;
    }
    private boolean wydajReszte(double doWydania){
        doWydania=Math.abs(doWydania);
        System.out.println("Do wydania: "+ doWydania);
        while(doWydania>0){
            double n=0;
            int indexNominalu=0;
            for(int i=0;i< Gotowka.Nominaly.values().length;i++){
                if(monety[i][iloscNominalu[i]-1].getWartosc()<=doWydania&&(monety[i][iloscNominalu[i]-1].getWartosc()>n)){
                    n=monety[i][iloscNominalu[i]-1].getWartosc();
                    indexNominalu=i;
                }
                if(iloscNominalu[i]==0){
                    return false;
                }
            }
            System.out.println("Wydano: "+monety[indexNominalu][iloscNominalu[indexNominalu]-1]);
            doWydania=(double)Math.round((doWydania-n)*100)/100;
            --iloscNominalu[indexNominalu];
        }
        return true;
    }

    private class Gotowka extends Pieniadz{
        private String nazwa;
        private enum Nominaly{
            _5gr(0.05,"5 groszy"),_10gr(0.1,"10 groszy"),_20gr(0.2,"20 groszy"),_50gr(0.5,"50 groszy"),_1zl(1,"1 złoty"),_2zl(2,"2 złote"),_5zl(5,"5 złotych");
            private double cena;
            private String nazwa;
            Nominaly(double i, String n){
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
        public Gotowka(double i,String n){
            super(i);
            nazwa=n;
        }
        @Override
        public String toString() {
            return nazwa;
        }
    }
    private boolean transakcjaBezgotowkowa(){
        if(iloscPrzelewow+1<MAX_PRZELEWOW) {
            historiaPrzelewow[iloscPrzelewow] = new Karta(doZaplaty);
            System.out.println(historiaPrzelewow[iloscPrzelewow]);
            iloscPrzelewow++;
            return true;
        }
        return false;
    }
    private class Karta extends Pieniadz {
        private int nrKonta;
        private final static int MAX=9999999;
        private final static int MIN=1000000;
        public Karta(double wartosc) {
            super(wartosc);
            nrKonta=(int)(Math.random()*(MAX-MIN))+MIN;
        }
        @Override
        public String toString(){
            return "Dokonano płatnosci z konta o numerze: '"+getNrKonta()+"' o wartosci: '"+getWartosc()+'\'';
        }
        public int getNrKonta() {
            return nrKonta;
        }
    }

}
