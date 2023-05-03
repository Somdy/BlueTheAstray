package rs.wolf.theastray.abstracts;

import rs.wolf.theastray.localizations.TALocalLoader;

public class DialogEventBlock {
    
    AbstractDialogImageEvent mainEvent;
    protected String title;
    protected String openingText;
    protected String imgPath;
    
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
    
    protected String formattedText(String ID, Object... formats) {
        String text = TALocalLoader.LEGACY(ID);
        if (formats != null && formats.length > 0)
            text = String.format(text, formats);
        return text;
    }
    
    protected String text(String ID) {
        return formattedText(ID);
    }
    
    protected String justEllipsis() {
        return text("EVENT_UNI_OPTION_2");
    }
    
    protected String leavingText() {
        return text("EVENT_UNI_OPTION_1");
    }
}