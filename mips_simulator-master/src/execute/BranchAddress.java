package execute;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Adder;

public class BranchAddress extends Wrapper {
    public BranchAddress(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            inputs:
            [0:31] -> pc+4
            [32:63] -> sign extended offset
            outputs:
            [0:31] -> branch address
         */

        Adder adder = new Adder("Adder1", "64X33");
        for (int i=0; i<32; i++)
            adder.addInput(getInput(i));
        for (int i=32; i<62; i++)
            adder.addInput(getInput(i+2));
        adder.addInput(Simulator.falseLogic);
        adder.addInput(Simulator.falseLogic);
        for (int i=0; i<32 ; i++){
            addOutput(adder.getOutput(i+1));
        }
    }
}
