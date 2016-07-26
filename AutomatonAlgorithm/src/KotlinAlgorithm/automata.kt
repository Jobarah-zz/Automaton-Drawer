/**
 * Created by Jobarah on 7/25/2016.
 */
fun main(arr : Array<String>){
    var a =  Automaton()
    val state1 = States("q0",true,false)
    val states2 = States("q1",false,true)
    var transition1 = Transition('0',"q0","q1")
    var transition2 = Transition('1',"q1","q0")
    state1._transition.add(transition1)
    states2._transition.add(transition2)
    a.states.add(state1)
    a.states.add(states2)
    a.alphabet.add('0')
    a.alphabet.add('1')
    println(a.evaluate("0"))
}
