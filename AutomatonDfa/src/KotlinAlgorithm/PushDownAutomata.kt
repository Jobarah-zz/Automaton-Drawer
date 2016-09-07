package KotlinAlgorithm

/**
 * Created by Jobarah on 9/4/2016.
 */
class PushDownAutomata: Automaton() {

    var stack = Stack()

    override fun evaluate(strEvString:String):Boolean {
        var strToEval = stringToCharList(strEvString)
        var currentState = getInitialState()
        var counter = 0
        var subStr = stringToCharList(strEvString)
        if (currentState != null) {
            for (character in strToEval) {
                for (transition in currentState!!._transitions) {
                    var _symbol = getTransitionSymbol(transition._symbol)
                    var pushValues = getTransitionPushValues(transition._symbol)
                    if (stack.stack.isEmpty() && getPopSymbol(transition._symbol).equals("Z0")) {

                        if (pushValues.contains("Z0")) {
                            pushValues = pushValues.removeSuffix("Z0")
                            pushValues = pushValues.removePrefix("Z0")
                        }
                        for (item in pushValues) {
                            stack.push(item.toString())
                        }
                        var destination = getState(currentState.getDestinyStateName(transition._symbol).toString())
                        if (destination != null) {
                            currentState = destination
                        }

                        ++counter
                        subStr.removeAt(subStr.size-1)

                        break
                    } else if (_symbol.equals(character) && !stack.stack.isEmpty() && !pushValues.contains("E") && !getPopSymbol(transition._symbol).equals("Z0") && stack.stack[stack.stack.size-1].equals(getPopSymbol(transition._symbol))) {
                        var pushValues = getTransitionPushValues(transition._symbol)
                        stack.pop()
                        for (item in pushValues) {
                            stack.push(item.toString())
                        }
                        var destination = getState(currentState.getDestinyStateName(transition._symbol).toString())
                        if (destination != null) {
                            currentState = destination
                        }
                        ++counter
                        subStr.removeAt(subStr.size-1)
                        break
                    } else if (pushValues.contains("E") && !stack.stack.isEmpty() && _symbol.equals(character) && stack.stack[stack.stack.size-1].equals(getPopSymbol(transition._symbol))) {
                        stack.pop()
                        var destination = getState(currentState.getDestinyStateName(transition._symbol).toString())
                        if (destination != null) {
                            currentState = destination
                        }
                        ++counter
                        subStr.removeAt(subStr.size-1)
                        break
                    } else if (_symbol.equals("E") && alphabet.contains("E")) {
//                        if (stack.stack.isEmpty() && getPopSymbol(transition._symbol).equals("Z0")) {
//
//                            if (pushValues.contains("Z0")) {
//                                pushValues = pushValues.removeSuffix("Z0")
//                                pushValues = pushValues.removePrefix("Z0")
//                            }
//                            for (item in pushValues) {
//                                stack.push(item.toString())
//                            }
//                            var destination = getState(currentState.getDestinyStateName(transition._symbol).toString())
//                            if (destination != null) {
//                                currentState = destination
//                            }
//                            ++counter
//                            break
//                        }
                    }
                }
            }
        }

        println("counter value is: "+counter+" subStr: "+subStr)
        if (isAcceptancePDA() && currentState!!._isAcceptanceState) {
            return true
        } else if (!isAcceptancePDA() && stack.stack.isEmpty() && counter == strToEval.size) {
            return true
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
        var removedParenthesisStr = str.removeSurrounding("(",")")
        var pushVal = removedParenthesisStr.subSequence(removedParenthesisStr.lastIndexOf("/",removedParenthesisStr.length, false)+1, removedParenthesisStr.length)
        return pushVal.toString()
    }
}