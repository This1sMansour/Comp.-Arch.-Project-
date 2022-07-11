package basiccomponents;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class Decoder extends Wrapper {

    public Decoder(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            5 -> 32 one hot decoder
         */

        Not[] nots = new Not[5];
        for (int i=0; i<5 ; i++){
            nots[i] = new Not("NOT"+i, getInput(i));
        }
        And[] ands = new And[32];
        for (int i = 0; i < 32; i++) {
            ands[i] = new And("And" + i);
        }
        ands[0].addInput(nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[1].addInput(getInput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[2].addInput(nots[0].getOutput(0), getInput(1), nots[2].getOutput(0)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[3].addInput(getInput(0), getInput(1), nots[2].getOutput(0)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[4].addInput(nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[5].addInput(getInput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[6].addInput(nots[0].getOutput(0), getInput(1), getInput(2)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[7].addInput(getInput(0), getInput(1), getInput(2)
                , nots[3].getOutput(0), nots[4].getOutput(0));
        ands[8].addInput(nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , getInput(3), nots[4].getOutput(0));
        ands[9].addInput(getInput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , getInput(3), nots[4].getOutput(0));
        ands[10].addInput(nots[0].getOutput(0), getInput(1), nots[2].getOutput(0)
                , getInput(3), nots[4].getOutput(0));
        ands[11].addInput(getInput(0), getInput(1), nots[2].getOutput(0)
                , getInput(3), nots[4].getOutput(0));
        ands[12].addInput(nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), nots[4].getOutput(0));
        ands[13].addInput(getInput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), nots[4].getOutput(0));
        ands[14].addInput(nots[0].getOutput(0), getInput(1), getInput(2)
                , getInput(3), nots[4].getOutput(0));
        ands[15].addInput(getInput(0), getInput(1), getInput(2)
                , getInput(3), nots[4].getOutput(0));
        ands[16].addInput(nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), getInput(4));
        ands[17].addInput(getInput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), getInput(4));
        ands[18].addInput(nots[0].getOutput(0), getInput(1), nots[2].getOutput(0)
                , nots[3].getOutput(0), getInput(4));
        ands[19].addInput(getInput(0), getInput(1), nots[2].getOutput(0)
                , nots[3].getOutput(0), getInput(4));
        ands[20].addInput(nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), getInput(4));
        ands[21].addInput(getInput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), getInput(4));
        ands[22].addInput(nots[0].getOutput(0), getInput(1), getInput(2)
                , nots[3].getOutput(0), getInput(4));
        ands[23].addInput(getInput(0), getInput(1), getInput(2)
                , nots[3].getOutput(0), getInput(4));
        ands[24].addInput(nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , getInput(3), getInput(4));
        ands[25].addInput(getInput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , getInput(3), getInput(4));
        ands[26].addInput(nots[0].getOutput(0), getInput(1), nots[2].getOutput(0)
                , getInput(3), getInput(4));
        ands[27].addInput(getInput(0), getInput(1), nots[2].getOutput(0)
                , getInput(3), getInput(4));
        ands[28].addInput(nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), getInput(4));
        ands[29].addInput(getInput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), getInput(4));
        ands[30].addInput(nots[0].getOutput(0), getOutput(1), getInput(2)
                , getInput(3), getInput(4));
        ands[31].addInput(getInput(0), getInput(1), getInput(2)
                , getInput(3), getInput(4));

        for (int i=0; i<32;i++){
            addOutput(ands[i].getOutput(0));
        }
    }
}
