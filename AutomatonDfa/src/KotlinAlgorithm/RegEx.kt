//=========================================Regex de Jesus
package KotlinAlgorithm


/**
 * Created by Jobarah on 9/16/2016.
 */

import org.unitec.regularexpresion.RegularExpressionParser
import org.unitec.regularexpresion.tree.*

class RegEx {
    var EPSILON = "e"
    var currentStateIndex = 0
    var verifiedRegExp = ""

    open fun generarER(regex: String):String{
        var miList = regex.toCharArray().toMutableList()
        var miStr = ""
        for(elem in miList){
            if(!(elem == '[') && !(elem == ']') && !(elem == ' ') && !(elem == ',') && !(elem == '.')){
                miStr += elem.toString()
            }
            if(elem == '['){
                miStr += "("
            }
            if(elem == ']'){
                miStr += ")"
            }
        }
        return miStr
    }

    open fun parseExpresion(regex: String):String{
        var str = generarER(regex)
        var expresionDividida = str.toCharArray().toMutableList()
        val miCharList = mutableListOf<Char>()
        for(elem in expresionDividida){
            if(miCharList.isNotEmpty()){
                if((miCharList.last() != '(' && miCharList.last() != '+') && (elem != '(' && elem != ')' && elem != '+' && elem != '*')){
                    miCharList.add('.')
                }
                if((miCharList.last() == ')' || miCharList.last() == '*') && elem == '('){
                    miCharList.add('.')
                }
                if((miCharList.last() != ')' && miCharList.last() != '*' && miCharList.last() != '('  && miCharList.last() != '+'  && miCharList.last() != '.') && elem == '('){
                    miCharList.add('.')
                }
            }
            miCharList.add(elem)
        }
        var returnStr = ""
        for(elem in miCharList){
            returnStr += elem.toString()
        }
        return returnStr
    }
    open fun regexToNfae(regex: String):nonDeterministicAutomatonEpsilon {
        verifiedRegExp = parseExpresion(regex)
        println(verifiedRegExp)
        var rootNode: Node = RegularExpressionParser().Parse(verifiedRegExp)
        return generateNfae(rootNode)
    }
    private fun generateNfae(node:Node):nonDeterministicAutomatonEpsilon{
        when(node){
            is CharNode -> {
                val nfae = nonDeterministicAutomatonEpsilon()
                val initial: State = State("q"+(currentStateIndex++), true, false)
                val final: State = State("q"+(currentStateIndex++), false, true)
                nfae.states.add(initial)
                nfae.states.add(final)
                nfae.addTransition(initial._name,final._name, node.value)
                return nfae
            }
            is ANDNode -> {
                var leftNode = generateNfae(node.leftNode)
                var rightNode = generateNfae(node.rightNode)
                var leftAcceptance = leftNode.getAcceptanceState()
                var rightInitial = rightNode.getInitialState()!!
                leftAcceptance!!._isAcceptanceState = false
                rightInitial._initialState = false
                leftAcceptance!!.addTransition(EPSILON,rightInitial._name)
                rightNode.alphabet.forEach { symbol -> leftNode.alphabet.add(symbol) }
                leftNode.alphabet = leftNode.alphabet.distinct().toMutableList()
                rightNode.states.forEach { state -> leftNode.states.add(state) }
                return leftNode
            }
            is ORNode -> {
                var leftNode = generateNfae(node.leftNode)
                var rightNode = generateNfae(node.rightNode)

                var leftAcceptanceState=leftNode.getAcceptanceState()
                var rightAcceptanceState=rightNode.getAcceptanceState()
                var leftInitial = leftNode.getInitialState()
                var rightInitial = rightNode.getInitialState()
                leftInitial!!._initialState = false
                rightInitial!!._initialState = false
                leftAcceptanceState!!._isAcceptanceState = false
                rightAcceptanceState!!._isAcceptanceState = false

                val initial: State = State("q"+(currentStateIndex++), true, false)
                val final: State = State("q"+(currentStateIndex++), false, true)

                initial.addTransition(EPSILON, leftInitial._name)
                initial.addTransition(EPSILON, rightInitial._name)
                leftAcceptanceState.addTransition(EPSILON,final._name)
                rightAcceptanceState.addTransition(EPSILON,final._name)

                leftNode.states.add(initial)
                leftNode.states.add(final)
                for (state in rightNode.states){
                    leftNode.states.add(state)
                }

                rightNode.alphabet.forEach { symbol -> leftNode.alphabet.add(symbol) }
                leftNode.alphabet = leftNode.alphabet.distinct().toMutableList()
                return leftNode
            }
            is RepeatNode -> {
                var returnNode = generateNfae(node.node)
                var initialState = returnNode.getInitialState()!!
                var finalState = returnNode.getAcceptanceState()!!

                initialState._initialState = false
                finalState._isAcceptanceState = false

                val newInitial: State = State("q"+(currentStateIndex++), true, false)
                val newFinal: State = State("q"+(currentStateIndex++), false, true)

                returnNode.states.add(newInitial)
                returnNode.states.add(newFinal)

                returnNode.addTransition(newInitial._name, initialState._name, EPSILON)
                returnNode.addTransition(newInitial._name, newFinal._name,EPSILON)

                returnNode.addTransition(finalState._name, initialState._name, EPSILON)
                returnNode.addTransition(finalState._name, newFinal._name, EPSILON)

                return returnNode
            }
            else -> {
                throw Exception("not implemented")
            }
        }
    }

    fun  generateAlphabet(): MutableList<String> {
        var newAlphabet:MutableList<String> = mutableListOf()
        for (item in verifiedRegExp) {
            if (item != '(' && item != ')' && item != '+' && item != '.' && item != '*') {
                newAlphabet.add(item.toString())
            }
        }
        return newAlphabet
    }

}