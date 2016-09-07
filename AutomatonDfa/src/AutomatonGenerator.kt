import KotlinAlgorithm.*
import com.mxgraph.model.mxCell

/**
 * Created by jenamorado on 8/31/16.
 */
class AutomatonGenerator(automatonType: String) {

    var _automatonType = automatonType

    fun generateAutomaton(nodes:MutableList<mxCell>, edges:MutableList<mxCell>, alphabet:MutableList<String>):Automaton? {
        if (!_automatonType.equals("dfa") && !_automatonType.equals("nfa") && !_automatonType.equals("ε-nfa") && !_automatonType.equals("pda")) {
            throw Exception("Invalid Automaton Type!")
        }
        when (_automatonType) {
            "nfa" -> {
                var newAutomaton = nonDeterministicFiniteAutomaton()
                newAutomaton.states = generateStates(nodes, edges)
                newAutomaton.alphabet = alphabet
                return  newAutomaton
            }
            "dfa" -> {
                var newAutomaton = deterministicFiniteAutomaton()
                newAutomaton.states = generateStates(nodes, edges)
                newAutomaton.alphabet = alphabet
                return  newAutomaton
            }
            "ε-nfa" -> {
                var newAutomaton = nonDeterministicAutomatonEpsilon()
                newAutomaton.states = generateStates(nodes, edges)
                newAutomaton.alphabet = alphabet
                return  newAutomaton
            }
            "pda" -> {
                var newAutomaton = PushDownAutomata()
                newAutomaton.states = generateStates(nodes, edges)
                newAutomaton.alphabet = alphabet
                return  newAutomaton
            }
        }
        return null
    }

    fun generateStateTransition(state:State, transitions:MutableList<mxCell>){
        var stateName = state._name
        for (transition in transitions) {
            if (transition.source.value.toString().equals(stateName)) {
                state.addTransition(transition.value.toString(), transition.target.value.toString())
            }
        }
    }

    fun generateStates(nodes:MutableList<mxCell>, edges:MutableList<mxCell>):MutableList<State> {
        var statesList:MutableList<KotlinAlgorithm.State> = mutableListOf()
        for (item in nodes) {
            when (item.style) {
                "shape=ellipse;fillColor=#4ECDC4" -> {
                    var newState = State(item.value as String, true, false)
                    generateStateTransition(newState, edges)
                    statesList.add(newState)
                }
                "shape=doubleEllipse;fillColor=#22A7F0" -> {
                    var newState = State(item.value as String, false, true)
                    generateStateTransition(newState, edges)
                    statesList.add(newState)
                }
                "shape=doubleEllipse;fillColor=#4ECDC4" -> {
                    var newState = State(item.value as String, true, true)
                    generateStateTransition(newState, edges)
                    statesList.add(newState)
                }
                else -> {
                    var newState = State(item.value as String, false, false)
                    generateStateTransition(newState, edges)
                    statesList.add(newState)
                }
            }
        }
        return statesList
    }
}