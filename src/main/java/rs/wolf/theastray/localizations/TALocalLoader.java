package rs.wolf.theastray.localizations;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import rs.wolf.theastray.utils.TAUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TALocalLoader {
    private static final Map<String, TACardLocals> CardLocalMap = new HashMap<>();
    
    public static void Initialize() {
        Gson gson = new Gson();
        String lang = TAUtils.SupLang();
        
        String cardPath = "AstrayAssets/locals/" + lang + "/cards.json";
        String cardJson = loadJson(cardPath);
        TACardLocals[] cardLocals = gson.fromJson(cardJson, TACardLocals[].class);
        assert cardLocals != null;
        for (TACardLocals local : cardLocals) {
            CardLocalMap.put(local.ID, local);
        }
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
}