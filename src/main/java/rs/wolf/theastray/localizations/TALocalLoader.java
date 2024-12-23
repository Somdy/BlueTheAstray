package rs.wolf.theastray.localizations;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAUtils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TALocalLoader {
    private static final Map<String, TACardLocals> CardLocalMap = new HashMap<>();
    private static Map<String, String> LegacyLocalMap = new HashMap<>();
    private static Map<String, TADialogDictLocals> DialogLocalMap = new HashMap<>();
    
    public static void Initialize() {
        Gson gson = new Gson();
        String lang = TAUtils.SupLang();
        
        String path = "AstrayAssets/locals/" + lang + "/cards.json";
        String json = loadJson(path);
        TACardLocals[] cardLocals = gson.fromJson(json, TACardLocals[].class);
        assert cardLocals != null;
        for (TACardLocals local : cardLocals) {
            if (local.DEPRECATED) continue;
            CardLocalMap.put(local.ID, local);
        }
        
        path = "AstrayAssets/locals/" + lang + "/legacies.json";
        json = loadJson(path);
        Type stringType = new TypeToken<Object>(){}.getType();
        LegacyLocalMap = gson.fromJson(json, stringType);
        
        path = "AstrayAssets/locals/" + lang + "/dialogdict.json";
        json = loadJson(path);
        Type dialogDictType = new TypeToken<Map<String, TADialogDictLocals>>(){}.getType();
        DialogLocalMap = gson.fromJson(json, dialogDictType);
    }
    
    private static String loadJson(String path) {
        if (!Gdx.files.internal(path).exists())
            TAUtils.Log("Localization does not exist: " + path);
        return Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
    }
    
    @NotNull
    public static TACardLocals CARD(String id) {
        if (id == null)
            return TACardLocals.GetMockingLocals();
        String trueID = TAUtils.RmPrefix(id);
        if (CardLocalMap.containsKey(trueID))
            return CardLocalMap.get(trueID);
        return TACardLocals.GetMockingLocals(trueID);
    }
    
    public static String LEGACY(String id) {
        if (id == null || !LegacyLocalMap.containsKey(id))
            return "No legacy";
        return LegacyLocalMap.get(id);
    }
    
    public static TADialogDictLocals DIALOG(String id) {
        if (id == null || !DialogLocalMap.containsKey(id))
            return DialogLocalMap.get("DEFAULT_CHARACTER");
        return DialogLocalMap.get(id);
    }
}