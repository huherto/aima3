/**
 * 
 */
package wumpus;

import java.util.List;



abstract class Agent {

    WumpusWorld ww;
    
    GridPos pos;
    
    public Agent(WumpusWorld ww) {
        this.ww = ww;
    }
    
    public abstract Action nextAction(Percept percept);
    
    protected Action getMoveDir(GridPos dest) {
        if (pos.x == dest.x) {
            if (pos.y + 1 == dest.y)
                return Action.Up;
            if (pos.y - 1 == dest.y)
                return Action.Down;
            throw new IllegalStateException();
        }
        if (pos.y == dest.y) {
            if (pos.x + 1 == dest.x)
                return Action.Right;
            if (pos.x - 1 == dest.x)
                return Action.Left;
            throw new IllegalStateException();
        }
        throw new IllegalStateException();
    }

    public int getWorldSize() {
        return ww.size;
    }
    
    public List<GridPos> neighbors(GridPos gpos) {
        return ww.neighbors(gpos);
    }

}