package rs.wolf.theastray.core;

import basemod.*;
import basemod.abstracts.CustomSavable;
import basemod.devcommands.ConsoleCommand;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.city.SnakePlant;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.lazymankits.LMDebug;
import rs.lazymankits.LManager;
import rs.lazymankits.interfaces.OnMakingCardInCombatSubscriber;
import rs.lazymankits.interfaces.OnShuffleSubscriber;
import rs.lazymankits.managers.LMCustomAtkEffectMgr;
import rs.lazymankits.utils.LMDamageInfoHelper;
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
import rs.wolf.theastray.events.dialogs.beyond.TheAstrayThirdDialogEvent;
import rs.wolf.theastray.events.dialogs.city.TheAstraySecondDialogEvent;
import rs.wolf.theastray.events.dialogs.exordium.TheAstrayFirstDialogEvent;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.monsters.BlueTheBoss;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.relics.Relic1;
import rs.wolf.theastray.relics.Relic11;
import rs.wolf.theastray.ui.cursor.AstrayCursor;
import rs.wolf.theastray.utils.*;
import rs.wolf.theastray.variables.TAExtNeed;
import rs.wolf.theastray.variables.TAExtraMagic;
import rs.wolf.theastray.variables.TAPromotion;
import rs.wolf.theastray.vfx.combat.MagicDamageEffect;
import sts.jigen.character.Jigen;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
@SuppressWarnings("unused")
public class Leader implements TAUtils, CustomSavable<String>, EditStringsSubscriber, EditKeywordsSubscriber, EditCardsSubscriber,
        PostInitializeSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, PostPowerApplySubscriber, OnPlayerLoseBlockSubscriber, 
        OnPlayerTurnStartSubscriber, PostBattleSubscriber, OnStartBattleSubscriber, OnShuffleSubscriber, OnMakingCardInCombatSubscriber, 
        PostCreateStartingRelicsSubscriber, AddAudioSubscriber, SetUnlocksSubscriber, StartGameSubscriber, PostDungeonUpdateSubscriber, 
        StartActSubscriber {
    public static final String MOD_ID = "BlueTheAstray";
    public static final String PREFIX = "astray";
    public static final String[] AUTHORS = {"Little wolf影幽"};
    public static final String DESCRIPTION = "An original character";
    public static final Color TAColor = LMSK.Color(32, 178, 170);
    
    private static final String TA_BTN = "AstrayAssets/images/char/button.png";
    private static final String TA_PTR = "AstrayAssets/images/char/portrait.jpg";
    private static final String TA_PTR_AFTER = "AstrayAssets/images/char/portrait_after.jpg";
    
    public static DataObject SaveData = new DataObject();
    
    private static List<AbstractGameAction> actionList = new ArrayList<>();
    
    public static boolean ALLOW_RELICS = true;
    public static boolean ALLOW_EPISODIC_EVENTS = true;
    public static boolean ASTRAY_CURSOR_AS_GLOBAL = false;
    
    public static boolean DEFEATED_HEART = false;
    public static boolean DEFEATED_THEBLUE_A20 = false;
    
    public static boolean SCENARIO_TMP_GOES = false;
    public static boolean SCENARIO_TMP_ENDS = false;
    
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
        properties.setProperty("ASTRAY_CURSOR_AS_GLOBAL", Boolean.toString(ASTRAY_CURSOR_AS_GLOBAL));
        
        properties.setProperty("DEFEATED_HEART", Boolean.toString(DEFEATED_HEART));
        properties.setProperty("DEFEATED_THEBLUE_A20", Boolean.toString(DEFEATED_THEBLUE_A20));
        
//        properties.setProperty("SCENARIO_START", Boolean.toString(SCENARIO_START));
//        properties.setProperty("SCENARIO_GOES", Boolean.toString(SCENARIO_GOES));
        
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
        ASTRAY_CURSOR_AS_GLOBAL = config.getBool("ASTRAY_CURSOR_AS_GLOBAL");
        
        DEFEATED_HEART = config.getBool("DEFEATED_HEART");
        DEFEATED_THEBLUE_A20 = config.getBool("DEFEATED_THEBLUE_A20");
        
//        SCENARIO_START = config.getBool("SCENARIO_START");
//        SCENARIO_GOES = config.getBool("SCENARIO_GOES");
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
        config.setBool("ASTRAY_CURSOR_AS_GLOBAL", ASTRAY_CURSOR_AS_GLOBAL);
        
        config.setBool("DEFEATED_HEART", DEFEATED_HEART);
        config.setBool("DEFEATED_THEBLUE_A20", DEFEATED_THEBLUE_A20);
        
//        config.setBool("SCENARIO_START", SCENARIO_START);
//        config.setBool("SCENARIO_GOES", SCENARIO_GOES);
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
    
    public static void addToBot(AbstractGameAction action) {
        actionList.add(action);
    }
    
    public static void addToTop(AbstractGameAction action) {
        actionList.add(0, action);
    }
    
    @Override
    public String onSave() {
        return SaveData.save();
    }
    
    @Override
    public void onLoad(String s) {
        if (s != null && Strings.isNotBlank(s))
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
    public void receiveStartGame() {
        if (!CardCrawlGame.loadingSave) {
            resetOneRunValues();
            SCENARIO_TMP_GOES = false;
            SCENARIO_TMP_ENDS = false;
            SaveData.clear();
        }
    }
    
    @Override
    public void receiveStartAct() {
        
    }
    
    private void resetOneRunValues() {
        DEFEATED_HEART = false;
    }
    
    @Override
    public void receivePostInitialize() {
        TAImageMst.Initialize();
        ConsoleCommand.addCommand("bluemana", ManaCMD.class);
        ConsoleCommand.addCommand("bluecheat", CheatCMD.class);
        
        GlobalCursorMst.Initialize(CardCrawlGame.cursor);
        GlobalCursorMst.AddCursor(TACardEnums.BlueTheAstray, new AstrayCursor());
        
        LMCustomAtkEffectMgr.RegisterEffect(TACardEnums.ASH_EXPLOSION, TAImageMst.ASH_EXPLOSION, "ATTACK_FIRE");
        
        makeModPanels();
        
        addMonsters();
        
        addEvents();
        
        BlueTheBoss.PutDialog(TACardEnums.BlueTheAstray, "BLUE_THE_ASTRAY");
        if (Loader.isModLoadedOrSideloaded("Jigen")) {
            BlueTheBoss.PutDialog(Jigen.Enums.JIGEN, "THE_CHORD");
        }
        
        MsgLogger.Log();
    }
    
    private static void addEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(TheAstrayFirstDialogEvent.ID, TheAstrayFirstDialogEvent.class)
                .eventType(EventUtils.EventType.NORMAL)
                .spawnCondition(() -> {
                    boolean cp = AbstractDungeon.player.chosenClass == TACardEnums.BlueTheAstray
                            || ALLOW_EPISODIC_EVENTS;
                    return cp && AbstractDungeon.actNum <= 1;
                }).dungeonIDs(Exordium.ID).create());
        BaseMod.addEvent(new AddEventParams.Builder(TheAstraySecondDialogEvent.ID, TheAstraySecondDialogEvent.class)
                .eventType(EventUtils.EventType.ONE_TIME)
//                .playerClass(TACardEnums.BlueTheAstray)
                .spawnCondition(() -> SaveData.getBool(TheAstraySecondDialogEvent.CONTINUES)).endsWithRewardsUI(true)
                .dungeonIDs(TheCity.ID).create());
        BaseMod.addEvent(new AddEventParams.Builder(TheAstrayThirdDialogEvent.ID, TheAstrayThirdDialogEvent.class)
                .eventType(EventUtils.EventType.ONE_TIME)
//                .playerClass(TACardEnums.BlueTheAstray)
                .spawnCondition(() -> SaveData.getBool(TheAstrayThirdDialogEvent.CONTINUES)).endsWithRewardsUI(true)
                .dungeonIDs(TheBeyond.ID).create());
    }
    
    private static void addMonsters() {
        BaseMod.addMonster(Encounter.SCENARIO_FIGHT, () -> new MonsterGroup(new AbstractMonster[]{
                new FungiBeast(-480F, 0F), new SnakePlant(-160F, 0F), new Mugger(180F, 0F)
        }));
        
        BaseMod.addMonster(BlueTheBoss.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new BlueTheBoss(0F, 0F)
        }));
        BaseMod.addBoss(TheEnding.ID, BlueTheBoss.ID, "AstrayAssets/images/ui/map/boss/bluetheboss_icon.png", 
                "AstrayAssets/images/ui/map/boss/bluetheboss_outline.png");
    }
    
    public static class Encounter {
        public static final String SCENARIO_FIGHT = PREFIX + ":Scenario Fight";
    }
    
    private static void makeModPanels() {
        ModPanel settings = new ModPanel();
        UIStrings settingStrings = TAUtils.UIStrings(TAUtils.MakeID("ModConfigSettings"));
        Map<String, String> settingDesc = settingStrings.TEXT_DICT;
        ModLabeledToggleButton allowRelics = new ModLabeledToggleButton(settingDesc.get("ALLOW_RELICS"),
                380F, 720F, Color.WHITE.cpy(), FontHelper.charDescFont, ALLOW_RELICS, settings, (l) -> {},
                (btn) -> {
                    ALLOW_RELICS = btn.enabled;
                    SpireConfig config = makeConfig();
                    config.setBool("ALLOW_RELICS", ALLOW_RELICS);
                    save(config);
                });
        ModLabeledToggleButton allowEvents = new ModLabeledToggleButton(settingDesc.get("ALLOW_EPISODIC_EVENTS"),
                380F, 680F, Color.WHITE.cpy(), FontHelper.charDescFont, ALLOW_EPISODIC_EVENTS, settings, (l) -> {},
                (btn) -> {
                    ALLOW_EPISODIC_EVENTS = btn.enabled;
                    SpireConfig config = makeConfig();
                    config.setBool("ALLOW_EPISODIC_EVENTS", ALLOW_EPISODIC_EVENTS);
                    save(config);
                });
        ModLabeledToggleButton globalCursor = new ModLabeledToggleButton(settingDesc.get("ASTRAY_CURSOR_AS_GLOBAL"), 
                settingDesc.get("ASTRAY_CURSOR_AS_GLOBAL_EXT"),
                380F, 640F, Color.WHITE.cpy(), FontHelper.charDescFont, ASTRAY_CURSOR_AS_GLOBAL, settings, (l) -> {},
                (btn) -> {
                    ASTRAY_CURSOR_AS_GLOBAL = btn.enabled;
                    SpireConfig config = makeConfig();
                    config.setBool("ASTRAY_CURSOR_AS_GLOBAL", ASTRAY_CURSOR_AS_GLOBAL);
                    save(config);
                });
        settings.addUIElement(allowRelics);
        settings.addUIElement(allowEvents);
        settings.addUIElement(globalCursor);
        BaseMod.registerModBadge(TAImageMst.BADGE, MOD_ID, Arrays.toString(AUTHORS), DESCRIPTION, settings);
    }
    
    public static void OnNextRoomTransition() {
        if (!CardCrawlGame.loadingSave) {
//            if (SCENARIO_TMP_GOES) {
//                SaveData.putValue(TheAstrayFirstDialogEvent.GO, false);
//                SaveData.putValue(TheAstraySecondDialogEvent.GO, true);
//                SCENARIO_TMP_GOES = false;
//            }
//            if (SCENARIO_TMP_ENDS) {
//                SaveData.putValue(TheAstraySecondDialogEvent.GO, false);
//                SaveData.putValue(TheAstrayThirdDialogEvent.END, true);
//                SCENARIO_TMP_ENDS = false;
//            }
        }
    }
    
    @Override
    public void receivePostDungeonUpdate() {
        if (actionList.size() > 0) {
            actionList.get(0).update();
            if (actionList.get(0).isDone)
                actionList.remove(0);
        }
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
                    AbstractRelic copy = r.makeCopy();
                    if (copy instanceof Relic11 && DEFEATED_THEBLUE_A20) {
                        if (copy.tier != AbstractRelic.RelicTier.RARE)
                            copy.tier = AbstractRelic.RelicTier.RARE;
                    }
                    BaseMod.addRelic(copy.makeCopy(), RelicType.SHARED);
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
    
    public static void PostOnCreatureDamage(AbstractCreature target, DamageInfo info) {
        // Magical Card Effect
        if (LMDamageInfoHelper.HasTag(info, TAUtils.DAMAGE_FROM_MAGICAL_CARD) && target.hb != null) {
            addToTop(new VFXAction(new MagicDamageEffect(target.hb.cX, target.hb.cY, target.lastDamageTaken)));
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