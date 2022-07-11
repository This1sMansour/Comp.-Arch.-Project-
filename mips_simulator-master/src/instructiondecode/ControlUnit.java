package instructiondecode;

import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class ControlUnit extends Wrapper {
    public ControlUnit(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
            input : opcode -> [0:5]
            output : regDst[0] , aluSrc[1] , memToReg[2] , regWrite[3] , memRead[4] , memWrite[5] , Branch[6] , aluOp[7:8], jump[9], bne[10]
         */
        Not[] nots = new Not[6];

        for (int i = 0; i < 6; i++) {
            nots[i] = new Not("not" + i, getInput(i));
        }

        And and1 = new And("AndCU1", nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), nots[4].getOutput(0), nots[5].getOutput(0));

        And and2 = new And("AndCU2", getInput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), getInput(4), getInput(5));

        And and3 = new And("AndCU3", getInput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), getInput(4), getInput(5));

        And and4 = new And("AndCU4", nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , getInput(3), nots[4].getOutput(0), nots[5].getOutput(0));

        And and5 = new And("AndCU5", nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , nots[3].getOutput(0), getInput(4), nots[5].getOutput(0));

        And and6 = new And("AndCU6", nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), nots[4].getOutput(0), nots[5].getOutput(0));

        And and7 = new And("AndCU7", nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), nots[4].getOutput(0), nots[5].getOutput(0));

        And and8 = new And("AndCU8", nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), nots[4].getOutput(0), getInput(5));

        And and9 = new And("AndCU9", nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , getInput(3), getInput(4), nots[5].getOutput(0));

        And and10 = new And("AndCU10", nots[0].getOutput(0), nots[1].getOutput(0), getInput(2)
                , nots[3].getOutput(0), getInput(4), nots[5].getOutput(0));

        And and11 = new And("AndCU11", nots[0].getOutput(0), nots[1].getOutput(0), nots[2].getOutput(0)
                , getInput(3), nots[4].getOutput(0), getInput(5));
        addOutput(and1.getOutput(0));

        addOutput(new Or("OrCU1", and2.getOutput(0), and3.getOutput(0), and6.getOutput(0), and7.getOutput(0), and8.getOutput(0), and9.getOutput(0)
                        , and10.getOutput(0)).getOutput(0));

        addOutput(and2.getOutput(0));

        addOutput(new Or("OrCU2", and2.getOutput(0), and1.getOutput(0), and6.getOutput(0), and7.getOutput(0), and8.getOutput(0), and9.getOutput(0)
                , and10.getOutput(0)).getOutput(0));

        addOutput(and2.getOutput(0));

        addOutput(and3.getOutput(0));

        addOutput(and4.getOutput(0));

        addOutput(and1.getOutput(0));

        addOutput(new Or("orCU3",and4.getOutput(0),and11.getOutput(0)).getOutput(0));

        addOutput(and5.getOutput(0));

        addOutput(and11.getOutput(0));
//        Simulator.debugger.addTrackItem(nots);
    }
}
