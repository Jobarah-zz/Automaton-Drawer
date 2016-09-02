package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  deterministicFiniteAutomaton()
    val state1 = State("q0",true,true)
    val state2 = State("q1",false,false)

    var transition1 = Transition("a","q0","q1")
    var transition2 = Transition("b","q0","q0")
    //------------------------------------------
    var transition3 = Transition("a","q1","q0")
    var transition4 = Transition("b","q1","q1")
//=====================================================
    var b = deterministicFiniteAutomaton()

    val statea = State("q0",true,true)
    val stateb = State("q1",false,false)

    var transitiona = Transition("a","q0","q0")
    var transitionb = Transition("b","q0","q1")
    //------------------------------------------
    var transitionc = Transition("a","q1","q1")
    var transitiond = Transition("b","q1","q0")

    state1._transitions.add(transition1)
    state1._transitions.add(transition2)
    state2._transitions.add(transition3)
    state2._transitions.add(transition4)

    statea._transitions.add(transitiona)
    statea._transitions.add(transitionb)
    stateb._transitions.add(transitionc)
    stateb._transitions.add(transitiond)

    a.states.add(state1)
    a.states.add(state2)
    a.alphabet.add("a")
    a.alphabet.add("b")

    b.states.add(statea)
    b.states.add(stateb)
    b.alphabet.add("a")
    b.alphabet.add("b")

    var nfa = automatonOps().complement(a)

    for(state in nfa.states){
        println(state._name)
        println(state._isAcceptanceState)
        for(transition in state._transitions) {
            print(transition._symbol)
            print(":")
            print(transition._destiny)
            print(",")
        }
        println(" ")
        println("----------")
    }

    println(nfa.evaluate("aaa"))
    println(a.evaluate("aa"))

    var ntest = nonDeterministicFiniteAutomaton()
    ntest.addState("q0", true, false)
    ntest.addState("q1", false, false)
    ntest.addState("q2", false, false)
    ntest.addState("q3", false, true)
    ntest.addState("q4", false, false)
    ntest.states[0].addTransition("0", "q1")
    ntest.states[0].addTransition("0", "q2")
    ntest.states[0].addTransition("0", "q3")
    ntest.states[3].addTransition("1", "q4")

    ntest.alphabet.add("0")
    ntest.alphabet.add("1")
    var dfa = ntest.convertToDFA()
    println(dfa.evaluate("0"))
}
