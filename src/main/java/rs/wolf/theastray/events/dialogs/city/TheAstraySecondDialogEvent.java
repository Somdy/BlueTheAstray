package rs.wolf.theastray.events.dialogs.city;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MeatOnTheBone;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rs.lazymankits.actions.tools.GridCardManipulator;
import rs.lazymankits.actions.utility.SimpleGridCardSelectBuilder;
import rs.wolf.theastray.abstracts.AbstractDialogImageEvent;
import rs.wolf.theastray.abstracts.DialogEventBlock;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.events.dialogs.beyond.TheAstrayThirdDialogEvent;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.relics.Relic12;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TheAstraySecondDialogEvent extends AbstractDialogImageEvent {
    public static final String ID = TAUtils.MakeID("SecondScenarioEvent");
    public static final String CONTINUES = "astray_scenario_two_goes";
    
    public TheAstraySecondDialogEvent() {
        construct(new PhaseA1Block(this));
    }
    
    protected static class PhaseA1Block extends DialogEventBlock {
        private static final float HP_HEAL = 0.3F;
        
        public PhaseA1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_TITLE"), TALocalLoader.LEGACY("EVENT_TWO_PHASE_A_TEXT_1"), 
                    "AstrayAssets/images/events/event_two_0.jpg", mainEvent);
            osu = e-> {
                e.setDialogOption(text("EVENT_TWO_PHASE_A_OPTION_1", (int) (HP_HEAL * 100)));
                e.addBlock(new PhaseB1Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            
            Leader.SaveData.putValue(TheAstrayThirdDialogEvent.CONTINUES, true);
            Leader.SaveData.putValue(CONTINUES, false);
            
            int healAmt = MathUtils.ceil((cpr().maxHealth) * HP_HEAL);
            cpr().heal(healAmt, true);
            logMetricHeal(ID, "A1", healAmt);
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseB1Block extends DialogEventBlock {
        private static final int HP_LOSS = 14;
        private static final int MAX_HP = 7;
        
        public PhaseB1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_PHASE_B_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_TWO_PHASE_B_OPTION_1"));
                e.setDialogOption(text("EVENT_TWO_PHASE_B_OPTION_2", HP_LOSS, MAX_HP));
                e.addBlock(new PhaseC1Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    CardGroup tmp = cpr().masterDeck.getPurgeableCards();
                    Leader.addToBot(new SimpleGridCardSelectBuilder(c -> true)
                            .setCardGroup(tmp).setMsg(ShopScreen.NAMES[13]).setAmount(1)
                            .setAnyNumber(false).setCanCancel(false).setManipulator(new GridCardManipulator() {
                                @Override
                                public boolean manipulate(AbstractCard card, int index, CardGroup cardGroup) {
                                    if (cpr().masterDeck.contains(card)) {
                                        cpr().masterDeck.removeCard(card);
                                        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card.makeStatEquivalentCopy(), 
                                                Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                                        logMetricCardRemoval(ID, "B1_1", card);
                                    }
                                    return false;
                                }
                            }));
                    event.clearOptions();
                    removeThisBlock();
                    break;
                default:
                    cpr().damage(new DamageInfo(cpr(), HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                    cpr().increaseMaxHp(MAX_HP, true);
                    logMetricDamageAndMaxHPGain(ID, "B1_2", HP_LOSS, MAX_HP);
                    event.clearOptions();
                    removeThisBlock();
            }
        }
    }
    
    protected static class PhaseC1Block extends DialogEventBlock {
        private static final int HP_LOSS = 14;
        private static final int MAX_HP = 7;
        private static final int A_MAX_HP = 14;
        
        public PhaseC1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_PHASE_C_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_TWO_PHASE_C_OPTION_1", HP_LOSS, MAX_HP));
                e.setDialogOption(text("EVENT_TWO_PHASE_C_OPTION_2", A_MAX_HP), new Doubt());
                e.addBlock(new PhaseD1Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    cpr().damage(new DamageInfo(cpr(), HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                    cpr().increaseMaxHp(MAX_HP, true);
                    logMetricDamageAndMaxHPGain(ID, "C1_1", HP_LOSS, MAX_HP);
                    break;
                default:
                    Doubt d = new Doubt();
                    effectToList(new ShowCardAndObtainEffect(d, Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                    cpr().increaseMaxHp(A_MAX_HP, true);
                    logMetricObtainCard(ID, "C1_2", d);
            }
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseD1Block extends DialogEventBlock {
        private static final int UPGRADES = 3;
        private static final int HP_LOSS = 21;
        private static final int MAX_HP = 14;
        
        public PhaseD1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_PHASE_D_TEXT_1"), mainEvent);
            osu = e -> {
                CardGroup cardGroup = cpr().masterDeck.getUpgradableCards();
                e.setDialogOption(text("EVENT_TWO_PHASE_D_OPTION_1", UPGRADES), cardGroup.size() <= 0);
                e.setDialogOption(text("EVENT_TWO_PHASE_D_OPTION_2", HP_LOSS, MAX_HP));
                e.addBlock(new PhaseE1Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    CardGroup cardGroup = cpr().masterDeck.getUpgradableCards();
                    List<String> cards = new ArrayList<>();
                    if (cardGroup.size() > 0) {
                        for (int i = 0; i < UPGRADES; i++) {
                            Optional<AbstractCard> opt = getRandom(cardGroup.group, cardRng());
                            if (!opt.isPresent()) break;
                            AbstractCard c = opt.get();
                            float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                            float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), x, y));
                            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                            c.upgrade();
                            cpr().bottledCardUpgradeCheck(c);
                            cardGroup.removeCard(c);
                            cards.add(c.cardID);
                        }
                        logMetricUpgradeCards(ID, "D1_1", cards);
                    }
                    break;
                default:
                    cpr().damage(new DamageInfo(cpr(), HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                    cpr().increaseMaxHp(MAX_HP, true);
                    logMetricDamageAndMaxHPGain(ID, "D1_2", HP_LOSS, MAX_HP);
            }
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseE1Block extends DialogEventBlock {
        private static final int HP_LOSS = 5;
        private final boolean hasMeatOnBone;
        
        public PhaseE1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_PHASE_E_TEXT_1"), mainEvent);
            hasMeatOnBone = cpr().hasRelic(MeatOnTheBone.ID);
            osu = e -> {
                e.setDialogOption(text("EVENT_TWO_PHASE_E_OPTION_1", HP_LOSS));
                if (hasMeatOnBone) {
                    e.setDialogOption(text("EVENT_TWO_PHASE_E_OPTION_2_A", HP_LOSS), new Relic12());
                } else {
                    e.setDialogOption(text("EVENT_TWO_PHASE_E_OPTION_2", HP_LOSS), new MeatOnTheBone());
                }
                e.setDialogOption(text("EVENT_TWO_PHASE_E_OPTION_3", HP_LOSS));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                case 1:
                    cpr().damage(new DamageInfo(cpr(), HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                    AbstractRelic r = index == 1 ? (hasMeatOnBone ? new Relic12() : new MeatOnTheBone())
                            : AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
                    currRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, r);
                    insertNextBlock(new PhaseF1Block(event));
                    logMetricObtainRelicAndDamage(ID, "E1_1", r, HP_LOSS);
                    event.clearOptions();
                    removeThisBlock();
                    break;
                default:
                    currRoom().monsters = MonsterHelper.getEncounter(Leader.Encounter.SCENARIO_FIGHT);
                    currRoom().rewards.clear();
                    currRoom().addGoldToRewards(cardRng().random(80, 100));
                    currRoom().addPotionToRewards();
                    currRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
                    currRoom().addRelicToRewards(hasMeatOnBone ? new Relic12() : new MeatOnTheBone());
                    currRoom().addCardToRewards();
                    event.enterCombatFromImage();
                    AbstractDungeon.lastCombatMetricKey = Leader.Encounter.SCENARIO_FIGHT;
                    logMetric(ID, "E1_2 combat");
            }
        }
    }
    
    protected static class PhaseF1Block extends DialogEventBlock {
        
        public PhaseF1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_PHASE_F_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(leavingText());
            };
        }
        
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.openMap();
        }
    }
    
    protected static class PhaseG1Block extends DialogEventBlock {
        public PhaseG1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_TWO_PHASE_G_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(leavingText());
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.openMap();
        }
    }
}