package KotlinAlgorithm

/**
 * Created by Jobarah on 8/6/2016.
 */
 class nonDeterministicAutomatonEpsilon: Automaton() {
    var evaluatedStates:MutableList<String> = mutableListOf<String>()
    var epsilonClosure = mutableListOf<State>()

    override fun evaluate(strEvString:String):Boolean{
        return true
    }
    open fun getClosure(state:State){
        if(!evaluatedStates.contains(state._name)){
            epsilonClosure.add( state )
            evaluatedStates.add( state._name )
            for(transition in state._transitions){
                if(transition._symbol.equals('e')){
                    var nextState = getState(transition._destiny) as State
                    getClosure( nextState )
                }
            }
        }
    }

    open fun getReachableStates(closureStates:MutableList<State>, alphabetItem:String):MutableList<State>{
        var  reachableStates:MutableList<State> = mutableListOf()
        for(state in closureStates){
            for (transition in state._transitions){
                if(transition._symbol.equals(alphabetItem)){
                    reachableStates.add(getState(transition._destiny) as State)
                }
            }
        }
        closureStates.clear()
        evaluatedStates.clear()
        return reachableStates
    }

    open fun convertToNFA():nonDeterministicFiniteAutomaton{
        var nfa:nonDeterministicFiniteAutomaton = nonDeterministicFiniteAutomaton()
        var acceptanceStates = mutableListOf<String>()
        nfa.alphabet = alphabet

        for(thisStates in states) {
           nfa.states.add(State(thisStates._name,thisStates._initialState,thisStates._isAcceptanceState))
            if(thisStates._isAcceptanceState)
                acceptanceStates.add(thisStates._name)
        }

        for(state in states){
            for(symbol in alphabet){
                getClosure(state)
                var reachableStates = getReachableStates(epsilonClosure, symbol)
                for(destinyState in reachableStates){
                    getClosure(destinyState)
                }
                for(currentState in epsilonClosure){
                    var stateToModify = nfa.getState(state._name)

                        if (acceptanceStates.contains(state._name)) {
                            stateToModify!!._isAcceptanceState = true
                        }

                    stateToModify!!.addTransition(symbol, currentState._name)
                }
                evaluatedStates.clear()
                epsilonClosure.clear()
            }
        }
        return nfa
    }
}