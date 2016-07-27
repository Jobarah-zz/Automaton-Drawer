
package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */

open class Automaton{
    val states = mutableListOf<States>()
    val alphabet = mutableListOf<Char>()

    open fun getInitialState():States?{
        for (i in states.indices) {
            if (states[i]._initialState) {
                return states[i]
            }
        }
        return null
    }

    open fun getState(stateName:String):States?{
        for (i in states.indices) {
            if(states[i]._name.equals(stateName)){
                return states[i]
            }
        }
        return null
    }

    open fun evaluate(strEvString:String):Boolean{
        var eval = strEvString.toCharArray()
        var currentState: States? = getInitialState()
        if(currentState!=null){
                for (charater in eval) {
                    if(alphabet.contains(charater)){
                        for (transition in currentState!!._transition) {
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