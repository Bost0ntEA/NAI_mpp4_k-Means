import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static ArrayList<Irys> IrysList = new ArrayList<Irys>();
    private static ArrayList<Centroid> CentroidList = new ArrayList<>();
//    private static  ArrayList<Centroid> CentroidListCopy = new ArrayList<>();
    public static int k;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("podaj k: ");
        k = scanner.nextInt();
        String file = "src/Resources/Iris_training.txt";
        readFile(file);
//        CentroidList = StworzListeCentroidow();
//        System.out.println(CentroidList);
        CentroidList = StworzListeCentroidow();
        int cos = 0;
        while (krokGrupowania()){
            printDistanceFromCentroids();
        }
        dispalyAllClusters();
        getEntrophyInfo();

    }

    public static void readFile(String fname){
        try(BufferedReader bf = new BufferedReader(new FileReader(fname))) {
            String line;
            String[] lineTab;
            while ((line = bf.readLine()) !=null){
                lineTab = line.replaceFirst(" ","")
                        .replaceAll("\\s+"," ")
                        .replaceAll(",",".")
                        .split(" ");
                Irys tmp = new Irys(lineTab,k);
//                System.out.println(tmp);
                IrysList.add(tmp);
//                System.out.println(tmp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static ArrayList<Centroid> StworzListeCentroidow(){
        ArrayList<Integer> wyrzucone = new ArrayList<>();
        Random random = new Random();
        ArrayList<Centroid> listaCentroidow =  new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            int el;
            while (true){
                el = random.nextInt(0, IrysList.size());
                if (!wyrzucone.contains(el)){
//                    System.out.println(el + " moja wartosc random");
                    wyrzucone.add(el);
                    listaCentroidow.add(new Centroid(IrysList.get(el),i));
                    break;
                }
            }
        }
        return listaCentroidow;
    }

    /* do pamy dodaje odleglosc kazdego irysa do aktualnego cntroidu.
     Patrze pozniej ktora odleglosc jest najmniejsza i przypisuje temu irysowi taką grupe do jakiego centroidu jest najbliższy

    */
    public static boolean krokGrupowania() {
        boolean zmiana = false;
        Map<Centroid, Double> mapaCentroidDistance = new HashMap<>();
        int grupaPrzydzielana = 0;

        for (Irys irys : IrysList) {
            mapaCentroidDistance.clear();
            for (Centroid centroid : CentroidList) {
                double distance = centroid.getDistance(irys);
                mapaCentroidDistance.put(centroid, distance);
            }

            double minVal = -1;
            for (Map.Entry<Centroid, Double> entry : mapaCentroidDistance.entrySet()) {
                Centroid centroid = entry.getKey();
                double distance = entry.getValue();

                if (minVal == -1 || distance < minVal) {
                    minVal = distance;
                    grupaPrzydzielana = centroid.grupa;
                }
            }
            irys.setGrupa(grupaPrzydzielana);
        }

        ArrayList<Centroid> kopiaCentroidow = CentroidList;

        for (Centroid centroid : CentroidList){
            if (centroid.getNewKoordynaty(IrysList)){
                zmiana=true;
            }
//            System.out.println(centroid.koordynaty + "kordy stare");

        }


        return zmiana;
    }

    private static boolean IsCentroidArraysEqual(ArrayList<Centroid> p1, ArrayList<Centroid> p2 ){
        if (p1.size() != p2.size()){
            return false;
        }
        for (Centroid centroid:p1){
            for (int i = 0; i < centroid.koordynaty.size(); i++) {
                if (p1.get(i) !=p2.get(i)){
                    return false;
                }
            }
        }
        return true;
    }
    private static void printDistanceFromCentroids(){
        for (Centroid el : CentroidList){
            for (Irys irys:IrysList){
                if (irys.grupa==el.grupa){
                    System.out.println(el.getDistance(irys) +": grupa to :" +el.grupa);
                }
            }
        }
    }


    private static void getEntrophyInfo(){
        Map<Centroid,IleCzego> mapaIlosci = new HashMap<>();
        for (Irys irys : IrysList){
            for (Centroid el : CentroidList){
                if (irys.grupa == el.grupa) {
                    if (mapaIlosci.containsKey(el)) {
                        IleCzego aktualnaIlosc = mapaIlosci.get(el);
                        switch (irys.kwiatek){
                            case"Iris-setosa":
                                aktualnaIlosc.addSetosa();
                                break;
                            case "Iris-versicolor":
                                aktualnaIlosc.addVersicolor();
                                break;
                            case "Iris-virginica":
                                aktualnaIlosc.addVirginica();
                                break;
                        }
                    } else {
                        mapaIlosci.put(el, new IleCzego());
                        IleCzego aktualnaIlosc = mapaIlosci.get(el);
                        switch (irys.kwiatek){
                            case"Iris-setosa":
                                aktualnaIlosc.addSetosa();
                                break;
                            case "Iris-versicolor":
                                aktualnaIlosc.addVersicolor();
                                break;
                            case "Iris-virginica":
                                aktualnaIlosc.addVirginica();
                                break;
                        }
                    }
                }
            }
        }
        double entropia=0;
        for (Map.Entry<Centroid,IleCzego> el : mapaIlosci.entrySet()){
            System.out.println(el.getValue().virginica);
            System.out.println(el.getValue().versicolor);
            System.out.println(el.getValue().setosa);
            System.out.println(el.getValue().ileMaks);
            double ulamekSetosa = (double) el.getValue().setosa/el.getValue().ileMaks;
            double ulamekVersicolor = (double) el.getValue().versicolor/el.getValue().ileMaks;
            double ulamekVirginica = (double) el.getValue().virginica/el.getValue().ileMaks;

            if (ulamekSetosa==0){
                entropia+=0;
            }else {
                entropia+= (ulamekSetosa*Math.log(ulamekSetosa));
            }


            if (ulamekVersicolor==0){
                entropia+=0;
            }else {
                entropia+= (ulamekVersicolor*Math.log(ulamekVersicolor));
            }


            if (ulamekVirginica==0){
                entropia+=0;
            }else {
                entropia+= (ulamekVirginica*Math.log(ulamekVirginica));
            }
//            System.out.println(Math.log(ulamekVirginica));
//            System.out.println(Math.log(ulamekSetosa));
//            System.out.println(Math.log(ulamekVersicolor));






            System.out.println("entropia dla centroidu: " + el.getKey().grupa + " to wartosci " + entropia*-1);
        }
        System.out.println("entropia ogólna " + entropia*-1);

    }

    private static void dispalyAllClusters(){
        for (Centroid centroid: CentroidList) {
            System.out.println("centroid grupa :" + centroid.grupa + "\n \t { \n");
            for (Irys irys: IrysList) {
                if (centroid.grupa==irys.grupa){
                    System.out.println("irys: " + irys);
                }

            }
            System.out.println("\n \t } \n");
        }
    }

}

