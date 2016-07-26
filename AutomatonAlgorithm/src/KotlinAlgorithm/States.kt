import java.util.*

open class States(name:String,isInitialState:Boolean,isAcceptanceState:Boolean){
    val _name = name
    val _transition = mutableListOf<Transition>()
    val _initialState = isInitialState
    val _isAcceptanceState = isAcceptanceState
}