package execute;

import simulator.control.Simulator;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class JumpAddress extends Wrapper {
    public JumpAddress(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            inputs:
            [0,3] -> pc+4[31-28]
            [4,29] -> instruction[0:25]
            outputs:
            jump address
         */
        for (int i=0; i<4; i++){
           addOutput(getInput(i));
        }
        for (int i=4; i<30; i++){
           addOutput(getInput(i));
        }
        addOutput(Simulator.falseLogic);
        addOutput(Simulator.falseLogic);
    }
}
