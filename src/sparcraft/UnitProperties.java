/**
* This file is based on and translated from the open source project: Sparcraft
* https://code.google.com/p/sparcraft/
* author of the source: David Churchill
**/
package sparcraft;

import javabot.types.UnitType;
import javabot.types.UpgradeType;

public class UnitProperties {
	static UnitProperties[]	props=new UnitProperties[256];

	UnitType			type;
	int pixelShift=10;
	UpgradeType			capacityUpgrade;
	UpgradeType			extraArmorUpgrade;
	UpgradeType			maxEnergyUpgrade;
	UpgradeType			sightUpgrade;
	UpgradeType			speedUpgrade;

	int[] capacity=new int[2];
	int[] extraArmor=new int[2];
	int[] maxEnergy=new int[2];
	int[] sightRange=new int[2];
	int[] speed=new int[2];
	
	
	
	
	public UnitProperties(){ 
		capacityUpgrade=UpgradeType.UpgradeTypes.None;
		maxEnergyUpgrade=UpgradeType.UpgradeTypes.None;
		sightUpgrade=UpgradeType.UpgradeTypes.None;
		//extraArmorUpgrade=bwapi.UpgradeType.None;
		speedUpgrade=UpgradeType.UpgradeTypes.None;
		capacity[0]		= capacity[1]		= 0;
	}

	public void SetType(UnitType type)
	{
		if (type!=null){
			this.type		= type;
			
			maxEnergy[0]	= maxEnergy[1]		= type.getMaxEnergy();
			sightRange[0]	= sightRange[1]		= type.getSightRange() << pixelShift;
			extraArmor[0]	= extraArmor[1]		= 0;
			speed[0]		= speed[1]			= (int)((1 << pixelShift) * type.getTopSpeed());
		}
	}

	public void SetSpeedUpgrade(UpgradeType upgrade, double rate)
	{
		if (upgrade!=null){
			speedUpgrade				= upgrade;
			speed[1]					= (int)((1 << pixelShift) * rate);
		}
	}

	public void SetCapacityUpgrade(UpgradeType upgrade, int capacity0, int capacity1)
	{
		if (upgrade!=null){
			capacityUpgrade				= upgrade;
			capacity[0]					= capacity0;
			capacity[1]					= capacity1;
		}
	}

	public void SetEnergyUpgrade(UpgradeType upgrade)
	{
		if (upgrade!=null){
			maxEnergyUpgrade			= upgrade;
			maxEnergy[1]				= 250;
		}
	}

	public void SetSightUpgrade(UpgradeType upgrade, int range)
	{ 
		if (upgrade!=null){
			sightUpgrade				= upgrade;
			sightRange[1]				= (range << 5) << pixelShift;
		}
	}

	public void SetExtraArmorUpgrade(UpgradeType upgrade, int amount)
	{
		if (upgrade!=null){
			extraArmorUpgrade			= upgrade;
			extraArmor[1]				= amount;
		}
	}
	
	public static void Init()
	{
//		for (bwapi.UnitType type : bwapi.UnitType.)
//		{
//			props[type]=new UnitProperties();
//			props[type].SetType(type);
//		}
//
//		double standardSpeed=UnitType.Terran_SCV.topSpeed();
//
//		props[UnitType.Terran_Ghost.            ].SetEnergyUpgrade(bwapi.UpgradeType.Moebius_Reactor);
//		props[UnitType.Terran_Ghost            ].SetSightUpgrade(bwapi.UpgradeType.Ocular_Implants, 11);
//
//		props[UnitType.Terran_Medic          ].SetEnergyUpgrade(bwapi.UpgradeType.Caduceus_Reactor);
//
//		props[UnitType.Terran_Vulture          ].SetSpeedUpgrade(bwapi.UpgradeType.Ion_Thrusters,            standardSpeed * 1.881);
//
//		props[UnitType.Terran_Wraith           ].SetEnergyUpgrade(bwapi.UpgradeType.Apollo_Reactor);
//
//		props[UnitType.Terran_Battlecruiser    ].SetEnergyUpgrade(bwapi.UpgradeType.Colossus_Reactor);
//		props[UnitType.Terran_Science_Vessel   ].SetEnergyUpgrade(bwapi.UpgradeType.Titan_Reactor);
//
//
//
//		props[UnitType.Zerg_Zergling       ].SetSpeedUpgrade(bwapi.UpgradeType.Metabolic_Boost,			    standardSpeed * 1.615);
//
//		props[UnitType.Zerg_Hydralisk    ].SetSpeedUpgrade(bwapi.UpgradeType.Muscular_Augments,		    standardSpeed * 1.105);
//
//		props[UnitType.Zerg_Ultralisk      ].SetExtraArmorUpgrade(bwapi.UpgradeType.Chitinous_Plating,	    2);
//		props[UnitType.Zerg_Ultralisk      ].SetSpeedUpgrade(bwapi.UpgradeType.Anabolic_Synthesis,		    standardSpeed * 1.556);
//
//		props[UnitType.Zerg_Defiler      ].SetEnergyUpgrade(bwapi.UpgradeType.Metasynaptic_Node);
//
//		props[UnitType.Zerg_Overlord       ].SetSightUpgrade(bwapi.UpgradeType.Antennae,					    11);
//		props[UnitType.Zerg_Overlord     ].SetSpeedUpgrade(bwapi.UpgradeType.Pneumatized_Carapace,		    UnitType.Protoss_Carrier.topSpeed());
//
//		props[UnitType.Zerg_Queen          ].SetEnergyUpgrade(bwapi.UpgradeType.Gamete_Meiosis);
//
//
//
//		props[UnitType.Protoss_Zealot      ].SetSpeedUpgrade(bwapi.UpgradeType.Leg_Enhancements,			    standardSpeed * 1.167);
//
//		props[UnitType.Protoss_High_Templar].SetEnergyUpgrade(bwapi.UpgradeType.Khaydarin_Amulet);
//
//		props[UnitType.Protoss_Reaver      ].SetCapacityUpgrade(bwapi.UpgradeType.Reaver_Capacity,		    5, 10);
//
//		props[UnitType.Protoss_Dark_Archon ].SetEnergyUpgrade(bwapi.UpgradeType.Argus_Talisman);
//
//		props[UnitType.Protoss_Observer    ].SetSightUpgrade(bwapi.UpgradeType.Sensor_Array,				    11);
//		props[UnitType.Protoss_Observer    ].SetSpeedUpgrade(bwapi.UpgradeType.Gravitic_Boosters,		    UnitType.Protoss_Corsair.topSpeed());
//
//		props[UnitType.Protoss_Shuttle     ].SetSpeedUpgrade(bwapi.UpgradeType.Gravitic_Drive,			    UnitType.Protoss_Corsair.topSpeed());
//
//		props[UnitType.Protoss_Scout       ].SetSightUpgrade(bwapi.UpgradeType.Apial_Sensors,			    10);
//		props[UnitType.Protoss_Scout       ].SetSpeedUpgrade(bwapi.UpgradeType.Gravitic_Thrusters,		    UnitType.Protoss_Corsair.topSpeed());
//
//		props[UnitType.Protoss_Corsair     ].SetEnergyUpgrade(bwapi.UpgradeType.Argus_Jewel);
//
//	    props[UnitType.Protoss_Carrier     ].SetCapacityUpgrade(bwapi.UpgradeType.Carrier_Capacity,		    4, 8);
//
//		props[UnitType.Protoss_Arbiter     ].SetEnergyUpgrade(bwapi.UpgradeType.Khaydarin_Core);
	}

//	public static UnitProperties Get(UnitType type2) {
//		// TODO Auto-generated method stub
////		return props[type2.getID()];
//	}
	
	public static UnitProperties Get(int unitTypeID) {
		return props[unitTypeID];
	}
	
	public int	GetArmor(PlayerProperties player){ return type.getArmor() + player.GetUpgradeLevel(type.getArmorUpgradeID()) + extraArmor[player.GetUpgradeLevel(extraArmorUpgrade)]; }
	public int	GetCapacity(PlayerProperties player) { return capacity[player.GetUpgradeLevel(capacityUpgrade)]; }
	public int	GetMaxEnergy(PlayerProperties player) { return maxEnergy[player.GetUpgradeLevel(maxEnergyUpgrade)]; }
	public int	GetSight(PlayerProperties player) { return sightRange[player.GetUpgradeLevel(sightUpgrade)]; }
	public int	GetSpeed(PlayerProperties player) { return speed[player.GetUpgradeLevel(speedUpgrade)]; }

	public WeaponProperties GetGroundWeapon() { return WeaponProperties.Get(type.getAirWeaponID()); }
	public WeaponProperties GetAirWeapon() { return WeaponProperties.Get(type.getGroundWeaponID()); }
}
