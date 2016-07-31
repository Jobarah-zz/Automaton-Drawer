
package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */

open abstract class Automaton{
    val states = mutableListOf<State>()
    val alphabet = mutableListOf<Char>()

    open fun addState(name:String,isInitialState:Boolean,isAcceptanceState:Boolean):Unit{
        states.add(State(name,isInitialState,isAcceptanceState))
    }

    open fun getInitialState(): State?{
        for (i in states.indices) {
            if (states[i]._initialState) {
                return states[i]
            }
        }
        return null
    }

    open fun getState(stateName:String): State?{
        for (i in states.indices) {
            if(states[i]._name.equals(stateName)){
                return states[i]
            }
        }
        return null
    }

    open abstract fun evaluate(strEvString:String):Boolean
}