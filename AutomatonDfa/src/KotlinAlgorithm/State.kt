package KotlinAlgorithm;

/**
 * Created by Jobarah on 7/25/2016.
 */

import java.io.Serializable
import java.util.*

open class State: Serializable {
    var _name = ""
    var _transitions = mutableListOf<Transition>()
    var _initialState = false
    var _isAcceptanceState = false

    constructor(){
    }

    constructor(name:String, isInitialState:Boolean, isAcceptanceState:Boolean){
        _name = name
        _initialState = isInitialState
        _isAcceptanceState = isAcceptanceState
    }

    open fun getName():String{
        return _name
    }
    open fun addTransition(symbol: String, destiny:String):Unit{
        _transitions.add(Transition(symbol,this.getName(),destiny))
    }

    fun getDestinyStateName(symbol:String):String? {
        for(transition in _transitions){
            if(transition._symbol.equals(symbol)){
                return transition._destiny
            }
        }
        return null
    }
}