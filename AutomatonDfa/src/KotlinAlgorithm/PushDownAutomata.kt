package KotlinAlgorithm

import jdk.nashorn.internal.ir.Symbol
import kotlin.system.measureTimeMillis

/**
 * Created by Jobarah on 9/4/2016.
 */
class PushDownAutomata: Automaton() {

    override var type = "pda"
    val EPSILON = "E"

    override fun evaluate(strEvString:String):Boolean {
        var stack = Stack()
        stack.push("Z0")
        var initialState = getInitialState()
        if (initialState != null) {
            return traversePda(strEvString.toMutableList(), initialState, initialState._transitions[0], stack, strEvString.length*2, 0)
        }
        return false
    }

    fun traversePda(str: MutableList<Char>, state: State, initialTransition: Transition, stack: Stack, depthLimit: Int, currentDepth: Int) :Boolean {

        var movementPossible = false
        var destinyState = ""
        var transitionSymbol = ""
        var popSymbol = ""
        var pushSymbol = ""
        var trans:Transition = initialTransition

        if (currentDepth == depthLimit) {
            if (trans == initialTransition) {

            }
        }


        for (transition in state._transitions) {
            if (getTransitionSymbol(transition._symbol).equals(str[0]) || getTransitionSymbol(transition._symbol).equals(EPSILON)) {
                if (currentDepth>0) {
                    if (transition._symbol == trans._symbol) {
                        continue
                    }
                }
                movementPossible = true
                destinyState = transition._destiny
                transitionSymbol = getTransitionSymbol(transition._symbol)
                popSymbol = getPopSymbol(transition._symbol)
                pushSymbol = getTransitionPushValues(transition._symbol)
                trans = transition
                break
            }
        }


        if (str.isEmpty() && !stack.isEmpty() || (!str.isEmpty() && stack.isEmpty())) {
            return false
        }

        if (str.isEmpty() && stack.isEmpty() && state._isAcceptanceState) {
            return true
        }

        if (movementPossible) {
            var nextState = getState(destinyState)

            if (!transitionSymbol.equals(EPSILON)) {
                str.removeAt(0)
            }

            if (!popSymbol.equals(EPSILON)) {
                if (popSymbol.equals(stack.at(0))) {
                    stack.pop()
                }
            }

            if (!pushSymbol.equals(EPSILON) && !pushSymbol.equals("Z0")) {
                if (pushSymbol.contains("Z0"))
                    pushSymbol = pushSymbol.replace("Z0", "")
                for (item in pushSymbol) {
                    stack.push(item.toString())
                }
            }

            if (nextState != null) {
                return traversePda(str, nextState, trans, stack, depthLimit, currentDepth+1)
            }
        }

        return false
    }

    fun isAcceptancePDA(): Boolean {
        for (state in states) {
            if (state._isAcceptanceState) {
                return true
            }
        }
        return false
    }

    fun getTransitionSymbol(str:String): String {
        var pushVal = str.subSequence(str.lastIndexOf("(",str.length, false)+1, str.lastIndexOf(",",str.length, false))
        return pushVal.toString()
    }
    fun getPopSymbol(str: String): String {
        var pushVal = str.subSequence(str.lastIndexOf(",",str.length, false)+1, str.lastIndexOf("/",str.length, false))
        return pushVal.toString()
    }
    fun getTransitionPushValues(str:String):  String {
        var pushVal = str.subSequence(str.lastIndexOf("/",str.length, false)+1, str.lastIndexOf(")",str.length, false))
        return pushVal.toString()
    }

    fun isTransitionExistent(state: State, symbol: String) :Boolean {
        for (transition in state._transitions) {
            if (getTransitionSymbol(transition._symbol).equals(symbol)) {
                return true
            }
        }
        return false
    }

}