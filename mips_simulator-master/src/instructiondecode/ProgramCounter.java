package instructiondecode;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.DFlipFlop;
import simulator.wrapper.wrappers.Multiplexer;

public class ProgramCounter extends Wrapper {
    public ProgramCounter(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            inputs:
            [0] -> clock
            [1:32] -> PC
            outputs:
            [0:31] -> new PC

         */
        DFlipFlop shoift0 = new DFlipFlop("SHIFT0", "2X2", getInput(0), Simulator.falseLogic);
        DFlipFlop shoift1 = new DFlipFlop("SHIFT1", "2X2", getInput(0), shoift0.getOutput(0));
        DFlipFlop[] counter = new DFlipFlop[32];
        for (int i = 0; i < 32; ++i) {
            counter[i] = new DFlipFlop("D" + i, "2X2", getOutput(0), getInput(i + 1));
            counter[i] = new DFlipFlop("D" + i, "2X2", getInput(0));
        }
        ProgramCounterAdder programCounterAdder = new ProgramCounterAdder("p", "32X32");
        for (int i = 0; i < 32; i++)
            programCounterAdder.addInput(counter[i].getOutput(0));

        Link[] counterInit = new Link[32];
        for (int i = 0; i < 32; ++i) {
            counterInit[i] = getInput(i+1);
        }

        Multiplexer[] counterSelect = new Multiplexer[32];
        for (int i = 0; i < 32; ++i) {
            counterSelect[i] = new Multiplexer("M" + i, "3X1",
                    shoift1.getOutput(0), programCounterAdder.getOutput(i), counterInit[i]);
        }
        for (int i = 0; i < 32; i++) {
            counter[i].addInput(counterSelect[i].getOutput(0));
        }
        for (int i = 0; i < 32; i++) {
            addOutput(counter[i].getOutput(0));
        }

    }
}