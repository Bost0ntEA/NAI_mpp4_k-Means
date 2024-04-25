import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static ArrayList<Irys> IrysList = new ArrayList<Irys>();
    private static ArrayList<Centroid> CentroidList = new ArrayList<>();
    private static  ArrayList<Centroid> CentroidListCopy = new ArrayList<>();
    public static int k;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("podaj k: ");
        k = scanner.nextInt();
        String file = "src/Resources/Iris_training.txt";
        readFile(file);
//        CentroidList = StworzListeCentroidow();
//        System.out.println(CentroidList);
        StworzListeCentroidow();
        CentroidListCopy=CentroidList;
//        System.out.println(CentroidList.size() + "ggdsffgfs");
//        System.out.println(CentroidListCopy);
        while (CentroidListCopy.isEmpty() || !IsCentroidArraysEqual(CentroidList,CentroidListCopy)){
            CentroidListCopy=CentroidList;
//            System.out.println("elo");
            krokGrupowania();

        }




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
        for (int i = 0; i < k; i++) {
            int el;
            while (true){
                el = random.nextInt(0, IrysList.size());
                if (!wyrzucone.contains(el)){
                    wyrzucone.add(el);
                    listaCentroidow.add(new Centroid(IrysList.get(el),i+1));
                    break;
                }
            }
        }
        return listaCentroidow;
    }

    /* do pamy dodaje odleglosc kazdego irysa do aktualnego cntroidu.
     Patrze pozniej ktora odleglosc jest najmniejsza i przypisuje temu irysowi taką grupe do jakiego centroidu jest najbliższy

    */
//    public static void krokGrupowania(){
//        Map<Centroid,Double> mapaCentroidDistance = new HashMap<>();
//        double minVal = -1;
//        int grupaPrzydzielana=0;
//        Double val;
//        for (int i = 0; i < IrysList.size(); i++) {
////            System.out.println("jestem" + i);
//            minVal = -1;
//            for (int j = 0; j < CentroidList.size(); j++) {
//                val = CentroidList.get(j).getDistance(IrysList.get(i));
//                System.out.println(val);
//                System.out.println(CentroidList.get(j));
//                mapaCentroidDistance.put(CentroidList.get(j), val);
//                System.out.println(mapaCentroidDistance);
//            }
//            for (Map.Entry<Centroid,Double> element : mapaCentroidDistance.entrySet()){
//                System.out.println("to sie wogóle wykonuje?");
//                if (minVal==-1){
//                    minVal = element.getValue();
//                    System.out.println("czy jestes tu");
//                }
//                if (element.getValue()<minVal){
//                    System.out.println("jednak jestem tu");
//                    minVal = element.getValue();
//                    grupaPrzydzielana = element.getKey().grupa;
//                }
//            }
////            System.out.println("doszedles?");
//            IrysList.get(i).setGrupa(grupaPrzydzielana);
//        }
////        System.out.println("przed dystansem");
//            printDistanceFromCentroids();
//
//        for (Centroid el : CentroidList){
//            el.getNewKoordynaty(IrysList);
//        }
//    }
    public static void krokGrupowania() {
        Map<Centroid, Double> mapaCentroidDistance = new HashMap<>();
        int grupaPrzydzielana = 0;

        for (Irys irys : IrysList) {
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

        printDistanceFromCentroids();

        for (Centroid centroid : CentroidList) {
            centroid.getNewKoordynaty(IrysList);
        }
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
                    System.out.println(el.getDistance(irys));
                }
            }
        }
    }


    private static void getEntrophyInfo(){
        Map<Centroid,Integer> mapaIlosci = new HashMap<>();
        for (Irys irys : IrysList){
            for (Centroid el : CentroidList){
                if (irys.grupa == el.grupa) {
                    if (mapaIlosci.containsKey(el)) {
                        int aktualnaIlosc = mapaIlosci.get(el);
                        mapaIlosci.put(el, aktualnaIlosc + 1);
                    } else {
                        mapaIlosci.put(el, 1);
                    }
                }
            }
        }
        double entropia=0;
        for (Map.Entry<Centroid,Integer> el : mapaIlosci.entrySet()){
            double value = (double) el.getValue() / IrysList.size()* Math.log(el.getValue())/Math.log(2);
            entropia+=value;
        }
        System.out.println(-entropia);

    }

}
