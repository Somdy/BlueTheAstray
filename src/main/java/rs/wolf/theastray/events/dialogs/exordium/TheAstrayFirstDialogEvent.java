package rs.wolf.theastray.events.dialogs.exordium;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.abstracts.AbstractDialogImageEvent;
import rs.wolf.theastray.abstracts.DialogEventBlock;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.utils.GlobalIDMst;

import java.util.ArrayList;
import java.util.Collection;

public class TheAstrayFirstDialogEvent extends AbstractDialogImageEvent {
    public TheAstrayFirstDialogEvent() {
        construct(new PhaseA1Block(this))
                .addBlock(new PhaseA2Block(this));
    }
    
    protected static class PhaseA1Block extends DialogEventBlock {
        public PhaseA1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_TITLE"), TALocalLoader.LEGACY("EVENT_ONE_PHASE_A_TEXT_1"), 
                    "AstrayAssets/images/events/event_one_0.jpg", mainEvent);
            mainEvent.setDialogOption(justEllipsis());
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.clearOptions();
            removeThisBlock();
        }
    }
    
    protected static class PhaseA2Block extends DialogEventBlock {
        public PhaseA2Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_A_TEXT_2"), mainEvent);
            mainEvent.setDialogOption(formattedText("EVENT_ONE_PHASE_A_OPTION_1", 2));
            mainEvent.setDialogOption(text("EVENT_ONE_PHASE_A_OPTION_2"
                    .concat(LMSK.AscnLv() >= 15 ? "_A" : "")));
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    //obtain two random magical cards
                    boolean hasB4 = cpr().masterDeck.group.stream().anyMatch(c -> GlobalIDMst.CardID(4).equals(c.cardID));
                    AbstractCard[] cards = new AbstractCard[2];
                    cards[0] = hasB4 ? CardMst.ReturnRndMagicInCombat(true) : CardMst.GetCard(4);
                    cards[1] = CardMst.ReturnRndMagicInCombat(true);
                    effectToList(new ShowCardAndObtainEffect(cards[0], Settings.WIDTH / 2F - scale(60F), Settings.HEIGHT / 2F));
                    effectToList(new ShowCardAndObtainEffect(cards[1], Settings.WIDTH / 2F + scale(60F), Settings.HEIGHT / 2F));
                    event.clearOptions();
                    insertNextBlock(new PhaseB1Block(event));
                    removeThisBlock();
                    break;
                default:
                    event.clearOptions();
                    insertNextBlock(new DialogEventBlock(text("EVENT_ONE_PHASE_C_TEXT_1"), event) {
                        {
                            event.setDialogOption(leavingText());
                        }
    
                        @Override
                        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
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
            mainEvent.setDialogOption(formattedText("EVENT_ONE_PHASE_B_OPTION_1", HP_LOSS));
            mainEvent.setDialogOption(text("EVENT_ONE_PHASE_B_OPTION_2"));
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            switch (index) {
                case 0:
                    cpr().damage(new DamageInfo(null, HP_LOSS, DamageInfo.DamageType.THORNS));
                    removeThisBlock();
                    break;
                default:
                    effectToList(new ShowCardAndObtainEffect(new Doubt(), Settings.WIDTH / 2F - scale(60F), Settings.HEIGHT / 2F));
                    insertNextBlock(new PhaseG1Block(event));
                    removeThisBlock();
            }
        }
    }
    
    protected static class PhaseD1Block extends DialogEventBlock {
        private static final int HP_LOSS = 12;
        
        public PhaseD1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_D_TEXT_1"), mainEvent);
            mainEvent.setDialogOption(justEllipsis());
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.clearOptions();
            insertNextBlock(new DialogEventBlock(text("EVENT_ONE_PHASE_D_TEXT_2"), event) {
                {
                    event.setDialogOption(formattedText("EVENT_ONE_PHASE_D_OPTION_1", HP_LOSS));
                    event.setDialogOption(text("EVENT_ONE_PHASE_D_OPTION_2"));
                }
                @Override
                protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
                    event.clearOptions();
                    switch (index) {
                        case 0:
                            cpr().damage(new DamageInfo(null, HP_LOSS, DamageInfo.DamageType.THORNS));
                            insertNextBlock(new PhaseE1Block(event));
                            removeThisBlock();
                        default:
                            effectToList(new ShowCardAndObtainEffect(new Doubt(), Settings.WIDTH / 2F - scale(60F), Settings.HEIGHT / 2F));
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
            mainEvent.setDialogOption(justEllipsis());
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            super.eventButtonEffect(event, index);
        }
    }
    
    protected static class PhaseG1Block extends DialogEventBlock {
        public PhaseG1Block(AbstractDialogImageEvent mainEvent) {
            super(TALocalLoader.LEGACY("EVENT_ONE_PHASE_G_TEXT_1"), mainEvent);
            mainEvent.setDialogOption(leavingText());
        }
    
        @Override
        protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
            event.openMap();
        }
    }
}