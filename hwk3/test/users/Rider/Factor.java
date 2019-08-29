import java.util.ArrayList;

interface Factor {
    /*
    Factor(String factor) {
        this.factor = factor;
    }*/
    ArrayList<Factor> derivate();
    
    StringBuffer print();
}
