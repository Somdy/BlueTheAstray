package rs.wolf.theastray.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class TACardEnums {
    @SpireEnum(name = "theastray_color")
    public static AbstractCard.CardColor TA_CardColor;
    @SpireEnum(name = "theastray_color")
    public static CardLibrary.LibraryType TA_LibrType;
    
    @SpireEnum(name = "theastray_ex_color")
    public static AbstractCard.CardColor TAE_CardColor;
    @SpireEnum(name = "theastray_ex_color")
    public static CardLibrary.LibraryType TAE_LibrType;
    
    @SpireEnum(name = "theastray_character")
    public static AbstractPlayer.PlayerClass BlueTheAstray;
    
    @SpireEnum(name = "theastray_magical_tag")
    public static AbstractCard.CardTags MAGICAL;
    @SpireEnum(name = "theastray_storage_tag")
    public static AbstractCard.CardTags STORAGE;
}