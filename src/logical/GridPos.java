/**
 * 
 */
package logical;

class GridPos {
    public GridPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x, y;

	@Override
	public String toString() {
		return x+"."+y;
	}
}