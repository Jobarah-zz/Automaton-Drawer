package KotlinAlgorithm

import com.sun.javafx.collections.MappingChange
import org.omg.CORBA.BAD_POLICY
import java.util.*

/**
 * Created by Jobarah on 9/8/2016.
 */
class contextFreeGrammar {

    val grammar: MutableMap<String, MutableList<String>> = hashMapOf()

    fun insertGrammar(root: String, productions: MutableList<String>) {
        if (grammar.get(root)!!.isNotEmpty()) {
            val _productions = grammar.get(root)
            for (product in productions) {
                if (!_productions!!.contains(product)) {
                    _productions.add(product)
                }
            }
        } else {
            grammar.put(root, productions)
        }
    }

}