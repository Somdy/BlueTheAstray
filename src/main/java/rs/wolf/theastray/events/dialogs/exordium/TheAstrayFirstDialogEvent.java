package rs.wolf.theastray.events.dialogs.exordium;

import rs.wolf.theastray.abstracts.AbstractDialogImageEvent;
import rs.wolf.theastray.abstracts.DialogEventBlock;
import rs.wolf.theastray.localizations.TALocalLoader;

public class TheAstrayFirstDialogEvent extends AbstractDialogImageEvent {
    public TheAstrayFirstDialogEvent() {
        construct(new PhaseA1Block(this));
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
        }
    }
}