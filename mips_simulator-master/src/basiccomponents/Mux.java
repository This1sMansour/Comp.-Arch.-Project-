package basiccomponents;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

import java.util.LinkedList;
import java.util.List;

public class Mux extends Wrapper {
    public Mux(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        //First 32 bits represent the first input
        //Second 32 bits represent the second input
        //Last bit represents the select line

        Not sNegate = new Not("NOT0", getInput(64));

        List<And> a0 = new LinkedList<>();
        List<And> a1 = new LinkedList<>();

        for(int i = 0; i < 32; i++){
            a0.add(new And("AND0" + i, getInput(i), sNegate.getOutput(0)));
            a1.add(new And("AND1" + i, getInput(i + 32), getInput(64)));
            addOutput(new Or("OR" + i, a0.get(i).getOutput(0), a1.get(i).getOutput(0)).getOutput(0));
        }

    }
}
