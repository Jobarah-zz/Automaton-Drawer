//import java.util.ArrayList;
//
///**
// * Created by jenamorado on 7/25/16.
// */
//public class automata {
//
//    public static void main(String[] args) {
//        Automaton a = new Automaton();
//        States state1 = new States();
//        States states2 =new States();
//        state1.isInitialState = true;
//        state1.name="Initial State";
//        state1.isAcceptanceState = false;
//        states2.isInitialState = false;
//        states2.name="Acceptance State";
//        state1.isInitialState = false;
//        states2.isAcceptanceState = false;
//        Transition transition1 = new Transition();
//        transition1.symbol='1';
//        transition1.origin = state1.name;
//        transition1.destiny = states2.name;
//        state1.transitions = new ArrayList<>();
//        state1.transitions.add(transition1);
//        Transition transition2 = new Transition();
//        transition2.symbol='0';
//        transition2.origin = states2.name;
//        transition2.destiny = state1.name;
//        states2.transitions = new ArrayList<>();
//        states2.transitions.add(transition2);
//        a.states = new ArrayList<>();
//        a.states.add(state1);
//        a.states.add(states2);
//        System.out.println(a.evaluate("1"));
//    }
//}
