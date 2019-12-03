package model;
import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 10/17/16.
 */
public class PatternPrinter extends JerkSONParser {
    private List<java.util.HashMap<String, String>> list;
    public JerkSONParser parser;
    model.Main main;

    public  PatternPrinter(JerkSONParser parser) {
        super();

        list = new ArrayList<>();
        main = new model.Main();
        try {
            this.parser = parser;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printSheet() {
        parser.addAllItems();
        for(String key : parser.getItemList().keySet()) {
            this.printNameAndSeen(key, parser.countItem(key));
            this.printDoubleLines();

            java.util.List<java.util.HashMap<String, Integer>> pricesList = parser.getItemList().get(key);
            for(java.util.HashMap<String, Integer> map : pricesList) {
                for(String innerKey : map.keySet()) {
                    this.printPriceAndSeen(innerKey, parser.countPrice(key, innerKey));
                    this.printSingleLines();
                }
            }
        }
        this.printError(parser.getErrorCounter());
    }

    public void printDoubleLines() {
        System.out.println("==============\t\t============");
    }

    public void printSingleLines() {
        System.out.println("--------------\t\t------------");
    }

    public void printNameAndSeen(String name, int seen) {
        System.out.printf("name: %1s\t\t seen:%6d\n", name, seen);
    }

    public void printPriceAndSeen(String price, int seen) {
        System.out.printf("Price: %4s\t\t     seen:%6d\n", price, seen);
    }

    public void printSeen(int val) {
        System.out.printf("   seen:  %2d\n", val);
    }

    public void printError(int val) {
        System.out.printf("Errors:  %16d", val);
    }
}