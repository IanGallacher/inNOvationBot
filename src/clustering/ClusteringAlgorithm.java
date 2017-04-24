package clustering;

import java.util.List;

import sparcraft.Unit;

public interface ClusteringAlgorithm {

	List<List<Unit>> getClusters(Unit[] units, int h, double hp);

	public String toString();
}
