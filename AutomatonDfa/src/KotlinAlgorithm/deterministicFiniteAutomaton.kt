package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
open class deterministicFiniteAutomaton() : Automaton(){
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
}