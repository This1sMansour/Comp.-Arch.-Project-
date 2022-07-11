package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Assembler {
    public static Boolean[][] mem = new Boolean[65536][8];
    protected static int address = 0;
    protected static HashMap<String, String> label = new HashMap<>();
    public static ArrayList<String> instruction = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        File file = new File("test.asm");
//        file.createNewFile();
        System.out.println(file.getAbsolutePath());
        Scanner input = new Scanner(file);
        int pc = 0;
        filePreProcess(file);
        initialMem();
        //check lowercase
        do {
            String inst = input.nextLine();
            inst = inst.split("#")[0];
            if (inst.equals("q")) {
                break;
            }else if(inst.isEmpty() || inst.charAt(0) == '#');
            else if (inst.equals(inst.toLowerCase())) {
                String temp = split_input_function(inst, pc);
                if ('0' <= temp.charAt(0) && '1' >= temp.charAt(0)) {
                    System.out.println(temp);
                    pc += 4;
                    instruction.add(temp);
                }

            } else {
                System.out.print("invalid input");
                break;
            }
        } while (input.hasNextLine());
        input.close();

    }

    public static void initialMem() {
        for (int i = 0; i < 65536; i++) {
            for (int j = 0; j < 8; j++) {
                mem[i][j] = false;
            }
        }
    }

    protected static void filePreProcess(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            int index = 0;
            String stLabel = scanner.nextLine();
            stLabel = stLabel.replaceAll(" +", "");
            String[] stComment = stLabel.split("#");
            stLabel = stComment[0];
            if (stLabel.indexOf(':') != -1) {
                label.put(stLabel.split(":")[0], "" + (address));
            } else
                address += 4;
        }
        address -= 4;
    }

    public static int makeInstructionSet(String instruction, int address) {
        for (int i = 0; i < 32; i++) {
            address = i % 8 == 0 && i != 0? address+1 : address;
            mem[address][i % 8] = instruction.charAt(i) != '0';
        }
        return address;
    }

    protected static String split_input_function(String input, int pc) throws Exception {
        String remove_comma = input.trim().replaceAll("[,]", " ").replaceAll(" +", " ");
        String[] split_input = remove_comma.split("\\s");
        // System.out.print(split_input[1]);
        return instructions(split_input[0], split_input, pc);
    }

    protected static String calculateBranchAddress(String stLabel, int pc) throws Exception {
        int bAddress = label.get(stLabel) == null ? Integer.parseInt(stLabel) : Integer.parseInt(label.get(stLabel));
        if (bAddress > address)
            throw new Exception("Invalid Label");
        bAddress = (bAddress - pc - 8) / 4;
        return bAddress + "";
    }

    protected static String instructions(String op, String[] data, int pc) throws Exception {

        if (op.equals("and")) {
            return rType(data[1], data[2], data[3], "00000", "100100");
        } else if (op.equals("sub")) {
            return rType(data[1], data[2], data[3], "00000", "100010");
        } else if (op.equals("add")) {
            return rType(data[1], data[2], data[3], "00000", "100000");
        } else if (op.equals("or")) {
            return rType(data[1], data[2], data[3], "00000", "100101");
        } else if (op.equals("xor")) {
            return rType(data[1], data[2], data[3], "00000", "100110");
        } else if (op.equals("slt")) {
            return rType(data[1], data[2], data[3], "00000", "101010");
        } else if (op.equals("addi")) {
            return iType("001000", data[2], data[1], data[3]);
        } else if (op.equals("slti")) {
            return iType("001010", data[2], data[1], data[3]);
        } else if (op.equals("andi")) {
            return iType("001100", data[2], data[1], data[3]);
        } else if (op.equals("ori")) {
            return iType("001101", data[2], data[1], data[3]);
        } else if (op.equals("xori")) {
            return iType("001110", data[2], data[1], data[3]);
        } else if (op.equals("lw")) {
            // remove ( ) with space
            String data_replace = data[2].replaceAll("[(|)]", " ");
            String[] data_split = data_replace.split(" ");

            return iType("100011", data_split[1], data[1], data_split[0]);
        } else if (op.equals("sw")) {
            // remove ( ) with space
            String data_replace = data[2].replaceAll("[(|)]", " ");
            String[] data_split = data_replace.split(" ");

            return iType("101011", data_split[1], data[1], data_split[0]);

        } else if (op.equals("beq")) {
            return iTypeLabel("000100", data[1], data[2], calculateBranchAddress(data[3], pc));
        } else if (op.equals("bne")) {
            return iTypeLabel("000101", data[1], data[2], calculateBranchAddress(data[3], pc));
        } else if (op.equals("j")) {
            int jAddress = label.get(data[1]) == null ? Integer.parseInt(data[1]) : Integer.parseInt(label.get(data[1]));
            if (jAddress > address) {
                throw new Exception("Invalid Label");
            }
            jAddress /= 4;
            return jType("000010", jAddress + "");
        } else if(op.indexOf(':') == -1) {
            System.out.print("Invalid Instruction.");
        }

        return op;
    }

    protected static String rType(String rd, String rs, String rt, String sa, String fn) {
        //op code
        return String.format("000000%05d%05d%05d%s%s", registerNumber(rs), registerNumber(rt), registerNumber(rd), sa, fn);

    }

    protected static String jType(String opc, String label) {
        //op code
        return String.format("%s%s", opc, signExtend(label,26));


    }

    protected static StringBuilder signExtend(String data, int maxLen) {
        int number = Integer.parseInt(data);
        StringBuilder binary = new StringBuilder(Integer.toBinaryString(number));
        while (binary.length() < maxLen)
            if (number < 0)
                binary.insert(0, binary.charAt(binary.length() - 1));
            else
                binary.insert(0, "0");
        if (binary.length() > maxLen)
            binary.delete(0, binary.length()-maxLen);
        return binary;

    }

    protected static String iType(String opc, String rs, String rt, String data) {
        //op code

        return String.format("%s%05d%05d%s", opc, registerNumber(rs), registerNumber(rt), signExtend(data, 16));


    }

    protected static String iTypeLabel(String opc, String rs, String rt, String data) {
        //op code
        return String.format("%s%05d%05d%s", opc, registerNumber(rs), registerNumber(rt), signExtend(data, 16));
    }

    protected static int registerNumber(String r) {
        int val = 0;
        char rtype = r.charAt(1);
        int value = Character.getNumericValue(r.charAt(2));
        if (r.equals("$zero")) {
            return 00000;
        } else if (r.equals("$at")) {
            return 00001;

        } else if (r.equals("$gp")) {
            return Integer.parseInt(Integer.toBinaryString(28));

        } else if (r.equals("$sp")) {
            return Integer.parseInt(Integer.toBinaryString(29));

        } else if (r.equals("$fp")) {
            return Integer.parseInt(Integer.toBinaryString(30));

        } else if (r.equals("$ra")) {
            return Integer.parseInt(Integer.toBinaryString(31));

        } else if (rtype == 'v') {
            val = 2 + value;
            return Integer.parseInt(Integer.toBinaryString(val));

        } else if (rtype == 'a') {
            val = 4 + value;
            return Integer.parseInt(Integer.toBinaryString(val));
        } else if (rtype == 't') {
            if (value >= 0 && value <= 7) {
                val = 8 + value;
                return Integer.parseInt(Integer.toBinaryString(val));
            } else if (value == 8) {
                return Integer.parseInt(Integer.toBinaryString(24));
            } else {
                return Integer.parseInt(Integer.toBinaryString(25));
            }

        } else if (rtype == 's') {
            val = 16 + value;
            // System.out.println(Integer.toBinaryString(val));
            return Integer.parseInt(Integer.toBinaryString(val));
        }


        return 0;

    }
}
