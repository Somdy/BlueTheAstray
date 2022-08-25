package rs.wolf.theastray.core;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.LMDebug;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.commands.CheatCMD;
import rs.wolf.theastray.commands.ManaCMD;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.relics.Relic1;
import rs.wolf.theastray.utils.MsgLogger;
import rs.wolf.theastray.utils.TAUtils;
import rs.wolf.theastray.variables.TAExtraMagic;
import rs.wolf.theastray.variables.TAPromotion;

import java.nio.charset.StandardCharsets;

@SpireInitializer
@SuppressWarnings("unused")
public class Leader implements TAUtils, EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber,
        PostInitializeSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, PostPowerApplySubscriber {
    public static final String MOD_ID = "BlueTheAstray";
    public static final String PREFIX = "astray";
    public static final Color TAColor = LMSK.Color(32, 178, 170);
    
    private static final String TA_BTN = "AstrayAssets/images/char/button.png";
    private static final String TA_PTR = "AstrayAssets/images/char/portrait.jpg";
    
    public static boolean SHOW_OVERDRAWN_CARDS = true;
    
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
        BaseMod.loadCustomStringsFile(RelicStrings.class, "AstrayAssets/locals/" + lang + "/relics.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "AstrayAssets/locals/" + lang + "/ui.json");
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
    
    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_ID)
                .packageFilter(Relic1.class)
                .any(AstrayRelic.class, (i, r) -> {
                    BaseMod.addRelicToCustomPool(r.makeCopy(), TACardEnums.TA_CardColor);
                    TAUtils.Log("[" + r.name + "] added");
                });
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature t, AbstractCreature s) {
        if (t instanceof AbstractPlayer) {
            AbstractPlayer player = LMSK.Player();
            for (AbstractCard card : player.drawPile.group) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).onPlayerReceivePower(p, s);
            }
            for (AbstractCard card : player.hand.group) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).onPlayerReceivePower(p, s);
            }
            for (AbstractCard card : player.discardPile.group) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).onPlayerReceivePower(p, s);
            }
        }
        if (s instanceof AbstractPlayer) {
            AbstractPlayer player = LMSK.Player();
            for (AbstractCard card : player.drawPile.group) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).onPlayerApplyPower(p, t);
            }
            for (AbstractCard card : player.hand.group) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).onPlayerApplyPower(p, t);
            }
            for (AbstractCard card : player.discardPile.group) {
                if (card instanceof AstrayCard)
                    ((AstrayCard) card).onPlayerApplyPower(p, t);
            }
        }
    }
}