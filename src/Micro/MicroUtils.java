//package Micro;
//
//import bwapi.Position;
//import bwapi.Unit;
//import bwapi.UnitCommand;
//import bwapi.UnitCommandType;
//
//import Globals.Globals;
//import Globals.Utility;
//
//// Giving credit where it is due: much is ported from UAlbertaBot
//// https://github.com/davechurchill/ualbertabot/blob/master/UAlbertaBot/Source/Micro.cpp
//
//class MicroUtils {
//	int TotalCommands = 0;
//	
//	int dotRadius = 2;
//	
//	void SmartAttackUnit(Unit attacker, Unit target)
//	{
////	    UAB_ASSERT(attacker, "SmartAttackUnit: Attacker not valid");
////	    UAB_ASSERT(target, "SmartAttackUnit: Target not valid");
////	
//	    if (attacker == null || target == null)
//	        return;
//	
//	    // if we have issued a command to this unit already this frame, ignore this one
//	    if (attacker.getLastCommandFrame() >= Globals.game.getFrameCount() || attacker.isAttackFrame())
//	        return;
//	
//	    // get the unit's current command
//	    UnitCommand currentCommand = attacker.getLastCommand();
//	
//	    // if we've already told this unit to attack this target, ignore this command
//	    if (currentCommand.getUnitCommandType() == UnitCommandType.Attack_Unit && currentCommand.getTarget() == target)
//	    {
//	        return;
//	    }
//	
//	    // if nothing prevents it, attack the target
//	    attacker.attack(target);
//	    TotalCommands++;
//	
////	    if (Config::Debug::DrawUnitTargetInfo) 
////	    {
////	        Broodwar.drawCircleMap(attacker.getPosition(), dotRadius, Red, true);
////	        Broodwar.drawCircleMap(target.getPosition(), dotRadius, Red, true);
////	        Broodwar.drawLineMap( attacker.getPosition(), target.getPosition(), Red );
////	    }
//	}
//	
//	void SmartAttackMove(Unit attacker, Position targetPosition)
//	{
//	    //UAB_ASSERT(attacker, "SmartAttackMove: Attacker not valid");
//	    //UAB_ASSERT(targetPosition.isValid(), "SmartAttackMove: targetPosition not valid");
//	
//	    if (attacker == null || !targetPosition.isValid())
//	        return;
//	
//	    // if we have issued a command to this unit already this frame, ignore this one
//	    if (attacker.getLastCommandFrame() >= Globals.game.getFrameCount() || attacker.isAttackFrame())
//	    {
//	        return;
//	    }
//	
//	    // get the unit's current command
//	    UnitCommand currentCommand = attacker.getLastCommand();
//	
//	    // if we've already told this unit to attack this target, ignore this command
//	    if (currentCommand.getUnitCommandType() == UnitCommandType.Attack_Move 
//	    &&  currentCommand.getTargetPosition() == targetPosition)
//			return;
//	
//	    // if nothing prevents it, attack the target
//	    attacker.attack(targetPosition);
//	    TotalCommands++;
////	
////	    if (Config::Debug::DrawUnitTargetInfo) 
////	    {
////	        Broodwar.drawCircleMap(attacker.getPosition(), dotRadius, Orange, true);
////	        Broodwar.drawCircleMap(targetPosition, dotRadius, Orange, true);
////	        Broodwar.drawLineMap(attacker.getPosition(), targetPosition, Orange);
////	    }
//	}
//	
//	void SmartMove(Unit attacker, Position targetPosition)
//	{
//	    //UAB_ASSERT(attacker, "SmartAttackMove: Attacker not valid");
//	    //UAB_ASSERT(targetPosition.isValid(), "SmartAttackMove: targetPosition not valid");
//	
//	    if (attacker == null|| !targetPosition.isValid())
//	        return;
//	
//	    // if we have issued a command to this unit already this frame, ignore this one
//	    if (attacker.getLastCommandFrame() >= Globals.game.getFrameCount() || attacker.isAttackFrame())
//	        return;
//	
//	    // get the unit's current command
//	    UnitCommand currentCommand = attacker.getLastCommand();
//	
//	    // if we've already told this unit to move to this position, ignore this command
//	    if ((currentCommand.getUnitCommandType() == UnitCommandType.Move) && (currentCommand.getTargetPosition() == targetPosition) && attacker.isMoving())
//	    {
//	        return;
//	    }
//	
//	    // if nothing prevents it, attack the target
//	    attacker.move(targetPosition);
//	    TotalCommands++;
////	
////	    if (Config::Debug::DrawUnitTargetInfo) 
////	    {
////	        Broodwar.drawCircleMap(attacker.getPosition(), dotRadius, White, true);
////	        Broodwar.drawCircleMap(targetPosition, dotRadius, White, true);
////	        Broodwar.drawLineMap(attacker.getPosition(), targetPosition, White);
////	    }
//	}
//	
//	void SmartRightClick(Unit unit, Unit target)
//	{
////	    UAB_ASSERT(unit, "SmartRightClick: Unit not valid");
////	    UAB_ASSERT(target, "SmartRightClick: Target not valid");
////	
////	    if (unit || !target)
////	        return;
//	
//	    // if we have issued a command to this unit already this frame, ignore this one
//	    if (unit.getLastCommandFrame() >= Globals.game.getFrameCount() || unit.isAttackFrame())
//	        return;
//	
//	    // get the unit's current command
//	    UnitCommand currentCommand = unit.getLastCommand();
//	
//	    // if we've already told this unit to move to this position, ignore this command
//	    if ((currentCommand.getUnitCommandType() == UnitCommandType.Right_Click_Unit) && (currentCommand.getTargetPosition() == target.getPosition()))
//	    {
//	        return;
//	    }
//	
//	    // if nothing prevents it, attack the target
//	    unit.rightClick(target);
//	    TotalCommands++;
//	
////	    if (Config::Debug::DrawUnitTargetInfo) 
////	    {
////	        Broodwar.drawCircleMap(unit.getPosition(), dotRadius, Cyan, true);
////	        Broodwar.drawCircleMap(target.getPosition(), dotRadius, Cyan, true);
////	        Broodwar.drawLineMap(unit.getPosition(), target.getPosition(), Cyan);
////	    }
//	}
//	
////	void SmartLaySpiderMine(Unit unit, Position pos)
////	{
////	    if (!unit)
////	    {
////	        return;
////	    }
////	
////	    if (!unit.canUseTech(TechTypes::Spider_Mines, pos))
////	    {
////	        return;
////	    }
////	
////	    UnitCommand currentCommand(unit.getLastCommand());
////	
////	    // if we've already told this unit to move to this position, ignore this command
////	    if ((currentCommand.getType() == UnitCommandType.Use_Tech_Position) && (currentCommand.getTargetPosition() == pos))
////	    {
////	        return;
////	    }
////	
////	    unit.canUseTechPosition(TechTypes::Spider_Mines, pos);
////	}
////	
////	void SmartRepair(Unit unit, Unit target)
////	{
////	    UAB_ASSERT(unit, "SmartRightClick: Unit not valid");
////	    UAB_ASSERT(target, "SmartRightClick: Target not valid");
////	
////	    if (!unit || !target)
////	    {
////	        return;
////	    }
////	
////	    // if we have issued a command to this unit already this frame, ignore this one
////	    if (unit.getLastCommandFrame() >= Broodwar.getFrameCount() || unit.isAttackFrame())
////	    {
////	        return;
////	    }
////	
////	    // get the unit's current command
////	    UnitCommand currentCommand = unit.getLastCommand();
////	
////	    // if we've already told this unit to move to this position, ignore this command
////	    if ((currentCommand.getType() == UnitCommandType.Repair) && (currentCommand.getTarget() == target))
////	    {
////	        return;
////	    }
////	
////	    // if nothing prevents it, attack the target
////	    unit.repair(target);
////	    TotalCommands++;
////	
////	    if (Config::Debug::DrawUnitTargetInfo) 
////	    {
////	        Broodwar.drawCircleMap(unit.getPosition(), dotRadius, Cyan, true);
////	        Broodwar.drawCircleMap(target.getPosition(), dotRadius, Cyan, true);
////	        Broodwar.drawLineMap(unit.getPosition(), target.getPosition(), Cyan);
////	    }
////	}
//	
//	
//	void SmartKiteTarget(Unit rangedUnit, Unit target)
//	{
////	    UAB_ASSERT(rangedUnit, "SmartKiteTarget: Unit not valid");
////	    UAB_ASSERT(target, "SmartKiteTarget: Target not valid");
//	
////	    if (!rangedUnit || !target)
////	    {
////	        return;
////	    }
//	
//		double range = rangedUnit.getType().groundWeapon().maxRange();
//		
//		if (rangedUnit.getType() == UnitType.Protoss_Dragoon && Globals.self.getUpgradeLevel(UpgradeType.Singularity_Charge))
//		{
//			range = 6*32;
//		}
//	
//		// determine whether the target can be kited
////	    bool kiteLonger = Config::KiteLongerRangedUnits.find(rangedUnit.getType()) != Config::KiteLongerRangedUnits.end();
//		if (/*kiteLonger == false && */(range <= target.getType().groundWeapon().maxRange()))
//		{
//			// if we can't kite it, there's no point
//			SmartAttackUnit(rangedUnit, target);
//			return;
//		}
//	
//		boolean kite = true;
//		double  dist = rangedUnit.getDistance(target);
//		double  speed = rangedUnit.getType().topSpeed();
//	
//	    
//	    // if the unit can't attack back don't kite
////	    if ((rangedUnit.isFlying() && !UnitUtil::CanAttackAir(target)) || (!rangedUnit.isFlying() && !UnitUtil::CanAttackGround(target)))
////	    {
////	        //kite = false;
////	    }
////	
////		double timeToEnter = std::max(0.0,(dist - range) / speed);
////		if ((timeToEnter >= rangedUnit.getGroundWeaponCooldown()))
////		{
////			kite = false;
////		}
//	
//		if (target.getType().isBuilding())
//		{
//			kite = false;
//		}
//	
//		// if we can't shoot, run away
//		if (kite)
//		{
//			Position fleePosition = rangedUnit.getPosition() - target.getPosition() + rangedUnit.getPosition();
//			//Broodwar.drawLineMap(rangedUnit.getPosition(), fleePosition, Cyan);
//			SmartMove(rangedUnit, fleePosition);
//		}
//		// otherwise shoot
//		else
//		{
//			SmartAttackUnit(rangedUnit, target);
//		}
//	}
//	
////	
////	void MutaDanceTarget(Unit muta, Unit target)
////	{
//////	    UAB_ASSERT(muta, "MutaDanceTarget: Muta not valid");
//////	    UAB_ASSERT(target, "MutaDanceTarget: Target not valid");
////	
////	    if (!muta || !target)
////	    {
////	        return;
////	    }
////	
////	    int cooldown                  = muta.getType().groundWeapon().damageCooldown();
////	    int latency                   = Broodwar.getLatency();
////	    double speed                  = muta.getType().topSpeed();
////	    double range                  = muta.getType().groundWeapon().maxRange();
////	    double distanceToTarget       = muta.getDistance(target);
////		double distanceToFiringRange  = Math.max(distanceToTarget - range,0.0);
////		double timeToEnterFiringRange = distanceToFiringRange / speed;
////		int framesToAttack            = (int)(timeToEnterFiringRange) + 2*latency;
////	
////		// How many frames are left before we can attack?
////		const int currentCooldown = muta.isStartingAttack() ? cooldown : muta.getGroundWeaponCooldown();
////	
////		Position fleeVector = GetKiteVector(target, muta);
////		Position moveToPosition(muta.getPosition() + fleeVector);
////	
////		// If we can attack by the time we reach our firing range
////		if(currentCooldown <= framesToAttack)
////		{
////			// Move towards and attack the target
////			muta.attack(target);
////		}
////		else // Otherwise we cannot attack and should temporarily back off
////		{
////			// Determine direction to flee
////			// Determine point to flee to
////			if (moveToPosition.isValid()) 
////			{
////				muta.rightClick(moveToPosition);
////			}
////		}
////	}
//	
//	Position GetKiteVector(Unit unit, Unit target)
//	{
//	    Position fleeVec(target.getPosition() - unit.getPosition());
//	    double fleeAngle = atan2(fleeVec.y, fleeVec.x);
//	    fleeVec = Position(static_cast<int>(64 * cos(fleeAngle)), static_cast<int>(64 * sin(fleeAngle)));
//	    return fleeVec;
//	}
//	
//	const double PI = 3.14159265;
//	void Rotate(double &x, double &y, double angle)
//	{
//		angle = angle*PI/180.0;
//		x = (x * cos(angle)) - (y * sin(angle));
//		y = (y * cos(angle)) + (x * sin(angle));
//	}
//	
//	void Normalize(double &x, double &y)
//	{
//		double length = sqrt((x * x) + (y * y));
//		if (length != 0)
//		{
//			x = (x / length);
//			y = (y / length);
//		}
//	}
//}