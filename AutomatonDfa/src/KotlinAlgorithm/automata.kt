package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  deterministicFiniteAutomaton()
    val state1 = State("q0",true,true)
    val state2 = State("q1",false,false)

    var transition1 = Transition('a',"q0","q1")
    var transition2 = Transition('b',"q0","q0")
    //------------------------------------------
    var transition3 = Transition('a',"q1","q0")
    var transition4 = Transition('b',"q1","q1")
//=====================================================
    var b = deterministicFiniteAutomaton()

    val statea = State("q2",true,true)
    val stateb = State("q3",false,false)

    var transitiona = Transition('a',"q2","q2")
    var transitionb = Transition('b',"q2","q3")
    //------------------------------------------
    var transitionc = Transition('a',"q3","q3")
    var transitiond = Transition('b',"q3","q2")

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
    a.alphabet.add('a')
    a.alphabet.add('b')

    b.states.add(statea)
    b.states.add(stateb)
    b.alphabet.add('a')
    b.alphabet.add('b')

    var nfa = automatonOps().union(a,b)

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

    print(nfa.evaluate("a"))

}
