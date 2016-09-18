package KotlinAlgorithm

/**
 * Created by Jobarah on 9/16/2016.
 */
class RegEx :Automaton(){

    var contadorEstados = 0
    var expresionRegular = ""
    override var type: String = "RegEx"

    override fun evaluate(strEvString:String):Boolean {
        return true
    }

    open fun transformarNFAe():nonDeterministicAutomatonEpsilon{
        var automata = nonDeterministicAutomatonEpsilon()
        automata.alphabet = generateAlfabet()
        setForNewAFNe()
        var vertex1 = getState("Inicial")
        var vertex2 = getState("Final")
        var lista = generarNivelActual(generarER())
        if (vertex1 != null && vertex2 != null) {
            unirVertexes(vertex1,vertex2,lista)
        }
        cleanEdges()
        fillAutomaton(automata)
        return automata
    }

    open fun fillAutomaton(automata:nonDeterministicAutomatonEpsilon){
        for(vertex in states){
            var estado = State(vertex._name,vertex._initialState,vertex._isAcceptanceState)
            automata.states.add(estado)
            for(transicion in vertex._transitions){
                estado.addTransition(transicion._symbol,transicion._destiny)
            }
        }
    }

    open fun setForNewAFNe(){
        states.clear()
        contadorEstados = 0
        addState("Inicial",true,false)
        addState("Final",false,true)
        expresionRegular = generarER()
    }

    open fun tests(){
        contadorEstados = 0
        var vertex1 = State("Inicial",true,false)
        var vertex2 = State("Final",false,true)
        states.add(vertex1)
        states.add(vertex2)
        var lista = generarNivelActual(generarER())
        unirVertexes(vertex1,vertex2,lista)
        printAutomata()
    }

    open fun checkStarsTest(){
        var lista = mutableListOf<Transition>()
        for(vertex in states){
            for(transicion in vertex._transitions){
                if(transicion._symbol.last() == '*' && verifySingleElement(transicion._symbol)){
                    lista.add(transicion)
                }
            }
        }
        for(elem in lista){
            var vertex1 = getState(elem._origin)
            vertex1!!._transitions.remove(elem)
        }
        for(elem in lista){
            var vertex1 = getState(elem._origin)
            var vertex2 = getState(elem._destiny)
            if (vertex1 != null && vertex2 != null) {
                resolverEstrellas(vertex1,vertex2,elem._symbol)
            }
        }
    }

    open fun verifySingleElement(miStr:String):Boolean{
        var miLista = miStr.toCharArray().toMutableList()
        var miStr2 = ""
        if(miStr.first() == '('){
            miStr2 = getOperacionParentesis(miLista)
        }else{
            miStr2 = miStr.first().toString()
        }
        if(miStr.last() == '*'){
            miStr2 += "*"
        }
        if(miStr == miStr2){
            return true
        }
        return false
    }

    open fun separarFunciones(miLista:kotlin.collections.MutableList<String>):MutableMap<Int,MutableList<String>>{
        var miMapa: MutableMap<Int,MutableList<String>> = mutableMapOf()
        var miListaFunciones = mutableListOf<String>()
        var contador = 0
        for(elem in miLista){
            if(elem != "+" && elem != "."){
                miListaFunciones.add(elem)
            }
            if(elem == "+"){
                var miListaFunciones2 = mutableListOf<String>()
                for(elem in miListaFunciones){
                    miListaFunciones2.add(elem)
                }
                miMapa.put(contador,miListaFunciones2)
                contador++
                miListaFunciones.clear()
            }
        }
        if(miListaFunciones.isNotEmpty()){
            miMapa.put(contador,miListaFunciones)
        }
        return miMapa
    }

    open fun unirVertexes(vertex1:State, vertex2:State, miLista:kotlin.collections.MutableList<String>){
        var miListaAIterar = miLista
        var miMapaIterar = separarFunciones(miListaAIterar)
        for(elem in miMapaIterar){
            var miListaAgregar = elem.value
            var miListaVertexes = mutableListOf<State>()
            miListaVertexes.add(vertex1)
            var contador = miListaAgregar.count()
            while(contador > 1){
                var vertex = State("q"+contadorEstados,false,false)
                contadorEstados++
                states.add(vertex)
                miListaVertexes.add(vertex)
                contador--
            }
            miListaVertexes.add(vertex2)
            for(elem2 in miListaAgregar.indices){
                miListaVertexes[elem2].addTransition(miListaAgregar[elem2],miListaVertexes[elem2+1].getName())
            }
        }
        //checkForStars()
        //checkForStrings()
        checkStarsTest()
        checkStringTest()
    }

    open fun cleanEdges(){
        for(vertex in states){
            var misTransAux = mutableListOf<Transition>()
            var misSTrAux = mutableListOf<String>()
            for(transicion in vertex._transitions){
                var strComp = transicion._origin + transicion._symbol + transicion._destiny
                if(!(misSTrAux.contains(strComp))){
                    misTransAux.add(transicion)
                    misSTrAux.add(strComp)
                }
            }
            vertex._transitions.clear()
            vertex._transitions = misTransAux
        }
    }

    open fun checkStringTest(){
        var lista = mutableListOf<Transition>()
        for(vertex in states){
            for(transicion in vertex._transitions){
                if(transicion._symbol.count() > 1){
                    lista.add(transicion)
                }
            }
        }
        for(elem in lista){
            var vertex1 = getState(elem._origin) as State
            vertex1._transitions.remove(elem)
        }
        for(elem in lista){
            var vertex1 = getState(elem._origin) as State
            var vertex2 = getState(elem._destiny) as State
            var lista = generarNivelActual(elem._symbol)
            unirVertexes(vertex1,vertex2,lista)
        }
    }

    open fun resolverEstrellas(vertex1:State, vertex2:State, miStr:String){
        var funcionArray = miStr.toCharArray().toMutableList()
        if(funcionArray.first() == '('){
            funcionArray.removeAt(funcionArray.count()-1)
            funcionArray.removeAt(funcionArray.count()-1)
            funcionArray.removeAt(0)
        }else{
            funcionArray.removeAt(funcionArray.count()-1)
        }
        var funcionString = ""
        for(elem in funcionArray){
            funcionString += elem.toString()
        }
        var vertexAux1 = State("q"+contadorEstados,false,false)
        contadorEstados++
        var vertexAux2 = State("q"+contadorEstados,false,false)
        contadorEstados++
        vertex1.addTransition("E",vertexAux1.getName())
        vertex1.addTransition("E",vertex2.getName())
        vertexAux1.addTransition(funcionString,vertexAux2.getName())
        vertexAux2.addTransition("E",vertex2.getName())
        vertex2.addTransition("E",vertexAux1.getName())
        states.add(vertexAux1)
        states.add(vertexAux2)
    }

    open fun generarER():String{
        var miList = expresionRegular.toCharArray().toMutableList()
        var miStr = ""
        for(elem in miList){
            if(!(elem == '.')){
                miStr += elem.toString()
            }
        }
        return miStr
    }

    open fun generarNivelActual(miString:String):kotlin.collections.MutableList<String>{
        var expresionDividida = miString.toCharArray().toMutableList()
        val miNuevaOperacion = mutableListOf<String>()
        while(expresionDividida.isNotEmpty()){
            var conjuntoActual = ""
            if(expresionDividida.first() == '('){
                conjuntoActual = getOperacionParentesis(expresionDividida)
                expresionDividida = removeFromListaChar(expresionDividida,conjuntoActual.count())
            }else if(expresionDividida.first() == '+'){
                conjuntoActual = "+"
                expresionDividida.removeAt(0)
            }else{
                conjuntoActual = expresionDividida.first().toString()
                expresionDividida.removeAt(0)
            }
            if(miNuevaOperacion.isNotEmpty() && miNuevaOperacion.last() != "+" && conjuntoActual != "+"){
                miNuevaOperacion.add(".")
            }
            if(expresionDividida.isNotEmpty() && expresionDividida.first() == '*'){
                conjuntoActual += "*"
                expresionDividida.removeAt(0)
            }
            if(conjuntoActual.first() == '(' && conjuntoActual.last() == ')'){
                var listaAux = conjuntoActual.toCharArray().toMutableList()
                listaAux.removeAt(0)
                listaAux.removeAt(listaAux.count()-1)
                conjuntoActual = ""
                for(elem in listaAux){
                    conjuntoActual += elem.toString()
                }
            }
            miNuevaOperacion.add(conjuntoActual)
        }
        return miNuevaOperacion
    }

    open fun removeFromListaChar(miLista:kotlin.collections.MutableList<Char>, cantidad:Int):kotlin.collections.MutableList<Char>{
        var miLista2 = miLista
        var contador = 0
        while(contador < cantidad){
            miLista2.removeAt(0)
            contador++
        }
        return miLista2
    }

    open fun getOperacionParentesis(miLista:kotlin.collections.MutableList<Char>):String{
        var miStr = ""
        var miNuevaOperacion = mutableListOf<Char>()
        if(miLista.first() == '('){
            var contadorAbriduras = 1
            var contadorExpresion = 1
            miNuevaOperacion.add('(')
            while(contadorAbriduras > 0){
                if(miLista[contadorExpresion] == ')'){
                    contadorAbriduras--
                }else if(miLista[contadorExpresion] == '('){
                    contadorAbriduras++
                }
                miNuevaOperacion.add(miLista[contadorExpresion])
                contadorExpresion++
            }
        }
        for(elem in miNuevaOperacion){
            miStr += elem
        }
        return miStr
    }

    open fun generateAlfabet():kotlin.collections.MutableList<String>{
        var charsInvalidos = llenarInvalidos()
        var miexp = expresionRegular.toMutableList()
        var mialphabet = mutableListOf<String>()
        for(elem in miexp){
            if(!charsInvalidos.contains(elem) && !mialphabet.contains(elem.toString())){
                mialphabet.add(elem.toString())
            }
        }
        return mialphabet
    }

    open fun llenarInvalidos():MutableList<Char>{
        var charsInvalidos = mutableListOf<Char>()
        charsInvalidos.add('(')
        charsInvalidos.add(')')
        charsInvalidos.add('+')
        charsInvalidos.add('*')
        charsInvalidos.add('.')
        return charsInvalidos
    }

    open fun printAutomata(){
        println(states.count())
        for(vertex in states){
            println("Nombre:" + vertex._name + ", inicial: "+ vertex._initialState + ", aceptado: " + vertex._isAcceptanceState)
            for(transicion in vertex._transitions){
                println(transicion._symbol + " -> " + transicion._destiny)
            }
        }
    }
}