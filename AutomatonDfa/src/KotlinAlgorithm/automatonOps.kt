package KotlinAlgorithm

/**
 * Created by Jobarah on 8/27/2016.
 */
class automatonOps {

    //List of new States belonging to the union of both DFA's received as parameters
    var statesList:MutableList<State> = mutableListOf()

    //List to verify if a states exists already
    var existentStates:MutableList<String> = mutableListOf<String>()

    var unifiedAutomaton = deterministicFiniteAutomaton()

    fun isAcceptanceState(stateName:String, operation: String):Boolean {
        var statesToVisit = stateName.split(",") as MutableList<String>
        when(operation) {
            "union"-> {
                for (thisState in statesToVisit) {
                    if(unifiedAutomaton.getState(thisState)!!._isAcceptanceState)
                        return true
                }
            }
            "intersection"-> {
                for (thisState in statesToVisit) {
                    if(!unifiedAutomaton.getState(thisState)!!._isAcceptanceState){
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

    fun getUnionDestinations(state:State, operation: String) {
        //Splitting the name of the state in case it's composed of two states ex: q0,qa
        var statesToVisit = state._name.split(",") as MutableList<String>
        //Iterate through the alphabet to get destinies with each symbol
        for(symbol in unifiedAutomaton.alphabet) {
            var destinationStateName = ""
            //for every old state that the name of the new state contains
            for(item in statesToVisit) {
                var evalState = unifiedAutomaton.getState(item)
                var nameToAppend = evalState!!.getDestinyStateName(symbol)

                if(!destinationStateName.equals("") && !nameToAppend.isNullOrEmpty())
                    destinationStateName += ","

                if(!nameToAppend.isNullOrEmpty())
                    destinationStateName += nameToAppend
            }

            if (!destinationStateName.equals("")) {
                var newState = State(destinationStateName,false,isAcceptanceState(destinationStateName, operation))
                if(!existentStates.contains(destinationStateName)) {
                    statesList.add(newState)
                    existentStates.add(destinationStateName)

                    //Debugging Purposes
//                    println("---Transition Info-------")
//                    println("("+state._name+"):"+symbol+"->("+destinationStateName+")")
//                    println("-------End of Info-------")
//                    println(" ")

                    state._transitions.add(Transition(symbol, state._name, destinationStateName))
                    getUnionDestinations(newState, operation)
                } else {
                    //Debugging Purposes
//                    println("---Transition Info-------")
//                    println("("+state._name+"):"+symbol+"->("+destinationStateName+")")
//                    println("---End of Info-----------")
//                    println(" ")

                    for (currentState in statesList) {
                        if (currentState._name.equals(state._name)) {
                            currentState._transitions.add(Transition(symbol, state._name, destinationStateName))
                        }
                    }
                }
            }
        }
    }

    fun newInitialState(a:State, b:State, operation:String):State {
        var isAcceptanceState = false
        when(operation) {
            "union"-> {
                if(a._isAcceptanceState || b._isAcceptanceState) {
                    isAcceptanceState = true
                }
            }
            "intersection"-> {
                if(a._isAcceptanceState && b._isAcceptanceState) {
                    isAcceptanceState = true
                }
            }
        }

        var newState = State(a._name +","+ b._name, true, isAcceptanceState)
        return newState
    }

    fun generateUnifiedAlphabet(alphabetA:MutableList<Char>, alphabetB: MutableList<Char>):MutableList<Char> {
        var unifiedAlphabet:MutableList<Char> = alphabetA

            for(symbolB in alphabetB) {

                if (!alphabetA.contains(symbolB)) {
                    unifiedAlphabet.add(symbolB)
                }
            }

        return unifiedAlphabet
    }

    fun union(a:deterministicFiniteAutomaton, b:deterministicFiniteAutomaton):deterministicFiniteAutomaton {
        //Generation of unified states
        unifiedAutomaton.states = (a.getAutomatonStates() + b.getAutomatonStates()) as MutableList<State>
        //generation of unified alphabet
        unifiedAutomaton.alphabet = generateUnifiedAlphabet(a.getAutomatonAlphabet(), b.getAutomatonAlphabet())
        //New initial state generation
        var newInitialState:State = newInitialState(a.getInitialState() as State, b.getInitialState() as State, "union")
        //Addition of generated initial state to a temp states list and to a state's name list
        statesList.add(newInitialState)
        existentStates.add(newInitialState._name)
        //function that fills states list
        getUnionDestinations(newInitialState, "union")

        var returnDfa = deterministicFiniteAutomaton()
        returnDfa.alphabet = unifiedAutomaton.getAutomatonAlphabet()
        returnDfa.states = statesList

        return returnDfa
  }
    fun intersection(a:deterministicFiniteAutomaton, b:deterministicFiniteAutomaton):deterministicFiniteAutomaton {
        //Generation of unified states
        unifiedAutomaton.states = (a.getAutomatonStates() + b.getAutomatonStates()) as MutableList<State>
        //generation of unified alphabet
        unifiedAutomaton.alphabet = generateUnifiedAlphabet(a.getAutomatonAlphabet(), b.getAutomatonAlphabet())
        //New initial state generation
        var newInitialState:State = newInitialState(a.getInitialState() as State, b.getInitialState() as State, "intersection")
        //Addition of generated initial state to a temp states list and to a state's name list
        statesList.add(newInitialState)
        existentStates.add(newInitialState._name)

        getUnionDestinations(newInitialState, "intersection")

        var returnDfa = deterministicFiniteAutomaton()
        returnDfa.alphabet = unifiedAutomaton.getAutomatonAlphabet()
        returnDfa.states = statesList

        return returnDfa
    }
}