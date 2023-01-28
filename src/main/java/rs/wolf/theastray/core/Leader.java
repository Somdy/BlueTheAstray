package rs.wolf.theastray.core;

import basemod.*;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import rs.wolf.theastray.data.saveData.DataObject;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.relics.Relic1;
import rs.wolf.theastray.utils.*;
import rs.wolf.theastray.variables.TAExtNeed;
import rs.wolf.theastray.variables.TAExtraMagic;
import rs.wolf.theastray.variables.TAPromotion;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@SpireInitializer
@SuppressWarnings("unused")
public class Leader implements TAUtils, CustomSavable<String>, EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber,
        PostInitializeSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, PostPowerApplySubscriber, OnPlayerLoseBlockSubscriber, 
        OnPlayerTurnStartSubscriber, PostBattleSubscriber, OnStartBattleSubscriber, OnShuffleSubscriber, OnMakingCardInCombatSubscriber, 
        PostCreateStartingRelicsSubscriber, AddAudioSubscriber, SetUnlocksSubscriber {
    public static final String MOD_ID = "BlueTheAstray";
    public static final String PREFIX = "astray";
    public static final String[] AUTHORS = {"Little wolf影幽"};
    public static final String DESCRIPTION = "An original character";
    public static final Color TAColor = LMSK.Color(32, 178, 170);
    
    private static final String TA_BTN = "AstrayAssets/images/char/button.png";
    private static final String TA_PTR = "AstrayAssets/images/char/portrait.jpg";
    private static final String TA_PTR_AFTER = "AstrayAssets/images/char/portrait_after.jpg";
    
    public static DataObject SaveData = new DataObject();
    
    public static boolean ALLOW_RELICS = true;
    public static boolean ALLOW_EPISODIC_EVENTS = true;
    
    public static boolean DEFEATED_HEART = false;
    
    public static void initialize() {
        Leader instance = new Leader();
        BaseMod.subscribe(instance);
        LManager.Sub(instance);
        BaseMod.addSaveField(MOD_ID, instance);
        SpireConfig config = makeConfig();
        loadProperties(config);
    }
    
    public Leader() {
        CardMst.RegisterColors();
    }
    
    @Nullable
    private static SpireConfig makeConfig() {
        Properties properties = new Properties();
        properties.setProperty("ALLOW_RELICS", Boolean.toString(ALLOW_RELICS));
        properties.setProperty("ALLOW_EPISODIC_EVENTS", Boolean.toString(ALLOW_EPISODIC_EVENTS));
        properties.setProperty("DEFEATED_HEART", Boolean.toString(DEFEATED_HEART));
        
        try {
            return new SpireConfig("BlueTheAstray", "BlueTheAstrayConfig", properties);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void loadProperties(SpireConfig config) {
        if (config == null) {
            Log("Missing rs.lunarshop.config file");
            return;
        }
        ALLOW_RELICS = config.getBool("ALLOW_RELICS");
        ALLOW_EPISODIC_EVENTS = config.getBool("ALLOW_EPISODIC_EVENTS");
        DEFEATED_HEART = config.getBool("DEFEATED_HEART");
    }
    
    private static void save(SpireConfig config) {
        if (config == null) return;
        try {
            config.save();
        } catch (Exception e) {
            Log("Failed to save current rs.lunarshop.config file");
        }
    }
    
    public static void SaveConfig() {
        SpireConfig config = makeConfig();
        assert config != null;
        config.setBool("ALLOW_RELICS", ALLOW_RELICS);
        config.setBool("ALLOW_EPISODIC_EVENTS", ALLOW_EPISODIC_EVENTS);
        config.setBool("DEFEATED_HEART", DEFEATED_HEART);
        save(config);
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
    public String onSave() {
        return SaveData.save();
    }
    
    @Override
    public void onLoad(String s) {
        SaveData.load(s);
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
        
        makeModPanels();
        
        BaseMod.addMonster("BlueTheBoss", () -> new MonsterGroup(new AbstractMonster[]{
                new BlueTheBoss(0F, 0F)
        }));
//        BaseMod.addBoss(TheEnding.ID, BlueTheBoss.ID, "AstrayAssets/images/ui/map/boss/bluetheboss_icon.png", 
//                "AstrayAssets/images/ui/map/boss/bluetheboss_outline.png");
        MsgLogger.Log();
    }
    
    private static void makeModPanels() {
        ModPanel settings = new ModPanel();
        ModLabeledToggleButton allowRelics = new ModLabeledToggleButton("允许其他职业遇见蓝新增的遗物",
                380F, 720F, Color.WHITE.cpy(), FontHelper.charDescFont, ALLOW_RELICS, settings, (l) -> {},
                (btn) -> {
                    ALLOW_RELICS = btn.enabled;
                    SpireConfig config = makeConfig();
                    config.setBool("ALLOW_RELICS", ALLOW_RELICS);
                    save(config);
                });
        ModLabeledToggleButton allowEvents = new ModLabeledToggleButton("允许其他职业遇见蓝的剧情事件",
                380F, 680F, Color.WHITE.cpy(), FontHelper.charDescFont, ALLOW_EPISODIC_EVENTS, settings, (l) -> {},
                (btn) -> {
                    ALLOW_EPISODIC_EVENTS = btn.enabled;
                    SpireConfig config = makeConfig();
                    config.setBool("ALLOW_EPISODIC_EVENTS", ALLOW_EPISODIC_EVENTS);
                    save(config);
                });
        settings.addUIElement(allowRelics);
//        settings.addUIElement(allowEvents);
        BaseMod.registerModBadge(TAImageMst.BADGE, MOD_ID, Arrays.toString(AUTHORS), DESCRIPTION, settings);
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
        BaseMod.addCharacter(new BlueTheAstray(), TA_BTN, GetPortrait(), TACardEnums.BlueTheAstray);
    }
    
    public static String GetPortrait() {
        return DEFEATED_HEART ? TA_PTR_AFTER : TA_PTR;
    }
    
    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_ID)
                .packageFilter(Relic1.class)
                .any(AstrayRelic.class, (i, r) -> {
                    BaseMod.addRelic(r.makeCopy(), RelicType.SHARED);
//                    UnlockTracker.markRelicAsSeen(r.relicId);
                    TAUtils.Log("[" + r.name + "] added");
                });
    }
    
    public static void PreOnCreatureDamage(AbstractCreature target, DamageInfo info) {
        for (AbstractPower p : target.powers) {
            if (p instanceof AstrayPower)
                ((AstrayPower) p).preModifyDamage(info);
        }
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
        for (AbstractCard c : LMSK.Player().hand.group) {
            if (c instanceof AstrayCard)
                ((AstrayCard) c).onPlayerTurnStart();
        }
        for (AbstractCard c : LMSK.Player().drawPile.group) {
            if (c instanceof AstrayCard)
                ((AstrayCard) c).onPlayerTurnStart();
        }
        for (AbstractCard c : LMSK.Player().discardPile.group) {
            if (c instanceof AstrayCard)
                ((AstrayCard) c).onPlayerTurnStart();
        }
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
//        relicList.add("astray:RelicTest1");
    }
    
    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(TAUtils.MakeID("FALLING_STAR"), "AstrayAssets/audio/sfx/FALLING_STAR_SFX.ogg");
        BaseMod.addAudio(TAUtils.MakeID("FALLING_STAR_ON_HIT"), "AstrayAssets/audio/sfx/FALLING_STAR_ON_HIT_SFX.ogg");
    }
    
    @Override
    public void receiveSetUnlocks() {
        addUnlockBundle(0, "空明刃", "未来已定", "流星雨");
        addRelicUnlockBundle(1, "爆裂冰袋", "星轮之书", "饕餮的胃袋");
        addUnlockBundle(2, "立场堆叠", "灵界之眼", "破碎晶体");
        addRelicUnlockBundle(3, "历练者徽章", "水晶魔方", "奇迹魔杖");
        addUnlockBundle(4, "净化咒语", "修正", "纸磷花");
    }
    
    private static void addRelicUnlockBundle(int level, @NotNull String... relics) {
        TAUtils.AddUnlockBundle(AbstractUnlock.UnlockType.RELIC, TACardEnums.BlueTheAstray, level, GlobalIDMst.RelicID(relics[0]), GlobalIDMst.RelicID(relics[1]), GlobalIDMst.RelicID(relics[2]));
    }
    
    private static void addUnlockBundle(int level, @NotNull String... cards) {
        TAUtils.AddUnlockBundle(TACardEnums.BlueTheAstray, level, GlobalIDMst.CardID(cards[0]), GlobalIDMst.CardID(cards[1]),GlobalIDMst.CardID(cards[2]));
    }
}