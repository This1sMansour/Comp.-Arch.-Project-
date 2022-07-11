package basiccomponents;

import simulator.control.Simulator;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Xor;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class SetLessThan extends Wrapper {
    public SetLessThan(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        OverflowDetector overflowDetector = new OverflowDetector("OF", "3X1");
        Not not = new Not("NOT", getInput(1));
        overflowDetector.addInput(getInput(0), not.getOutput(0), getInput(2));

        for(int i = 0; i < 31; i++)
            addOutput(Simulator.falseLogic);

        Xor xor = new Xor("XOR");

        xor.addInput(not.getOutput(0), overflowDetector.getOutput(0));

//        Simulator.debugger.addTrackItem(overflowDetector);
        addOutput(xor.getOutput(0));
    }
}
