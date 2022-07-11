package basiccomponents;

import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Adder;

public class Subtractor extends Wrapper {
    public Subtractor(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        Negator n1 = new Negator("NEG1", "32X32");
        Adder adder = new Adder("ADDER1", "64X33");

        for(int i = 32; i < 64; i++) {
            n1.addInput(getInput(i));
        }

        for(int i = 0; i < 32; i++) {
            adder.addInput(n1.getOutput(i));
        }

        for(int i = 0; i < 32; i++)
            adder.addInput(getInput(i));

        for(int i = 0; i < 33; i++)
            addOutput(adder.getOutput(i));
    }
}
