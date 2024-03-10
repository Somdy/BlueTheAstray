package rs.wolf.theastray.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.random.Random;
import rs.lazymankits.interfaces.cards.AdditionalSpawnCard;
import rs.wolf.theastray.abstracts.AstrayCard;
import rs.wolf.theastray.data.DataMst;
import rs.wolf.theastray.patches.TACardEnums;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展牌继承该抽象类
 */
public abstract class AstrayExtCard extends AstrayCard implements AdditionalSpawnCard {
    public AstrayExtCard(int index, int cost, int extNeed, CardTarget target) {
        super(DataMst.GetCardData(index), cost, TACardEnums.TAE_CardColor, target);
        setExtension(true, extNeed);
    }
    
    public AstrayExtCard(String localname, int cost, int extNeed, CardTarget target) {
        super(DataMst.GetCardData(localname), cost, TACardEnums.TAE_CardColor, target);
        setExtension(true, extNeed);
    }
    
    @Override
    protected boolean selfCanSpawnInCombat() {
        return currRoom() != null && !cpr().masterDeck.isEmpty() && hasEnoughEnlightenCards();
    }
    
    protected boolean hasEnoughEnlightenCards() {
        List<AbstractCard> tmp = new ArrayList<>(cpr().masterDeck.group);
        tmp.removeIf(c -> !(c instanceof AstrayCard) || !((AstrayCard) c).isEnlightenCard());
        return getExtensionNeed() <= tmp.size();
    }
    
    protected final boolean playingTheAstray(AbstractPlayer.PlayerClass clz) {
        return TACardEnums.BlueTheAstray == clz;
    }
    
    @Override
    public boolean canSpawnInCombat(ArrayList<AbstractCard> arrayList, AbstractPlayer.PlayerClass clz) {
        return canSpawnInCombat() && playingTheAstray(clz);
    }
    
    @Override
    public boolean canSpawnInCombat(ArrayList<AbstractCard> arrayList, CardType cardType, AbstractPlayer.PlayerClass clz) {
        return this.type == cardType && canSpawnInCombat() && playingTheAstray(clz);
    }
    
    @Override
    public boolean canSpawnInCombatAsColorless(ArrayList<AbstractCard> arrayList, Random random, String s, AbstractPlayer.PlayerClass clz) {
        return false;
    }
    
    @Override
    public AbstractCard makeCopy() {
        return super.makeCopy();
    }
}