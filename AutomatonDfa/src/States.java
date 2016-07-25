import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Created by jenamorado on 7/25/16.
 */
public class States {
    public String name;
    public ArrayList<Transition> transitions;
    boolean isInitialState;
    boolean isAcceptanceState;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInitialState() {
        return isInitialState;
    }

    public void setInitialState(boolean initialState) {
        isInitialState = initialState;
    }

    public boolean isAcceptanceState() {
        return isAcceptanceState;
    }

    public void setAcceptanceState(boolean acceptanceState) {
        isAcceptanceState = acceptanceState;
    }


}
