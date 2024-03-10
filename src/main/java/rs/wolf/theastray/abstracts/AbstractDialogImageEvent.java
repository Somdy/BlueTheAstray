package rs.wolf.theastray.abstracts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import rs.wolf.theastray.core.Leader;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;

public class AbstractDialogImageEvent extends AbstractImageEvent implements TAUtils {
    
    protected final DialogEventBuilding eventBuilding = new DialogEventBuilding();
    
    public AbstractDialogImageEvent(String title, String body, String imgUrl) {
        super(title, body, imgUrl);
    }
    
    public AbstractDialogImageEvent() {
        this("", "", "");
    }
    
    protected DialogEventBuilding construct(DialogEventBlock eventBlock) {
        eventBuilding.addBlock(eventBlock);
        updateBodyFromBlock(eventBlock);
        updateOptionsFromBlock(eventBlock);
        return eventBuilding;
    }
    
    public void addBlock(DialogEventBlock... eventBlocks) {
        if (eventBlocks != null && eventBlocks.length > 0) {
            for (DialogEventBlock eb : eventBlocks) {
                TAUtils.Log("Adding new block");
                eventBuilding.addBlock(eb);
            }
        }
    }
    
    protected void removeCurrBlock() {
        eventBuilding.removeCurrBlock();
        updateBodyFromBlock(eventBuilding.getCurrBlock());
        updateOptionsFromBlock(eventBuilding.getCurrBlock());
    }
    
    protected void insertBlock(int index, DialogEventBlock eventBlock) {
        eventBuilding.insertBlock(index, eventBlock);
    }
    
    protected void insertNextBlock(int index, DialogEventBlock eventBlock) {
        eventBuilding.insertBlock(index + 1, eventBlock);
    }
    
    protected void updateOptionsFromBlock(DialogEventBlock eventBlock) {
        if (eventBlock.osu != null)
            eventBlock.osu.setup(this);
    }
    
    protected void updateBodyFromBlock(DialogEventBlock eventBlock) {
        if (eventBlock.title != null) 
            title = eventBlock.title;
        if (eventBlock.openingText != null) {
            body = eventBlock.openingText;
            updateImageBodyText(body);
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
    
    public void clearRemainingOptions() {
        imageEventText.clearRemainingOptions();
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
    
    @Override
    public void openMap() {
        super.openMap();
    }
    
    @Override
    public void update() {
        super.update();
        DialogEventBlock block = eventBuilding.getCurrBlock();
        if (block != null)
            block.onEventUpdate();
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
        
        public void insertBlock(int index, DialogEventBlock eventBlock) {
            if (index > eventBlocks.size()) {
                addBlock(eventBlock);
            } else if (index >= 0) {
                eventBlocks.add(index, eventBlock);
            }
        }
        
        public boolean hasBlock() {
            return eventBlocks.size() > 0;
        }
    
        public List<DialogEventBlock> getAllBlocks() {
            return eventBlocks;
        }
    
        public DialogEventBlock getCurrBlock() {
            return eventBlocks.get(0);
        }
        
        public void removeCurrBlock() {
            eventBlocks.remove(0);
        }
    }
}