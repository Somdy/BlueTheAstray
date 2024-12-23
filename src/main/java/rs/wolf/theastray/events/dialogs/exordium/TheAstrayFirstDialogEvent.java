package rs.wolf.theastray.events.dialogs.exordium;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.LivingWall;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AbstractDialogImageEvent;
import rs.wolf.theastray.abstracts.DialogEventBlock;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.events.dialogs.city.TheAstraySecondDialogEvent;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.utils.GlobalIDMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.Arrays;

public class TheAstrayFirstDialogEvent extends AbstractDialogImageEvent {
    public static final String ID = TAUtils.MakeID("FirstScenarioEvent");
    
    public TheAstrayFirstDialogEvent() {
        construct(new PhaseA1Block(this));
    }
    
    protected static class PhaseA1Block extends DialogEventBlock {
        public PhaseA1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_TITLE"), TALocalLoader.LEGACY("EVENT_ONE_PHASE_A_TEXT_1"), 
                    "AstrayAssets/images/events/event_one_0.jpg", mainEvent);
            osu = e-> {
                e.setDialogOption(justEllipsis());
                e.addBlock(new PhaseA2Block(e));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            logMetric(ID, "A1");
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseA2Block extends DialogEventBlock {
        public PhaseA2Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_A_TEXT_2"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_ONE_PHASE_A_OPTION_1", 2));
                e.setDialogOption(text("EVENT_ONE_PHASE_A_OPTION_2"
                        .concat(LMSK.AscnLv() >= 15 ? "_A" : "")));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    //obtain two random magical cards
                    boolean hasB4 = cpr().masterDeck.group.stream().anyMatch(c -> GlobalIDMst.CardID(4).equals(c.cardID));
                    AbstractCard[] cards = new AbstractCard[2];
                    cards[0] = hasB4 ? CardMst.ReturnRndMagicOutOfCombat(true) : CardMst.GetCard(4);
                    cards[1] = CardMst.ReturnRndMagicOutOfCombat(true);
                    effectToList(new ShowCardAndObtainEffect(cards[0], Settings.WIDTH / 2F - scale(100F), Settings.HEIGHT / 2F));
                    effectToList(new ShowCardAndObtainEffect(cards[1], Settings.WIDTH / 2F + scale(100F), Settings.HEIGHT / 2F));
                    Leader.SaveData.putValue(TheAstraySecondDialogEvent.CONTINUES, true);
                    logMetricObtainCards(ID, "A2_1", Arrays.asList(cards[0].cardID, cards[1].cardID));
                    event.clearOptions();
                    insertNextBlock(new PhaseB1Block(event));
                    removeThisBlock();
                    break;
                default:
                    logMetric(ID, "A2_2");
                    event.clearOptions();
                    insertNextBlock(new DialogEventBlock(text("EVENT_ONE_PHASE_C_TEXT_1"), event) {
                        {
                            event.setDialogOption(leavingText());
                        }
    
                        @Override
                        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
                            Leader.SaveData.putValue(TheAstraySecondDialogEvent.CONTINUES, false);
                            event.openMap();
                        }
                    });
                    removeThisBlock();
            }
        }
    }
    
    protected static class PhaseB1Block extends DialogEventBlock {
        private static final int HP_LOSS = 6;
        
        public PhaseB1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_B_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_ONE_PHASE_B_OPTION_1", HP_LOSS));
                e.setDialogOption(text("EVENT_ONE_PHASE_B_OPTION_2"));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    cpr().damage(new DamageInfo(null, HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                    logMetricTakeDamage(ID, "B1_1", HP_LOSS);
                    event.clearOptions();
                    insertNextBlock(new PhaseD1Block(event));
                    removeThisBlock();
                    break;
                default:
                    Doubt d = new Doubt();
                    effectToList(new ShowCardAndObtainEffect(d, Settings.WIDTH / 2F - scale(60F), Settings.HEIGHT / 2F));
                    logMetricObtainCard(ID, "B1_2", d);
                    event.clearOptions();
                    insertNextBlock(new PhaseG1Block(event));
                    removeThisBlock();
            }
        }
    }
    
    protected static class PhaseD1Block extends DialogEventBlock {
        private static final int HP_LOSS = 12;
        
        public PhaseD1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_D_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(justEllipsis());
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            logMetric(ID, "D1");
            event.clearOptions();
            // insert D2 Block
            insertNextBlock(new DialogEventBlock(text("EVENT_ONE_PHASE_D_TEXT_2"), event) {
                {
                    event.setDialogOption(text("EVENT_ONE_PHASE_D_OPTION_1", HP_LOSS));
                    event.setDialogOption(text("EVENT_ONE_PHASE_D_OPTION_2"));
                }
                @Override
                protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
                    event.clearOptions();
                    switch (index) {
                        case 0:
                            cpr().damage(new DamageInfo(null, HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                            logMetricTakeDamage(ID, "D2_1", HP_LOSS);
                            event.clearOptions();
                            insertNextBlock(new PhaseE1Block(event));
                            removeThisBlock();
                            break;
                        default:
                            Doubt d = new Doubt();
                            effectToList(new ShowCardAndObtainEffect(d, Settings.WIDTH / 2F - scale(60F), Settings.HEIGHT / 2F));
                            logMetricObtainCard(ID, "D2_2", d);
                            event.clearOptions();
                            insertNextBlock(new PhaseG1Block(event));
                            removeThisBlock();
                    }
                }
            });
            removeThisBlock();
        }
    }
    
    protected static class PhaseE1Block extends DialogEventBlock {
    
        public PhaseE1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_E_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(justEllipsis());
            };
            mainEvent.addBlock(ellipsisBlock(text("EVENT_ONE_PHASE_E_TEXT_2")), ellipsisBlock(text("EVENT_ONE_PHASE_E_TEXT_3")));
            mainEvent.addBlock(new PhaseE4Block(mainEvent));
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            logMetric(ID, "E1");
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseE4Block extends DialogEventBlock {
        private static final int HP_LOSS = 18;
        public PhaseE4Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_E_TEXT_4"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_ONE_PHASE_E_OPTION_1", HP_LOSS), new Pain());
                e.setDialogOption(text("EVENT_ONE_PHASE_E_OPTION_2"), new Doubt());
            };
        }
        
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    cpr().damage(new DamageInfo(null, HP_LOSS, DamageInfo.DamageType.HP_LOSS));
                    Pain p = new Pain();
                    effectToList(new ShowCardAndObtainEffect(p, Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                    logMetricObtainCardAndDamage(ID, "E4_1", p, HP_LOSS);
                    event.clearOptions();
                    insertNextBlock(new PhaseF1Block(event));
                    break;
                case 1:
                    Doubt d = new Doubt();
                    effectToList(new ShowCardAndObtainEffect(d, Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                    logMetricObtainCard(ID, "E4_2", d);
                    event.clearOptions();
                    insertNextBlock(new PhaseG1Block(event));
                    break;
            }
            removeThisBlock();
        }
    }
    
    protected static class PhaseF1Block extends DialogEventBlock {
        
        public PhaseF1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_F_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(justEllipsis());
            };
            mainEvent.addBlock(ellipsisBlock(text("EVENT_ONE_PHASE_F_TEXT_2")), ellipsisBlock(text("EVENT_ONE_PHASE_F_TEXT_3")));
            mainEvent.addBlock(new PhaseF4Block(mainEvent));
        }
        
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            logMetric(ID, "F1");
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseF4Block extends DialogEventBlock {
        private static final float HP_HEAL = 0.5F;
        private static final float HP_HEAL_A = 0.3F;
        
        public PhaseF4Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_F_TEXT_4"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_ONE_PHASE_F_OPTION_1", (int) (getHeal() * 100F), 2),
                        !AbstractDungeon.player.masterDeck.hasUpgradableCards());
                e.setDialogOption(text("EVENT_ONE_PHASE_F_OPTION_2", (int) (getHeal() * 100F), 2));
            };
        }
        
        private float getHeal() {
            return ascenLv() >= 15 ? HP_HEAL_A : HP_HEAL;
        }
        
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    cpr().heal(MathUtils.ceil((cpr().maxHealth - cpr().currentHealth) * getHeal()), true);
                    AbstractDungeon.gridSelectScreen.open(cpr().masterDeck.getUpgradableCards(), 2, 
                            ArmamentsAction.TEXT[0], false, false, false, false);
                    if (event instanceof TheAstrayFirstDialogEvent) {
                        ((TheAstrayFirstDialogEvent) event).chooseType = 0;
                    }
                    break;
                case 1:
                    cpr().heal(MathUtils.ceil((cpr().maxHealth - cpr().currentHealth) * getHeal()), true);
                    if (event instanceof TheAstrayFirstDialogEvent) {
                        ((TheAstrayFirstDialogEvent) event).chooseType = 1;
                    }
                    AbstractDungeon.gridSelectScreen.open(cpr().masterDeck.getPurgeableCards(), 2,
                            LivingWall.OPTIONS[4], false, false, false, false);
                    break;
            }
            event.clearOptions();
            insertNextBlock(new PhaseG1Block(event));
            removeThisBlock();
        }
    }
    
    private boolean cardSelected = false;
    private int chooseType = -1;
    
    @Override
    public void update() {
        super.update();
        if (!cardSelected && chooseType > -1) {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 2) {
                cardSelected = true;
                for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                    AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                    if (chooseType == 0) {
                        card.upgrade();
                        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
                            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy(), 
                                    Settings.WIDTH / 2F - scale(100F) + scale(200F) * i, 
                                    Settings.HEIGHT / 2F));
                            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2F
                                    - scale(100F) + scale(200F) * i, Settings.HEIGHT / 2F));
                        }
                        logMetricCardUpgrade(ID, "F4_1", card);
                    } else {
                        LMSK.Player().masterDeck.removeCard(card);
                        AbstractDungeon.transformCard(card, false, miscRng());
                        AbstractCard transformed = AbstractDungeon.getTransformedCard();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transformed, Settings.WIDTH / 2F
                                - scale(100F) + scale(200F) * i, Settings.HEIGHT / 2F));
                        logMetricCardRemoval(ID, "F4_2", card);
                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
        }
    }
    
    protected static class PhaseG1Block extends DialogEventBlock {
        public PhaseG1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_G_TEXT_1"), mainEvent);
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