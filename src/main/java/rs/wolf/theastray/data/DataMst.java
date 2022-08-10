package rs.wolf.theastray.data;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import rs.wolf.theastray.utils.TAUtils;
import rs.wolf.theastray.utils.MsgLogger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DataMst {
    private static final Map<Integer, CardData> CardDataIndexMap = new HashMap<>();
    private static final Map<String, CardData> CardDataNameMap = new HashMap<>();
    private static final Map<Integer, RelicData> RelicDataIndexMap = new HashMap<>();
    private static final Map<String, RelicData> RelicDataNameMap = new HashMap<>();
    
    public static void Initialize() {
        Gson gson = new Gson();
        String mainDataPath = "AstrayAssets/data/card_data_main.json";
        String dataJson = Gdx.files.internal(mainDataPath).readString(String.valueOf(StandardCharsets.UTF_8));
        {
            LocalCardDataUnit[] dataUnits = gson.fromJson(dataJson, LocalCardDataUnit[].class);
            assert dataUnits != null;
            MsgLogger.PreLoad("CARD DATA LOADED");
            for (LocalCardDataUnit unit : dataUnits) {
                int index = Integer.parseInt(unit.id.substring(1));
                CardData data = CardData.Format(unit);
                CardDataIndexMap.put(index, data);
                CardDataNameMap.put(unit.localname, data);
                MsgLogger.Append(index);
            }
            MsgLogger.End();
        }
        mainDataPath = "AstrayAssets/data/relic_data_main.json";
        dataJson = Gdx.files.internal(mainDataPath).readString(String.valueOf(StandardCharsets.UTF_8));
        {
            LocalRelicDataUnit[] dataUnits = gson.fromJson(dataJson, LocalRelicDataUnit[].class);
            assert dataUnits != null;
            MsgLogger.PreLoad("RELIC DATA LOADED");
            for (LocalRelicDataUnit unit : dataUnits) {
                int index = Integer.parseInt(unit.id.substring(5));
                RelicData data = RelicData.Format(unit);
                RelicDataIndexMap.put(index, data);
                RelicDataNameMap.put(unit.localname, data);
                MsgLogger.Append(index);
            }
            MsgLogger.End();
        }
    }
    
    public static String CardID(int index) {
        if (CardDataIndexMap.containsKey(index)) return CardDataIndexMap.get(index).getCardID();
        TAUtils.Log("UNDEFINED CARD INDEX: [" + index + "]");
        return "UNDEFINED";
    }
    
    public static String CardID(String localname) {
        if (CardDataNameMap.containsKey(localname)) return CardDataNameMap.get(localname).getCardID();
        TAUtils.Log("UNDEFINED CARD LOCAL NAME: [" + localname + "]");
        return "UNDEFINED";
    }
    
    public static CardData GetCardData(int index) {
        if (CardDataIndexMap.containsKey(index)) return CardDataIndexMap.get(index);
        TAUtils.Log("UNDEFINED CARD INDEX: [" + index + "]");
        return CardData.MockingData();
    }
    
    public static CardData GetCardData(String localname) {
        if (CardDataNameMap.containsKey(localname)) return CardDataNameMap.get(localname);
        TAUtils.Log("UNDEFINED CARD LOCAL NAME: [" + localname + "]");
        return CardData.MockingData();
    }
    
    public static String RelicID(int index) {
        if (RelicDataIndexMap.containsKey(index)) return RelicDataIndexMap.get(index).getRelicID();
        TAUtils.Log("UNDEFINED RELIC INDEX: [" + index + "]");
        return "UNDEFINED";
    }
    
    public static String RelicID(String localname) {
        if (RelicDataNameMap.containsKey(localname)) return RelicDataNameMap.get(localname).getRelicID();
        TAUtils.Log("UNDEFINED RELIC LOCAL NAME: [" + localname + "]");
        return "UNDEFINED";
    }
    
    public static RelicData GetRelicData(int index) {
        if (RelicDataIndexMap.containsKey(index)) return RelicDataIndexMap.get(index);
        TAUtils.Log("UNDEFINED RELIC INDEX: [" + index + "]");
        return RelicData.MockingData();
    }
    
    public static RelicData GetRelicData(String localname) {
        if (RelicDataNameMap.containsKey(localname)) return RelicDataNameMap.get(localname);
        TAUtils.Log("UNDEFINED RELIC LOCAL NAME: [" + localname + "]");
        return RelicData.MockingData();
    }
}