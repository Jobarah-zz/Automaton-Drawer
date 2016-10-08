package KotlinAlgorithm

import java.util.*

/**
 * Created by Jobarah on 7/31/2016.
 */
class nonDeterministicFiniteAutomaton :Automaton(){
    var evaluatedStates:MutableList<String> = mutableListOf<String>()
    var epsilonClosure = mutableListOf<State>()
    override var type = "nfa"

    override fun evaluate(strEvString:String):Boolean{
        val characters = strEvString.toCharArray()
        var currentState = getInitialState()
        var currentStates = mutableListOf<State>()
        if (currentState != null) {
            currentStates.add(currentState)
        }

        for (character in characters) {
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currState._transitions) {
                    if (character.toString() == transition._symbol) {
                        var stateToAdd = getState(transition._destiny)
                        if (stateToAdd != null) {
                            currentStatesFiltered.add(stateToAdd)
                        }
                    }
                }
            }
            currentStates.clear()
            for(newCurrentState in currentStatesFiltered){
                currentStates.add(newCurrentState)
            }
        }
        if(strEvString.isEmpty()){
            val currentStatesFiltered = mutableListOf<State>()
            for (currState in currentStates)
            {
                for (transition in currState._transitions) {
                    if ("e" == transition._symbol) {
                        var addState = getState(transition._destiny)
                        if (addState != null) {
                            currentStatesFiltered.add(addState)
                        }
                    }
                }
            }
            currentStates.clear()
            for(newCurrentState in currentStatesFiltered){
                currentStates.add(newCurrentState)
            }
        }
        for (state in currentStates)
        {
            if (state._isAcceptanceState)
            {
                return true
            }
        }
        return false
    }

    open fun getClosure(state:State){
        if(!evaluatedStates.contains(state._name)){
            epsilonClosure.add( state )
            evaluatedStates.add( state._name )
            for(transition in state._transitions){
                if(transition._symbol.equals('e')){
                    var nextState = getState(transition._destiny) as State
                    getClosure( nextState )
                }
            }
        }
    }

    open fun getReachableStates(closureStates:MutableList<State>, alphabetItem:Char):MutableList<State>{
        var  reachableStates:MutableList<State> = mutableListOf()
        for(state in closureStates){
            for (transition in state._transitions){
                if(transition._symbol.equals(alphabetItem)){
                    reachableStates.add(getState(transition._destiny) as State)
                }
            }
        }
        closureStates.clear()
        evaluatedStates.clear()
        return reachableStates
    }
    open fun convertToDFA(): deterministicFiniteAutomaton{
        var automata2 = deterministicFiniteAutomaton()
        val listaDeAceptados: MutableList<String> = mutableListOf()
        for (t in states.indices){
            if(states[t]._isAcceptanceState){
                listaDeAceptados.add(states[t]._name)
            }
        }
        var newStates: MutableList<State> = mutableListOf()
        val initVertex = getInitialState() as State
        getClosure(initVertex)
        for (state in epsilonClosure) {
            if (state._isAcceptanceState) {
                initVertex._isAcceptanceState = true
                break
            }
        }
        newStates.add(State(initVertex._name,true,initVertex._isAcceptanceState))
        var indexesCounter = 0
        val indexesMap: MutableMap<String,Int> = mutableMapOf()
        indexesMap.put(initVertex._name,indexesCounter)
        indexesCounter++

        automata2.alphabet = getAutomatonAlphabet()
        automata2.states.add(State(initVertex._name,true,initVertex._isAcceptanceState))
        while(newStates.count() > 0){
            val currentState = newStates[0]
            val namesMap: MutableMap<String,String> = mutableMapOf()
            for (p in automata2.alphabet.indices){
                val split = currentState._name.split(",")
                for (w in split.indices){
                    val subCurrentState = getState(split[w]) as State
                    for (o in subCurrentState._transitions.indices){
                        if(subCurrentState._transitions[o]._symbol == automata2.alphabet[p]){
                            if(namesMap.containsKey(automata2.alphabet[p])){
                                val _str = (namesMap.get(automata2.alphabet[p])) as String
                                val brokenDown = _str.split(",")
                                if(!brokenDown.contains(subCurrentState._transitions[o]._destiny))
                                    namesMap.set(automata2.alphabet[p],_str + "," + subCurrentState._transitions[o]._destiny)
                            }else{
                                namesMap.put(automata2.alphabet[p], subCurrentState._transitions[o]._destiny)
                            }
                        }
                    }
                }
            }
            val counter1 = namesMap.count()
            var counter2 = 0
            var names = namesMap.entries
            while(counter1>counter2){
                val thisStr = names.elementAt(counter2).value
                val stateName = existsString(automata2,thisStr)
                if(!existsBoolean(automata2, thisStr)){
                    val isAccept = isAcceptanceVertex(listaDeAceptados,stateName)
                    indexesMap.put(stateName,indexesCounter)
                    indexesCounter++
                    automata2.states.add(State(stateName,false,isAccept))
                    newStates.add(State(stateName,false,isAccept))
                }
                var state:State? = automata2.getState(currentState._name)
                state!!._transitions.add(Transition(names.elementAt(counter2).key.toString(),currentState._name,stateName))
                counter2++
            }
            newStates.removeAt(0)
        }
        return automata2
    }

    open fun existsString(miAFD:deterministicFiniteAutomaton, miStr:String):String{
        for (i in miAFD.states.indices){
            val strCompare = miAFD.states[i]._name
            val miArr = strCompare.split(",")
            val miArr2 = miStr.split(",")
            if(miArr.count() == miArr2.count()){
                val set1 = HashSet<String>()
                set1.addAll(miArr)
                val set2 = HashSet<String>()
                set2.addAll(miArr2)
                if(set1.equals(set2)){
                    return strCompare
                }
            }
        }
        return miStr
    }

    fun generateTransitions(state:State) {
        var statesNames = state._name.split(",")
        for (stateName in statesNames) {
            var originalState = getState(stateName)
            for (symbol in alphabet) {
                var destiny = ""
                for (transition in originalState!!._transitions) {
                    if(destiny.isEmpty() && transition._symbol.equals(symbol)){
                        destiny += transition._destiny
                    }else{
                        if(!destiny.contains(transition._destiny) && transition._symbol.equals(symbol)){
                            destiny += "," + transition._destiny
                        }
                    }
                }
                if (!destiny.isEmpty()) {
                    state.addTransition(symbol, destiny)
                }
            }
        }
    }

    open fun isAcceptanceVertex(acceptanceStates:kotlin.collections.MutableList<String>, str:String): Boolean{
        val toCompare = str.split(",")
        for (i in acceptanceStates.indices){
            if(toCompare.contains(acceptanceStates[i])){
                return true
            }
        }
        return false
    }

    open fun existsBoolean(nfa:deterministicFiniteAutomaton, miStr:String):Boolean{
        for (i in nfa.states.indices){
            val strCompare = nfa.states[i]._name
            val firstArray = strCompare.split(",")
            val secondArray = miStr.split(",")
            if(firstArray.count() == secondArray.count()){
                val set1 = HashSet<String>()
                set1.addAll(firstArray)
                val set2 = HashSet<String>()
                set2.addAll(secondArray)
                if(set1.equals(set2)){
                    return true
                }
            }
        }
        return false
    }

}