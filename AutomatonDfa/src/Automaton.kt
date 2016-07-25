import java.util.ArrayList

/**
 * Created by jenamorado on 7/25/16.
 */
class Automaton {
    open var states: ArrayList<States>? = null
    open var alphabet: ArrayList<Char>? = null
    open var strToEval: String? = null

    val initialState: States?
        get() {
            for (j in states!!.indices) {
                if (states!![j].isAcceptanceState) {
                    return states!![j]
                }
            }
            return null
        }

    fun getState(stateName: String?): States? {
        for (j in states!!.indices) {
            if (states!![j].name == stateName) {
                return states!![j]
            }
        }
        return null
    }

    fun evaluate(strEvString: String): Boolean {
        val eval = strEvString.toCharArray()
        var currentState = initialState
        if (currentState != null) {
            for (i in eval.indices) {
                for (j in currentState!!.transitions!!.indices) {
                    if (currentState.transitions!![j].symbol == eval[i]) {
                        currentState = getState(currentState.transitions!![j].destiny)
                    }
                }
            }
            if (currentState!!.isAcceptanceState) {
                return true
            } else {
                return false
            }
        }
        return false
    }
}
