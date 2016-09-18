package KotlinAlgorithm

/**
 * Created by Jobarah on 9/11/2016.
 */
class turingMachine: Automaton() {

    override var type = "Turing Machine"

    override fun evaluate(strEvString:String): Boolean {
        var ribbon = "B"+strEvString+"B"
        var _ribbon = ribbon.toMutableList()

        var currentState = getInitialState()

        var index = 1

        if (currentState != null) {
            while (!currentState!!._isAcceptanceState) {
                if (currentState != null) {
                    var transitionsSize = currentState._transitions.size
                    var transitionCount = 0
                    for (transition in currentState!!._transitions) {

                        if (getSymbol(transition._symbol).equals(_ribbon[index])) {
                            if (getDirection(transition._symbol).equals("L")) {
                                index -= 1
                                if (index >= 0) {
                                    _ribbon[index+1] = getWriteSybol(transition._symbol)
                                    currentState = getState(transition._destiny)
                                    break
                                } else {
                                    return false
                                }
                            } else if (getDirection(transition._symbol).equals("R")) {
                                index += 1

                                if (index <= _ribbon.size - 1) {
                                    _ribbon[index-1] = getWriteSybol(transition._symbol)
                                    currentState = getState(transition._destiny)
                                    break
                                } else {
                                    return false
                                }
                            }
                        }
                        transitionCount++
                    }
                    if (transitionCount == transitionsSize) {
                        return false
                    }
                }
            }
        }
        return true
    }

    open fun getDirection(symbol: String): String{
        var direction = symbol.subSequence(symbol.lastIndexOf("/",symbol.length, false)+1, symbol.lastIndexOf(")",symbol.length, false))
        return direction.toString()
    }

    open fun getWriteSybol(symbol: String): Char {
        var writeSymbol = symbol.subSequence(symbol.lastIndexOf(",",symbol.length, false)+1, symbol.lastIndexOf("/",symbol.length, false))
        return writeSymbol.toString()[0]
    }

    open fun getSymbol(symbol: String): Char {
        var symbol = symbol.subSequence(symbol.lastIndexOf("(",symbol.length, false)+1, symbol.lastIndexOf(",",symbol.length, false))
        return symbol.toString()[0]
    }
}