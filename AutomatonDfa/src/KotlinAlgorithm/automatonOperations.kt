package KotlinAlgorithm

/**
 * Created by Jobarah on 8/28/2016.
 */
class automatonOperations {

    fun getTransition(state:State, symbol:String): Transition? {
        for(transition in state._transitions){
            if(transition._symbol.equals(symbol)){
                return transition
            }
        }
        return null
    }

    fun Union(automatonA:deterministicFiniteAutomaton, automatonB:deterministicFiniteAutomaton) : deterministicFiniteAutomaton{
        var returnDFA:deterministicFiniteAutomaton = merge(automatonA, automatonB)
        returnDFA.automatonName = "Union: " + automatonA.automatonName + " " + automatonB.automatonName
        for(stateA in automatonA.states){
            for(stateB in automatonB.states){
                if(stateA._initialState && stateB._initialState){
                    for(newState in returnDFA.states){
                        if(newState._name.equals(stateA._name + stateB._name)){
                            newState._initialState = true
                        }
                    }
                }
                if(stateA._isAcceptanceState || stateB._isAcceptanceState){
                    for(newState in returnDFA.states){
                        if(newState._name.equals(stateA._name + stateB._name)){
                            newState._isAcceptanceState = true
                        }
                    }
                }
            }
        }
        return returnDFA
    }

    fun Intersection(automatonA:deterministicFiniteAutomaton, automatonB:deterministicFiniteAutomaton) : deterministicFiniteAutomaton{
        var returnDFA:deterministicFiniteAutomaton = merge(automatonA, automatonB)
        returnDFA.automatonName = "Interseccion: " + automatonA.automatonName + " " + automatonB.automatonName
        for(stateA in automatonA.states){
            for(stateB in automatonB.states){
                if(stateA._initialState && stateB._initialState){
                    for(newState in returnDFA.states){
                        if(newState._name.equals(stateA._name + stateB._name)){
                            newState._initialState = true
                        }
                    }
                }
                if(stateA._isAcceptanceState && stateB._isAcceptanceState){
                    for(newState in returnDFA.states){
                        if(newState._name.equals(stateA._name + stateB._name)){
                            newState._isAcceptanceState = true
                        }
                    }
                }
            }
        }
        return returnDFA
    }

    fun Subtraction(automatonA:deterministicFiniteAutomaton, automatonB:deterministicFiniteAutomaton) : deterministicFiniteAutomaton{
        var returnDFA:deterministicFiniteAutomaton = merge(automatonA, automatonB)
        returnDFA.automatonName = "Subtraction: " + automatonA.automatonName + " " + automatonB.automatonName
        for(stateA in automatonA.states){
            for(stateB in automatonB.states){
                if(stateA._initialState && stateB._initialState){
                    for(newState in returnDFA.states){
                        if(newState._name.equals(stateA._name + stateB._name)){
                            newState._initialState = true
                        }
                    }
                }
                if(stateA._isAcceptanceState && !stateB._isAcceptanceState){
                    for(newState in returnDFA.states){
                        if(newState._name.equals(stateA._name + stateB._name)){
                            newState._isAcceptanceState = true
                        }
                    }
                }
            }
        }
        return returnDFA
    }

    fun merge(automatonA:deterministicFiniteAutomaton, automatonB:deterministicFiniteAutomaton) : deterministicFiniteAutomaton{
        var returnDFA:deterministicFiniteAutomaton = deterministicFiniteAutomaton()
        for(stateA in automatonA.states){
            for(stateB in automatonB.states){
                returnDFA.states.add(State(stateA._name + stateB._name, false, false))
            }
        }
        var transitionSymbols:MutableList<String> = mutableListOf()
        for(stateA in automatonA.states){
            for(transition in stateA._transitions){
                if(!transitionSymbols.contains(transition._symbol + "")){
                    transitionSymbols.add(transition._symbol + "")
                }
            }
        }
        for(stateB in automatonB.states){
            for(transition in stateB._transitions){
                if(!transitionSymbols.contains(transition._symbol + "")){
                    transitionSymbols.add(transition._symbol + "")
                }
            }
        }

        for(stateA in automatonA.states){
            for(stateB in automatonB.states){
                for(symbol in transitionSymbols){
                    val transition1 = getTransition(stateA, symbol)
                    val transition2 = getTransition(stateB, symbol)
                    val originStateName = stateA._name + stateB._name
                    var destinyStateName = ""
                    if(transition1!=null){
                        destinyStateName += transition1._destiny
                    }
                    if(transition2!=null){
                        destinyStateName += transition2._destiny
                    }
                    println(symbol[0])
                    returnDFA.getState(originStateName)!!._transitions.add(Transition(symbol[0], originStateName, returnDFA.getState(destinyStateName)!!._name))
                }
            }
        }

        return returnDFA
    }

}