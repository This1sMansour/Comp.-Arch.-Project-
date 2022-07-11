package instructiondecode;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Adder;

public class ProgramCounterAdder extends Wrapper {
    public ProgramCounterAdder(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            inputs:
            [0:31] -> PC
            outputs:
            [0:31] -> PC + 4

         */
        Adder adder = new Adder("ADDER", "64X33");
        for (int i = 0; i < 32; ++i) {
            adder.addInput(getInput(i));
        }
        Link[] four = new Link[32];
        for (int i = 0; i < 29; ++i) {
            four[i] = Simulator.falseLogic;
        }
        four[29] = Simulator.trueLogic;
        four[30] = Simulator.falseLogic;
        four[31] = Simulator.falseLogic;

        adder.addInput(four);

        for (int i = 0; i < 32; i++) {
            addOutput(adder.getOutput(i+1));
        }
    }
}
