package KotlinAlgorithm

/**
 * Created by Jobarah on 8/27/2016.
 */
class automatonOps {

    //List of new States belonging to the union of both DFA's received as parameters
    var statesList:MutableList<State> = mutableListOf()
    //List to verify if a states exists already
    var existentStates:MutableList<String> = mutableListOf<String>()
    //temporary automaton to fill with states, pretty much implemented to use function getState
    var unifiedAutomaton = deterministicFiniteAutomaton()
    //subtraction List of acceptance states
    var subAcceptanceStates:MutableList<String> = mutableListOf()
    var subSecondAutomatonAcceptance:MutableList<String> = mutableListOf()

    fun updateStatesTransition(statesList:MutableList<State>, oldStateName: String, newStateName: String) {
        for (state in statesList) {
            for(transition in state._transitions) {
                if(transition._destiny.equals(oldStateName)) {
                    transition._destiny = newStateName
                }
                if (transition._origin.equals(oldStateName)) {
                    transition._origin = newStateName
                }
            }
        }
    }

    fun generateUnifiedStates(statesA:MutableList<State>, statesB: MutableList<State>):MutableList<State> {
        var unifiedStates:MutableList<State> = mutableListOf()
        for (stateA in statesA) {
            unifiedStates.add(stateA)
        }
        var listOfStatesName:MutableList<String> = mutableListOf()

        for(state in unifiedStates) {

            listOfStatesName.add(state._name)
        }

        for (stateB in statesB) {
            if (listOfStatesName.contains(stateB._name)) {
                var newStateName = stateB._name + "(2)"
                updateStatesTransition(statesB, stateB._name, newStateName)
                stateB._name = newStateName
            }
            unifiedStates.add(stateB)
            if (stateB._isAcceptanceState) {
                subSecondAutomatonAcceptance.add(stateB._name)
            }
        }

        return unifiedStates
    }

    fun generateUnifiedAlphabet(alphabetA:MutableList<String>, alphabetB: MutableList<String>):MutableList<String> {
        var unifiedAlphabet:MutableList<String> = alphabetA

        for(symbolB in alphabetB) {

            if (!alphabetA.contains(symbolB)) {
                unifiedAlphabet.add(symbolB)
            }
        }

        return unifiedAlphabet
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
            "subtraction"-> {
                if (subAcceptanceStates.contains(a._name) && !b._isAcceptanceState){
                    isAcceptanceState = true
                }
            }
        }

        var newState = State(a._name +","+ b._name, true, isAcceptanceState)
        return newState
    }

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
            "subtraction"-> {

                if ((subAcceptanceStates.contains(statesToVisit[0]) && !subSecondAutomatonAcceptance.contains(statesToVisit[1])) || (subAcceptanceStates.contains(statesToVisit[1]) && !subSecondAutomatonAcceptance.contains(statesToVisit[0]))){
                    return true
                } else if (subAcceptanceStates.contains(statesToVisit[0]) && subAcceptanceStates.contains(statesToVisit[1])) {
                    return true
                }

                return false
            }
        }
        return false
    }

    fun getDestinations(state:State, operation: String) {
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
                    state._transitions.add(Transition(symbol, state._name, destinationStateName))
                    getDestinations(newState, operation)

                } else {
                    for (currentState in statesList) {
                        if (currentState._name.equals(state._name)) {
                            currentState._transitions.add(Transition(symbol, state._name, destinationStateName))
                        }
                    }
                }
            }
        }
    }

    fun operation(a:deterministicFiniteAutomaton, b:deterministicFiniteAutomaton, operation:String):deterministicFiniteAutomaton {

        //Generation of unified states
        unifiedAutomaton.states = generateUnifiedStates(a.getAutomatonStates(), b.getAutomatonStates())
        //generation of unified alphabet
        unifiedAutomaton.alphabet = generateUnifiedAlphabet(a.getAutomatonAlphabet(), b.getAutomatonAlphabet())

        //If the received operation is different from intersection, union and subtraction, operation not supported
        if (!operation.equals("intersection") && !operation.equals("union") && !operation.equals("subtraction")) {
            throw Exception("Invalid Operation")
        } else {
            //fill list of possible acceptance states
            if (operation.equals("subtraction")) {
                //subtraction acceptance states
                for (state in a.states) {
                    if (state._isAcceptanceState) {
                        subAcceptanceStates.add(state._name)
                    }
                }
            }
        }

        //New initial state generation
        var newInitialState: State = newInitialState(a.getInitialState() as State, b.getInitialState() as State, operation)
        //Addition of generated initial state to a temp states list and to a state's name list
        statesList.add(newInitialState)
        existentStates.add(newInitialState._name)

        for (state in subSecondAutomatonAcceptance) {
            println(state)
        }
        //this function receives an automaton, splits it's name and searches the destination of the split states and generates a new state
        //then it applies recursion with the new state generated
        getDestinations(newInitialState, operation)
        //Construction of automaton to return
        var returnDfa = deterministicFiniteAutomaton()
        //get the constructed alphabet from the temporary automaton
        returnDfa.alphabet = unifiedAutomaton.getAutomatonAlphabet()
        //get the constructed states from the temporary states list
        returnDfa.states = statesList

        return returnDfa
    }

    open fun cloneDfa(automaton: deterministicFiniteAutomaton):deterministicFiniteAutomaton {
        var clonedAutomaton = deterministicFiniteAutomaton()

        for (state in automaton.states) {
            var clonedState = State(state._name, state._initialState, state._isAcceptanceState)
            for (transition in state._transitions) {
                clonedState.addTransition(transition._symbol, transition._destiny)
            }
            clonedAutomaton.states.add(clonedState)
        }

        for (symbol in automaton.alphabet) {
            clonedAutomaton.alphabet.add(symbol)
        }
        return clonedAutomaton
    }

    fun complement(automaton: deterministicFiniteAutomaton):deterministicFiniteAutomaton {
        var complementAutomaton = cloneDfa(automaton)
        var isSewerCreated = false
        //swap acceptance states
        for(state in complementAutomaton.states) {
            if (state._isAcceptanceState) {
                state._isAcceptanceState = false
            } else {
                state._isAcceptanceState = true
            }
        }
        //verifying every state has a destiny with every symbol, create sewer if needed
        for (state in complementAutomaton.states) {
            for (symbol in complementAutomaton.alphabet) {
                if (complementAutomaton.getDestinyState(state._name, symbol) == null) {
                    if (isSewerCreated == false) {
                        var sewer = State("sewer", false, true)
                        for (_symbol in complementAutomaton.alphabet) {
                            sewer.addTransition(_symbol, "sewer")
                            isSewerCreated = true
                        }
                    }
                    state.addTransition(symbol, "sewer")
                }
            }
        }
        return complementAutomaton
    }
  }