/**
* This file is based on and translated from the open source project: Sparcraft
* https://code.google.com/p/sparcraft/
* author of the source: David Churchill
**/
package sparcraft;

import javabot.types.UnitSizeType;
import javabot.types.UpgradeType;
import javabot.types.UpgradeType.UpgradeTypes;
import javabot.types.WeaponType;
import javabot.types.WeaponType.WeaponTypes;

public class WeaponProperties {
	static WeaponProperties[] props=new WeaponProperties[256];

	WeaponType			type;

	UpgradeType			rangeUpgrade;
	UpgradeType			speedUpgrade;

	int[] cooldown=new int[2];
	int[] maxRange=new int[2];
	
	int pixelShift=10;
	float[][] damageMultipliers=new float[][]{
		{0,  0,   0,    0,     0,  0}, 
		{0,  .5f, .75f, 1,     0,  0}, 
		{0,  1,   .5f,  .25f,  0,  0},
		{0,  1,   1,    1,     0,  0},
		{0,  1,   1,    1,     0,  0},
		{0,  0,   0,    0,     0,  0}, 
		{0,  0,   0,    0,     0,  0}
	};
	
	public WeaponProperties(){
	    rangeUpgrade=UpgradeTypes.None;
	    speedUpgrade=UpgradeTypes.None;
	}

	public void SetType(WeaponType type)
	{
		if (type!=null){
			this.type		= type; 
			cooldown[0]		= type.getDamageCooldown();
		    cooldown[1]		= type.getDamageCooldown();
			maxRange[0]		= type.getMaxRange();
		    maxRange[1]		= type.getMaxRange();
		}
	}

	public void SetRangeUpgrade(UpgradeType upgrade, int maxRange)
	{
		if (upgrade!=null){
			rangeUpgrade		= upgrade;
			this.maxRange[1]	= (maxRange << 5);
		}
	}

	public void SetSpeedUpgrade(UpgradeType upgrade, int cooldown)
	{
		if (upgrade!=null){
			speedUpgrade		= upgrade;
			this.cooldown[1]	= cooldown;
		}
	}


	public static void Init()
	{
	    for (WeaponTypes type : WeaponTypes.values())
		{
			props[type.ordinal()]=new WeaponProperties(); // NEED TO INIT
//			props[type.ordinal()].SetType(type); // TODO: UNCOMMENT THIS. POTENTIAL SOURCE OF BUG/ERROR
		}

		props[WeaponTypes.Gauss_Rifle.ordinal()].SetRangeUpgrade(UpgradeTypes.U_238_Shells, 5);	// Terran Marine ground/air attack

		props[WeaponTypes.Hellfire_Missile_Pack.ordinal()].SetRangeUpgrade(UpgradeTypes.Charon_Boosters, 8);	// Terran Goliath air attack

		props[WeaponTypes.Claws.ordinal()].SetSpeedUpgrade(UpgradeTypes.Adrenal_Glands, 6);	// Zerg Zergling ground attack

		props[WeaponTypes.Needle_Spines.ordinal()].SetRangeUpgrade(UpgradeTypes.Grooved_Spines, 5); // Zerg Hydralisk ground/air attack

		props[WeaponTypes.Phase_Disruptor.ordinal()].SetRangeUpgrade(UpgradeTypes.Singularity_Charge,6);	// Protoss Dragoon ground/air attack
	}

	public int GetDamageBase(PlayerProperties player) 
	{ 
	    return type.getDamageAmount() + player.GetUpgradeLevel(type) * type.getDamageFactor(); 
	}

	public float GetDamageMultiplier(UnitSizeType targetSize) 
	{ 
	    return damageMultipliers[type.getDamageTypeID()][targetSize.getID()]; 
	}
	
	public float GetDamageMultiplier(int targetSize) 
	{ 
	    return damageMultipliers[type.getDamageTypeID()][targetSize]; 
	}

	public int GetCooldown(PlayerProperties player)
	{ 
	    return cooldown[player.GetUpgradeLevel(speedUpgrade)]; 
	}

	public int GetMaxRange(PlayerProperties player)
	{ 
	    return maxRange[player.GetUpgradeLevel(rangeUpgrade)];
	}

	public static WeaponProperties Get(WeaponType type) 
	{ 
	    return props[type.getID()]; 
	}
	public static WeaponProperties Get(int type) 
	{ 
	    return props[type]; 
	}
}
