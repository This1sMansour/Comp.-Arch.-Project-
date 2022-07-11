package basiccomponents;

import simulator.gates.combinational.Not;
import simulator.gates.combinational.Xor;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class Xnor32 extends Wrapper {
    public Xnor32(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        List<Xor> xorList = new ArrayList<>();
        List<Not> notList = new ArrayList<>();

        for(int i = 0; i < 32; i++){
            xorList.add(new Xor("XOR" + i, getInput(i), getInput(i + 32)));
            notList.add(new Not("NOT" + i, xorList.get(i).getOutput(0)));
            addOutput(notList.get(i).getOutput(0));
        }
    }
}
