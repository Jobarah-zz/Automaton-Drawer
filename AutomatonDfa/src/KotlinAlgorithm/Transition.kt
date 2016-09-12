package KotlinAlgorithm;

import java.io.Serializable

/**
 * Created by Jobarah on 7/25/2016.
 */

open class Transition(symbol: String, origin:String, destiny:String): Serializable {
	var _symbol = symbol
	var _origin = origin
	var _destiny = destiny
}