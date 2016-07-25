/**
 * Created by jenamorado on 7/25/16.
 */
import java.util.ArrayList;

/**
 * Created by jenamorado on 7/25/16.
 */
public class Automaton {
    ArrayList<States> states;
    ArrayList<Character> alphabet;
    String strToEval;

    public States getInitialState(){
        for(int j = 0; j < states.size(); j++){
            if(states.get(j).isAcceptanceState){
                return states.get(j);
            }
        }
        return null;
    }

    public States getState(String stateName){
        for(int j = 0; j < states.size(); j++){
            if(states.get(j).getName().equals(stateName)){
                return states.get(j);
            }
        }
        return null;
    }

    public boolean evaluate(String strEvString){
        char eval[] = strEvString.toCharArray();
        States currentState = getInitialState();
        if(currentState!=null){
            for(int i = 0; i < eval.length; i++){
                for(int j = 0; j < currentState.transitions.size(); j++){
                    if(currentState.transitions.get(j).symbol == eval[i]){
                        currentState = getState(currentState.transitions.get(j).destiny);
                    }
                }
            }
            if(currentState.isAcceptanceState){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}

