package KotlinAlgorithm

/**
 * Created by Jobarah on 7/31/2016.
 */
open class deterministicFiniteAutomaton() : Automaton(){
    override fun evaluate(strEvString:String):Boolean{
        var eval = stringToCharList(strEvString)
        var currentState:State = getInitialState() as State
        if(currentState!=null){
            for (character in eval) {
                if(alphabet.contains(character.toString())) {
                    for (transition in currentState!!._transitions) {
                        if(transition._symbol.equals(character.toString())){
                            currentState = getState(transition._destiny) as State
                        }
                    }
                }
            }
            if(currentState!!._isAcceptanceState){
                return true
            }else {
                return false
            }
        }
        return false
    }

    fun stringToCharList(string:String):MutableList<Char> {
        var stringList = string.toCharArray()
        var charList:MutableList<Char> = mutableListOf()

        for (char in stringList) {
            charList.add(char)
        }

        return charList
    }
}