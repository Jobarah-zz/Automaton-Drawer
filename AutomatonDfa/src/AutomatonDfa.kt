import KotlinAlgorithm.*
import com.mxgraph.model.mxCell
import com.mxgraph.model.mxICell
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph

import javafx.application.Application
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.util.*

fun main(args: Array<String>) {
    Application.launch(MainApplication::class.java)
}

class MainApplication : Application() {
    override fun start(stage: Stage) {

        val graph = mxGraph()
        var automaton: Automaton = nonDeterministicFiniteAutomaton()
        automaton.alphabet.add('0')
        automaton.alphabet.add('1')

        var automata2:deterministicFiniteAutomaton = deterministicFiniteAutomaton()

        val parent = graph.defaultParent

        graph.update {}

        graph.minimumGraphSize = mxRectangle(0.0,0.0,800.0,320.0)

        val graphComponent = mxGraphComponent(graph)

        graph.isAllowLoops = true

        graph.isAllowDanglingEdges = false

        graph.isEdgeLabelsMovable = false

        graph.isCellsResizable = false

        mxConstants.DEFAULT_HOTSPOT = 1.0

        val statesCombo = ComboBox<String>()

        val nodes: MutableList<mxCell> = mutableListOf()

        val TransformLabel = Label("Transform")
        val insertLabel = Label("New Vertex name: ")
        val strToEvalLabel = Label("String to Evaluate: ")
        val alphabetLabel = Label("Define Alphabet: ")
        val editVertex = Label("Edit vertex")
        val trans0 = Label("Transition 0")
        val initialState = Label("Is initial state")
        val comboTransition0 = ComboBox<String>()
        val trans1 = Label("Transition 1")
        val comboTransition1 = ComboBox<String>()
        val isAcceptanceState = Label("Is acceptance state")
        val acceptanceCombo = ComboBox<String>()
        acceptanceCombo.items.add("True")
        acceptanceCombo.items.add("False")
        val initialCombo = ComboBox<String>()
        initialCombo.items.add("True")
        initialCombo.items.add("False")
        val deleteLabel = Label("Delete vertex")
        val deleteCombo = ComboBox<String>()
        val separator = Separator()
        val separator2 = Separator()
        val separator3 = Separator()
        val separator4 = Separator()
        val stateNameTextField = TextField()
        val strToEval = TextField()
        val alphabet = TextField()
        stateNameTextField.setMaxSize(120.0,20.0)
        strToEval.setMaxSize(120.0,20.0)
        alphabet.setMaxSize(120.0,20.0)
        var estadoInicial = ""
        val fButton = Button("From DFA to NFA")
        var isTransformed = false

        val statesButton = Button("Insert")
        val transitionsButton = Button("Apply Changes")
        val deleteButton = Button("Delete")
        val evalButton = Button("Evaluate Automaton")
        statesButton.onMouseClicked = EventHandler<MouseEvent> {

            val aceptado = acceptanceCombo.getSelectionModel().getSelectedItem().toString()
            val isInitialState = initialCombo.getSelectionModel().getSelectedItem().toString()
            var sizeX=0.0
            var sizeY=0.0
            var acceptance:Boolean =false
            var initial:Boolean = false
            if(acceptanceCombo.selectionModel.selectedIndex>=0 && stateNameTextField.text!=""){
                var style=""
                if(aceptado == "True"){
                    acceptance = true
                    style = "shape=doubleEllipse;fillColor=white"
                }else{
                    style = "shape=ellipse;fillColor=white"
                }
                if(isInitialState == "True"){
                    initial = true
                    sizeX = 100.00
                    sizeY = 100.00
                }else{
                    sizeX = 50.00
                    sizeY = 50.00
                }
                automaton.states.add(State(stateNameTextField.text,initial,acceptance))
                val vertex = graph.insertVertex(parent, null, stateNameTextField.text, 150.0, 150.0, sizeX, sizeY,style)
                statesCombo.items.add(stateNameTextField.text)
                comboTransition0.items.add(stateNameTextField.text)
                comboTransition1.items.add(stateNameTextField.text)
                deleteCombo.items.add(stateNameTextField.text)
                nodes.add(vertex as mxCell)
            }
            graph.refresh()

        }

        transitionsButton.onMouseClicked = EventHandler<MouseEvent> {
            val verEdit = statesCombo.getSelectionModel().getSelectedItem().toString()

            if(statesCombo.selectionModel.selectedIndex >=0){
                var ct0Opcion = ""
                if(comboTransition0.selectionModel.selectedIndex>=0){
                    ct0Opcion = comboTransition0.getSelectionModel().getSelectedItem().toString()
                }

                var ct1Opcion = ""
                if(comboTransition1.selectionModel.selectedIndex >=0){
                    ct1Opcion = comboTransition1.getSelectionModel().getSelectedItem().toString()
                }

                if(comboTransition0.selectionModel.selectedIndex >=0){
                    graph.insertEdge(parent, null, "0", nodes[statesCombo.items.indexOf(verEdit)], nodes[comboTransition0.items.indexOf(ct0Opcion)])
                    var state: State? = automaton.getState(nodes[statesCombo.items.indexOf(verEdit)].value.toString())
                    state!!._transitions.add(Transition('0',nodes[statesCombo.items.indexOf(verEdit)].value.toString(),nodes[comboTransition0.items.indexOf(ct0Opcion)].value.toString()))
                }

                if(comboTransition1.selectionModel.selectedIndex >=0){
                    graph.insertEdge(parent, null, "1", nodes[statesCombo.items.indexOf(verEdit)], nodes[comboTransition1.items.indexOf(ct1Opcion)])
                    var state: State? = automaton.getState(nodes[statesCombo.items.indexOf(verEdit)].value.toString())
                    state!!._transitions.add(Transition('1',nodes[statesCombo.items.indexOf(verEdit)].value.toString(),nodes[comboTransition0.items.indexOf(ct1Opcion)].value.toString()))
                }
            }
            graph.refresh()
            comboTransition0.selectionModel.clearSelection()
            comboTransition1.selectionModel.clearSelection()
        }

        deleteButton.onMouseClicked = EventHandler<MouseEvent> {
            if(deleteCombo.selectionModel.selectedIndex > -1){
                val verDelete = deleteCombo.getSelectionModel().getSelectedItem().toString()
                nodes[statesCombo.items.indexOf(verDelete)].removeFromParent()
                graph.removeSelectionCell(nodes[statesCombo.items.indexOf(verDelete)])
                graph.refresh()
                nodes.removeAt(deleteCombo.items.indexOf(verDelete))
                comboTransition0.items.removeAt(deleteCombo.items.indexOf(verDelete))
                comboTransition1.items.removeAt(deleteCombo.items.indexOf(verDelete))
                statesCombo.items.removeAt(deleteCombo.items.indexOf(verDelete))
                deleteCombo.items.removeAt(deleteCombo.items.indexOf(verDelete))
                graph.refresh()
            }
        }
        evalButton.onMouseClicked = EventHandler<MouseEvent> {

            if(strToEval.text!=""){
                println(automaton.evaluate(strToEval.text))
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Automaton Evaluation"
                alert.headerText = null
                alert.contentText = "The result of the evaluation is: "+automaton.evaluate(strToEval.text).toString()
                alert.showAndWait()
            }
        }


        fButton.onMouseClicked = EventHandler<MouseEvent> {
            graph.removeCells(graph.getChildVertices(graph.getDefaultParent()))
            if(isTransformed){
                //reconstruir AFN
                isTransformed = false
            }else{
                //build dfa
                automata2.states.clear()
                val dfaList: MutableList<mxCell> = mutableListOf()
                val listaDeAceptados: MutableList<String> = mutableListOf()
                for (t in automaton.states.indices){
                    if(automaton.states[t]._isAcceptanceState){
                        listaDeAceptados.add(automaton.states[t]._name)
                    }
                }

                var nuevosVertex: MutableList<State> = mutableListOf()
                val initVertex = automaton.getInitialState() as State
                nuevosVertex.add(State(initVertex._name,true,initVertex._isAcceptanceState))

                var acc = initVertex._isAcceptanceState
                var style = "shape=ellipse;fillColor=white"
                if(acc)
                    style = "shape=doubleEllipse;fillColor=white"
                val vertex2 = graph.insertVertex(parent, null, initVertex._name, 740.0, 350.0, 100.0, 100.0, style)
                dfaList.add(vertex2 as mxCell)
                var contadorIndices = 0
                val mapaIndices: MutableMap<String,Int> = mutableMapOf()
                mapaIndices.put(initVertex._name,contadorIndices)
                contadorIndices++

                automata2.alphabet = automaton.alphabet
                automata2.states.add(State(initVertex._name,true,initVertex._isAcceptanceState))
                while(nuevosVertex.count() > 0){
                    val vertexActual = nuevosVertex[0]
                    val mapaNombres: MutableMap<Char,String> = mutableMapOf()

                    for (p in automata2.alphabet.indices){
                        val separados = vertexActual._name.split(",")
                        for (w in separados.indices){
                            val subVertexActual = automaton.getState(separados[w]) as State
                            for (o in subVertexActual._transitions.indices){
                                if(subVertexActual._transitions[o]._symbol == automata2.alphabet[p]){
                                    if(mapaNombres.containsKey(automata2.alphabet[p])){
                                        val miStr = (mapaNombres.get(automata2.alphabet[p])) as String
                                        val cortado = miStr.split(",")
                                        if(!cortado.contains(subVertexActual._transitions[o]._destiny))
                                            mapaNombres.set(automata2.alphabet[p],miStr + "," + subVertexActual._transitions[o]._destiny)
                                    }else{
                                        mapaNombres.put(automata2.alphabet[p], subVertexActual._transitions[o]._destiny)
                                    }
                                }
                            }
                        }
                    }
                    val contador1 = mapaNombres.count()
                    var contador2 = 0
                    var misNombres = mapaNombres.entries
                    while(contador1>contador2){
                        val miStr = misNombres.elementAt(contador2).value
                        val miStrSeparado = miStr.split(",")
                        println(miStrSeparado)
                        for (m in miStrSeparado.indices){
                            println(miStrSeparado[m])
                        }
                        val nombreV = yaExisteString(automata2,miStr)
                        if(!yaExisteBoolean(automata2, miStr)){
                            val isAccept = esVertexAceptable(listaDeAceptados,nombreV)
                            var accept = "shape=ellipse;fillColor=white"
                            if(isAccept)
                                accept = "shape=doubleEllipse;fillColor=white"
                            val state = graph.insertVertex(parent, null, nombreV, 740.0, 350.0, 50.0, 50.0, accept)
                            dfaList.add(state as mxCell)
                            mapaIndices.put(nombreV,contadorIndices)
                            contadorIndices++
                            automata2.states.add(State(nombreV,false,isAccept))
                            nuevosVertex.add(State(nombreV,false,isAccept))
                        }
                        graph.insertEdge(parent, null, misNombres.elementAt(contador2).key, dfaList.elementAt(mapaIndices.get(vertexActual._name) as Int), dfaList.elementAt(mapaIndices.get(nombreV) as Int)) //Si es uno que ya existe no lo agrega
                        var state:KotlinAlgorithm.State? = automata2.getState(vertexActual._name)
                        state!!._transitions.add(Transition(misNombres.elementAt(contador2).key,vertexActual._name,nombreV))
                        contador2++
                    }

                    println(nuevosVertex.count())
                    nuevosVertex.removeAt(0)
                }
                graph.refresh()
                isTransformed = true
            }
        }

        val sceneRoot = GridPane()
        sceneRoot.setPrefSize(400.0, 400.0)
        sceneRoot.setVgap(5.0)
        sceneRoot.setHgap(1.0)
        sceneRoot.add(SwingNode().apply {
            content = graphComponent
        }, 0, 0)
        sceneRoot.add(insertLabel, 0, 1)
        sceneRoot.add(stateNameTextField, 0, 2)
        sceneRoot.add(isAcceptanceState, 0, 3)
        sceneRoot.add(acceptanceCombo, 0, 4)
        sceneRoot.add(initialState,0,5)
        sceneRoot.add(initialCombo, 0, 6)
        sceneRoot.add(statesButton, 0, 7)
        sceneRoot.add(separator, 0, 8)
        sceneRoot.add(editVertex, 0, 9)
        sceneRoot.add(statesCombo, 0, 10)
        sceneRoot.add(trans0, 0, 11)
        sceneRoot.add(comboTransition0, 0, 12)
        sceneRoot.add(trans1, 0, 13)
        sceneRoot.add(comboTransition1, 0, 14)
        sceneRoot.add(transitionsButton, 0, 15)
        sceneRoot.add(separator2, 0, 16)
        sceneRoot.add(deleteLabel, 0, 17)
        sceneRoot.add(deleteCombo, 0, 18)
        sceneRoot.add(deleteButton, 0, 19)
        sceneRoot.add(separator3, 0, 20)
//        sceneRoot.add(alphabetLabel,0,21)
//        sceneRoot.add(alphabet,0,22)
        sceneRoot.add(strToEvalLabel,0,21)
        sceneRoot.add(strToEval,0,22)
        sceneRoot.add(evalButton,0,23)
        sceneRoot.add(separator4,0,24)
        sceneRoot.add(TransformLabel,0,25)
        sceneRoot.add(fButton,0,26)

        stage.scene = Scene(sceneRoot, 825.0, 1000.0)

        stage.show()
    }



    private fun  mxGraph.update(block: () -> Any) {
        model.beginUpdate()
        try {
            block()
        }
        finally {
            model.endUpdate()
        }
    }
    open fun yaExisteString(miAFD:deterministicFiniteAutomaton, miStr:String):String{
        for (i in miAFD.states.indices){
            val strCompare = miAFD.states[i]._name
            val miArr = strCompare.split(",")
            val miArr2 = miStr.split(",")
            if(miArr.count() == miArr2.count()){
                val set1 = HashSet<String>()
                set1.addAll(miArr)
                val set2 = HashSet<String>()
                set2.addAll(miArr2)
                if(set1.equals(set2)){
                    return strCompare
                }
            }
        }
        return miStr
    }

    open fun yaExisteBoolean(dfa:deterministicFiniteAutomaton, miStr:String):Boolean{
        for (i in dfa.states.indices){
            val strCompare = dfa.states[i]._name
            val miArr = strCompare.split(",")
            val miArr2 = miStr.split(",")
            if(miArr.count() == miArr2.count()){
                val set1 = HashSet<String>()
                set1.addAll(miArr)
                val set2 = HashSet<String>()
                set2.addAll(miArr2)
                if(set1.equals(set2)){
                    return true
                }
            }
        }
        return false
    }

    open fun esVertexAceptable(acceptanceStates:kotlin.collections.MutableList<String>, str:String): Boolean{
        val toCmp = str.split(",")
        for (i in acceptanceStates.indices){
            if(toCmp.contains(acceptanceStates[i])){
                return true
            }
        }
        return false
    }
}
