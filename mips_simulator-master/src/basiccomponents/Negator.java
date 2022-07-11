package basiccomponents;

import simulator.control.Simulator;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Adder;

import java.util.LinkedList;
import java.util.List;

public class Negator extends Wrapper {
    public Negator(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        List<Not> nots = new LinkedList<>();
        Adder adder = new Adder("ADD0", "64X33");

        for(int i = 0; i < 32; i++) {
            nots.add(new Not("NOT" + i, getInput(i)));
            adder.addInput(nots.get(i).getOutput(0));
        }



        for(int i = 1; i < 32; i++) {
            adder.addInput(Simulator.falseLogic);
        }

        adder.addInput(Simulator.trueLogic);

        for(int i = 0; i < 32; i++){
            addOutput(adder.getOutput(i + 1));
        }

    }
}
