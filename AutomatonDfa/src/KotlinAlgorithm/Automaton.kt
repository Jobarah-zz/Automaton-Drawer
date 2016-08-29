
package KotlinAlgorithm;
/**
 * Created by Jobarah on 7/25/2016.
 */

open abstract class Automaton{
    var states = mutableListOf<State>()
    var alphabet = mutableListOf<Char>()
    var automatonName = ""

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
        for (state in states) {
            if(state._name.equals(stateName)){
                return state
            }
        }
        return null
    }

    open abstract fun evaluate(strEvString:String):Boolean

    open fun getDestinyState(stateName: String, symbol:Char):State? {
        val estadoOrigen = getState(stateName) as State
        for(transition in estadoOrigen._transitions){
            if(transition._symbol == symbol){
                return getState(transition._destiny)
            }
        }
        return null
    }
    open fun clearAutomaton(){
        states.clear()
        alphabet.clear()
    }

    open fun getAutomatonAlphabet(): MutableList<Char>{
        return alphabet
    }
    open fun getAutomatonStates(): MutableList<State> {
        return this.states
    }

}