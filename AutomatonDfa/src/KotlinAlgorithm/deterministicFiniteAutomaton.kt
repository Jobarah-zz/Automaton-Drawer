package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
open class deterministicFiniteAutomaton() : Automaton(){
    override fun evaluate(strEvString:String):Boolean{
        var eval = strEvString.toCharArray()
        var currentState: State? = getInitialState()
        if(currentState!=null){
            for (charater in eval) {
                if(alphabet.contains(charater)){
                    for (transition in currentState!!._transitions) {
                        if(transition._symbol==charater){
                            currentState = getState(transition._destiny)
                        }
                    }
                }
            }
            if(currentState!!._isAcceptanceState){
                return true
            }else {
                return false
            }
        }
        return false
    }
}