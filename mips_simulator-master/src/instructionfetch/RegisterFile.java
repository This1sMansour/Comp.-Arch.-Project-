package instructionfetch;

import basiccomponents.Decoder;
import basiccomponents.Mux;
import instructionfetch.Register;
import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Or;
import simulator.gates.sequential.flipflops.DFlipFlop;
import simulator.network.Link;
import simulator.network.Linkable;
import simulator.wrapper.Wrapper;

//import simulator.wrapper.wrappers.DFlipFlop;
import simulator.wrapper.wrappers.Multiplexer;
import simulator.wrapper.wrappers.RealDFlipFlop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RegisterFile extends Wrapper {


    public RegisterFile(String label, String stream, Link... links) {
        super(label, stream, links);
    }


    public void writeReg(Register[] regFile, Link[] data, Link... links) {
        /*
            first 5 bit rd
            6th bit regWrite
            32 bit data
         */

        Decoder decoder = new Decoder("Decoder", "5X32", getInput(12), getInput(13), getInput(14), getInput(15), getInput(16));
        Mux[] mux = new Mux[32];
        for (int i = 1; i < 32; i++) {
            mux[i] = new Mux("MUX132" + i, "65X32");
            for (int j = 0; j < 32; j++) {
                mux[i].addInput(regFile[i].getOutput(j));
            }
            for (int j = 0; j < 32; j++) {
                mux[i].addInput(data[j]);
            }
            mux[i].addInput(new And("And" + i, decoder.getOutput(i), getInput(1)).getOutput(0));
            for (int j = 0; j < 32; j++) {
                regFile[i].addInput(mux[i].getOutput(j));
            }
        }

    }

    public void readReg(Register[] registers, Link... links) {
        Mux[] mux = new Mux[30];
        int i =0;
        for (int j = 0; j < 30; j++) {
            mux[j] = new Mux("MUX" + j, "65X32");
        }
        for (i=0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                mux[i].addInput(registers[2 * i].getOutput(j));
            }
            for (int j = 0; j < 32; j++) {
                mux[i].addInput(registers[2 * i + 1].getOutput(j));
            }
            mux[i].addInput(links[0]);
        }
        for (i = 0; i < 8; i++) {
            for (int j = 0; j < 32; j++)
                mux[i + 16].addInput(mux[2 * i].getOutput(j));
            for (int j = 0; j < 32; j++)
                mux[i + 16].addInput(mux[2 * i + 1].getOutput(j));
            mux[i + 16].addInput(links[1]);
        }
        for (i = 0; i < 4; i++) {
            for (int j = 0; j < 32; j++)
                mux[i + 24].addInput(mux[2 * i + 16].getOutput(j));
            for (int j = 0; j < 32; j++)
                mux[i + 24].addInput(mux[2 * i + 17].getOutput(j));
            mux[i + 24].addInput(links[2]);
        }
        for (i = 0; i < 2; i++) {
            for (int j = 0; j < 32; j++)
                mux[i + 28].addInput(mux[2 * i + 24].getOutput(j));
            for (int j = 0; j < 32; j++)
                mux[i + 28].addInput(mux[2 * i + 25].getOutput(j));
            mux[i + 28].addInput(links[3]);
        }

        Mux mux1 = new Mux("Mux31", "65X32");
        for (int j = 0; j < 32; j++)
            mux1.addInput(mux[28].getOutput(j));
        for (int j = 0; j < 32; j++)
            mux1.addInput(mux[29].getOutput(j));
        mux1.addInput(links[4]);

        for (i = 0; i < 32; i++) {
            addOutput(mux1.getOutput(i));
        }
    }

    @Override
    public void initialize() {
        /*
            get 49 bit
            first bit is clock
            second bit is write signal
            [2,6] -> rs
            [7,11] -> rt
            [12,16] -> rd
            [17,48] -> write data
         */
        Register[] registers = new Register[32];
        registers[0] = new Register("$zero", "33X32", getInput(0));
        for (int i = 0; i < 32; i++) {
            registers[0].addInput(Simulator.falseLogic);
        }
        registers[1] = new Register("$ta", "33X32", getInput(0));
        registers[2] = new Register("$v0", "33X32", getInput(0));
        registers[3] = new Register("$v1", "33X32", getInput(0));
        for (int i = 4; i < 8; i++) {
            registers[i] = new Register("$a" + (i - 4), "33X32", getInput(0));
        }
        for (int i = 8; i < 16; i++) {
            registers[i] = new Register("$t" + (i - 8), "33X32", getInput(0));
        }
        for (int i = 16; i < 24; i++) {
            registers[i] = new Register("$s" + (i - 16), "33X32", getInput(0));
        }
        for (int i = 24; i < 26; i++) {
            registers[i] = new Register("$t" + (i - 16), "33X32", getInput(0));
        }
        for (int i = 26; i < 28; i++) {
            registers[i] = new Register("$k" + (i - 26), "33X32", getInput(0));
        }
        registers[28] = new Register("$gp", "33X32", getInput(0));
        registers[29] = new Register("$sp", "33X32", getInput(0));
        registers[30] = new Register("$fp", "33X32", getInput(0));
        registers[31] = new Register("$ra", "33X32", getInput(0));

        Link[] data = new Link[32];

        for (int i = 0; i < 32; i++) {
            data[i] = getInput(i + 17);
        }
        writeReg(registers, data, getInput(12), getInput(13), getInput(14), getInput(15)
                , getInput(16), getInput(1));

        readReg(registers, getInput(6),getInput(5),getInput(4),getInput(3),getInput(2));
        readReg(registers, getInput(11),getInput(10),getInput(9),getInput(8),getInput(7));
        Simulator.debugger.addTrackItem(registers);
    }

}