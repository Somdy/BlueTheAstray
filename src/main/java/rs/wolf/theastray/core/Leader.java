package rs.wolf.theastray.core;

import basemod.BaseMod;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.LMDebug;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.commands.CheatCMD;
import rs.wolf.theastray.commands.ManaCMD;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.utils.MsgLogger;
import rs.wolf.theastray.utils.TAUtils;
import rs.wolf.theastray.variables.TAExtraMagic;
import rs.wolf.theastray.variables.TAPromotion;

import java.nio.charset.StandardCharsets;

@SpireInitializer
@SuppressWarnings("unused")
public class Leader implements TAUtils, EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber,
        PostInitializeSubscriber, EditCharactersSubscriber {
    public static final String PREFIX = "astray";
    public static final Color TAColor = LMSK.Color(32, 178, 170);
    
    private static final String TA_BTN = "AstrayAssets/images/char/button.png";
    private static final String TA_PTR = "AstrayAssets/images/char/portrait.jpg";
    
    public static void initialize() {
        BaseMod.subscribe(new Leader());
    }
    
    public Leader() {
        CardMst.RegisterColors();
    }
    
    public static void Log(Object what) {
        LMDebug.deLog(Leader.class, "THE ASTRAY-[LOG]> " + what);
    }
    
    public static void PatchLog(Object what) {
        LMDebug.deLog(Leader.class, "THE ASTRAY-[LOG]> " + what);
    }
    
    @Override
    public void receiveEditStrings() {
        DataMst.Initialize();
        TALocalLoader.Initialize();
        String lang = getSupLang();
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "AstrayAssets/locals/" + lang + "/chars.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "AstrayAssets/locals/" + lang + "/powers.json");
    }
    
    @Override
    public void receiveEditKeywords() {
        String lang = getSupLang();
        Gson gson = new Gson();
        String keywordJson = Gdx.files.internal("AstrayAssets/locals/" + lang + "/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(keywordJson, Keyword[].class);
        assert keywords != null;
        MsgLogger.PreLoad("KEYWORDS LOADED");
        for (Keyword k : keywords) {
            addKeyword(k);
        }
        MsgLogger.End();
    }
    
    private void addKeyword(@NotNull Keyword k) {
        MsgLogger.Append("[" + k.NAMES[0] + "]");
        String keyName = k.NAMES[0];
        if (Settings.language == Settings.GameLanguage.ENG)
            for (int i = 0; i < k.NAMES.length; i++) {
                k.NAMES[i] = k.NAMES[i].toLowerCase();
            }
        BaseMod.addKeyword(PREFIX, keyName, k.NAMES, k.DESCRIPTION);
    }
    
    @Override
    public void receivePostInitialize() {
        ConsoleCommand.addCommand("bluemana", ManaCMD.class);
        ConsoleCommand.addCommand("bluecheat", CheatCMD.class);
    }
    
    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new TAPromotion());
        BaseMod.addDynamicVariable(new TAExtraMagic());
        CardMst.RegisterCards();
    }
    
    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new BlueTheAstray(), TA_BTN, TA_PTR, TACardEnums.BlueTheAstray);
    }
}