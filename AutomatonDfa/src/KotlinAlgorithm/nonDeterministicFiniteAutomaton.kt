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
        val listaDeAceptados: MutableList<String> = mutableListOf() //Para saber cuando un nuevo estado es de aceptacion
        for (t in states.indices){
            if(states[t]._isAcceptanceState){
                listaDeAceptados.add(states[t]._name)
            }
        }
        var nuevosVertex: MutableList<State> = mutableListOf() //Para ir haciendo pop
        val initVertex = getInitialState() as State //Tomar estado inicial
        nuevosVertex.add(State(initVertex._name,true,initVertex._isAcceptanceState)) //Agregar estado inicial
        var contadorIndices = 0
        val mapaIndices: MutableMap<String,Int> = mutableMapOf()
        mapaIndices.put(initVertex._name,contadorIndices)
        contadorIndices++

        automata2.alphabet = getAutomatonAlphabet() //Extraer el alfabeto del automata AFN
        automata2.states.add(State(initVertex._name,true,initVertex._isAcceptanceState))
        while(nuevosVertex.count() > 0){
            val vertexActual = nuevosVertex[0]
            val mapaNombres: MutableMap<String,String> = mutableMapOf() //Valores con sus nodos
            for (p in automata2.alphabet.indices){
                val separados = vertexActual._name.split(",")
                for (w in separados.indices){
                    val subVertexActual = getState(separados[w]) as State
                    for (o in subVertexActual._transitions.indices){
                        if(subVertexActual._transitions[o]._symbol == automata2.alphabet[p]){
                            if(mapaNombres.containsKey(automata2.alphabet[p])){
                                val miStr = (mapaNombres.get(automata2.alphabet[p])) as String
                                val cortado = miStr.split(",")
                                if(!cortado.contains(subVertexActual._transitions[o]._destiny))
                                    mapaNombres.set(automata2.alphabet[p],miStr + "," + subVertexActual._transitions[o]._destiny)
                            }else{
                                mapaNombres.put(automata2.alphabet[p], subVertexActual._transitions[o]._destiny)
                            }
                        }
                    }
                }
            }
            val contador1 = mapaNombres.count()
            var contador2 = 0
            var misNombres = mapaNombres.entries
            while(contador1>contador2){
                val miStr = misNombres.elementAt(contador2).value
                val nombreV = yaExisteString(automata2,miStr)
                if(!yaExisteBoolean(automata2, miStr)){
                    val isAccept = esVertexAceptable(listaDeAceptados,nombreV)
                    mapaIndices.put(nombreV,contadorIndices)
                    contadorIndices++
                    automata2.states.add(State(nombreV,false,isAccept))
                    nuevosVertex.add(State(nombreV,false,isAccept))
                }
                var state:State? = automata2.getState(vertexActual._name)
                state!!._transitions.add(Transition(misNombres.elementAt(contador2).key.toString(),vertexActual._name,nombreV))
                contador2++
            }
            nuevosVertex.removeAt(0)
        }
        return automata2
    }

    open fun yaExisteString(miAFD:deterministicFiniteAutomaton, miStr:String):String{
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

    open fun esVertexAceptable(misVertexAceptables:kotlin.collections.MutableList<String>, str:String): Boolean{
        val loQueComparo = str.split(",")
        for (i in misVertexAceptables.indices){
            if(loQueComparo.contains(misVertexAceptables[i])){
                return true
            }
        }
        return false
    }

    open fun yaExisteBoolean(miAFD:deterministicFiniteAutomaton, miStr:String):Boolean{
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
                    return true
                }
            }
        }
        return false
    }
}