import java.util.ArrayList;

class Centroid{
    public ArrayList<Double> koordynaty = new ArrayList<>();
    public int grupa;
    public Centroid(Irys irys,int grupa) {
        for (int i = 0; i < irys.getSizeOfWartosci(); i++) {
            koordynaty.add(irys.getWartosci().get(i));
        }
        this.grupa=grupa;
    }
    public void setKoordynaty(ArrayList<Double> tab){
        this.koordynaty=tab;
    }


    public Double getDistance(Irys irys){
        double dis = 0;
        for (int i = 0; i < koordynaty.size(); i++){
            dis += Math.pow(koordynaty.get(i) - irys.getWartosci().get(i),2);
        }
        return Math.sqrt(dis);
    }
    public void getNewKoordynaty(ArrayList<Irys> irysArray){
        ArrayList<Double> noweKoordynaty =new ArrayList<>();
        int count=0;
        for (Irys el : irysArray) {
            if (el.grupa==this.grupa) {
                for (int i = 0; i < el.wartosci.size(); i++) {
                    if (noweKoordynaty.isEmpty()){
                        for (int j = 0; j < el.wartosci.size(); j++) noweKoordynaty.add(0.0);
                    }else {
                        noweKoordynaty.set(i,noweKoordynaty.get(i)+el.wartosci.get(i));
                    }
                }
                count++;
            }
        }
        for (int i = 0; i < noweKoordynaty.size(); i++) {
            noweKoordynaty.set(i,noweKoordynaty.get(i) /count);
        }
        this.setKoordynaty(noweKoordynaty);
    }

    @Override
    public String toString() {
        String odp = "{ ";
        for (int i = 0; i < koordynaty.size(); i++) {
            if (i== koordynaty.size()-1){
                odp += koordynaty.get(i)+ "} ";
            }else{
            odp += koordynaty.get(i)+ "; ";
            }
        }
        odp += this.grupa;
        return odp;
    }
}