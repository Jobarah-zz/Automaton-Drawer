package KotlinAlgorithm

/**
 * Created by Jobarah on 9/4/2016.
 */
class Stack{

    public var stack:MutableList<String> = mutableListOf()

    fun push(item:String) {
        stack.add(item)
    }

    fun pop() {
        stack.removeAt(stack.size-1)
    }



}