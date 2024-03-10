package rs.wolf.theastray.events.dialogs.beyond;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.wolf.theastray.abstracts.AbstractDialogImageEvent;
import rs.wolf.theastray.abstracts.DialogEventBlock;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.relics.Relic11;
import rs.wolf.theastray.utils.TAUtils;

import java.util.stream.Collectors;

public class TheAstrayThirdDialogEvent extends AbstractDialogImageEvent {
    public static final String ID = TAUtils.MakeID("ThirdScenarioEvent");
    public static final String CONTINUES = "astray_scenario_three_goes";
    
    public TheAstrayThirdDialogEvent() {
        construct(new PhaseA1Block(this));
    }
    
    protected static class PhaseA1Block extends DialogEventBlock {
        
        public PhaseA1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_THREE_TITLE"), TALocalLoader.LEGACY("EVENT_THREE_PHASE_A_TEXT_1"), 
                    "AstrayAssets/images/events/event_three_0.jpg", mainEvent);
            osu = e-> {
                e.setDialogOption(text("EVENT_THREE_PHASE_A_OPTION_1"));
                e.addBlock(new PhaseA2Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            logMetric(ID, "A1");
            Leader.SaveData.putValue(CONTINUES, false);
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseA2Block extends DialogEventBlock {
        
        public PhaseA2Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_THREE_PHASE_A_TEXT_2"), mainEvent);
            osu = e-> {
                e.setDialogOption(text("EVENT_THREE_PHASE_A_OPTION_2"));
                e.addBlock(new PhaseB1Block(mainEvent));
            };
        }
        
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseB1Block extends DialogEventBlock {
        
        public PhaseB1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_THREE_PHASE_B_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_THREE_PHASE_B_OPTION_1"));
                e.setDialogOption(text("EVENT_THREE_PHASE_B_OPTION_2"));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    logMetric(ID, "B1_1");
                    insertNextBlock(new PhaseC1Block(event));
                    break;
                default:
                    logMetric(ID, "B1_2");
                    insertNextBlock(new PhaseD1Block(event));
            }
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseC1Block extends DialogEventBlock {
        
        public PhaseC1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_THREE_PHASE_C_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_THREE_PHASE_C_OPTION_1"));
                e.addBlock(new PhaseD1Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseD1Block extends DialogEventBlock {
        private static final int RELIC = 1;
        private static final int GOLD = 200;
        private static final int POTION = 3;
        private static final int CARD_REWARD = 3;
        
        public PhaseD1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_THREE_PHASE_D_TEXT_1"), mainEvent);
            osu = e -> {
                if (!Leader.DEFEATED_THEBLUE_A20) {
                    e.setDialogOption(text("EVENT_THREE_PHASE_D_OPTION_1", GOLD, POTION, CARD_REWARD), new Relic11());
                } else {
                    e.setDialogOption(text("EVENT_THREE_PHASE_D_OPTION_1_C", RELIC, GOLD, POTION, CARD_REWARD));
                }
                e.addBlock(new PhaseE1Block(mainEvent));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            AbstractRelic runic;
            if (!Leader.DEFEATED_THEBLUE_A20) {
                runic = new Relic11();
            } else {
                runic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE);
            }
            currRoom().rewards.clear();
            currRoom().addRelicToRewards(runic);
            currRoom().addGoldToRewards(GOLD);
            for (int i = 0; i < POTION; i++) {
                AbstractPotion potion = PotionHelper.getRandomPotion();
                currRoom().addPotionToRewards(potion);
            }
            for (int i = 0; i < CARD_REWARD; i++) {
                currRoom().addCardToRewards();
            }
            CardGroup curses = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : cpr().masterDeck.group) {
                if (isCardTypeOf(card, AbstractCard.CardType.CURSE))
                    curses.addToRandomSpot(card);
            }
            if (!curses.isEmpty()) {
                curses = curses.getPurgeableCards();
                if (!curses.isEmpty())
                    curses.group.forEach(c -> cpr().masterDeck.removeCard(c));
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.combatRewardScreen.open();
            logMetricRemoveCards(ID, "D1", curses.group.stream().map(c -> c.cardID).collect(Collectors.toList()));
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseE1Block extends DialogEventBlock {
        
        public PhaseE1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_THREE_PHASE_E_TEXT_1"), mainEvent);
            osu = e -> {
                e.setDialogOption(text("EVENT_THREE_PHASE_E_OPTION_1"));
            };
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.openMap();
        }
    }
}