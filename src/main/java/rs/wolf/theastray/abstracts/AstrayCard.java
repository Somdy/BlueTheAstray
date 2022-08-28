package rs.wolf.theastray.abstracts;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
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
import rs.lazymankits.interfaces.cards.SwappableUpgBranchCard;
import rs.lazymankits.interfaces.cards.UpgradeBranch;
import rs.lazymankits.utils.LMSK;
import rs.wolf.theastray.commands.Cheat;
import rs.wolf.theastray.data.CardData;
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
public abstract class AstrayCard extends LMCustomCard implements TAUtils, BranchableUpgradeCard, SwappableUpgBranchCard {
    
    private int baseExtraMagic; // 基础的额外数值
    private int extraMagic; // 额外数值
    private boolean upgradedExtraMagic;
    private boolean isExtraMagicModified;
    private int basePromos; // 基础晋升数值
    private int promos; // 晋升数值
    private boolean upgradedPromos;
    private boolean isPromosModified;
    private int extensionNeed; // 扩展所需启迪牌数
    private boolean isExtension; // 是否为扩展牌
    private int manaCost; // 魔法牌法力消耗
    private boolean isMagical; // 是否为魔法牌
    private boolean isMagicalDerivative; // 是否为衍生魔法牌
    
    private boolean branchable; // 是否可分支
    private boolean swappable; // 是否可互换分支
    
    public final CardData data;
    protected TACardLocals cardLocals;
    protected String NAME;
    protected String DESCRIPTION;
    protected String[] UPGRADED_DESC;
    protected String[] UPDATED_DESC;
    protected String[] MSG;
    
    protected final TACardLocals cardStrings = TALocalLoader.CARD(TAUtils.MakeID("AstrayCard"));
    protected final String[] U_MSG = cardStrings.MSG;
    
    public AstrayCard(@NotNull CardData data, int cost, CardColor color, CardTarget target) {
        super(data.getCardID(), "uninitialized", TAUtils.CardImage(Integer.parseInt(data.getInternalID().substring(1))), 
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
        if (isMagical() || isMagicalDerivative()) {
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
                    tmp += ((MagicModifier) r).modifyValue(this);
            }
        }
        boolean hasPower = cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier);
        if (hasPower) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyValue(this);
            }
        }
        if (cpr().stance instanceof MagicModifier)
            tmp += ((MagicModifier) cpr().stance).modifyValue(this);
        if (hasPower) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyValueLast(this);
            }
        }
        if (tmp < 0F) tmp = 0F;
        return tmp;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (isMagical() || isMagicalDerivative()) {
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
                    tmp += ((MagicModifier) r).modifyValue(this);
            }
        }
        boolean hasPower = cpr().powers.stream().anyMatch(p -> p instanceof MagicModifier);
        if (hasPower) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyValue(this);
            }
        }
        if (cpr().stance instanceof MagicModifier)
            tmp += ((MagicModifier) cpr().stance).modifyValue(this);
        boolean hasMonsterPower = mo != null && !mo.isDeadOrEscaped()
                && mo.powers.stream().anyMatch(p -> p instanceof MagicModifier);
        if (hasMonsterPower) {
            for (AbstractPower p : mo.powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyValue(this);
            }
        }
        if (hasPower) {
            for (AbstractPower p : cpr().powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyValueLast(this);
            }
        }
        if (hasMonsterPower) {
            for (AbstractPower p : mo.powers) {
                if (p instanceof MagicModifier)
                    tmp += ((MagicModifier) p).modifyValueLast(this);
            }
        }
        if (tmp < 0F) tmp = 0F;
        return tmp;
    }
    
    @Override
    protected void applyPowersToBlock() {
        if (isMagical() || isMagicalDerivative()) {
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
                    tmp += ((MagicModifier) p).modifyValue(this);
                    tmp += ((MagicModifier) p).modifyValueLast(this);
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
                        tmp += ((MagicModifier) p).modifyValue(this);
                        tmp += ((MagicModifier) p).modifyValueLast(this);
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
                        tmp += ((MagicModifier) p).modifyValue(this);
                        tmp += ((MagicModifier) p).modifyValueLast(this);
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
        boolean hasEnoughMana = !isMagical() && !isMagicalDerivative() || hasEnoughMana();
        return super.hasEnoughEnergy() && hasEnoughMana;
    }
    
    protected boolean hasEnoughMana() {
//        if (!isMagical() && !isMagicalDerivative()) return true;
        if (isInAutoplay) return true;
        int thisManaCost = getManaOnUse();
        boolean hasMana = GlobalManaMst.HasMana();
        boolean hasEnoughMana = GlobalManaMst.HasEnoughMana(thisManaCost);
        if (isMagicalDerivative() && hasMana) return true;
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
        play(p, m);
    }
    
    /**
     * 卡牌打出效果，即在 {@link #use(AbstractPlayer, AbstractMonster)} 时触发，用于子类卡牌重写卡牌效果
     * @param s 来源，一般为 {@link com.megacrit.cardcrawl.dungeons.AbstractDungeon#player}
     * @param t 目标，即玩家指向的目标，无指向的牌会接收 null
     */
    public abstract void play(AbstractCreature s, AbstractCreature t);
    
    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (isUpgradedPromos())
            setPromosModified(true);
    }
    
    @Override
    public boolean canUpgrade() {
        return super.canUpgrade() && (!canEnlighten() || readyToEnlighten());
    }
    
    @Override
    public final void upgrade() {
        if (canUpgrade())
            selfUpgrade();
    }
    
    public abstract void selfUpgrade();
    
    /**
     * 以选定的分支升级
     */
    protected void branchingUpgrade() {
        upgradeWithTheCorrectBranch();
    }
    
    /**
     * 更新文本描述
     * @param newDescription 新的文本描述
     */
    protected void updateDescription(String newDescription) {
        rawDescription = newDescription;
        initializeDescription();
    }
    
    /**
     * 升级该牌的名称，同时根据所给的分支，升级相应的文本。
     * @param branchIndex 启迪牌的分支，0 或 1
     */
    protected void upgradeTexts(int branchIndex) {
        upgradeName();
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
     * 升级该牌的名称以及相应的文本。
     */
    protected void upgradeTexts() {
        upgradeTexts(0);
    }
    
    /**
     * 设置该牌是否有 <strong>储能</strong>
     * @param storage 要该牌具有储能，传入 true
     */
    public void setStorage(boolean storage) {
        if (storage) 
            addTags(TACardEnums.STORAGE);
        else 
            tags.remove(TACardEnums.STORAGE);
    }
    
    public void upgradeExtraMagic(int num) {
        baseExtraMagic += num;
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
     * 设置该扩展牌牌所需的启迪牌数量。子类不要使用该方法设置扩展牌，而应使用 {@link #setExtension(boolean, int)}
     * @param extensionNeed 扩展所需的启迪牌数量
     */
    public final void setExtensionNeed(int extensionNeed) {
        this.extensionNeed = extensionNeed;
    }
    
    public final boolean isExtension() {
        return isExtension;
    }
    
    /**
     * 设置该牌是否为扩展牌，所有牌默认为非扩展牌。
     * @param extension 要设该牌为扩展牌，传入 true
     * @param extensionNeed 扩展所需的启迪牌数量
     * @return 该牌本身
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
     * 判断该牌是否为魔法牌
     * @return 当该牌为魔法牌时，返回 true，否则返回 false
     */
    public final boolean isMagical() {
        return isMagical;
    }
    
    /**
     * 设置该牌为魔法牌，所有牌默认为非魔法牌
     * @param magical 要设该牌为魔法牌，传入 true
     * @param manaCost 该牌消耗的法力，默认为 1
     * @return 该牌本身
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
     * 设置该牌为魔法牌，且法力消耗为 1，所有牌默认为非魔法牌
     * @param magical 要设该牌为魔法牌，传入 true
     * @return 该牌本身
     * @see #setMagical(boolean, int) 
     */
    public final AstrayCard setMagical(boolean magical) {
        return setMagical(magical, 1);
    }
    
    /**
     * 判断该牌是否为衍生魔法牌
     * @return 当该牌为衍生魔法牌时，返回 true，否则返回 false
     */
    public final boolean isMagicalDerivative() {
        return isMagicalDerivative && isMagical();
    }
    
    /**
     * 设置该牌为衍生魔法牌，且法力消耗为 0，所有牌默认为非衍生魔法牌
     * @param magicalDerivative 要设该牌为衍生魔法牌，传入 true
     * @see #setMagical(boolean, int)
     * @see #setMagical(boolean) 
     */
    public final void setMagicalDerivative(boolean magicalDerivative) {
        isMagicalDerivative = magicalDerivative;
        setMagical(magicalDerivative, 0);
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
     * 设置该牌是否为启迪牌，所有卡牌默认为非启迪牌
     * @param canEnlighten 要设该牌为启迪牌，传入 true
     * @return 该牌本身
     */
    public final AstrayCard setCanEnlighten(boolean canEnlighten) {
        setBranchable(canEnlighten); 
        setSwappable(canEnlighten);
        return this;
    }
    
    /**
     * 判断该牌是否为启迪牌。包含两个条件 {@link #isBranchable()} 和 {@link #isSwappable()}
     * @return 当且仅当 {@link #isBranchable()} 和 {@link #isSwappable()} 均为 true 时，返回 true，否则返回 false
     */
    public boolean canEnlighten() {
        return isBranchable() && isSwappable();
    }
    
    public boolean readyToEnlighten() {
        return canEnlighten() && !upgraded;
    }
    
    @Override
    public final boolean canBranch() {
        return canEnlighten();
    }
    
    /**
     * 判断玩家是否在火堆
     * @return 当玩家在火堆，返回 true，否则 false
     */
    protected final boolean inRestroom() {
        return TAUtils.RoomChecker(RestRoom.class, AbstractRoom.RoomPhase.INCOMPLETE);
    }
    
    protected final boolean outOfDungeon() {
        return !TAUtils.RoomAvailable();
    }
    
    @Override
    public final boolean canSwap() {
        return canEnlighten();
    }
    
    @Override
    public final boolean usingLocalBranch() {
        return true;
    }
    
    /**
     * 返回该牌可用的升级分支，仅对 {@link #canEnlighten()} 为 true 的牌生效。
     * 当 {@link #inRestroom()} 为 true，即在火堆升级，该方法返回所有可用的升级分支
     * 当 {@link #inRestroom()} 为 false，即不在火堆处升级，
     * 该方法通过 {@link com.megacrit.cardcrawl.dungeons.AbstractDungeon#cardRandomRng} 随机返回一个可用分支
     * @return 当 {@link #inRestroom()} 为 true，返回所有可用分支，否则返回一个随机分支
     */
    @Override
    public List<UpgradeBranch> getPossibleBranches() {
        if (canEnlighten()) {
            if (inRestroom() || outOfDungeon() || Cheat.IsCheating(Cheat.IEL)) return possibleBranches();
            Random copy = LMSK.CardRandomRng().copy();
            int index = copy.random(branches().size() - 1);
            return new ArrayList<UpgradeBranch>() {{
                add(branches().get(index));
            }};
        }
        return null;
    }
    
    @Override
    public final List<UpgradeBranch> possibleBranches() {
        return branches();
    }
    
    /**
     * 启迪牌需重写该方法，以返回可用的升级分支
     * @return 一组该牌的升级分支（1和2）
     */
    protected List<UpgradeBranch> branches() {
        return null;
    }
    
    protected void log(Object what) {
        TAUtils.Log(this, "[" + name + "] " + what);
    }
    
    /**
     * 根据目标敌人返回余烬层数
     * @param originalValue 基础层数
     * @param target 目标敌人
     * @return 若目标无攻击意图，返回基础层数，否则返回两倍的层数
     */
    protected int burnt(int originalValue, AbstractCreature target) {
        if (target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() > 0)
            originalValue *= 2;
        if (originalValue < 0) originalValue = 0;
        return originalValue;
    }
    
    /**
     * 根据目标敌人返回相应层数的余烬效果
     * @param t 目标敌人
     * @param s 来源
     * @param amount 基础层数
     * @return 若目标无攻击意图，返回基础层数的余烬，否则返回两倍层数的余烬
     */
    protected BurntPower burntPower(AbstractCreature t, AbstractCreature s, int amount) {
        return new BurntPower(t, s, burnt(amount, t));
    }
    
    /**
     * 根据目标敌人返回冰霜层数
     * @param originalValue 基础层数
     * @param target 目标敌人
     * @return 若目标有攻击意图，返回基础层数，否则返回两倍的层数
     */
    protected int frost(int originalValue, AbstractCreature target) {
        if (target instanceof AbstractMonster && ((AbstractMonster) target).getIntentBaseDmg() <= 0)
            originalValue *= 2;
        if (originalValue < 0) originalValue = 0;
        return originalValue;
    }
    
    /**
     * 根据目标敌人返回相应层数的冰霜效果
     * @param t 目标敌人
     * @param s 来源
     * @param amount 基础层数
     * @return 若目标有攻击意图，返回基础层数的冰霜，否则返回两倍层数的冰霜
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
     * 当玩家获得任意能力时触发
     * @param power 获得的能力
     * @param source 来源
     */
    public void onPlayerReceivePower(AbstractPower power, AbstractCreature source) {}
    
    /**
     * 当玩家给予任意能力时触发
     * @param power 给予的能力
     * @param target 目标
     */
    public void onPlayerApplyPower(AbstractPower power, AbstractCreature target) {}
    
    public void onManaRunOut() {}
    
    public int modifyOnGainingMana(int amount) {
        return amount;
    }
    
    public void onManaGained(int amount) {}
    
    public void onStorageTriggered(boolean energy, boolean mana) {}
    
    public void onNeitherStorageTriggers() {}
}