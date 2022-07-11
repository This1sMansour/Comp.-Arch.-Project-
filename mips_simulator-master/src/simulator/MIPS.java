package simulator;

import execute.BranchAddress;
import execute.JumpAddress;
import instructiondecode.ControlUnit;
import writeback.DataMemory;
import instructiondecode.ProgramCounterAdder;
import instructionfetch.*;
import instructionfetch.RegisterFile;
import execute.ALU;
import execute.AluControl;
import basiccomponents.Mux;
import basiccomponents.SignExtend;
import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Not;
import simulator.gates.combinational.Xor;
import simulator.gates.sequential.Clock;
import simulator.gates.sequential.flipflops.DFlipFlop;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

//import simulator.wrapper.wrappers.DFlipFlop;
import simulator.wrapper.wrappers.Multiplexer;

public class MIPS extends Wrapper {
    public MIPS(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        //Generating clock pulse for the whole circuit

        Clock clk = new Clock("CLK", 1000);

        //Instantiating program counter
        DFlipFlop shoift0 = new DFlipFlop("SHIFT0", clk.getOutput(0), Simulator.trueLogic);
        DFlipFlop shoift1 = new DFlipFlop("SHIFT1",clk.getOutput(0), shoift0.getOutput(0));
        DFlipFlop[] pc = new DFlipFlop[32];
        for (int i = 0; i < 32; ++i) {
            pc[i] = new DFlipFlop("D"+ i ,clk.getOutput(0));
        }

        ProgramCounterAdder programCounterAdder = new ProgramCounterAdder("p", "32X32");
        for (int i = 0; i < 32; i++)
            programCounterAdder.addInput(pc[i].getOutput(0));

        Link[] counterInit = new Link[32];
        for (int i = 0; i < 32; ++i) {
            counterInit[i] = Simulator.falseLogic;
        }

        //Instantiating Instruction memory
        InstructionMemory instructionMemory = new InstructionMemory("INSM", "32X32");
        for (int i = 0; i < 32; i++) {
            instructionMemory.addInput(pc[i].getOutput(0));
        }

        //Instantiating Control unit
        ControlUnit cu = new ControlUnit("CU", "6X11");

        for (int i = 0; i < 6; i++) {
            cu.addInput(instructionMemory.getOutput(i));
        }
        //Shift to left the address by two
        JumpAddress jumpAddress = new JumpAddress("JADDRESS", "30X32");

        for (int i = 0; i < 4; i++) {
            jumpAddress.addInput(programCounterAdder.getOutput(i));
        }

        for (int i = 6; i < 32; i++) {
            jumpAddress.addInput(instructionMemory.getOutput(i));
        }
//        Calculate branch address
        //Instantiating Register file

        RegisterFile registerFile = new RegisterFile("REGFILE", "49X64", clk.getOutput(0),
                                                                                    cu.getOutput(3));
        Mux mux0 = new Mux("MUX0", "65X32");
        for (int i = 0; i < 27; i++) {
            mux0.addInput(Simulator.falseLogic);
        }
        for (int i = 11; i < 16; i++) {
            mux0.addInput(instructionMemory.getOutput(i));
        }

        for (int i = 0; i < 27; i++) {
            mux0.addInput(Simulator.falseLogic);
        }

        for (int i = 16; i < 21; i++) {
            mux0.addInput(instructionMemory.getOutput(i));
        }

        mux0.addInput(cu.getOutput(0));

        for (int i = 6; i < 16; i++) {
            registerFile.addInput(instructionMemory.getOutput(i));
        }
        for (int i = 31; i>=27; i--) {
            registerFile.addInput(mux0.getOutput(i));
        }

//
//        for (int i = 0; i < 32; i++) {
//            registerFile.addInput(Simulator.falseLogic);
//        }

        SignExtend signExtend = new SignExtend("SE", "16X32");

        for (int i = 16; i < 32; i++) {
            signExtend.addInput(instructionMemory.getOutput(i));
        }

        Mux mux1 = new Mux("MUX1", "65X32");
        for (int i = 0; i < 32; i++) {
            mux1.addInput(registerFile.getOutput(i + 32));
        }

        for (int i = 0; i < 32; i++) {
            mux1.addInput(signExtend.getOutput(i));
        }

        mux1.addInput(cu.getOutput(1));


        //Branch address calculation

        BranchAddress branchAddress = new BranchAddress("BA", "64X32");
        for (int i = 0; i < 32; i++) {
            branchAddress.addInput(programCounterAdder.getOutput(i));
        }

        for (int i = 0; i < 32; i++) {
            branchAddress.addInput(signExtend.getOutput(i));
        }

        //Instantiating Alu control

        AluControl aluControl = new AluControl("ALUC", "9X4", clk.getOutput(0));

        aluControl.addInput(cu.getOutput(7), cu.getOutput(8));
        for (int i = 26; i < 32; i++) {
            aluControl.addInput(instructionMemory.getOutput(i));
        }

        //Instantiating ALU

        ALU alu = new ALU("ALU", "67X33");

        for (int i = 0; i < 32; i++) {
            alu.addInput(registerFile.getOutput(i));

        }

        for (int i = 0; i < 32; i++) {
            alu.addInput(mux1.getOutput(i));

        }

        alu.addInput(aluControl.getOutput(1), aluControl.getOutput(2), aluControl.getOutput(3));

//        Instantiating branch mux
//        BranchMUX branchMUX = new BranchMUX("BRM", "65X32");
        Mux branchMUX = new Mux("BranchMUX", "65X32");
        And and = new And("AND", cu.getOutput(6), alu.getOutput(32));


        for (int i = 0; i < 32; i++) {
            branchMUX.addInput(programCounterAdder.getOutput(i));
        }

        for (int i = 0; i < 32; i++) {
            branchMUX.addInput(branchAddress.getOutput(i));
        }
        Xor xor = new Xor("Xor",and.getOutput(0),new And("And1", cu.getOutput(10),
                new Not("Not",alu.getOutput(32)).getOutput(0)).getOutput(0));
        branchMUX.addInput(xor.getOutput(0));

//        JumpMUX jumpMUX = new JumpMUX("JMPX", "65X32", cu.getOutput(9));
        Mux jumpMux = new Mux("mux4", "65X32");
        for (int i = 0; i < 32; i++) {
//            jumpMUX.addInput(branchMUX.getOutput(i));
            jumpMux.addInput(branchMUX.getOutput(i));
        }

        for (int i = 0; i < 32; i++) {
//            jumpMUX.addInput(jumpAddress.getOutput(i));
            jumpMux.addInput(jumpAddress.getOutput(i));
        }
        jumpMux.addInput(cu.getOutput(9));
//        Multiplexer[] counterSelect = new Multiplexer[32];
//        for (int i = 0; i < 32; ++i) {
//            counterSelect[i] = new Multiplexer("M" + i, "3X1",
//                    shoift1.getOutput(0), counterInit[i], programCounterAdder.getOutput(i));
//        }
//        for (int i = 0; i < 32; i++) {
//            pc[i].addInput(counterSelect[i].getOutput(0));
//        }

        // Instantiating data mem

        DataMemory dataMemory = new DataMemory("DataMem", "66X32", cu.getOutput(5),cu.getOutput(4));

        for (int i = 0; i < 32; i++) {
            dataMemory.addInput(alu.getOutput(i));
        }
        for (int i = 32; i < 64; i++) {
            dataMemory.addInput(registerFile.getOutput(i));
        }
        Mux mux2 = new Mux("Mux2", "65X32");

        for (int i = 0; i < 32; i++) {
            mux2.addInput(alu.getOutput(i));
        }

        for (int i = 0; i < 32; i++) {
            mux2.addInput(dataMemory.getOutput(i));
        }
        mux2.addInput(cu.getOutput(2));

        Mux mux3 = new Mux("Mux3" , "65X32");
        for (int i = 0; i < 32; i++) {
            mux3.addInput(counterInit[i]);
        }
        for (int i = 0; i < 32; i++) {
            mux3.addInput(mux2.getOutput(0));
        }
        mux3.addInput(shoift1.getOutput(0));
        for (int i = 0; i < 32; i++) {
            registerFile.addInput(mux2.getOutput(i));
        }

        Multiplexer[] counterSelect = new Multiplexer[32];
        for (int i = 0; i < 32; ++i) {
            counterSelect[i] = new Multiplexer("M" + i, "3X1",
                    shoift1.getOutput(0), counterInit[i], jumpMux.getOutput(i));
        }
        for (int i = 0; i < 32; i++) {
            pc[i].addInput(counterSelect[i].getOutput(0));
        }
//        Simulator.debugger.addTrackItem(registerFile);
        Simulator.debugger.addTrackItem(pc);
//        Simulator.debugger.addTrackItem(alu);
    }
}
