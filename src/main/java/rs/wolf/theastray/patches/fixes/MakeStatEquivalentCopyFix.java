package rs.wolf.theastray.patches.fixes;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch( clz = AbstractCard.class, method = "makeStatEquivalentCopy" )
public class MakeStatEquivalentCopyFix {
    @SpireInsertPatch( rloc = 21, localvars = {"card"})
    public static void Insert(AbstractCard _inst, AbstractCard card) {
        card.purgeOnUse = _inst.purgeOnUse;
        card.isEthereal = _inst.isEthereal;
        card.exhaust = _inst.exhaust;
        card.tags.clear();
        card.tags.addAll(_inst.tags);
    }
}