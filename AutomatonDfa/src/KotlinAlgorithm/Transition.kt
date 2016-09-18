package KotlinAlgorithm;

import java.io.Serializable

/**
 * Created by Jobarah on 7/25/2016.
 */

open class Transition(): Serializable {
	var _symbol = ""
	var _origin = ""
	var _destiny = ""

	constructor(symbol: String, origin:String, destiny:String) :this(){
		_symbol = symbol
		_origin = origin
		_destiny = destiny
	}
}