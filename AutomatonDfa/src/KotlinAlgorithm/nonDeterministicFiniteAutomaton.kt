package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
class nonDeterministicFiniteAutomaton :Automaton(){
    override fun evaluate(strEvString:String):Boolean{
        var eval = strEvString.toCharArray()
        var currentStates = mutableListOf<State>()
        var currentState: State? = getInitialState()
        if (currentState != null)
            currentStates.add(currentState)

        for (character in eval) {
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currentState!!._transitions) {
                    if (character == transition._symbol)
                        currentStatesFiltered.add(getState(transition._destiny) as State)
                }
            }
            currentStates = currentStatesFiltered
        }

        for (state in currentStates)
        {
            if (state._isAcceptanceState)
            {
                return true
            }
        }
        return false
    }

    open fun getClosure(state:State?):MutableList<State>{
        return mutableListOf<State>()
    }

    open fun convertToDFA(): deterministicFiniteAutomaton{
        var dfa: deterministicFiniteAutomaton = deterministicFiniteAutomaton()
        var dfaStates: MutableList<State> = mutableListOf()
        for(state in this.states){
            for(symbol in alphabet){
                var newStateName = ""
                var newState:State = State()
                for(transition in state._transitions){
                    if(newStateName.isEmpty()){
                        newStateName += transition._destiny
                    }else{
                        if(!newStateName.contains(transition._destiny)){
                            newStateName += "," + transition._destiny
                        }
                    }
                    var destinyState = getState(transition._destiny)
                    newState._name = newStateName
                    if(destinyState!!._isAcceptanceState){
                        newState._isAcceptanceState = true
                    }
                }
                    if(!newState._name.isEmpty()){
                        if(dfaStates.isEmpty()){
                            newState._name = newStateName
                            dfaStates.add(newState)
                        }else{
                            for(state1 in dfaStates){
                                if(!state1._name.equals(newState._name)){
                                    newState._name = newStateName
                                    dfaStates.add(newState)
                                }
                            }
                        }
                    }
            }
        }

        var statesToAdd:MutableList<State> = dfaStates.distinct() as MutableList<State>

        for(newState in statesToAdd){
            dfa.addState(newState._name, newState._initialState, newState._isAcceptanceState)
        }

        return dfa
    }


}