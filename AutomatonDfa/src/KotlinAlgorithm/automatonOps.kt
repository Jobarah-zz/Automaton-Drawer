package KotlinAlgorithm

import com.sun.org.apache.xpath.internal.operations.Bool
import javax.xml.transform.sax.SAXTransformerFactory
import kotlin.comparisons.naturalOrder

/**
 * Created by Jobarah on 8/27/2016.
 */
class automatonOps {

    //List of new States belonging to the union of both DFA's received as parameters
    var statesList:MutableList<State> = mutableListOf(State())

    //List to verify if a states exists already
    var existentStates:MutableList<String> = mutableListOf<String>()

    var unifiedAutomaton = deterministicFiniteAutomaton()

    var unionAutomaton = deterministicFiniteAutomaton()

    fun isAcceptanceState(stateName:String):Boolean {
        var statesToVisit = stateName.split(",") as MutableList<String>

        for (thisState in statesToVisit) {
            if(unifiedAutomaton.getState(thisState)!!._isAcceptanceState)
                return true
        }
        return false
    }

    fun getDestinations(state:State) {
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
                println(destinationStateName)
                var newState = State(destinationStateName,false,isAcceptanceState(destinationStateName))
                if(!existentStates.contains(destinationStateName)) {
                    unionAutomaton.states.add(newState)
                    existentStates.add(destinationStateName)
                    state._transitions.add(Transition(symbol, state._name, destinationStateName))
                    getDestinations(newState)
                } else {
                    println(destinationStateName)
                    unionAutomaton.getState(destinationStateName)!!._transitions.add(Transition(symbol, state._name, destinationStateName))
                }
            }
        }
    }

    fun newInitialState(a:State, b:State):State {
        var isAcceptanceState = false
        if(a._isAcceptanceState || b._isAcceptanceState)
            isAcceptanceState = true
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
        var newInitialState:State = newInitialState(a.getInitialState() as State, b.getInitialState() as State)
        //Addition of generated initial state
        statesList.add(newInitialState)
        existentStates.add(newInitialState._name)

        getDestinations(newInitialState)

        var returnDfa = deterministicFiniteAutomaton()
        returnDfa.alphabet = unifiedAutomaton.getAutomatonAlphabet()
        returnDfa.states = statesList


//        var statesToVisit = newInitialState._name.split(",") as MutableList<String>

//            for(symbol in unifiedAlphabet) {
//                var destinationStateName = ""
//                var isAcceptance = false
//                for(item in statesToVisit) {
//                    var evalState = unifiedAutomaton.getState(item)
//
//                    if(!destinationStateName.equals(""))
//                        destinationStateName += ","
//
//                   destinationStateName += evalState!!.getDestinyStateName(symbol) as String
//                    if( evalState._isAcceptanceState)
//                        isAcceptance = true
//                }
//                if (!destinationStateName.equals("")) {
//                    var newState = State(destinationStateName,false,isAcceptance)
//                    if(!existentStates.contains(destinationStateName)) {
//                        statesList.add(newState)
//                        existentStates.add(destinationStateName)
//                    }
//                }
//            }

        return returnDfa
  }
}