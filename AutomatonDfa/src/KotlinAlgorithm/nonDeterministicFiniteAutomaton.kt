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
}