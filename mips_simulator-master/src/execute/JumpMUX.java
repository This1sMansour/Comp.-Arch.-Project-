package execute;

import basiccomponents.Mux;
import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class JumpMUX extends Wrapper {
    public JumpMUX(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            input:
            [0] -> jump signal
            [1:32] -> branch mux
            [33:64] -> jump address
         */
        Mux mux = new Mux("mux1", "65X32");
        for (int i=0; i<64;i++){
            mux.addInput(getInput(i+1));
        }
        mux.addInput(getInput(0));
        for (int i=0; i<32;i++){
            addOutput(mux.getInput(i));
        }
        Simulator.debugger.addTrackItem(mux);
    }
}
