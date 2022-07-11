package basiccomponents;

import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class SignExtend extends Wrapper {

    public SignExtend(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
        16 bit -> for extend
         */
        for (int i=0 ; i < getOutputSize()-getInputSize()  ;i++){
            addOutput(getInput(0));
        }
        for (int i=0 ; i < getInputSize() ;i++){
            addOutput(getInput(i));
        }

    }
}
