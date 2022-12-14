package rs.wolf.theastray.abstracts;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import org.jetbrains.annotations.NotNull;
import rs.lazymankits.abstracts.LMCustomCard;
import rs.lazymankits.actions.common.BetterDamageAllEnemiesAction;
import rs.lazymankits.actions.common.NullableSrcDamageAction;
import rs.lazymankits.actions.utility.QuickAction;
import rs.lazymankits.interfaces.cards.BranchableUpgradeCard;
import rs.lazymankits.interfaces.cards.SensitiveTriggerOnUseCard;
import rs.lazymankits.interfaces.cards.SwappableUpgBranchCard;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.commands.Cheat;
import rs.wolf.theastray.core.CardMst;
import rs.wolf.theastray.data.CardData;
import rs.wolf.theastray.interfaces.DeMagicSensitiveGear;
import rs.wolf.theastray.interfaces.MagicModifier;
import rs.wolf.theastray.localizations.TACardLocals;
import rs.wolf.theastray.localizations.TALocalLoader;
import rs.wolf.theastray.patches.TACardEnums;
import rs.wolf.theastray.powers.BurntPower;
import rs.wolf.theastray.powers.FrostPower;
import rs.wolf.theastray.utils.GlobalManaMst;
import rs.wolf.theastray.utils.TAUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class AstrayCard extends LMCustomCard implements TAUtils, BranchableUpgradeCard, SwappableUpgBranchCard, 
        SensitiveTriggerOnUseCard {
    
    private int baseExtraMagic; // ?????????????????????
    private int extraMagic; // ????????????
    private boolean upgradedExtraMagic;
    private boolean isExtraMagicModified;
    private int basePromos; // ??????????????????
    private int promos; // ????????????
    private boolean upgradedPromos;
    private boolean isPromosModified;
    private int extensionNeed; // ????????????????????????
    private boolean isExtension; // ??????????????????
    private int manaCost; // ?????????????????????
    private boolean isMagical; // ??????????????????
    private boolean isMagicalDerivative; // ????????????????????????
    
    private boolean branchable; // ???????????????
    private boolean swappable; // ?????????????????????
    private boolean fakeRestroom;
    
    public final CardData data;
    protected TACardLocals cardLocals;
    protected String NAME;
    protected String DESCRIPTION;
    protected String[] UPGRADED_DESC;
    protected String[] UPDATED_DESC;
    protected String[] MSG;
    
    protected final TACardLocals cardStrings = TALocalLoader.CARD(TAUtils.MakeID("AstrayCard"));
    protected final String[] U_MSG = cardStrings.MSG;
    protected final TooltipInfo ILLUSION_CARD = new TooltipInfo(U_MSG[1], U_MSG[2]);
    
    public AstrayCard(@NotNull CardData data, int cost, CardColor color, CardTarget target) {
        super(data.getCardID(), "uninitialized", TAUtils.CardImage(data.getInternalID().substring(1)), 
                cost, "uninitialized", data.getType(), color, data.getRarity(), target);
        this.data = data;
        setCanEnlighten(false);
        setMagical(false);
        setMagicalDerivative(false);
        initLocals();
    }
    
    private void initLocals() {
        cardLocals = TALocalLoader.CARD(data.getInternalID());
        NAME = cardLocals.NAME;
        DESCRIPTION = cardLocals.DESCRIPTION;
        if (cardLocals.UPGRADED_DESC != null)
            UPGRADED_DESC = cardLocals.UPGRADED_DESC;
        if (cardLocals.UPDATED_DESC != null)
            UPDATED_DESC = cardLocals.UPDATED_DESC;
        if (cardLocals.MSG != null)
            MSG = cardLocals.MSG;
        name = NAME;
        rawDescription = DESCRIPTION;
        initializeTitle();
        initializeDescription();
    }
    
    @Override
    public void applyPowers() {
        if (isMagical()) {
            applyMagicalPowers();
        } else {
            super.applyPowers();
        }
    }
    
    protected void applyMagicalPowers() {
        applyPowersToMagics();
        applyPowersToBlock();
        isDamageModified = false;
        if (!isMultiDamage) {
            float tmp = baseDamage;
            tmp = applyMagicalDmg(tmp);
            if (baseDamage != MathUtils.floor(tmp))
                isDamageModified = true;
            damage = MathUtils.floor(tmp);
        } else {
            float[] values = new float[currRoom().monsters.monsters.size()];
            multiDamage = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                float tmp = baseDamage;
                tmp = applyMagicalDmg(tmp);
                if (baseDamage != MathUtils.floor(tmp))
                    isDamageModified = true;
                multiDamage[i] = MathUtils.floor(tmp);
            }
            damage = multiDamage[0];
        }
    }
    
    private float applyMagicalDmg(float base) {
        float tmp = base;
        if (cpr().relics.stream().anyMatch(r -> r instanceof MagicModifier)) {
            for (AbstractRelic r : cpr().relics) {
                if (r instanceof MagicModifier) 
                    tmp += ((MagicModifier) r).modifyDamageValue(this);
            }
        }
        boolean hasPower = cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier);
        if (hasPower) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyDamageValue(this);
            }
        }
        if (cpr().stance instanceof MagicModifier)
            tmp += ((MagicModifier) cpr().stance).modifyDamageValue(this);
        if (tmp < 0F) tmp = 0F;
        return tmp;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (isMagical()) {
            calculateMagicalCardDamage(mo);
        } else {
            super.calculateCardDamage(mo);
        }
    }
    
    protected void calculateMagicalCardDamage(AbstractMonster mo) {
        applyPowersToMagics();
        applyPowersToBlock();
        isDamageModified = false;
        if (!isMultiDamage && mo != null) {
            float tmp = baseDamage;
            tmp = calculateMagicalDmg(tmp, mo);
            if (baseDamage != MathUtils.floor(tmp))
                isDamageModified = true;
            damage = MathUtils.floor(tmp);
        } else {
            List<AbstractMonster> m = currRoom().monsters.monsters;
            float[] values = new float[m.size()];
            multiDamage = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                float tmp = baseDamage;
                tmp = calculateMagicalDmg(tmp, m.get(i));
                if (baseDamage != MathUtils.floor(tmp))
                    isDamageModified = true;
                multiDamage[i] = MathUtils.floor(tmp);
            }
            damage = multiDamage[0];
        }
    }
    
    private float calculateMagicalDmg(float base, AbstractMonster mo) {
        float tmp = base;
        if (cpr().relics.stream().anyMatch(r -> r instanceof MagicModifier)) {
            for (AbstractRelic r : cpr().relics) {
                if (r instanceof MagicModifier)
                    tmp += ((MagicModifier) r).modifyDamageValue(this);
            }
        }
        boolean hasPower = cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier);
        if (hasPower) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyDamageValue(this);
            }
        }
        if (cpr().stance instanceof MagicModifier)
            tmp += ((MagicModifier) cpr().stance).modifyDamageValue(this);
        boolean hasMonsterPower = mo != null && !mo.isDeadOrEscaped()
                && mo.powers.stream().anyMatch(p -> p instanceof MagicModifier);
        if (hasMonsterPower) {
            for (AbstractPower p : mo.powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyDamageValue(this);
            }
        }
        if (tmp < 0F) tmp = 0F;
        return tmp;
    }
    
    @Override
    protected void applyPowersToBlock() {
        if (isMagical()) {
            applyMagicalBlock();
        } else {
            super.applyPowersToBlock();
        }
    }
    
    protected void applyMagicalBlock() {
        isBlockModified = false;
        float tmp = baseBlock;
        if (cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier)) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier) {
                    tmp += ((MagicModifier) p).modifyBlockValue(this);
                }
            }
        }
        if (baseBlock != MathUtils.floor(tmp))
            isBlockModified = true;
        if (tmp < 0F) tmp = 0F;
        block = MathUtils.floor(tmp);
    }
    
    protected void applyPowersToMagics() {
        {
            isMagicNumberModified = false;
            float tmp = baseMagicNumber;
            if (cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier)) {
                for (AbstractPower p : cpr().powers) {
                    if (p instanceof MagicModifier) {
                        tmp += ((MagicModifier) p).modifyMagicValue(this);
                    }
                }
            }
            if (baseMagicNumber != MathUtils.floor(tmp))
                isMagicNumberModified = true;
            if (tmp < 0F) tmp = 0F;
            magicNumber = MathUtils.floor(tmp);
        }
        {
            isExtraMagicModified = false;
            float tmp = baseExtraMagic;
            if (cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier)) {
                for (AbstractPower p : cpr().powers) {
                    if (p instanceof MagicModifier) {
                        tmp += ((MagicModifier) p).modifyMagicValue(this);
                    }
                }
            }
            if (baseExtraMagic != MathUtils.floor(tmp))
                isExtraMagicModified = true;
            if (tmp < 0F) tmp = 0F;
            extraMagic = MathUtils.floor(tmp);
        }
    }
    
    @Override
    public boolean hasEnoughEnergy() {
        boolean hasEnoughMana = !isMagical() || hasEnoughMana();
        return super.hasEnoughEnergy() && hasEnoughMana;
    }
    
    protected boolean hasEnoughMana() {
//        if (!isMagical() && !isMagicalDerivative()) return true;
        if (isInAutoplay || Cheat.IsCheating(Cheat.IMC)) return true;
        int thisManaCost = getManaOnUse();
        boolean hasMana = GlobalManaMst.HasMana();
        boolean hasEnoughMana = GlobalManaMst.HasEnoughMana(thisManaCost);
        if (isMagicalDerivative()) return true;
        if (hasEnoughMana && hasMana) return true;
        cantUseMessage = U_MSG[0];
        return false;
    }
    
    protected int selfModifyManaOnUse(int manaCost) {
        return manaCost;
    }
    
    public final int getManaOnUse() {
        return selfModifyManaOnUse(manaCost);
    }
    
    @Override
    public final void use(AbstractPlayer p, AbstractMonster m) {
        beforePlaying(p, m);
        play(p, m);
    }
    
    /**
     * ??????????????????????????? {@link #use(AbstractPlayer, AbstractMonster)} ????????????????????????????????????????????????
     * @param s ?????????????????? {@link com.megacrit.cardcrawl.dungeons.AbstractDungeon#player}
     * @param t ???????????????????????????????????????????????????????????? null
     */
    public abstract void play(AbstractCreature s, AbstractCreature t);
    
    protected void beforePlaying(AbstractCreature s, AbstractCreature t) {}
    
    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (isUpgradedPromos())
            setPromosModified(true);
        if (isUpgradedExtraMagic())
            setExtraMagicModified(true);
    }
    
    @Override
    public final void upgrade() {
        if (canUpgrade())
            selfUpgrade();
    }
    
    public abstract void selfUpgrade();
    
    /**
     * ????????????????????????
     */
    protected void branchingUpgrade() {
        upgradeAndCorrectBranch();
    }
    
    /**
     * ??????????????????
     * @param newDescription ??????????????????
     */
    public void updateDescription(String newDescription) {
        rawDescription = newDescription;
        initializeDescription();
    }
    
    /**
     * ??????????????????????????????????????????????????????????????????????????????
     * @param branchIndex ?????????????????????0 ??? 1
     */
    protected void upgradeTexts(int branchIndex) {
        upgradeName();
        if (isEnlightenCard())
            setLocalBranch(branchIndex);
        if (UPGRADED_DESC != null) {
            if (branchIndex >= UPGRADED_DESC.length) {
                log("has no [" + branchIndex + "] upgraded description");
                branchIndex = UPGRADED_DESC.length - 1;
            }
            updateDescription(UPGRADED_DESC[branchIndex]);
        }
    }
    
    /**
     * ?????????????????????????????????????????????
     */
    protected void upgradeTexts() {
        upgradeTexts(0);
    }
    
    /**
     * ????????????????????? <strong>??????</strong>
     * @param storage ?????????????????????????????? true
     */
    public void setStorage(boolean storage) {
        if (storage) 
            addTags(TACardEnums.STORAGE);
        else 
            tags.remove(TACardEnums.STORAGE);
    }
    
    public void upgradeExtraMagic(int num) {
        baseExtraMagic += num;
        extraMagic = baseExtraMagic;
        setUpgradedExtraMagic(true);
    }
    
    public int getBaseExtraMagic() {
        return baseExtraMagic;
    }
    
    public void setExtraMagicValue(int num, boolean ignoreModifier) {
        if (ignoreModifier || !isExtraMagicModified() && !isUpgradedExtraMagic()) {
            extraMagic = baseExtraMagic = num;
            if (baseExtraMagic < 0)
                baseExtraMagic = 0;
        }
    }
    
    public int getExtraMagic() {
        return extraMagic;
    }
    
    public final boolean isUpgradedExtraMagic() {
        return upgradedExtraMagic;
    }
    
    public final void setUpgradedExtraMagic(boolean upgradedExtraMagic) {
        this.upgradedExtraMagic = upgradedExtraMagic;
    }
    
    public final boolean isExtraMagicModified() {
        return isExtraMagicModified;
    }
    
    public final void setExtraMagicModified(boolean extraMagicModified) {
        isExtraMagicModified = extraMagicModified;
    }
    
    public void upgradePromos(int num) {
        basePromos += num;
        promos += num;
        setUpgradedPromos(true);
    }
    
    public final int getBasePromos() {
        return basePromos;
    }
    
    public void setPromosValue(int num, boolean ignoreModifier) {
        if (ignoreModifier || !isPromosModified() && !isUpgradedPromos()) {
            promos = basePromos = num;
            if (basePromos < 0) {
                basePromos = 0;
            }
        }
    }
    
    public void setBasePromos(int basePromos) {
        this.basePromos = basePromos;
    }
    
    public void setCurrPromos(int promos) {
        this.promos = promos;
    }
    
    public final int getPromos() {
        return promos;
    }
    
    public final boolean isUpgradedPromos() {
        return upgradedPromos;
    }
    
    public final void setUpgradedPromos(boolean upgradedPromos) {
        this.upgradedPromos = upgradedPromos;
    }
    
    public final boolean isPromosModified() {
        return isPromosModified;
    }
    
    public final void setPromosModified(boolean promosModified) {
        isPromosModified = promosModified;
    }
    
    public final int getExtensionNeed() {
        return extensionNeed;
    }
    
    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????? {@link #setExtension(boolean, int)}
     * @param extensionNeed ??????????????????????????????
     */
    public final void setExtensionNeed(int extensionNeed) {
        this.extensionNeed = extensionNeed;
    }
    
    public final boolean isExtension() {
        return isExtension;
    }
    
    /**
     * ??????????????????????????????????????????????????????????????????
     * @param extension ????????????????????????????????? true
     * @param extensionNeed ??????????????????????????????
     * @return ????????????
     */
    public final AstrayCard setExtension(boolean extension, int extensionNeed) {
        isExtension = extension;
        setExtensionNeed(extensionNeed);
        return this;
    }
    
    public final int getManaCost() {
        return manaCost;
    }
    
    public final void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }
    
    /**
     * ??????????????????????????????
     * @return ????????????????????????????????? true??????????????? false
     */
    public final boolean isMagical() {
        return isMagical || isMagicalDerivative;
    }
    
    /**
     * ?????????????????????????????????????????????????????????
     * @param magical ????????????????????????????????? true
     * @param manaCost ????????????????????????????????? 1
     * @return ????????????
     * @see #setMagical(boolean) 
     */
    public final AstrayCard setMagical(boolean magical, int manaCost) {
        isMagical = magical;
        if (magical) {
            setManaCost(manaCost);
            addTags(TACardEnums.MAGICAL);
        } else {
            tags.remove(TACardEnums.MAGICAL);
        }
        return this;
    }
    
    /**
     * ????????????????????????????????????????????? 1?????????????????????????????????
     * @param magical ????????????????????????????????? true
     * @return ????????????
     * @see #setMagical(boolean, int) 
     */
    public final AstrayCard setMagical(boolean magical) {
        return setMagical(magical, 1);
    }
    
    /**
     * ????????????????????????????????????
     * @return ??????????????????????????????????????? true??????????????? false
     */
    public final boolean isMagicalDerivative() {
        return isMagicalDerivative;
    }
    
    /**
     * ??????????????????????????????????????????????????? 0???????????????????????????????????????
     * @param magicalDerivative ??????????????????????????????????????? true
     * @see #setMagical(boolean, int)
     * @see #setMagical(boolean) 
     */
    public final void setMagicalDerivative(boolean magicalDerivative) {
        isMagicalDerivative = magicalDerivative;
        if (magicalDerivative) {
            setMagical(true, 0);
            addTags(TACardEnums.DE_MAGICAL);
        } else {
            tags.remove(TACardEnums.DE_MAGICAL);
        }
    }
    
    public final void setReturnToHand(boolean returnToHand) {
        if (returnToHand) 
            addTags(TACardEnums.RETURN_TO_HAND);
        else 
            tags.remove(TACardEnums.RETURN_TO_HAND);
    }
    
    public final boolean isBranchable() {
        return branchable;
    }
    
    public final void setBranchable(boolean branchable) {
        this.branchable = branchable;
    }
    
    public final boolean isSwappable() {
        return swappable;
    }
    
    public final void setSwappable(boolean swappable) {
        this.swappable = swappable;
    }
    
    /**
     * ??????????????????????????????????????????????????????????????????
     * @param canEnlighten ????????????????????????????????? true
     * @return ????????????
     */
    public final AstrayCard setCanEnlighten(boolean canEnlighten) {
        setBranchable(canEnlighten); 
        setSwappable(canEnlighten);
        return this;
    }
    
    /**
     * ??????????????????????????????????????????????????? {@link #isBranchable()} ??? {@link #isSwappable()}
     * @return ???????????? {@link #isBranchable()} ??? {@link #isSwappable()} ?????? true ???????????? true??????????????? false
     */
    public boolean isEnlightenCard() {
        return isBranchable() && isSwappable();
    }
    
    @Override
    public final boolean canBranch() {
        return isEnlightenCard();
    }
    
    /**
     * ???????????????????????????
     * @return ??????????????????????????? true????????? false
     */
    protected final boolean inRestroom() {
        return TAUtils.RoomChecker(RestRoom.class) || fakeRestroom;
    }
    
    public final void setFakeRestroom(boolean fakeRestroom) {
        this.fakeRestroom = fakeRestroom;
    }
    
    protected final boolean outOfDungeon() {
        return !TAUtils.RoomAvailable();
    }
    
    protected final boolean inSinglePopupView() {
        return CardCrawlGame.cardPopup != null && CardCrawlGame.cardPopup.isOpen;
    }
    
    @Override
    public final boolean canSwap() {
        return isEnlightenCard();
    }
    
    @Override
    public final boolean usingLocalBranch() {
        return true;
    }
    
    /**
     * ?????????????????????????????????????????? {@link #isEnlightenCard()} ??? true ???????????????
     * ??? {@link #inRestroom()} ??? true??????????????????????????????????????????????????????????????????
     * ??? {@link #inRestroom()} ??? false??????????????????????????????
     * ??????????????? {@link com.megacrit.cardcrawl.dungeons.AbstractDungeon#cardRandomRng} ??????????????????????????????
     * @return ??? {@link #inRestroom()} ??? true????????????????????????????????????????????????????????????
     */
    @Override
    public List<UpgradeBranch> getPossibleBranches() {
        if (isEnlightenCard()) {
            if (inRestroom() || outOfDungeon() || inSinglePopupView() || Cheat.IsCheating(Cheat.IEL))
                return possibleBranches();
            Random copy = LMSK.CardRandomRng().copy();
            int index = copy.random(branches().size() - 1);
            return new ArrayList<UpgradeBranch>() {{
                add(branches().get(index));
            }};
        }
        return new ArrayList<>();
    }
    
    @Override
    public int getBranchForRandomUpgrading(int msg) {
        if (isEnlightenCard()) {
            List<UpgradeBranch> branches = branches();
            if (branches != null) {
                return LMSK.CardRandomRng().random(branches.size() - 1);
            } else {
                log("branchable but has no branches");
            }
        }
        return defaultBranch();
    }
    
    @Override
    public final List<UpgradeBranch> possibleBranches() {
        return branches();
    }
    
    /**
     * ????????????????????????????????????????????????????????????
     * @return ??????????????????????????????1???2???
     */
    protected List<UpgradeBranch> branches() {
        return null;
    }
    
    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if (card instanceof AstrayCard) {
            ((AstrayCard) card).setFakeRestroom(this.fakeRestroom);
            ((AstrayCard) card).setPromosValue(this.getBasePromos(), true);
        }
        return card;
    }
    
    @Override
    public final boolean isSensitive() {
        return isMagicalDerivative();
    }
    
    @Override
    public final boolean canTriggerOnGear(Object o, String s) {
        return o instanceof DeMagicSensitiveGear;
    }
    
    @Override
    public boolean countInCombatHistory() {
        return false;
    }
    
    @Override
    public void doSelfCombatRecord() {
        CardMst.DeMagicPlayedThisCombat.add(this);
    }
    
    @Override
    public void doSelfTurnRecord() {
        CardMst.DeMagicPlayedThisTurn.add(this);
    }
    
    protected void log(Object what) {
        TAUtils.Log(this, "[" + name + "] " + what);
    }
    
    /**
     * ????????????????????????????????????
     * @param originalValue ????????????
     * @param target ????????????
     * @return ???????????????????????????????????????????????????????????????????????????
     */
    protected int burnt(int originalValue, AbstractCreature target) {
        if (target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() > 0)
            originalValue *= 2;
        if (originalValue < 0) originalValue = 0;
        return originalValue;
    }
    
    /**
     * ???????????????????????????????????????????????????
     * @param t ????????????
     * @param s ??????
     * @param amount ????????????
     * @return ??????????????????????????????????????????????????????????????????????????????????????????
     */
    protected BurntPower burntPower(AbstractCreature t, AbstractCreature s, int amount) {
        return new BurntPower(t, s, burnt(amount, t));
    }
    
    /**
     * ????????????????????????????????????
     * @param originalValue ????????????
     * @param target ????????????
     * @return ???????????????????????????????????????????????????????????????????????????
     */
    protected int frost(int originalValue, AbstractCreature target) {
        if (target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() <= 0)
            originalValue *= 2;
        if (originalValue < 0) originalValue = 0;
        return originalValue;
    }
    
    /**
     * ???????????????????????????????????????????????????
     * @param t ????????????
     * @param s ??????
     * @param amount ????????????
     * @return ??????????????????????????????????????????????????????????????????????????????????????????
     */
    protected FrostPower frostPower(AbstractCreature t, AbstractCreature s, int amount) {
        return new FrostPower(t, s, frost(amount, t));
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage,
                                                   DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        return new NullableSrcDamageAction(t, crtDmgInfo(s, damage, type), effect);
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, int damage,
                                                   AbstractGameAction.AttackEffect effect) {
        return DamageAction(t, s, damage, damageTypeForTurn, effect);
    }
    
    protected NullableSrcDamageAction DamageAction(AbstractCreature t, AbstractCreature s, AbstractGameAction.AttackEffect effect) {
        return DamageAction(t, s, damage, damageTypeForTurn, effect);
    }
    
    protected BetterDamageAllEnemiesAction DamageAllEnemiesAction(AbstractCreature s, DamageInfo.DamageType type, 
                                                                  AbstractGameAction.AttackEffect effect, 
                                                                  Consumer<AbstractCreature> func) {
        if (!isMultiDamage) {
            log("WARNING !!! [" + name + "] USING MULTI DAMAGE WITHOUT SETTING IT BEFORE");
            isMultiDamage = true;
            applyPowers();
        }
        return new BetterDamageAllEnemiesAction(multiDamage, crtDmgSrc(s), type, effect, func);
    }
    
    protected BetterDamageAllEnemiesAction DamageAllEnemiesAction(AbstractCreature s, AbstractGameAction.AttackEffect effect, 
                                                                  Consumer<AbstractCreature> func) {
        return DamageAllEnemiesAction(s, damageTypeForTurn, effect, func);
    }
    
    protected BetterDamageAllEnemiesAction DamageAllEnemiesAction(AbstractCreature s, DamageInfo.DamageType type, 
                                                                  AbstractGameAction.AttackEffect effect) {
        return DamageAllEnemiesAction(s, type, effect, null);
    }
    
    protected BetterDamageAllEnemiesAction DamageAllEnemiesAction(AbstractCreature s, AbstractGameAction.AttackEffect effect) {
        return DamageAllEnemiesAction(s, damageTypeForTurn, effect, null);
    }
    
    protected ApplyPowerAction ApplyPower(AbstractCreature t, AbstractCreature s, AbstractPower p, int stackAmt) {
        return new ApplyPowerAction(t, s, p, stackAmt);
    }
    
    protected ApplyPowerAction ApplyPower(AbstractCreature t, AbstractCreature s, AbstractPower p) {
        return ApplyPower(t, s, p, p.amount);
    }
    
    protected void atbTmpAction(Runnable action) {
        addToBot(new QuickAction(action));
    }
    
    protected void attTmpAction(Runnable action) {
        addToTop(new QuickAction(action));
    }
    
    public final boolean canSpawnInCombat() {
        return selfCanSpawnInCombat();
    }
    
    protected boolean selfCanSpawnInCombat() {
        return true;
    }
    
    /**
     * ????????????????????????????????????
     * @param power ???????????????
     * @param source ??????
     */
    public void onPlayerReceivePower(AbstractPower power, AbstractCreature source) {}
    
    /**
     * ????????????????????????????????????
     * @param power ???????????????
     * @param target ??????
     */
    public void onPlayerApplyPower(AbstractPower power, AbstractCreature target) {}
    
    public void onManaRunOut() {}
    
    public int modifyOnGainingMana(int amount) {
        return amount;
    }
    
    public void onManaGained(int amount) {}
    
    public void onStorageTriggered(boolean energy, boolean mana) {}
    
    public void onNeitherStorageTriggers() {}
    
    public void onVictory() {}
    
    public void onBattleStart() {}
    
    public void onPlayerTurnStart() {}
}