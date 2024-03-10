package rs.wolf.theastray.data.saveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.wolf.theastray.core.Leader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataObject {
    private HashMap<String, String> dataMap;
    
    public DataObject() {
        dataMap = new HashMap<>();
    }
    
    public String save() {
        Leader.Log("Saving data map: \n" + dataMap.toString());
        Gson gson = new GsonBuilder().create();
        return gson.toJson(dataMap, new TypeToken<Map<String, String>>(){}.getType());
    }
    
    public void load(String save) {
        Gson gson = new GsonBuilder().create();
        dataMap.putAll(gson.fromJson(save, new TypeToken<Map<String, String>>(){}.getType()));
        if (dataMap != null && !dataMap.isEmpty()) {
            dataMap.forEach((k, v) -> Leader.Log("{" + k + ", " + v + "} loaded"));
        } else {
            Leader.Log("Failed to load save data");
        }
    }
    
    public void putValue(String key, Object value) {
        String boolValue = String.valueOf(value);
        if (dataMap.containsKey(key)) printReplace(key, boolValue);
        dataMap.put(key, boolValue);
        Leader.Log("Associated the key [" + key + "] with value [" + value + "]");
    }
    
    public boolean getBool(String key) {
        if (dataMap.containsKey(key))
            return Boolean.parseBoolean(dataMap.get(key));
        Leader.Log("No value associated the key [" + key + "], returning false");
        return false;
    }
    
    public void clear() {
        dataMap.clear();
    }
    
    private void printReplace(String key, Object newValue) {
        Leader.Log("Replacing save data [" + key + "] with new value [" + newValue + "]");
    }
}