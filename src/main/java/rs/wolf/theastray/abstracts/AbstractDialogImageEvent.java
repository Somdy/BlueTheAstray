package rs.wolf.theastray.abstracts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import rs.wolf.theastray.localizations.TALocalLoader;

import java.util.ArrayList;
import java.util.List;

public class AbstractDialogImageEvent extends AbstractImageEvent {
    
    private final DialogEventBuilding eventBuilding = new DialogEventBuilding();
    
    public AbstractDialogImageEvent(String title, String body, String imgUrl) {
        super(title, body, imgUrl);
    }
    
    public AbstractDialogImageEvent() {
        this("", "", "");
    }
    
    protected DialogEventBuilding construct(DialogEventBlock eventBlock) {
        updateBodyFromBlock(eventBlock);
        eventBuilding.addBlock(eventBlock);
        return eventBuilding;
    }
    
    protected void removeCurrBlock() {
        eventBuilding.removeCurrBlock();
        updateBodyFromBlock(eventBuilding.getCurrBlock());
    }
    
    protected void updateBodyFromBlock(DialogEventBlock eventBlock) {
        if (eventBlock.title != null && eventBlock.openingText != null) {
            title = eventBlock.title;
            body = eventBlock.openingText;
        }
        if (eventBlock.imgPath != null) {
            imageEventText.loadImage(eventBlock.imgPath);
        }
    }
    
    @Override
    protected void buttonEffect(int buttonPressed) {
        if (eventBuilding.hasBlock()) {
            DialogEventBlock eventBlock = eventBuilding.getCurrBlock();
            eventBlock.buttonEffect(buttonPressed);
        }
    }
    
    public void setDialogOption(String text) {
        imageEventText.setDialogOption(text);
    }
    
    public void setDialogOption(String text, AbstractCard previewCard) {
        imageEventText.setDialogOption(text, previewCard);
    }
    
    public void setDialogOption(String text, AbstractRelic previewRelic) {
        imageEventText.setDialogOption(text, previewRelic);
    }
    
    public void setDialogOption(String text, AbstractCard previewCard, AbstractRelic previewRelic) {
        imageEventText.setDialogOption(text, previewCard, previewRelic);
    }
    
    public void setDialogOption(String text, boolean disabled) {
        imageEventText.setDialogOption(text, disabled);
    }
    
    public void setDialogOption(String text, boolean disabled, AbstractCard previewCard) {
        imageEventText.setDialogOption(text, disabled, previewCard);
    }
    
    public void setDialogOption(String text, boolean disabled, AbstractRelic previewRelic) {
        imageEventText.setDialogOption(text, disabled, previewRelic);
    }
    
    public void setDialogOption(String text, boolean disabled, AbstractCard previewCard, AbstractRelic previewRelic) {
        imageEventText.setDialogOption(text, disabled, previewCard, previewRelic);
    }
    
    public void clearOptions() {
        imageEventText.clearAllDialogs();
    }
    
    public void updateImageBodyText(String text, DialogWord.AppearEffect ae) {
        imageEventText.updateBodyText(text, ae);
    }
    
    public void updateImageBodyText(String text) {
        imageEventText.updateBodyText(text);
    }
    
    public void updateDialogOption(int index, String text) {
        imageEventText.updateDialogOption(index, text);
    }
    
    public void updateDialogOption(int index, String text, boolean disabled) {
        imageEventText.updateDialogOption(index, text, disabled);
    }
    
    public void updateDialogOption(int index, String text, AbstractCard previewCard) {
        imageEventText.updateDialogOption(index, text, previewCard);
    }
    
    public void updateDialogOption(int index, String text, AbstractRelic previewRelic) {
        if (!imageEventText.optionList.isEmpty()) {
            if (imageEventText.optionList.size() > index) {
                imageEventText.optionList.set(index, new LargeDialogOptionButton(index, text, previewRelic));
            } else {
                imageEventText.optionList.add(new LargeDialogOptionButton(index, text, previewRelic));
            }
        } else {
            imageEventText.optionList.add(new LargeDialogOptionButton(index, text, previewRelic));
        }
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
    
    protected static class DialogEventBuilding {
        private final List<DialogEventBlock> eventBlocks = new ArrayList<>();
        
        public void addBlock(DialogEventBlock eventBlock) {
            eventBlocks.add(eventBlock);
        }
        
        public boolean hasBlock() {
            return eventBlocks.size() > 0;
        }
        
        public DialogEventBlock getCurrBlock() {
            return eventBlocks.get(0);
        }
        
        public void removeCurrBlock() {
            eventBlocks.remove(0);
        }
    }
}