/**
 * 
 */
package logical;


abstract class Agent {

    GridPos pos;
    
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


}