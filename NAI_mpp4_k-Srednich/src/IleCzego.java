public class IleCzego {
    public Integer ileMaks;
    public Integer setosa;
    public Integer virginica;
    public Integer versicolor;

    public IleCzego() {
        this.ileMaks = 0;
        this.setosa = 0;
        this.virginica = 0;
        this.versicolor = 0;
    }
    public void addSetosa(){
        ileMaks++;
        setosa++;
    }
    public void addVirginica(){
        ileMaks++;
        virginica++;
    }
    public void addVersicolor(){
        ileMaks++;
        versicolor++;
    }

}
