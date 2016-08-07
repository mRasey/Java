package instructions;

import java.util.ArrayList;

public class _move extends Instruction {

    public _move(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "move" :
            case "move/16" :
            case "move/from16" :
            case "move-wide" :
            case "move-wide/from16" :
            case "move-object" :
            case "move-object/16" :
            case "move-object/from16" :
            case "move-result" :
            case "move-result-wide" :
            case "move-result-object" :
            case "move-exception" :
        }
    }
}
