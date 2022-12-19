package rs.wolf.theastray.data.saveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import rs.wolf.theastray.core.Leader;

import java.util.HashMap;
import java.util.Map;

public class DataObject {
    private Map<String, String> dataMap;
    
    public DataObject() {
        dataMap = new HashMap<>();
    }
    
    public String save() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(dataMap, new TypeToken<Map<String, String>>(){}.getType());
    }
    
    public void load(String save) {
        Gson gson = new GsonBuilder().create();
        Map<String, String> map = gson.fromJson(save, new TypeToken<Map<String, String>>(){}.getType());
        if (map != null) {
            map.forEach((k, v) -> Leader.Log("{" + k + ", " + v + "} loaded"));
        } else {
            Leader.Log("Failed to load save data");
        }
    }
    
    public void putValue(String key, Object value) {
        String boolValue = String.valueOf(value);
        if (dataMap.containsKey(key)) printReplace(key, boolValue);
        dataMap.put(key, boolValue);
    }
    
    public boolean getBool(String key) {
        if (dataMap.containsKey(key))
            return Boolean.parseBoolean(dataMap.get(key));
        return false;
    }
    
    private void printReplace(String key, Object newValue) {
        Leader.Log("Replacing save data [" + key + "] with new value [" + newValue + "]");
    }
}