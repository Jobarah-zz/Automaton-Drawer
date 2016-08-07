package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  nonDeterministicFiniteAutomaton()
    val state1 = State("q0",true,false)
    val state2 = State("q1",false,false)
    val state3 = State("q2",false,true)
    var transition1 = Transition('a',"q0","q0")
    var transition2 = Transition('b',"q0","q0")
    var transition3 = Transition('a',"q0","q1")
    state1._transitions.add(transition1)
    state1._transitions.add(transition2)
    state1._transitions.add(transition3)
    var transition4 = Transition('a',"q1","q2")
    var transition5 = Transition('b',"q1","q2")
    state2._transitions.add(transition4)
    state2._transitions.add(transition5)
    a.states.add(state1)
    a.states.add(state2)
    a.states.add(state3)
    a.alphabet.add('a')
    a.alphabet.add('b')
    if(a.evaluate("abba")){
        println("paso")
    }
    else{
        println("no paso")
    }

    var  dfa=a.convertToDFA()
    var states = dfa.states
    for (state in states){
        println(state._name)
    }
}
