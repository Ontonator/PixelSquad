package tommy.pixelsquad;

import java.util.ArrayList;

public class TileTypeManager {

	public ArrayList<ManagedTileType> managedTileType = new ArrayList<ManagedTileType>();

	public void addTileType(TileType tileType, int id) {

		managedTileType.add(new ManagedTileType(tileType, id));

	}

	public TileType getTileType(int id) {

		for (ManagedTileType i : managedTileType)
			if (id == i.id)
				return i.tileType;

		return null;

	}

	private static class ManagedTileType {

		TileType tileType;
		int id;

		ManagedTileType(TileType tileType, int id) {

			this.tileType = tileType;
			this.id = id;

		}

	}

}