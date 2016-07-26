import java.security.PublicKey
import java.util.ArrayList

/**
 * Created by jenamorado on 7/25/16.
 */
class States {
    var name: String? = null
    var transitions: ArrayList<Transition>? = null
    var isInitialState: Boolean = false
    var isAcceptanceState: Boolean = false
}
