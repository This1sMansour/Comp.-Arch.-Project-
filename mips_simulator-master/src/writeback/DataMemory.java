package writeback;

import simulator.control.Simulator;
import simulator.gates.combinational.ByteMemory;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Multiplexer;

public class DataMemory extends Wrapper {

    ByteMemory dataMemory;

    public DataMemory(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {
        /*
        input :
            1 bit -> Mem Write[0]
            1 bit -> Mem Read[1]
            32 bit -> Address[2:33]
            32 bit -> Write Data[34:65]
         output :
            32 bit -> Read data[0:31]
         */
        Link[] address = new Link[32];
        Link[] writeData = new Link[32];
        Link memWrite = getInput(0);
        Link memRead = getInput(1);

        Boolean[][] mem = new Boolean[65536][8];
        for (int i = 0; i < 65536; i++){
            for (int j = 0; j < 8; j++) {
                mem[i][j] = false;
            }
        }


        for (int i = 0 ; i < 32 ; i++){
            address[i] = getInput(i + 2);
            writeData[i] = getInput(i + 34);
        }

        this.dataMemory = new ByteMemory("Data Memory");
        setDataMemory(mem);
        writeData(address , writeData , memWrite);
        readData(address , memRead);

    }

    // for store data
    public void writeData(Link[] address ,Link[] writeData , Link memWrite){
        this.dataMemory.addInput(Simulator.falseLogic);// This is for read previous value of memory
        for (int i = 0 ; i < 16 ; i++){
            this.dataMemory.addInput(address[i + 16]);
        }

        Multiplexer[] mux1 = new Multiplexer[48];

        // It is Address
        for (int i = 0 ; i < 16 ; i++){
            mux1[i] = new Multiplexer("Mux" + i,"3X1",
                    memWrite ,Simulator.falseLogic, address[i + 16]);
        }

        // It is Data
        for (int i = 0 ; i < 32 ; i++){
            mux1[i + 16] = new Multiplexer("Mux" + i + 16 , "3X1" ,
                    memWrite,this.dataMemory.getOutput(i), writeData[i]);
        }

        this.dataMemory.addInput(Simulator.trueLogic);// This is write signal for byte memory
        for (int i = 0 ; i < 48 ; i++){
            this.dataMemory.addInput(mux1[i].getOutput(0));
        }
        for (int i = 0 ; i < 32 ; i++){
            addOutput(this.dataMemory.getOutput(i));
        }

    }

    // for load data
    public void readData(Link[] address , Link memRead){
        Multiplexer[] mux2 = new Multiplexer[16];
        this.dataMemory.addInput(Simulator.falseLogic);

        // It is Address
        for (int i = 0 ; i < 16 ; i++){
            mux2[i] = new Multiplexer("Mux" + i ,"3X1" ,
                    memRead ,Simulator.trueLogic ,address[i + 16]);
        }


        for (int i = 0 ; i < 16 ; i++){
            this.dataMemory.addInput(mux2[i].getOutput(0));
        }

        for (int i = 0 ; i < 32 ; i++){
            addOutput(this.dataMemory.getOutput(i));
        }

    }


    public void setDataMemory(Boolean[][] DataMemory){
        this.dataMemory.setMemory(DataMemory);
    }
}
