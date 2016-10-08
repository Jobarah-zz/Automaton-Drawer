package KotlinAlgorithm
import java.util.ArrayList

/**
 * Created by Jobarah on 9/17/2016.
 */
class RegexState :State(){
    var statesPointingToMe:ArrayList<String> = ArrayList()

    fun hasTransitionToItself():Boolean{
        return _transitions.filter { it._destiny.equals(_name) }.size > 0
    }
}