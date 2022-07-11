package execute;

import simulator.control.Simulator;
import simulator.gates.combinational.And;
import simulator.gates.combinational.Nand;
import simulator.gates.combinational.Or;
import simulator.network.Link;
import simulator.wrapper.Wrapper;

public class AluControl extends Wrapper{


	public AluControl(String label, String stream, Link... links) {
		super(label, stream, links);
	}

	@Override
	public void initialize() {
		/*
		input :
			1 bit -> clock [0]
			2 bit -> Alu op [1:2]
			6 bit -> Function [3:8]
		output :
			4 bit -> Alu control[0:3]
		 */

		Or or1 = new Or("Or1" , getInput(5),getInput(8));
		And and1 = new And("And1",or1.getOutput(0),getInput(1));
		And and2 = new And("And2",getInput(7),getInput(1));
		Or or2 = new Or("Or2" , getInput(2),and2.getOutput(0));
		Nand nand1 = new Nand("Nand1",getInput(1),getInput(6));

		addOutput(Simulator.falseLogic,or2.getOutput(0),nand1.getOutput(0),and1.getOutput(0));
	}

}
