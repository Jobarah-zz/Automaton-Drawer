package KotlinAlgorithm;

import java.util.*

open class State(name:String, isInitialState:Boolean, isAcceptanceState:Boolean){
    val _name = name
    val _transitions = mutableListOf<Transition>()
    val _initialState = isInitialState
    val _isAcceptanceState = isAcceptanceState
    open fun getName():String{
        return _name
    }
    open fun addTransition(symbol:Char, destiny:String):Unit{
        _transitions.add(Transition(symbol,this.getName(),destiny))
    }
}