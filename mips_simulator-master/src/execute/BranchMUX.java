package execute;

import basiccomponents.Mux;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class BranchMUX extends Wrapper {
    public BranchMUX(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
          /*
            input:
            [0] -> branch signal
            [1:32] -> pc+4
            [33:64] -> branch address
         */
        Mux mux = new Mux("mux2", "65X32");
        for (int i = 0; i < 64; i++) {
            mux.addInput(getInput(i+1));
        }
        mux.addInput(getInput(0));
        for (int i = 0; i < 32; i++) {
            addOutput(mux.getInput(i));
        }
    }
}
