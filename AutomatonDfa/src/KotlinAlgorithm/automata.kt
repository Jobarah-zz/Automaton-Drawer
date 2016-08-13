package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  nonDeterministicAutomatonEpsilon()
    val state1 = State("p",true,false)
    val state2 = State("q",false,false)
    val state3 = State("r",false,true)
//    State P Transitions
    var transition1 = Transition('a',"p","p")
    var transition2 = Transition('b',"p","q")
    var transition3 = Transition('c',"p","r")
//    State q Transitions
    var transition4 = Transition('e',"q","p")
    var transition5 = Transition('a',"q","q")
    var transition6 = Transition('b',"q","r")
//    State r Transitions
    var transition7 = Transition('e',"r","q")
    var transition8 = Transition('a',"r","r")
    var transition9 = Transition('c',"r","p")

    state1._transitions.add(transition1)
    state1._transitions.add(transition2)
    state1._transitions.add(transition3)
    state2._transitions.add(transition4)
    state2._transitions.add(transition5)
    state2._transitions.add(transition6)
    state3._transitions.add(transition7)
    state3._transitions.add(transition8)
    state3._transitions.add(transition9)
    a.states.add(state1)
    a.states.add(state2)
    a.states.add(state3)
    a.alphabet.add('a')
    a.alphabet.add('b')
    a.alphabet.add('c')

    var nfa = a.convertToNFA()
    for(state in nfa.states){
        println(state._name)
        println(state._isAcceptanceState)
        for(transition in state._transitions) {
            print(transition._symbol)
            print(":")
            print(transition._destiny)
            print(",")
        }
        println(' ')
    }

    print(nfa.evaluate("a"))
//    var  dfa=a.convertToDFA()
//    var states = dfa.states
//    for (state in states){
//        println(state._name)
//    }
}
