package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
class nonDeterministicFiniteAutomaton :Automaton(){
    var evaluatedStates:MutableList<String> = mutableListOf<String>()
    var epsilonClosure = mutableListOf<State>()
    override fun evaluate(strEvString:String):Boolean{
        return convertToDFA().evaluate(strEvString)
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

    open fun getReachableStates(closureStates:MutableList<State>, alphabetItem:Char):MutableList<State>{
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
    open fun convertToDFA(): deterministicFiniteAutomaton{
        var dfa: deterministicFiniteAutomaton = deterministicFiniteAutomaton()
        var dfaStates: MutableList<State> = mutableListOf()
        var initialState = this.getInitialState()
        var statesNames: MutableList<String> = mutableListOf()
        if (initialState != null) {
            dfaStates.add(State(initialState._name, initialState._initialState, initialState._isAcceptanceState))
            statesNames.add(initialState._name)
        }
        for(state in this.states){
            for(symbol in this.alphabet){
                var newStateName = ""
                var newState:State = State()
                for(transition in state._transitions){
                    if(newStateName.isEmpty() && transition._symbol.equals(symbol)){
                        newStateName += transition._destiny
                    }else{
                        if(!newStateName.contains(transition._destiny) && transition._symbol.equals(symbol)){
                            newStateName += "," + transition._destiny
                        }
                    }
                    var destinyState = getState(transition._destiny)
                    newState._name = newStateName
                    if(destinyState!!._isAcceptanceState){
                        newState._isAcceptanceState = true
                    }
                }
                if (!statesNames.contains(newStateName) && !newStateName.equals("")) {
                    newState._name = newStateName
                    dfaStates.add(newState)
                    statesNames.add(newStateName)
                }
            }
        }

        var statesToAdd = dfaStates

        for(newState in statesToAdd){
            generateTransitions(newState)
        }

        dfa.states = statesToAdd
        dfa.alphabet = alphabet

        return dfa
    }

    fun generateTransitions(state:State) {
        var statesNames = state._name.split(",")
        for (stateName in statesNames) {
            var originalState = getState(stateName)
            for (symbol in alphabet) {
                var destiny = ""
                for (transition in originalState!!._transitions) {
                    if(destiny.isEmpty() && transition._symbol.equals(symbol)){
                        destiny += transition._destiny
                    }else{
                        if(!destiny.contains(transition._destiny) && transition._symbol.equals(symbol)){
                            destiny += "," + transition._destiny
                        }
                    }
                }
                if (!destiny.isEmpty()) {
                    state.addTransition(symbol, destiny)
                }
            }
        }
    }
}