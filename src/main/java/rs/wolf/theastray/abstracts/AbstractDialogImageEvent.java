package rs.wolf.theastray.abstracts;

import com.megacrit.cardcrawl.events.AbstractImageEvent;

public class AbstractDialogImageEvent extends AbstractImageEvent {
    
    private DialogEventBlock eventBlock;
    
    public AbstractDialogImageEvent(String title, String body, String imgUrl) {
        super(title, body, imgUrl);
    }
    
    @Override
    protected void buttonEffect(int buttonPressed) {
        
    }
    
    protected DialogEventBlock construct(DialogEventBlock eventBlock) {
        this.eventBlock = eventBlock;
        return this.eventBlock;
    }
}