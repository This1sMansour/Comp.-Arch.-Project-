package instructionfetch;

import simulator.gates.sequential.flipflops.DFlipFlop;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
//import simulator.wrapper.wrappers.DFlipFlop;

public class Register extends Wrapper {
    public Register(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        DFlipFlop[] register = new DFlipFlop[32];
        for (int i = 0; i < 32; i++) {
            register[i] = new DFlipFlop("dFlipFlop" + i, getInput(0), getInput(i + 1));
        }
        for (int i = 0; i < 32; i++) {
            addOutput(register[i].getOutput(0));
        }

    }
}