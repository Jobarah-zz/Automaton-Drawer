package KotlinAlgorithm

import java.util.*

/**
 * Created by Jobarah on 8/6/2016.
 */
 class nonDeterministicAutomatonEpsilon: Automaton() {
    var evaluatedStates:MutableList<String> = mutableListOf<String>()
    var epsilonClosure = mutableListOf<State>()

    var clausuras: MutableMap<String,MutableList<String>> = mutableMapOf()
    var combinaciones = mutableListOf<String>()
    override var type = "ε-nfa"

    override fun evaluate(strEvString:String):Boolean{
        return transformToDfa().evaluate(strEvString)
    }
    open fun getClosure(state:State){
        if(!evaluatedStates.contains(state._name)){
            epsilonClosure.add( state )
            evaluatedStates.add( state._name )
            for(transition in state._transitions){
                if(transition._symbol.equals("e")){
                    var nextState = getState(transition._destiny) as State
                    getClosure( nextState )
                }
            }
        }
    }

    open fun getReachableStates(closureStates:MutableList<State>, alphabetItem:String):MutableList<State>{
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

    open fun convertToNFA():nonDeterministicFiniteAutomaton {
        var nfa:nonDeterministicFiniteAutomaton = nonDeterministicFiniteAutomaton()
        var acceptanceStates = mutableListOf<String>()
        nfa.alphabet = alphabet

            for(thisStates in states) {
           nfa.states.add(State(thisStates._name,thisStates._initialState,thisStates._isAcceptanceState))
            if(thisStates._isAcceptanceState)
                acceptanceStates.add(thisStates._name)
        }

        for(state in states){
            for(symbol in alphabet){
                getClosure(state)
                var reachableStates = getReachableStates(epsilonClosure, symbol)
                for(destinyState in reachableStates){
                    getClosure(destinyState)
                }
                for(currentState in epsilonClosure){
                    var stateToModify = nfa.getState(state._name)

                        if (acceptanceStates.contains(state._name)) {
                            stateToModify!!._isAcceptanceState = true
                        }

                    stateToModify!!.addTransition(symbol, currentState._name)
                }
                evaluatedStates.clear()
                epsilonClosure.clear()
            }
        }
        return nfa
    }

    open fun generarClausuras(){
        clausuras.clear()
        for(vertex in states){
            var lista = mutableListOf<String>()
            lista.add(vertex.getName())
            clausuras.put(vertex.getName(),lista)
        }
        for(elem in clausuras){
            var vertexActual = getState(elem.key) as State
            for(transicion in vertexActual._transitions){
                if(transicion._symbol == 'e'.toString() && !(elem.value.contains(transicion._destiny))){
                    elem.value.add(transicion._destiny)
                }
            }
        }
        for(elem in clausuras){
            var listaPop = mutableListOf<String>()
            for(str in elem.value){
                listaPop.add(str)
            }
            listaPop.remove(elem.key)
            while(listaPop.isNotEmpty()){
                var vertexActual = getState(listaPop.first()) as State
                for(transicion in vertexActual._transitions){
                    if(transicion._symbol == 'e'.toString() && !(elem.value.contains(transicion._destiny))){
                        listaPop.add(transicion._destiny)
                        elem.value.add(transicion._destiny)
                    }
                }
                listaPop.removeAt(0)
            }
        }
    }

    open fun transformToDfa():deterministicFiniteAutomaton {
        var nuevoAutomata = deterministicFiniteAutomaton()
        generarClausuras()
        nuevoAutomata.alphabet = getAutomatonAlphabet()
        var clausuraVertexInicial = getClausura((getInitialState() as State))
        nuevoAutomata.addState(clausuraVertexInicial, true, contieneAceptado(clausuraVertexInicial))
        var listaPop = mutableListOf<String>()
        listaPop.add(clausuraVertexInicial)
        while (listaPop.count() > 0) {
            var vertexActual = nuevoAutomata.getState(listaPop.first()) as State
            for (valor in nuevoAutomata.alphabet) {
                var nuevaSalida = getSalida(valor, vertexActual)
                if (nuevaSalida != "") {
                    var destinoName = yaExisteString(nuevoAutomata, nuevaSalida)
                    vertexActual.addTransition(valor, destinoName)
                    var nuevoVertex = State(destinoName,false,contieneAceptado(destinoName))
                    if(!containsVertex(nuevoAutomata,nuevoVertex)){
                        nuevoAutomata.states.add(nuevoVertex)
                        listaPop.add(destinoName)
                    }
                }
            }
            listaPop.removeAt(0)
        }
        var numeroEstado = 0
        for(vertex in nuevoAutomata.states){
            nuevoAutomata.changeName(vertex.getName(),"q"+numeroEstado)
            numeroEstado++
        }
        return nuevoAutomata
    }

    open fun contieneAceptado(estado:String):Boolean{
        var separado = estado.split(",")
        for(elem in separado){
            var vertexActual = getState(elem) as State
            if(vertexActual._isAcceptanceState){
                return true
            }
        }
        return false
    }

    open fun getSalida(valor:String, estado:State):String{
        var lista = mutableListOf<String>()
        var strSplit = estado.getName().split(",")
        for(elem in strSplit){
            var vertexActual = getState(elem) as State
            for(transicion in vertexActual._transitions){
                if(transicion._symbol == valor.toString()){
                    var clausuraActual = getClausura(getState(transicion._destiny) as State)
                    var splitClausura = clausuraActual.split(",")
                    for(elem2 in splitClausura){
                        if(!(lista.contains(elem2))){
                            lista.add(elem2)
                            lista.add(",")
                        }
                    }
                }
            }
        }
        var returnString = ""
        if(lista.isNotEmpty()){
            lista.removeAt(lista.count()-1)
        }
        for(elem in lista){
            returnString += elem.toString()
        }
        return returnString
    }

    open fun getClausura(vertex:State):String{
        var clausura = ""
        var lista = mutableListOf<String>()
        for(elem in clausuras){
            if(elem.key == vertex.getName()){
                for(valor in elem.value){
                    lista.add(valor)
                    lista.add(",")
                }
            }
        }
        lista.removeAt(lista.count()-1)
        for(elem in lista){
            clausura += elem.toString()
        }
        return clausura
    }
    fun  setInitialState(_name: String) {
        for (state in states) {
            if (state._name.equals(_name)) {
                state._initialState = true
            }
        }
    }

    fun  setFinalState(_name: String) {
        for (state in states) {
            if (state._name.equals(_name)) {
                state._isAcceptanceState = true
            }
        }
    }

    fun  getAcceptanceState(): State? {

        for (state in states) {
            if (state._isAcceptanceState) {
                return state
            }
        }
        return null
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

    open fun containsVertex(automata:deterministicFiniteAutomaton,vertex:State):Boolean{
        for(vertice in automata.states){
            if(vertice.getName() == vertex.getName()){
                return true
            }
        }
        return false
    }
}