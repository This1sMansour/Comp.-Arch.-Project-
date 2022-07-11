package basiccomponents;

import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class Or32 extends Wrapper {
    public Or32(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        List<Or> orList = new ArrayList<>();

        for(int i = 0; i < 32; i++){
            orList.add(new Or("OR" + i, getInput(i), getInput(i + 32)));
            addOutput(orList.get(i).getOutput(0));
        }
    }
}
