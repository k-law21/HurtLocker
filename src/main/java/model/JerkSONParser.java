package model;

import java.util.*;
import java.util.regex.*;

/**
 * Created by adam on 10/17/16.
 */
public class JerkSONParser {

    private List<String> list;
    private int errorCounter;
    private String text;
    private String delimiter;
    private Map<String, List<HashMap<String, Integer>>> itemList = new HashMap<>();

    private String foodRegex = "(?i)(c..kies|bread|milk|apples)*";
    private String priceRegex = "([0-9]\\.[0-9]*)*";
    private String typeRegex = "(Food)";
    private String dateRegex = "(\\d/\\d\\d/\\d\\d\\d\\d)";

    private String fullRegex = "(?i)(name)." + foodRegex + "(.)??(price)" + "(.)??" + priceRegex + "(.)??" + "(type)(.)??" + typeRegex + "(.)??" + "(expiration)(.)??" + dateRegex;
    private Pattern pattern = Pattern.compile(fullRegex);

    public JerkSONParser(String text, String delimiter) {
        this.text = text;
        this.delimiter = delimiter;
    }

    public JerkSONParser() {

    }


    public void parseToList() {
        this.fixCookieSpelling();
        this.fixBreadSpelling();
        this.fixMilkSpelling();
        this.fixApplesSpelling();
        this.list = new ArrayList<>(Arrays.asList(this.text.split(this.delimiter)));
    }

    public void fixCookieSpelling() {
        Pattern pattern = Pattern.compile("(?i)(c..kies)");
        Matcher matcher = pattern.matcher(text);
        this.text = matcher.replaceAll("Cookies");
    }

    public void fixBreadSpelling() {
        Pattern pattern = Pattern.compile("(?i)(bread)");
        Matcher matcher = pattern.matcher(text);
        this.text = matcher.replaceAll("Bread");
    }

    public void fixMilkSpelling() {
        Pattern pattern = Pattern.compile("(?i)(milk)");
        Matcher matcher = pattern.matcher(text);
        this.text = matcher.replaceAll("Milk");
    }

    public void fixApplesSpelling() {
        Pattern pattern = Pattern.compile("(?i)(apples)");
        Matcher matcher = pattern.matcher(text);
        this.text = matcher.replaceAll("Apples");
    }

    public void addItemToMap(String line) {
        Matcher m = pattern.matcher(line);
        while (m.find()) {
            if (m.group(2) == null) {
                this.errorCounter++;
                continue;
            }
            if (itemList.containsKey(m.group(2))) {
                this.errorCounter++;
                if (m.group(6) == null) {
                    continue;
                }
                List<HashMap<String, Integer>> tempPrices = itemList.get(m.group(2));
                for (HashMap<String, Integer> map : tempPrices) {
                    if (map.containsKey(m.group(6))) {
                        map.put(m.group(6), map.get(m.group(6)) + 1);
                        return;
                    }
                }
                itemList.get(m.group(2)).add(new HashMap<>());
                itemList.get(m.group(2)).get(itemList.get(m.group(2)).size() - 1).put(m.group(6), 1);
            }
            else {
                itemList.put(m.group(2), new ArrayList<>());
                itemList.get(m.group(2)).add(new HashMap<>());
                itemList.get(m.group(2)).get(itemList.get(m.group(2)).size() - 1).put(m.group(6), 1);
            }
        }
    }

    public int countItem(String key) {
        int counter = 0;
        List<HashMap<String, Integer>> temp = this.itemList.get(key);
        for (HashMap<String, Integer> map : temp) {
            for (Integer val : map.values()) {
                counter += val;
            }
        }
        return counter;
    }

    public int countPrice(String name, String price) {
        List<HashMap<String, Integer>> temp = this.itemList.get(name);
        for (HashMap<String, Integer> map : temp) {
            if (map.containsKey(price)) {
                return map.get(price);
            }
        }
        return -99;
    }

    public void addAllItems() {
        this.parseToList();
        for (String line : this.list) {
            this.addItemToMap(line);
        }
    }


    public List<String> getList() {
        return this.list;
    }

    public String getText() {
        return this.text;
    }

    public Map<String, List<HashMap<String, Integer>>> getItemList() {
        return this.itemList;
    }

    public int getErrorCounter() {
        return this.errorCounter;
    }
}
