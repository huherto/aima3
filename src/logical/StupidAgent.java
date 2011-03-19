/**
 * 
 */
package logical;


class StupidAgent extends Agent {
	    	
	@Override
	public Action nextAction(Percept percept) {
		int idx = LogicalAgentsExamples.rand.nextInt(4);
		Action[] values = Action.values();
		return values[idx];
	}
	
}