//Dedicated to Goli

package simulator;

import assembler.Assembler;
import simulator.control.Simulator;

public class Sample {
    public static void main(String[] args) throws Exception {
        Assembler.main(args);

        MIPS mips = new MIPS("MIPS", "0X0");


        Simulator.debugger.addTrackItem(mips);
        Simulator.debugger.setDelay(500);
        Simulator.circuit.startCircuit();
    }
}
