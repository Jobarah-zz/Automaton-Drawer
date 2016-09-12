package KotlinAlgorithm

/**
 * Created by Jobarah on 9/11/2016.
 */
class turingMachine: Automaton() {

    var type = "turing"

    override fun evaluate(strEvString:String): Boolean {
        var ribbon = "B"+strEvString+"B"
        var _ribbon = ribbon.toMutableList()

        var currentState = getInitialState()

        var index = 1

        if (currentState != null) {
            while (!currentState!!._isAcceptanceState) {
                if (currentState != null) {
                    for (transition in currentState!!._transitions) {

                        if (getSymbol(transition._symbol).equals(_ribbon[index])) {
                            if (getDirection(transition._symbol).equals("L")) {
                                index -= index

                                if (index >= 0) {
                                    _ribbon[index] = getWriteSybol(transition._symbol)
                                    currentState = getState(transition._destiny)
                                } else {
                                    return false
                                }
                            } else if (getDirection(transition._symbol).equals("R")) {
                                index += index

                                if (index <= _ribbon.size - 1) {
                                    _ribbon[index] = getWriteSybol(transition._symbol)
                                    currentState = getState(transition._destiny)
                                } else {
                                    return false
                                }
                            }
                        }
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