package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
open class deterministicFiniteAutomaton() : Automaton(){
    override var type = "dfa"

    override fun evaluate(strEvString:String):Boolean{
        var eval = stringToCharList(strEvString)
        var consumedCount = stringToCharList(strEvString)
        var currentState:State = getInitialState() as State
        if(currentState!=null){
            for (character in eval) {
                if(alphabet.contains(character.toString())) {
                    for (transition in currentState!!._transitions) {
                        if(transition._symbol.equals(character.toString())){
                            var nextState = getState(transition._destiny)
                            if (nextState != null) {
                                currentState = nextState
                                consumedCount.removeAt(0)
                            }
                        }
                    }
                }
            }
            if(currentState!!._isAcceptanceState && consumedCount.size == 0){
                return true
            }else {
                return false
            }
        }
        return false
    }

    fun minimize(): deterministicFiniteAutomaton {
        var returnDfa = deterministicFiniteAutomaton()
        var states:MutableList<State> = mutableListOf()
        var acceptanceStates:MutableList<State> = mutableListOf()

        for (state in this.states) {
            if (state._isAcceptanceState){
                acceptanceStates.add(state)
            }
            else{
                states.add(state)
            }
        }

        var equivalentStates:MutableList<MutableList<State>> = mutableListOf()
        if (states.isNotEmpty()) {
            equivalentStates.add(states)
        }
        if (acceptanceStates.isNotEmpty()) {
            equivalentStates.add(acceptanceStates)
        }

        var symbols:MutableList<String> = mutableListOf()

        for (state in states) {
            for (transition in  state._transitions) {
                if(!symbols.contains(transition._symbol)) {
                    symbols.add(transition._symbol)
                }
            }
        }
        var hasNotEquivalentStates = true
        while (hasNotEquivalentStates) {
            var stateReferenceList:MutableList<MutableList<State>> = mutableListOf()
            var notEquivalentStates:MutableList<State> = mutableListOf()

            for (equivalentState in equivalentStates) {
                stateReferenceList.add(mutableListOf<State>())
                stateReferenceList.last().add(equivalentState.first())
                for (stateItem in equivalentState) {
                    if (stateItem._name != equivalentState[0]._name) {
                        var equivalent = true
                        for (symbol in symbols) {
                            if (getTransitions(stateItem, symbol).isNotEmpty() && getTransitions(equivalentState.first(), symbol).isNotEmpty()) {
                                val state1 = getTransitions(stateItem, symbol).first()
                                val state2 = getTransitions(equivalentState.first(), symbol).first()
                                if (state1 != null && state2 != null) {
                                    if (!statesAreEquivalent(getState(state1._destiny) as State, getState(state2._destiny) as State, equivalentStates)) {
                                        equivalent = false
                                    }
                                }
                            }
                        }
                        if (equivalent) {
                            stateReferenceList[stateReferenceList.size - 1].add(stateItem)
                        } else {
                            notEquivalentStates.add(stateItem)
                        }
                    }
                }
            }
            equivalentStates = stateReferenceList
            if (notEquivalentStates.isNotEmpty()) {
                equivalentStates.add(notEquivalentStates)
            } else {
                hasNotEquivalentStates = false
            }

        }

        for (equivalentState in equivalentStates) {
            var stateName:String = ""
            var acceptedState:Boolean = false
            var initialState:Boolean = false
            for (state in equivalentState) {
                stateName += state._name
                if (state._isAcceptanceState){
                    acceptedState = true
                }
                if (state._initialState){
                    initialState = true
                }
            }
            var newState:State = State(stateName, initialState, acceptedState)
            returnDfa.states.add(newState)
        }

        for (index in equivalentStates.indices) {
            for (state in this.states) {
                if (state._name == equivalentStates[index].first()._name) {
                    for (transition in state._transitions) {
                        for (destinyIndex in equivalentStates.indices) {
                            if (equivalentStates[destinyIndex].contains(getState(transition._destiny))) {
                                returnDfa.addTransition(returnDfa.states[index]._name, returnDfa.states[destinyIndex]._name, transition._symbol)
                            }
                        }
                    }
                }
            }
        }
        return returnDfa
    }

    fun statesAreEquivalent(stateA:State, stateB:State, equivalentStates:MutableList<MutableList<State>>):Boolean {
        if (stateA._name == stateB._name) {
            return true
        }
        for (equivalentState in equivalentStates) {
            var existsEquivalence = false
            for (state in equivalentState) {
                if (state._name == stateA._name || state._name == stateB._name) {
                    if (existsEquivalence) {
                        return true
                    }
                    else {
                        existsEquivalence = true
                    }
                }
            }
            if (existsEquivalence) {
                return false
            }
        }
        return true
    }

    fun getTransitions(state:State, symbol:String) : MutableList<Transition> {
        var transitions:MutableList<Transition> = mutableListOf()
        for(transition in state._transitions){
            if(transition._symbol.equals(symbol)){
                transitions.add(transition)
            }
        }
        return transitions
    }
}