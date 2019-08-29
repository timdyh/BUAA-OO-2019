package poly;

import java.util.ArrayList;

import static poly.Main.headDeal;

public class Items {
    private ArrayList<Item> items = new ArrayList<>();

    public Items() {
    }

    public Items(String s) {
        String ss = headDeal(s);
        while (!ss.equals("")) {
            Item item = new Item(ss);
            items.add(item);
            ss = item.getSt();
        }
    }

    public String dri() {
        String pt = "";
        for (int i = 0; i < items.size(); i++) {
            pt = pt.concat(items.get(i).dri());
        }
        return "(" + pt + ")";
    }
}
