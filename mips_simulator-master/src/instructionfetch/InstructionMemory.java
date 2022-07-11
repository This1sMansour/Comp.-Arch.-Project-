package instructionfetch;

import assembler.Assembler;
import simulator.control.Simulator;
import simulator.gates.combinational.ByteMemory;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

import java.util.ArrayList;

public class InstructionMemory extends Wrapper {

    ByteMemory insMemory;

    public InstructionMemory(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
         /*
        input :
            32 bit -> Pc[0:31]
        output :
            32 bit -> Instruction[0:31]
         */

        Link[] pc = new Link[32];
        for (int i = 0 ; i < 32 ; i++){
            pc[i] = getInput(i);
        }
        this.insMemory = new ByteMemory("Instruction Memory");

        Boolean[][] mem = new Boolean[65536][8];
        for (int i = 0; i < 65536; i++){
            for (int j = 0; j < 8; j++) {
                mem[i][j] = false;
            }
        }

        // This part is for test
        // Add instructions to instruction memory
        String bin = "00100000000010010000000011111110";
        mem = MemBin(Assembler.instruction);

        //
        setInsMemory(mem);
        instructionRead(pc);
    }

    public void setInsMemory(Boolean[][] InsMemory){
        this.insMemory.setMemory(InsMemory);
    }

    public void instructionRead(Link[] pc){
        this.insMemory.addInput(Simulator.falseLogic);
        for (int i = 0 ; i < 16 ; i++){
            this.insMemory.addInput(pc[i + 16]);
        }
        for (int i = 0 ; i < 32 ; i++){
            addOutput(this.insMemory.getOutput(i));
        }
    }

    public Boolean[][] MemBin(ArrayList<String> instruction){
        Boolean[][] mm = new Boolean[65536][8];

        for (int i = 0; i < 65536; i++){
            for (int j = 0; j < 8; j++) {
                mm[i][j] = false;
            }
        }
        int address = 0;
        for (String bin : instruction) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    mm[i+address][j] = bin.charAt((i * 8) + j) != '0';
                }
            }
            address += 4;
        }
        return mm;
    }



}
