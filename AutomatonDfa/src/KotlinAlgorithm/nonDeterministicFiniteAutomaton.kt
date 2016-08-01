package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
class nonDeterministicFiniteAutomaton :Automaton(){
    var acceptanStates= mutableListOf<State>()

    override open fun addState(name:String,isInitialState:Boolean,isAcceptanceState:Boolean):Unit{
        states.add(State(name,isInitialState,isAcceptanceState))
        if(isAcceptanceState==true){
            acceptanStates.add(State(name,isInitialState,isAcceptanceState))
        }
    }
    fun getStateIndex(name: String): Int{
        var pos = 0
        for(state: State in states)
        {
            if(state._name == name)
                break
            pos++
        }

        return  pos
    }
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
                        currentStatesFiltered.add(states[getStateIndex(transition._destiny)])
                }
            }
            currentStates = currentStatesFiltered
        }

        for (state in currentStates)
        {
            if (acceptanStates.contains(state))
            {
                return true
            }
        }
        return false
    }
}