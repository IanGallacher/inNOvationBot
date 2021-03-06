import Macro.MacroController;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import Debug.DebugController;
import Globals.Globals;
import Globals.MapTools;
import Information.InformationManager;
import UnitController.ScoutingController;

public class StrategyController {

	public enum UnitProduction {
		FocusOnWorkers, FocusOnArmy
	};

	public enum TechGoal {
		Nexus, DragoonTech
	};
	
	private static int _armySquadSizeGoal = 15;
	private static TechGoal techGoal = TechGoal.DragoonTech;
	private static UnitProduction unitProductionFocus = UnitProduction.FocusOnWorkers;

	// If there is a planned nexus, don't keep trying to expand.
	public static void onUnitCreate(Unit unit) {
		if (unit.getType() == UnitType.Protoss_Nexus && unit.getPlayer() == Globals.self) {
			techGoal = TechGoal.DragoonTech;
		}
	}

	public static void detectStrategy() {

	}

	public static void calculateStrategy() {
		techGoal = TechGoal.DragoonTech;
		unitProductionFocus = UnitProduction.FocusOnWorkers;
	}

	public static void debugVariables() {

		DebugController.debugConsolePrint("nexus count", InformationManager.getUnitCount(UnitType.Protoss_Nexus));
		// DebugController.debugConsolePrint("MineralWorkers",
		// UnitManager.mineralWorkers.size());
		// DebugController.debugConsolePrint("GasWorkers",
		// UnitManager.gasWorkers.size());
	}

	// be sure to only call once per frame.
	public static void executeStrategy() {
		if(Globals.game.isPaused())
			Globals.game.resumeGame();
		
		if (techGoal == TechGoal.DragoonTech) { // heavy macro strategy
			MacroController.preventSupplyBlock();
			
			
			// Mineral spending goals.

			// Do we have enough probes that we require another nexus?
			// Take our number of probes we have and compare it to the number of probes it would take to almost saturate all your bases.
			if (MacroController.AlmostFullySaturated()) {
				techGoal = TechGoal.Nexus;
				MacroController.PlanNexus();
				System.out.printf("EXPANDING");
			} else if (InformationManager.getUnitCount(UnitType.Protoss_Gateway) < 1
					|| (InformationManager.getUnitCount(UnitType.Protoss_Cybernetics_Core) >= 1
					&& InformationManager.getUnitCount(UnitType.Protoss_Forge) >= 1)) {
				if (InformationManager.getUnitCount(UnitType.Protoss_Gateway) < 5) {
					MacroController.buildBuilding(UnitType.Protoss_Gateway);
				}
			} else if (InformationManager.getUnitCount(UnitType.Protoss_Assimilator) < 1 * InformationManager.getUnitCount(UnitType.Protoss_Nexus)) {
				MacroController.buildBuilding(UnitType.Protoss_Assimilator);
			} else if (InformationManager.getUnitCount(UnitType.Protoss_Cybernetics_Core) < 1) {
				MacroController.buildBuilding(UnitType.Protoss_Cybernetics_Core);
			} else if (InformationManager.getUnitCount(UnitType.Protoss_Forge) < 1) {
				MacroController.buildBuilding(UnitType.Protoss_Forge);
			}

			if (unitProductionFocus == UnitProduction.FocusOnWorkers) {
				// If the current strategy is focusing on training workers, do
				// that first.
				MacroController.researchUpgrades();
				MacroController.trainWorkers();
				MacroController.trainArmy();
			} else {
				// If the current strategy is focusing on training army, do that
				// first.
				MacroController.researchUpgrades();
				MacroController.trainArmy();
				MacroController.trainWorkers();
			}
		}

		// Save up to expand.
		if (techGoal == TechGoal.Nexus) {
			if (MacroController.buildAtLocation(UnitType.Protoss_Nexus, MapTools.getNextExpansion())) {
				techGoal = TechGoal.DragoonTech;

				// Have to make a move and build function in order for this to
				// work properly
			}
		}

		// multiply expected supply by 2 because zerglings take half supply.
		if (Globals.game.getStartLocations().size() > 2 && Globals.self.supplyUsed() > (8 * 2)) {
			ScoutingController.onFrame();
		}
	}
}
