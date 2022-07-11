package basiccomponents;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Xor;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class OverflowDetector extends Wrapper {
    public OverflowDetector(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        Xor xor0 = new Xor("XOR0");
        Xor xor1 = new Xor("XOR1");
        And and = new And("AND");
        Not not = new Not("NOT");

        xor0.addInput(getInput(0), getInput(1));
        not.addInput(xor0.getOutput(0));
        xor1.addInput(getInput(1), getInput(2));
        and.addInput(not.getOutput(0), xor1.getOutput(0));
        addOutput(and.getOutput(0));
    }
}
