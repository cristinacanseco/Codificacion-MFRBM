package com.example.mfrbmv10.Imagen;

import java.util.ArrayList;
import java.util.Random;

public class CMeans {
    private int c;
    private PatronRepresentativo[] centroides;

    public CMeans(int c) {
        if(c>151) this.c=150;
        else this.c = c;
        this.centroides= new PatronRepresentativo[c];
    }
    public void entrenar(ArrayList<Patron> interfaces, int pos[]){
        for(int x=0; x<pos.length;x++){
            this.centroides[x]= new PatronRepresentativo(interfaces.get(pos[x]));
            this.getCentroides()[x].setClase(""+x);
        }
    }

    public void entrenar(ArrayList<Patron> interfaces) {
        Random ran = new Random();
        int pos = ran.nextInt(interfaces.size());
        this.centroides[0] = new PatronRepresentativo(interfaces.get(pos));
        this.getCentroides()[0].setClase(""+0);
        int x=1;

        while(x <  this.c){
            pos = ran.nextInt(interfaces.size());
            if (!Exist(interfaces.get(pos))){
                this.centroides[x]= new PatronRepresentativo(interfaces.get(pos));
                this.getCentroides()[x].setClase(""+x);
                x++;
                //System.out.println(x);
            }
        }
        // System.out.print("");
    }

    public void clasificar(ArrayList<Patron> patrones) {
        PatronRepresentativo[] nuevos;
        do{
            clasificacion(patrones);
            //recalculo centroides
            nuevos = reAjustarCentroides(patrones);

        }while(diferenciaCentroides(nuevos));
    }

    private boolean Exist(Patron get) {
        for (Patron centroide : this.getCentroides()) {
            if (centroide == null){break;}
            if (centroide.equals(get)){return true;}
        }
        return false;
    }

    private void clasificacion(ArrayList<Patron> patrones) {
        patrones.forEach((e) -> {
            double distC = calcularDistanciaEuclidiana(e, this.getCentroides()[0]);
            e.setClase(this.getCentroides()[0].getClase());
            for (int i=1; i<this.c;i++){
                double nDistancia = calcularDistanciaEuclidiana(e, this.getCentroides()[i]);
                if(distC>nDistancia){
                    distC=nDistancia;
                    e.setClase(this.getCentroides()[i].getClase());

                }
            }
        });
    }

    private PatronRepresentativo[] reAjustarCentroides(ArrayList<Patron> patrones) {
        PatronRepresentativo[] aux= new PatronRepresentativo[this.getCentroides().length];
        patrones.forEach((Patron wop) -> {
            int i = Integer.parseInt(wop.getClase());
            if(aux[i]==null){
                aux[i]= new PatronRepresentativo(wop);
                aux[i].setClase(""+i);
            }else{
                aux[i].acumular(wop);
            }
        });

        for(PatronRepresentativo act: aux){
            // System.out.println(act.toString());
            act.actualizar();
        }
        return aux;
    }

    private boolean diferenciaCentroides(PatronRepresentativo[] nuevos) {
        for(int x=0; x<nuevos.length;x++){
            if(!nuevos[x].equals(this.centroides[x])){
                this.centroides=nuevos.clone();
                return true;
            }
        }
        return false;
    }

    /**
     * @return the centroides
     */
    public PatronRepresentativo[] getCentroides() {
        return centroides;
    }

    public static double calcularDistanciaEuclidiana(Patron a, Patron b){
        double aux = 0;
        for (int x=0;x<a.getVector().length;x++){
            aux+=Math.pow((a.getVector()[x]-b.getVector()[x]),2);
        }
        return Math.sqrt(aux);
    }

}
