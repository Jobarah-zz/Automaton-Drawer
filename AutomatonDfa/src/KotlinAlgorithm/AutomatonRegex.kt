package KotlinAlgorithm

/**
 * Created by Jobarah on 9/17/2016.
 */
class AutomatonRegex {

    private val EPSILON = "e"

    fun ParseDFAToRegex(_states: MutableList<State>):String{
        val states = replaceStateForRegexState(_states)
        val initialState = states.filter { it._initialState }
        if(initialState.isEmpty())
            throw Exception("State not found!")
        changeTheNewInitialState(states)
        changeTheFinalState(states)
        updateTargetCountForEachState(states)
        sortByLessTarget(states)
        updatePoint_destinyMeStates(states)
        return getRegex(states,initialState.elementAt(0)).replace("U", "+")
    }

    private fun changeTheNewInitialState(states: MutableList<RegexState>){
        val newInitialState = RegexState()
        newInitialState._name = "S"
        newInitialState._initialState = true
        val transition = Transition()
        transition._symbol = EPSILON
        transition._origin = newInitialState._name
        transition._destiny = (states.filter { it._initialState }).elementAt(0)._name
        (states.filter { it._initialState }).elementAt(0)._initialState = false
        newInitialState._transitions.add(transition)
        states.add(newInitialState)
    }

    private fun changeTheFinalState(states: MutableList<RegexState>){
        val newFinalState = RegexState()
        newFinalState._name = "F"
        newFinalState._isAcceptanceState = true
        states.filter { it._isAcceptanceState }.forEach {
            val transition = Transition()
            transition._symbol = EPSILON
            transition._origin = it._name
            transition._destiny = "F"
            it._transitions.add(transition)
            it._isAcceptanceState = false
        }
        states.add(newFinalState)
    }

    private fun updateTargetCountForEachState(states: MutableList<RegexState>){
        for(it in states) {
            if(it._name.equals("S")||it._name.equals("F"))
                continue
            it.destiniesCount = 0
            for(xState in 0..(states.size-1)){
                for(transition in states[xState]._transitions){
                    if(transition._symbol.equals(EPSILON))
                        continue
                    if(transition._destiny.equals(it._name)) it.destiniesCount++ else it.destiniesCount
                }
            }
        }
    }

    private fun replaceStateForRegexState(states: MutableList<State>):MutableList<RegexState>{
        val _states:MutableList<RegexState> = mutableListOf()
        states.forEach {
            val state = RegexState()
            state._name = it._name
            state._isAcceptanceState = it._isAcceptanceState
            state._initialState = it._initialState
            state._transitions = it._transitions
            _states.add(state)
        }
        return _states
    }

    private fun sortByLessTarget(states: MutableList<RegexState>){
        states.sortBy { it.destiniesCount }
    }

    private fun removeStateByLessTarget(state_destinyRemove:RegexState,states: MutableList<RegexState>){
        if(state_destinyRemove.hasTransitionToItself())
            remove_transitionsThatPointing_destinyMyself(state_destinyRemove)
        for(source_destinyMe in state_destinyRemove.statesPointingToMe){
            states.filter {it._name.equals(source_destinyMe)}.forEach {stateSource ->
                val _transitionsPending_destinyAdd:MutableList<Transition> = mutableListOf()
                stateSource._transitions.filter { it._destiny.equals(state_destinyRemove._name)}
                        .forEach {
                            transition_destinyState_destinyRemove ->
                            state_destinyRemove._transitions.filter {!it._destiny.equals(state_destinyRemove._name)}
                                    .forEach { transition_destinyOtherStateFromState_destinyRemove ->
                                        val newTransition = getNewTransition(stateSource, state_destinyRemove,
                                                transition_destinyOtherStateFromState_destinyRemove, transition_destinyState_destinyRemove)
                                        _transitionsPending_destinyAdd.add(newTransition)
                                        stateSource._transitions.remove(transition_destinyState_destinyRemove)
                                    }
                        }
                _transitionsPending_destinyAdd.forEach { stateSource._transitions.add(it)}
            }
        }
        states.remove(state_destinyRemove)
    }

    private fun getNewTransition(stateSource: RegexState, state_destinyRemove: RegexState,
                                 transition_destinyOtherStateFromState_destinyRemove: Transition,
                                 transition_destinyState_destinyRemove: Transition):Transition {
        val newTransition = Transition()
        newTransition._origin = stateSource._name
        newTransition._destiny = transition_destinyOtherStateFromState_destinyRemove._destiny
        newTransition._symbol = transition_destinyState_destinyRemove._symbol
        newTransition._symbol += if (state_destinyRemove.hasTransitionToItself())
            "(" + state_destinyRemove._transitions.filter { it._destiny.equals(state_destinyRemove._name) }
                    .elementAt(0)._symbol + ")*" else ""
        newTransition._symbol += transition_destinyOtherStateFromState_destinyRemove._symbol
        return newTransition
    }

    private fun updatePoint_destinyMeStates(states: MutableList<RegexState>){
        for(it in states) {
            if(it._name.equals("S")||it._name.equals("F"))
                continue
            it.statesPointingToMe.clear()
            for(x in 0..(states.size-1)){
                if(states[x]._name.equals(it._name)||(states[x]._name.equals("S")||states[x]._name.equals("F")))
                    continue
                for(iTrans in states[x]._transitions){
                    if(iTrans._destiny.equals(it._name)&&!iTrans._symbol.equals(it._name))
                        it.statesPointingToMe.add(iTrans._origin!!)
                }
            }
        }
    }

    private fun remove_transitionsThatPointing_destinyMyself(state: RegexState){
        val trans = Transition()
        trans._origin = state._name
        trans._destiny = state._name
        trans._symbol = ""
        state._transitions.filter { it._destiny.equals(state._name) }.forEach {
            trans._symbol += it._symbol + "+"
            state._transitions.remove(it)
        }
        trans._symbol = trans._symbol.toString().substring(0,(trans._symbol.toString().length-1))
        state._transitions.add(trans)
    }

    private fun getRegex(states: MutableList<RegexState>, initialState: RegexState):String{
        while(states.size > 3){
            var position_destinyRemove = 2
            if(ignoreState(states.elementAt(position_destinyRemove),initialState))
                position_destinyRemove = 3
            removeStateByLessTarget(states.elementAt(position_destinyRemove),states)
            updateTargetCountForEachState(states)
            sortByLessTarget(states)
            updatePoint_destinyMeStates(states)
        }
        if(states.elementAt(2).hasTransitionToItself())
            remove_transitionsThatPointing_destinyMyself(states.elementAt(2))
        val finalState =states.filter { !it._name.equals("S")&&!it._name.equals("F") }.elementAt(0)
        var regex = concatDifferentRegex(finalState)
        regex = deleteTheLastUFromRegex(regex)
        return regex.toString()
    }

    private fun concatDifferentRegex(finalState: RegexState): String {
        val q0Destiny = finalState._transitions.filter { it._destiny.equals(it._origin) }
        val loopTransition = if(q0Destiny.isEmpty()) "" else q0Destiny.elementAt(0)._symbol
        var regex1 = ""
        finalState._transitions.filter { !it._destiny.equals(it._origin) }.forEach {
            transition ->
            regex1 += "["
            regex1 += if (!loopTransition.isNullOrBlank()) "($loopTransition)*" else loopTransition
            regex1 += transition._symbol
            regex1 += "] U "
        }
        return regex1
    }

    private fun deleteTheLastUFromRegex(regex: String): String {
        var regex1 = regex
        if (regex1.elementAt(regex1.length - 2).equals('U')) {
            regex1 = regex1.substring(0, (regex1.length - 2))
            val regexSp = regex1.split(EPSILON)
            regex1 = ""
            regexSp.forEach {
                regex1 += it
            }
        }
        return regex1
    }

    private fun ignoreState(state:RegexState, initialState: RegexState):Boolean{
        return state._name.equals(initialState._name)||state._name.equals("S")||state._name.equals("F")
    }
}