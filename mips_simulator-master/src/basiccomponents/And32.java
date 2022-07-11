package basiccomponents;

import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class And32 extends Wrapper {
    public And32(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        List<And> ands = new ArrayList<>();

        for(int i = 0; i < 32; i++){
            ands.add(new And("AND" + i, getInput(i), getInput(i + 32)));
            addOutput(ands.get(i).getOutput(0));
        }
    }
}
