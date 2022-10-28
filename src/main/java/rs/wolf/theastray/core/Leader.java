package rs.wolf.theastray.core;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.LMDebug;
import rs.lazymankits.LManager;
import rs.lazymankits.interfaces.OnMakingCardInCombatSubscriber;
import rs.lazymankits.interfaces.OnShuffleSubscriber;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.abstracts.AstrayPower;
import rs.wolf.theastray.abstracts.AstrayRelic;
import rs.wolf.theastray.characters.BlueTheAstray;
import rs.wolf.theastray.commands.Cheat;
import rs.wolf.theastray.commands.CheatCMD;
import rs.wolf.theastray.commands.ManaCMD;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.relics.Relic1;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.utils.MsgLogger;
import rs.wolf.theastray.utils.TAImageMst;
import rs.wolf.theastray.utils.TAUtils;
import rs.wolf.theastray.variables.TAExtNeed;
import rs.wolf.theastray.variables.TAExtraMagic;
import rs.wolf.theastray.variables.TAPromotion;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpireInitializer
@SuppressWarnings("unused")
public class Leader implements TAUtils, EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber,
        PostInitializeSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, PostPowerApplySubscriber, 
        OnPlayerLoseBlockSubscriber, OnPlayerTurnStartSubscriber, PostBattleSubscriber, OnStartBattleSubscriber, 
        OnShuffleSubscriber, OnMakingCardInCombatSubscriber, PostCreateStartingRelicsSubscriber {
    public static final String MOD_ID = "BlueTheAstray";
    public static final String PREFIX = "astray";
    public static final Color TAColor = LMSK.Color(32, 178, 170);
    
    private static final String TA_BTN = "AstrayAssets/images/char/button.png";
    private static final String TA_PTR = "AstrayAssets/images/char/portrait.jpg";
    
    public static boolean SHOW_OVERDRAWN_CARDS = true;
    
    public static void initialize() {
        Leader instance = new Leader();
        BaseMod.subscribe(instance);
        LManager.Sub(instance);
    }
    
    public Leader() {
        CardMst.RegisterColors();
    }
    
    public static void Log(String what) {
        LMDebug.deLog(Leader.class, "THE ASTRAY-[LOG]> " + what);
    }
    
    public static void devLog(String what) {
        if (Cheat.IsCheating(Cheat.SSD)) {
            if (!DevConsole.visible)
                DevConsole.visible = true;
            DevConsole.log(what);
            Log(what);
        }
    }
    
    public static void PatchLog(String what) {
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
        BaseMod.loadCustomStringsFile(MonsterStrings.class, "AstrayAssets/locals/" + lang + "/monsters.json");
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
        TAImageMst.Initialize();
        ConsoleCommand.addCommand("bluemana", ManaCMD.class);
        ConsoleCommand.addCommand("bluecheat", CheatCMD.class);
        BaseMod.addMonster("BlueTheBoss", () -> new MonsterGroup(new AbstractMonster[]{
                new BlueTheBoss(0F, 0F)
        }));
        MsgLogger.Log();
    }
    
    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new TAPromotion());
        BaseMod.addDynamicVariable(new TAExtraMagic());
        BaseMod.addDynamicVariable(new TAExtNeed());
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
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
                    UnlockTracker.markRelicAsSeen(r.relicId);
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
    
    @Override
    public int receiveOnPlayerLoseBlock(int block) {
        float tmp = block;
        AbstractPlayer player = LMSK.Player();
        for (AbstractPower p : player.powers) {
            if (p instanceof AstrayPower)
                tmp = ((AstrayPower) p).onPlayerLosingBlock(tmp);
        }
        if (tmp < 0) tmp = 0F;
        block = MathUtils.floor(tmp);
        return block;
    }
    
    @Override
    public void receiveOnPlayerTurnStart() {
        CardMst.DeMagicPlayedThisTurn.clear();
    }
    
    @Override
    public void receivePostBattle(AbstractRoom r) {
        AbstractPlayer p = LMSK.Player();
        for (AbstractCard card : p.masterDeck.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onVictory();
        }
        
        CardMst.DeMagicPlayedThisCombat.clear();
        GlobalManaMst.ClearPostBattle();
    }
    
    @Override
    public void receiveOnBattleStart(AbstractRoom r) {
        AbstractPlayer p = LMSK.Player();
        for (AbstractCard card : p.masterDeck.group) {
            if (card instanceof AstrayCard)
                ((AstrayCard) card).onBattleStart();
        }
    }
    
    @Override
    public void receiveOnShuffle() {
        AbstractPlayer p = LMSK.Player();
        for (AbstractPower power : p.powers) {
            if (power instanceof AstrayPower)
                ((AstrayPower) power).onShuffle();
        }
    }
    
    @Override
    public void receiveOnMakingCardInCombat(AbstractCard card, CardGroup destination) {
        AbstractPlayer p = LMSK.Player();
        for (AbstractPower power : p.powers) {
            if (power instanceof AstrayPower)
                ((AstrayPower) power).onMakingCardInCombat(card, destination);
        }
    }
    
    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relicList) {
        relicList.add("astray:RelicTest1");
    }
}