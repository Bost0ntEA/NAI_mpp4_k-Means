import java.util.ArrayList;
import java.util.Random;

public class Irys {
    protected ArrayList<Double> wartosci=new ArrayList<>();
    protected int grupa;
    public Irys(String[] dane,int k){
        for (int i = 0; i < dane.length; i++) {
            try {
                wartosci.add(Double.valueOf(dane[i]));
            }catch (NumberFormatException e){
                Random random = new Random();
                int j = random.nextInt(k)+1;
                this.grupa=j;
            }
        }
    }

    public ArrayList<Double> getWartosci() {
        return wartosci;
    }
    public int getSizeOfWartosci(){
        return wartosci.size();
    }

    public int getGrupa() {
        return grupa;
    }

    @Override
    public String toString() {
        String info = "{";
        for (int i = 0; i < wartosci.size(); i++) {
            info += i!= wartosci.size()-1 ? wartosci.get(i) +"; ":wartosci.get(i)+"} ";
        }
        info += grupa;
        return info;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }
}
