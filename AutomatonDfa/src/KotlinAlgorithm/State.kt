package KotlinAlgorithm;

import java.util.*

open class State{
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
    open fun addTransition(symbol:Char, destiny:String):Unit{
        _transitions.add(Transition(symbol,this.getName(),destiny))
    }
}