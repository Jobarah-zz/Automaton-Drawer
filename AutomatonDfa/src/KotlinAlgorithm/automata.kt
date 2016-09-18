package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  deterministicFiniteAutomaton()
    val state1 = State("q0",true,false)
    val state2 = State("q1",false,true)

    var transition1 = Transition("a","q0","q1")
    var transition2 = Transition("b","q0","q0")
    //------------------------------------------
    var transition3 = Transition("a","q1","q0")
    var transition4 = Transition("b","q1","q1")

    state1._transitions.add(transition1)
    state1._transitions.add(transition2)
    state2._transitions.add(transition3)
    state2._transitions.add(transition4)
//=====================================================
    var b = deterministicFiniteAutomaton()

    val statea = State("q0",true,true)
    val stateb = State("q1",false,false)

    var transitiona = Transition("a","q0","q0")
    var transitionb = Transition("b","q0","q1")
    //------------------------------------------
    var transitionc = Transition("a","q1","q1")
    var transitiond = Transition("b","q1","q0")


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

////    var nfa = automatonOps().complement(a)
////
////    for(state in nfa.states){
////        println(state._name)
////        println(state._isAcceptanceState)
////        for(transition in state._transitions) {
////            print(transition._symbol)
////            print(":")
////            print(transition._destiny)
////            print(",")
////        }
////        println(" ")
////        println("----------")
////    }
//
////    println(nfa.evaluate("aaa"))
////    println(a.evaluate("aa"))
//
//    var ntest = nonDeterministicFiniteAutomaton()
//    ntest.addState("q0", true, false)
//    ntest.addState("q1", false, false)
//    ntest.addState("q2", false, false)
//    ntest.addState("q3", false, true)
//    ntest.addState("q4", false, false)
//    ntest.states[0].addTransition("0", "q1")
//    ntest.states[0].addTransition("0", "q2")
//    ntest.states[0].addTransition("0", "q3")
//    ntest.states[3].addTransition("1", "q4")
//
////    ntest.alphabet.add("0")
////    ntest.alphabet.add("1")
////    var dfa = ntest.convertToDFA()
////    println(dfa.evaluate("0"))
////
////    var strToTest = "Z0,a/Z0"
////     var secString = strToTest.removeSuffix("Z0")
////    secString = secString.removePrefix("Z0")
////    println(secString)
//
////    println(strToTest.lastIndexOf(",",strToTest.length, false))
////   var pushVal = strToTest.subSequence(strToTest.lastIndexOf("/",strToTest.length, false)+1, strToTest.length)
////    println(pushVal)
////    println(strToTest[(strToTest.lastIndexOf(",",strToTest.length, false))+1])
////    var str2 = strToTest.removeSurrounding("(",")")
////    println(str2)
//
//
//    //===============================================PDA================================================================\
//    var initialState = State("q0",true, false)
//    var secondState = State("q1", false, false)
//    secondState.addTransition("(b,a/E)", "q1")
//
//    initialState.addTransition("(a,Z0/aZ0)", "q0")
//    initialState.addTransition("(b,a/E)", "q1")
//    initialState.addTransition("(a,a/aa)", "q0")
//
//    var pda = PushDownAutomata()
//    pda.states.add(initialState)
//    pda.states.add(secondState)
//
//    pda.alphabet.add("a")
//    pda.alphabet.add("b")
//
//    println(pda.evaluate("aaaabbbb"))

//    var nfae = nonDeterministicAutomatonEpsilon()
//    var state0 = State("0", true, false)
//    state0.addTransition("e","1")
//    state0.addTransition("e","3")
//
//    var state1 = State("1", false, false)
//    state1.addTransition("b", "2")
//
//    var state2 = State("2", false, false)
//    state2.addTransition("e", "5")
//
//    var state3 = State("3", false, false)
//    state3.addTransition("a", "4")
//
//    var state4 = State("4", false, false)
//    state4.addTransition("e", "5")
//
//    var state5 = State("5", false, false)
//    state5.addTransition("e", "0")
//
//    var state6 = State("6", false, false)
//    state6.addTransition("b","7")
//
//    var state7 = State("7", false, false)
//    state6.addTransition("c","8")
//
//    var state8 = State("8", false, false)
//    state6.addTransition("d","9")
//
//    var state9 = State("9", false, true)
//
//    nfae.states.add(state0)
//    nfae.states.add(state1)
//    nfae.states.add(state2)
//    nfae.states.add(state3)
//    nfae.states.add(state4)
//    nfae.states.add(state5)
//    nfae.states.add(state6)
//    nfae.states.add(state7)
//    nfae.states.add(state8)
//    nfae.states.add(state9)
//
//    nfae.alphabet.add("a")
//    nfae.alphabet.add("b")
//    nfae.alphabet.add("c")
//    nfae.alphabet.add("d")
//
//
//    var nfa = nfae.convertToNFA()
//    println(nfa.states.size)
//
//    for (state in nfa.states) {
////        println(state._name+"has transitions"+state._transitions.size)
//        for(transition in state._transitions) {
//            println(state._name+":"+transition._symbol+"->"+transition._destiny)
//        }
//    }

//    var automata = nonDeterministicAutomatonEpsilon()
//    automata.alphabet.add("a")
//    automata.alphabet.add("b")
//    automata.alphabet.add("c")
//    automata.alphabet.add("d")
//
//    automata.addState("0",true,false)
//    automata.addState("1",false,false)
//    automata.addState("2",false,false)
//    automata.addState("3",false,false)
//    automata.addState("4",false,false)
//    automata.addState("5",false,false)
//    automata.addState("6",false,false)
//    automata.addState("7",false,false)
//    automata.addState("8",false,false)
//    automata.addState("9",false,true)
//
//    automata.states[0].addTransition("e","1")
//    automata.states[0].addTransition("e","3")
//    automata.states[1].addTransition("b","2")
//    automata.states[2].addTransition("e","5")
//    automata.states[3].addTransition("a","4")
//    automata.states[4].addTransition("e","5")
//    automata.states[5].addTransition("e","6")
//    automata.states[5].addTransition("e","0")
//    automata.states[6].addTransition("b","7")
//    automata.states[7].addTransition("c","8")
//    automata.states[8].addTransition("d","9")
//
//    println(automata.evaluate("bbcd"))
//
//    var transformado = automata.convertToNFA().convertToDFA()
//
//    for(min in transformado.states){
//        for(arista in min._transitions){
//            println(min._name + " con salida " + arista._symbol + " hacia " + arista._destiny + " es aceptado: " + min._isAcceptanceState)
//        }
//    }

//    var turingMachine = turingMachine()
//    turingMachine.alphabet.add("0")
//    turingMachine.alphabet.add("1")
//    turingMachine.addState("q0", true, false)
//    turingMachine.addState("q1", false, false)
//    turingMachine.addState("q2", false, true)
//
//    turingMachine.states[0].addTransition("(0,0/R)","q0")
//    turingMachine.states[0].addTransition("(1,1/R)","q0")
//    turingMachine.states[0].addTransition("(B,B/L)","q1")
//
//    turingMachine.states[1].addTransition("(0,0/R)","q2")
//
//
//    println(turingMachine.evaluate("0"))

//    var regExp = RegEx()
//    regExp.expresionRegular = "((0+10)*] + [(0+10)*1] + [(0+10)*11(1+01)*] + [(0+10)*11(1+01)*0)"
//    var nfa = regExp.transformarNFAe()
//
//    regExp.printAutomata()


    var reg = AutomatonRegex()
    var regex = reg.ParseDFAToRegex(a.states)
    print(regex)


}
