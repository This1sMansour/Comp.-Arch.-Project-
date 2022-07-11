package basiccomponents;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Decoder2To4 extends Wrapper {

	public Decoder2To4(String label, String stream, Link[] links) {
		super(label, stream, links);
		
	}

	@Override
	public void initialize() {
		
		
		Not[] nots = new Not[2];
        for (int i=0; i<2 ; i++){
            nots[i] = new Not("NOT"+i, getInput(i));
        }
        And[] ands = new And[4];
        for (int i = 0; i < 4; i++) {
            ands[i] = new And("And" + i);
        }
		ands[0].addInput(nots[0].getOutput(0),nots[1].getOutput(0));
		ands[1].addInput(nots[0].getOutput(0),getInput(1));
		ands[2].addInput(getInput(0),nots[1].getOutput(0));
		ands[3].addInput(getInput(0),getInput(1));
        
		for (int i=0; i<4;i++){
            addOutput(ands[i].getOutput(0));
        }
	}

}
