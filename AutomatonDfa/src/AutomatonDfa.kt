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

fun main(args: Array<String>) {
    Application.launch(MainApplication::class.java)
}

class MainApplication : Application() {
    override fun start(stage: Stage) {

        val graph = mxGraph()
        var automaton: Automaton = nonDeterministicFiniteAutomaton()
        automaton.alphabet.add('0')
        automaton.alphabet.add('1')
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
        val stateNameTextField = TextField()
        val strToEval = TextField()
        val alphabet = TextField()
        stateNameTextField.setMaxSize(120.0,20.0)
        strToEval.setMaxSize(120.0,20.0)
        alphabet.setMaxSize(120.0,20.0)
        var estadoInicial = ""

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

//                val transitions = graph.getEdges(nodes[statesCombo.items.indexOf(verEdit)],parent,false,true,true)
//                var cont2 = 0
//                while (cont2<misEdges.count()){
//                    (misEdges[cont2] as mxICell).removeFromParent()
//                    cont2++
//                }
//                graph.refresh()

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

//            val _alphabet = alphabet.text
//            var chars = mutableListOf<Char>()
//            for (symbol in _alphabet)
//            {
//                chars.add(symbol)
//            }
//            for(element in chars){
//                if(element!=',')
//                    println(element)
//            }

            if(strToEval.text!=""){
                println(automaton.evaluate(strToEval.text))
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Automaton Evaluation"
                alert.headerText = null
                alert.contentText = "The result of the evaluation is: "+automaton.evaluate(strToEval.text).toString()
//                alert.contentText = "The result of the evaluation is: "+automaton.evaluate(strToEval.text).toString()

                alert.showAndWait()
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
}