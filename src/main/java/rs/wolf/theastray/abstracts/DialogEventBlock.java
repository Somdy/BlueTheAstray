package rs.wolf.theastray.abstracts;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.utils.TAUtils;

public class DialogEventBlock implements TAUtils {
    
    AbstractDialogImageEvent mainEvent;
    protected String title;
    protected String openingText;
    protected String imgPath;
    public OptionSetup osu;
    
    public DialogEventBlock(String title, String openingText, String imgPath, AbstractDialogImageEvent mainEvent) {
        this.title = title;
        this.openingText = openingText;
        this.imgPath = imgPath;
        this.mainEvent = mainEvent;
    }
    
    public DialogEventBlock(String title, String openingText, AbstractDialogImageEvent mainEvent) {
        this(title, openingText, null, mainEvent);
    }
    
    public DialogEventBlock(String body, AbstractDialogImageEvent mainEvent) {
        this(null, body, null, mainEvent);
    }
    
    public DialogEventBlock(AbstractDialogImageEvent mainEvent) {
        this(null, null, null, mainEvent);
    }
    
    protected final void buttonEffect(int index) {
        eventButtonEffect(mainEvent, index);
    }
    
    protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
        
    }
    
    protected void removeThisBlock() {
        mainEvent.removeCurrBlock();
    }
    
    public void onEventUpdate() { }
    
    protected void insertNextBlock(DialogEventBlock eventBlock) {
        int curIndex = mainEvent.eventBuilding.getAllBlocks().indexOf(this);
        if (curIndex >= 0)
            mainEvent.insertNextBlock(curIndex, eventBlock);
    }
    
    protected String text(String ID, Object... formats) {
        String text = TALocalLoader.LEGACY(ID);
        if (formats != null && formats.length > 0)
            text = String.format(text, formats);
        return text;
    }
    
    protected String text(String ID) {
        return text(ID, (Object) null);
    }
    
    protected String justEllipsis() {
        return text("EVENT_UNI_OPTION_2");
    }
    
    protected String leavingText() {
        return text("EVENT_UNI_OPTION_1");
    }
    
    protected AbstractPlayer cpr() {
        return AbstractDungeon.player;
    }
    
    protected DialogEventBlock ellipsisBlock(String body) {
        return new DialogEventBlock(body, mainEvent) {
            {
                osu = e -> {
                    e.setDialogOption(justEllipsis());
                };
            }
    
            @Override
            protected void eventButtonEffect(AbstractDialogImageEvent event, int index) {
                event.clearOptions();
                removeThisBlock();
            }
        };
    }
    
    public interface OptionSetup {
        void setup(AbstractDialogImageEvent event);
    }
}