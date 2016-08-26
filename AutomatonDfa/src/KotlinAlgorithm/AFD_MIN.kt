package KotlinAlgorithm;
import java.util.*

/**
 * Created by VirtualChus on 8/25/2016.
 */

open class AFD_MIN() : deterministicFiniteAutomaton(){
    var afd_minimizado:deterministicFiniteAutomaton = deterministicFiniteAutomaton()
    val combinaciones: MutableMap<String,Int> = mutableMapOf()

    open fun evaluarMin(evaluarEsto:String):Boolean{
        return afd_minimizado.evaluate(evaluarEsto)
    }

    open fun minimizar_automata(){
        afd_minimizado.clearAutomaton()
        combinaciones.clear()
        afd_minimizado.alphabet = getAutomatonAlphabet()
        combinar()
        firstSet()
        recursionIterar()
        masCombinaciones()
        conectarVertexes()
    }

    open fun conectarVertexes(){
        for(estado in afd_minimizado.states){
            var separado = estado._name.split(",")
            var esteVertex = getState(separado[0]) as State
            for(salida in esteVertex._transitions){
                estado.addTransition(salida._symbol,((encontrarEsteVertex(salida._destiny)) as State)._name)
            }
        }
    }

    open fun recursionIterar(){
        for(combo in combinaciones){
            recursionIterarAux(combo.key)
        }
    }

    open fun recursionIterarAux(combinationName:String){
        if(combinaciones[combinationName] == -1){
            val separado = combinationName.split(",")
            var estadoA = separado.first()
            var estadoB = separado.last()
            var sonLoMismo = 0
            for(alfabetElem in alphabet){
                var destinoA = getDastinyState(estadoA,alfabetElem) as State
                var destinoB = getDastinyState(estadoB,alfabetElem) as State
                if(destinoA._name == destinoB._name){
                    sonLoMismo++
                }else{
                    var temp = destinoA._name + "," + destinoB._name
                    var nuevoComboActual = yaExisteString(temp)
                    if(combinaciones[nuevoComboActual] == 1){
                        sonLoMismo++
                    }else if(combinaciones[nuevoComboActual] == -1){
                        recursionIterarAux(nuevoComboActual)
                        if(combinaciones[nuevoComboActual] == 1){
                            sonLoMismo++
                        }
                    }
                }
            }
            if(sonLoMismo == alphabet.count()){
                combinaciones[combinationName] = 1
            }else{
                combinaciones[combinationName] = 0
            }
        }
    }

    open fun firstSet(){
        for(combo in combinaciones){
            var isAccepted = firstSetAux(combo.key)
            if(isAccepted){
                combo.setValue(0)
            }
        }
    }

    open fun firstSetAux(str:String):Boolean{
        val separado = str.split(",")
        for(miEstadoName in separado){
            if((getState(miEstadoName) as State)._isAcceptanceState){
                return true
            }
        }
        return false
    }

    open fun combinar() {
        val misEstadosNames = mutableListOf<String>()
        for(estadoActual in states){
            misEstadosNames.add(estadoActual.getName())
        }
        var estadosCant = states.count()
        var estadoAcombinar = 0

        for(estadoA in misEstadosNames){
            if(estadoAcombinar+1 < estadosCant){
                var empezarAqui = estadoAcombinar+1
                while(empezarAqui < estadosCant){
                    var nuevoNombre = estadoA + "," +  misEstadosNames[empezarAqui]
                    combinaciones.put(nuevoNombre,-1)
                    empezarAqui++
                }
            }
            estadoAcombinar++
        }
    }

    open fun masCombinaciones(){
        val misCombosNames = mutableListOf<String>()
        val misEstadosNames = mutableListOf<String>()
        for(combo in combinaciones){
            if(combo.value == 1){
                misCombosNames.add(combo.key)
            }
        }
        for(estado in states){
            misEstadosNames.add(estado._name)
        }
        var estadosDone = 0
        while(estadosDone < misEstadosNames.count()){
            var vertexComparar = misEstadosNames.first()
            var miNuevoVertex = vertexComparar
            var esInic = false
            var esAcep = false
            if((getState(miNuevoVertex) as State)._initialState){
                esInic = true
            }
            if((getState(miNuevoVertex) as State)._isAcceptanceState){
                esAcep = true
            }
            for(combo in misCombosNames){
                var separado = combo.split(",")
                if(separado.contains(vertexComparar)){
                    var estadoA = separado.first()
                    var estadoB = separado.last()
                    if(estadoA == vertexComparar){
                        miNuevoVertex += "," + estadoB
                        if((getState(estadoB) as State)._initialState){
                            esInic = true
                        }
                        if((getState(estadoB) as State)._isAcceptanceState){
                            esAcep = true
                        }
                        misEstadosNames.remove(estadoB)
                    }else{
                        miNuevoVertex += "," + estadoA
                        if((getState(estadoA) as State)._initialState){
                            esInic = true
                        }
                        if((getState(estadoA) as State)._isAcceptanceState){
                            esAcep = true
                        }
                        misEstadosNames.remove(estadoA)
                    }
                }
            }
            afd_minimizado.addState(miNuevoVertex,esInic,esAcep)
            misEstadosNames.remove(vertexComparar)
        }
    }

    open fun yaExisteString(miStr:String):String{
        val misEstadosNames = mutableListOf<String>()
        for(combo in combinaciones){
            misEstadosNames.add(combo.key)
        }
        for (i in misEstadosNames.indices){
            val strCompare = misEstadosNames[i]
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

    open fun encontrarEsteVertex(buscar:String):State?{
        for(vertice in afd_minimizado.states){
            var separar = vertice._name
            if(separar.contains(buscar)){
                return vertice
            }
        }
        return null
    }
}