package vn.nextpay.nextshop.util;

import java.util.HashMap;

public class Param extends HashMap<String, String> {
    @Override
    public String get(Object key) {
        if(!super.containsKey(key)){
            super.put(key.toString(), "");
        }
        return super.getOrDefault(key, "t");
    }

    @Override
    public boolean containsKey(Object arg0) {
        return true;
    }
}
