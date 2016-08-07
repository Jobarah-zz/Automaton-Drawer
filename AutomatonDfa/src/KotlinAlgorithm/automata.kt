package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  nonDeterministicAutomatonEpsilon()
    val state1 = State("A",true,false)
    val state2 = State("B",false,false)
    val state3 = State("C",false,false)
    val state4 = State("D",false,true)
    var transition1 = Transition('e',"A","B")
    var transition2 = Transition('0',"A","A")
    var transition3 = Transition('0',"B","C")
    var transition4 = Transition('e',"B","D")
    var transition5 = Transition('1',"C","B")
    var transition6 = Transition('0',"D","D")
    var transition7 = Transition('1',"D","D")
    state1._transitions.add(transition1)
    state1._transitions.add(transition2)
    state2._transitions.add(transition3)
    state2._transitions.add(transition4)
    state3._transitions.add(transition5)
    state4._transitions.add(transition6)
    state4._transitions.add(transition7)
    a.states.add(state1)
    a.states.add(state2)
    a.states.add(state3)
    a.states.add(state4)
    a.alphabet.add('0')
    a.alphabet.add('1')
//    a.alphabet.add('e')

    var nfa = a.convertToNFA()
    for(state in nfa.states){
        println(state._name)
        for(transition in state._transitions) {
            print(transition._symbol)
            print(":")
            print(transition._destiny)
            print(",")
        }
        println(' ')
    }
//    var  dfa=a.convertToDFA()
//    var states = dfa.states
//    for (state in states){
//        println(state._name)
//    }
}
