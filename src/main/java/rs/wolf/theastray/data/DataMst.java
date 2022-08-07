package rs.wolf.theastray.data;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import rs.wolf.theastray.utils.TAUtils;
import rs.wolf.theastray.utils.MsgLogger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DataMst {
    private static final Map<Integer, CardData> DataIndexMap = new HashMap<>();
    private static final Map<String, CardData> DataNameMap = new HashMap<>();
    
    public static void Initialize() {
        Gson gson = new Gson();
        
        String mainDataPath = "AstrayAssets/data/card_data_main.json";
        String dataJson = Gdx.files.internal(mainDataPath).readString(String.valueOf(StandardCharsets.UTF_8));
        LocalDataUnit[] dataUnits = gson.fromJson(dataJson, LocalDataUnit[].class);
        assert dataUnits != null;
        MsgLogger.PreLoad("CARD DATA LOADED");
        for (LocalDataUnit unit : dataUnits) {
            int index = Integer.parseInt(unit.id.substring(1));
            DataIndexMap.put(index, CardData.Format(unit));
            DataNameMap.put(unit.localname, CardData.Format(unit));
            MsgLogger.Append(index);
        }
        MsgLogger.End();
    }
    
    public static String ID(int index) {
        if (DataIndexMap.containsKey(index)) return DataIndexMap.get(index).getID();
        TAUtils.Log("UNDEFINED INDEX: [" + index + "]");
        return "UNDEFINED";
    }
    
    public static CardData Get(int index) {
        if (DataIndexMap.containsKey(index)) return DataIndexMap.get(index);
        TAUtils.Log("UNDEFINED INDEX: [" + index + "]");
        return CardData.MockingData();
    }
    
    public static String ID(String localname) {
        if (DataNameMap.containsKey(localname)) return DataNameMap.get(localname).getID();
        TAUtils.Log("UNDEFINED LOCAL NAME: [" + localname + "]");
        return "UNDEFINED";
    }
    
    public static CardData Get(String localname) {
        if (DataNameMap.containsKey(localname)) return DataNameMap.get(localname);
        TAUtils.Log("UNDEFINED LOCAL NAME: [" + localname + "]");
        return CardData.MockingData();
    }
}