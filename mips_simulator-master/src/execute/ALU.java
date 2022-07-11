package execute;

import basiccomponents.*;
import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.network.Link;
import simulator.wrapper.Wrapper;
import simulator.wrapper.wrappers.Adder;

import java.util.ArrayList;
import java.util.List;

public class ALU extends Wrapper {
    public ALU(String label, String stream, Link... links) {
        super(label, stream, links);
    }

    @Override
    public void initialize() {

        /*
        32 bit for first operand
        32 bit for the second operand
        3 bit for alu operation
         */

        // TODO: 7/24/2020 Add support for set less than instruction

        Adder adder = new Adder("ADD0", "64X33");
        Subtractor subtractor = new Subtractor("SUB0", "64X33");
        And32 and32 = new And32("AND32", "64X32");
        Or32 or32 = new Or32("OR32", "64X32");

        //Feed input A to all sub modules
        for(int i = 0; i < 32; i++){
            adder.addInput(getInput(i));
            subtractor.addInput(getInput(i));
            and32.addInput(getInput(i));
            or32.addInput(getInput(i));
        }

        //Feed input B to all sub modules
        for(int i = 32; i < 64; i++){
            adder.addInput(getInput(i));
            subtractor.addInput(getInput(i));
            and32.addInput(getInput(i));
            or32.addInput(getInput(i));
        }

        //SetLessThan

        SetLessThan setLessThan = new SetLessThan("SLT", "3X32", getInput(0), getInput(32), subtractor.getOutput(1));

        List<Mux> muxes0 = new ArrayList<>();
        List<Mux> muxes1 = new ArrayList<>();
        List<Mux> muxes2 = new ArrayList<>();

        //First stage muxes

        //first mux
        muxes0.add(new Mux("MUX0", "65X32"));
        for(int i = 0; i < 32; i++)
            muxes0.get(0).addInput(adder.getOutput(i + 1));

        for(int i = 0; i < 32; i++)
            muxes0.get(0).addInput(subtractor.getOutput(i + 1));

        muxes0.get(0).addInput(getInput(64));



        //second mux
        muxes0.add(new Mux("MUX1", "65X32"));
        for(int i = 0; i < 32; i++)
            muxes0.get(1).addInput(and32.getOutput(i));

        for(int i = 0; i < 32; i++)
            muxes0.get(1).addInput(setLessThan.getOutput(i));

        muxes0.get(1).addInput(getInput(64));

        //third mux
        muxes0.add(new Mux("MUX2", "65X32"));
        for(int i = 0; i < 32; i++)
            muxes0.get(2).addInput(or32.getOutput(i));

        for(int i = 0; i < 32; i++)
            muxes0.get(2).addInput(subtractor.getOutput(i));

        muxes0.get(2).addInput(getInput(64));


        //fourth mux
        muxes0.add(new Mux("MUX3", "65X32"));
        for(int i = 0; i < 32; i++)
            muxes0.get(3).addInput(and32.getOutput(i));

        for(int i = 0; i < 32; i++)
            muxes0.get(3).addInput(setLessThan.getOutput(i));

        muxes0.get(3).addInput(getInput(64));

        //Second stage muxes

        //first mux
        muxes1.add(new Mux("MUX4", "65X32"));

        for(int i = 0; i < 32; i++){
            muxes1.get(0).addInput(muxes0.get(1).getOutput(i));
        }

        for(int i = 0; i < 32; i++){
            muxes1.get(0).addInput(muxes0.get(0).getOutput(i));
        }

        muxes1.get(0).addInput(getInput(65));
        //second mux

        muxes1.add(new Mux("MUX5", "65X32"));

        for(int i = 0; i < 32; i++){
            muxes1.get(1).addInput(muxes0.get(2).getOutput(i));
        }

        for(int i = 0; i < 32; i++){
            muxes1.get(1).addInput(muxes0.get(3).getOutput(i));
        }

        muxes1.get(1).addInput(getInput(65));

        //Third stage mux

        muxes2.add(new Mux("MUX6", "65X32"));


        for(int i = 0; i < 32; i++){
            muxes2.get(0).addInput(muxes1.get(0).getOutput(i));
        }

        for(int i = 0; i < 32; i++){
            muxes2.get(0).addInput(muxes1.get(1).getOutput(i));
        }

        muxes2.get(0).addInput(getInput(66));

        //Generate output

        for(int i = 0; i < 32; i++)
            addOutput(muxes2.get(0).getOutput(i));


        //Zero signal

        Xnor32 xnor = new Xnor32("XNOR", "64X32");
        And and = new And("AND");

        for(int i = 0; i < 32; i++)
            xnor.addInput(subtractor.getOutput(i + 1));


        for(int i = 0; i < 32; i++) {
            xnor.addInput(Simulator.falseLogic);
            and.addInput(xnor.getOutput(i));
        }

        addOutput(and.getOutput(0));
    }


}
