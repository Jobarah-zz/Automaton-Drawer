package KotlinAlgorithm

/**
 * Created by Jobarah on 9/18/2016.
 */
class CFG {

    var grammarMap: MutableMap<Char,MutableList<String>> = mutableMapOf()
    var simboloInicial = '0'
    /*
        open fun evaluar(evaluarEsto:String):Boolean{
            if(verifyEvaluar(evaluarEsto)){
                var evaluar = evaluarEsto.toCharArray().toMutableList()
                while(evaluar.count() > 0){
                    println("Hola")
                }
                return true
            }else{
                return false
            }
        }

        open fun recorrer(currentChar:Char, listaEvaluar:MutableList<Char>):Boolean{
            return true
        }
    */
    open fun addBranch(indice:Char,producciones:String){
        var strLista = mutableListOf<String>()
        strLista.add(producciones)
        if(grammarMap.containsKey(indice)){
            var strLista2 = grammarMap.get(indice) as MutableList<String>
            for(elem in strLista2){
                strLista.add(elem)
            }
            grammarMap.remove(indice)
            grammarMap.put(indice,strLista)
        }else{
            grammarMap.put(indice,strLista)
        }
    }

    open fun setInicial(indice:Char){
        if(grammarMap.containsKey(indice)){
            simboloInicial = indice
        }
    }

    open fun fillNoTerminales():MutableList<Char>{
        var noTerminales = mutableListOf<Char>()
        for(elem in grammarMap){
            noTerminales.add(elem.key)
        }
        return noTerminales
    }

    open fun fillTerminales():MutableList<String>{
        var Terminales = mutableListOf<String>()
        for(elem in grammarMap){
            var miLista = elem.value
            for(elem2 in miLista){
                var misChars = elem2.toCharArray().toMutableList()
                for(charact in misChars){
                    if(!Terminales.contains(charact.toString()) && !grammarMap.containsKey(charact)){
                        Terminales.add(charact.toString())
                    }
                }
            }
        }
        return Terminales
    }

    open fun verifyEvaluar(evaluarEsto:String):Boolean{
        var miStrArray = evaluarEsto.toCharArray().toMutableList()
        for(elem in grammarMap){
            if(!miStrArray.contains(elem.key)){
                return false
            }
        }
        return true
    }

    open fun transformarPDA():PushDownAutomata{
        var Terminales = fillTerminales()
        var automataPDA = PushDownAutomata()
        automataPDA.alphabet = Terminales
        automataPDA.addState("q0",true,false)
        automataPDA.addState("q1",false,false)
        automataPDA.addState("q2",false,false)
        var miStr = simboloInicial + "Z"
        automataPDA.states[0].addTransition("(E,Z/$miStr)","q1")
        for(elem in grammarMap){
            var miStrLista = elem.value
            for(str in miStrLista){
                automataPDA.states[1].addTransition("(E,"+elem.key+"/"+str+")", "q1")
            }
        }
        for(elem in Terminales){
            automataPDA.states[1].addTransition("($elem,$elem/E)", "q1")
        }
        automataPDA.states[1].addTransition("(E,Z/E)", "q2")
        return automataPDA
    }
}