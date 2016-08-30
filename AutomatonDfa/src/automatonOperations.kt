import KotlinAlgorithm.*
import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph
import javafx.application.Application
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Paint
import javafx.stage.Stage
import java.awt.Color
import java.awt.Label
import java.util.*

/**
 * Created by Jobarah on 8/27/2016.
 */

fun main(args: Array<String>) {
    Application.launch(automatonOperations::class.java)
}

class automatonOperations: Application() {
    override fun start(stage: Stage) {

            val graph = mxGraph()
            var automaton: nonDeterministicAutomatonEpsilon = nonDeterministicAutomatonEpsilon()
            automaton.alphabet.add('0')
            automaton.alphabet.add('1')
            var NFA: Automaton = nonDeterministicFiniteAutomaton()
            var DFA: deterministicFiniteAutomaton = deterministicFiniteAutomaton()

            val parent = graph.defaultParent

            graph.update {}

            graph.minimumGraphSize = mxRectangle(0.0, 0.0, 636.0, 320.0)

            val graphComponent = mxGraphComponent(graph)

            graph.isAllowLoops = true

            graph.isAllowDanglingEdges = false

            graph.isEdgeLabelsMovable = false

            graph.isCellsResizable = false
        //==============================================================================================================
        val graph2 = mxGraph()

        val parent2 = graph2.defaultParent

        graph2.update {}

        graph2.minimumGraphSize = mxRectangle(0.0, 0.0, 636.0, 320.0)

        val graphComponent2 = mxGraphComponent(graph2)

        graph2.isAllowLoops = true

        graph2.isAllowDanglingEdges = false

        graph2.isEdgeLabelsMovable = false

        graph2.isCellsResizable = false
        //==============================================================================================================
        //==============================================================================================================
        val graph3 = mxGraph()

        val parent3 = graph3.defaultParent

        graph3.update {}

        graph3.minimumGraphSize = mxRectangle(0.0, 0.0, 636.0, 320.0)

        val graphComponent3 = mxGraphComponent(graph3)

        graph3.isAllowLoops = true

        graph3.isAllowDanglingEdges = false

        graph3.isEdgeLabelsMovable = false

        graph3.isCellsResizable = false
        //==============================================================================================================

            mxConstants.DEFAULT_HOTSPOT = 1.0

            val statesCombo = ComboBox<String>()

            val nodes: MutableList<mxCell> = mutableListOf()

            val TransformLabel = javafx.scene.control.Label("Transform")
            val insertLabel = javafx.scene.control.Label("New Vertex name: ")
            val strToEvalLabel = javafx.scene.control.Label("String to Evaluate: ")
            val alphabetLabel = javafx.scene.control.Label("Define Alphabet: ")
            val editVertex = javafx.scene.control.Label("Edit vertex")
            val trans0 = javafx.scene.control.Label("Transition 0")
            val initialState = javafx.scene.control.Label("Is initial state")
            val comboTransition0 = ComboBox<String>()
            val trans1 = javafx.scene.control.Label("Transition 1")
            val comboTransition1 = ComboBox<String>()
            val trans2 = javafx.scene.control.Label("Transition e")
            val comboTransition2 = ComboBox<String>()
            val isAcceptanceState = javafx.scene.control.Label("Is acceptance state")
            val acceptanceCombo = ComboBox<String>()
            acceptanceCombo.items.add("True")
            acceptanceCombo.items.add("False")
            val initialCombo = ComboBox<String>()
            initialCombo.items.add("True")
            initialCombo.items.add("False")
            val deleteLabel = javafx.scene.control.Label("Delete vertex")
            val deleteCombo = ComboBox<String>()
            val separator = Separator()
            val separator2 = Separator()
            val separator3 = Separator()
            val separator4 = Separator()
            val stateNameTextField = TextField()
            val strToEval = TextField()
            val alphabet = TextField()
            stateNameTextField.setMaxSize(120.0, 20.0)
            strToEval.setMaxSize(120.0, 20.0)
            alphabet.setMaxSize(120.0, 20.0)
            var estadoInicial = ""
            val fButton = Button("Convert to DFA")
            var isTransformed = false

            val statesButton = Button("Insert")
            val transitionsButton = Button("Apply Changes")
            val deleteButton = Button("Delete")
            val evalButton = Button("Evaluate Automaton")

        //================================================================================================================
        val statesCombo2 = ComboBox<String>()
        val TransformLabel2 = javafx.scene.control.Label("Transform")
        val insertLabel2 = javafx.scene.control.Label("New Vertex name: ")
        val strToEvalLabel2 = javafx.scene.control.Label("String to Evaluate: ")
        val alphabetLabel2 = javafx.scene.control.Label("Define Alphabet: ")
        val editVertex2 = javafx.scene.control.Label("Edit vertex")
        val trans02 = javafx.scene.control.Label("Transition 0")
        val initialState2 = javafx.scene.control.Label("Is initial state")
        val comboTransition02 = ComboBox<String>()
        val trans12 = javafx.scene.control.Label("Transition 1")
        val comboTransition12 = ComboBox<String>()
        val trans22 = javafx.scene.control.Label("Transition e")
        val comboTransition22 = ComboBox<String>()
        val isAcceptanceState2 = javafx.scene.control.Label("Is acceptance state")
        val acceptanceCombo2 = ComboBox<String>()
        acceptanceCombo2.items.add("True")
        acceptanceCombo2.items.add("False")
        val initialCombo2 = ComboBox<String>()
        initialCombo2.items.add("True")
        initialCombo2.items.add("False")
        val deleteLabel2 = javafx.scene.control.Label("Delete vertex")
        val deleteCombo2 = ComboBox<String>()
        val separator1 = Separator()
        val separator22 = Separator()
        val separator32 = Separator()
        val separator42 = Separator()
        val stateNameTextField2 = TextField()
        val strToEval2 = TextField()
        val alphabet2 = TextField()
        stateNameTextField2.setMaxSize(120.0, 20.0)
        strToEval2.setMaxSize(120.0, 20.0)
        alphabet2.setMaxSize(120.0, 20.0)
        var estadoInicial2 = ""
        val fButton2 = Button("Convert to DFA")
        var isTransformed2 = false

        val statesButton2 = Button("Insert")
        val transitionsButton2 = Button("Apply Changes")
        val deleteButton2 = Button("Delete")
        val evalButton2 = Button("Evaluate Automaton")
        //================================================================================================================
        val operationCombo = ComboBox<String>()
        operationCombo.items.add("Union")
        operationCombo.items.add("Intersection")
        operationCombo.items.add("Subtraction")
        operationCombo.items.add("Complement")
        val operationEval = Button("Evaluate Operation")

        //================================================================================================================
            statesButton.onMouseClicked = EventHandler<MouseEvent> {

                val aceptado = acceptanceCombo.getSelectionModel().getSelectedItem().toString()
                val isInitialState = initialCombo.getSelectionModel().getSelectedItem().toString()
                var sizeX = 0.0
                var sizeY = 0.0
                var acceptance: Boolean = false
                var initial: Boolean = false
                if (acceptanceCombo.selectionModel.selectedIndex >= 0 && stateNameTextField.text != "") {
                    var style = ""
                    if (aceptado == "True") {
                        acceptance = true
                        style = "shape=doubleEllipse;fillColor=white"
                    } else {
                        style = "shape=ellipse;fillColor=white"
                    }
                    if (isInitialState == "True") {
                        initial = true
                        sizeX = 100.00
                        sizeY = 100.00
                    } else {
                        sizeX = 50.00
                        sizeY = 50.00
                    }
                    automaton.states.add(State(stateNameTextField.text, initial, acceptance))
                    val vertex = graph.insertVertex(parent, null, stateNameTextField.text, 150.0, 150.0, sizeX, sizeY, style)
                    statesCombo.items.add(stateNameTextField.text)
                    comboTransition0.items.add(stateNameTextField.text)
                    comboTransition1.items.add(stateNameTextField.text)
                    comboTransition2.items.add(stateNameTextField.text)
                    deleteCombo.items.add(stateNameTextField.text)
                    nodes.add(vertex as mxCell)
                }
                graph.refresh()

            }

            transitionsButton.onMouseClicked = EventHandler<MouseEvent> {
                val verEdit = statesCombo.getSelectionModel().getSelectedItem().toString()

                if (statesCombo.selectionModel.selectedIndex >= 0) {
                    var ct0Opcion = ""
                    if (comboTransition0.selectionModel.selectedIndex >= 0) {
                        ct0Opcion = comboTransition0.getSelectionModel().getSelectedItem().toString()
                    }

                    var ct1Opcion = ""
                    if (comboTransition1.selectionModel.selectedIndex >= 0) {
                        ct1Opcion = comboTransition1.getSelectionModel().getSelectedItem().toString()
                    }

                    var ct2Opcion = ""
                    if (comboTransition2.selectionModel.selectedIndex >= 0) {
                        ct2Opcion = comboTransition2.getSelectionModel().getSelectedItem().toString()
                    }

                    if (comboTransition0.selectionModel.selectedIndex >= 0) {
                        graph.insertEdge(parent, null, "0", nodes[statesCombo.items.indexOf(verEdit)], nodes[comboTransition0.items.indexOf(ct0Opcion)])
                        var state: State? = automaton.getState(nodes[statesCombo.items.indexOf(verEdit)].value.toString())
                        state!!._transitions.add(Transition('0', nodes[statesCombo.items.indexOf(verEdit)].value.toString(), nodes[comboTransition0.items.indexOf(ct0Opcion)].value.toString()))
                    }

                    if (comboTransition1.selectionModel.selectedIndex >= 0) {
                        graph.insertEdge(parent, null, "1", nodes[statesCombo.items.indexOf(verEdit)], nodes[comboTransition1.items.indexOf(ct1Opcion)])
                        var state: State? = automaton.getState(nodes[statesCombo.items.indexOf(verEdit)].value.toString())
                        state!!._transitions.add(Transition('1', nodes[statesCombo.items.indexOf(verEdit)].value.toString(), nodes[comboTransition1.items.indexOf(ct1Opcion)].value.toString()))
                    }

                    if (comboTransition2.selectionModel.selectedIndex >= 0) {
                        graph.insertEdge(parent, null, "e", nodes[statesCombo.items.indexOf(verEdit)], nodes[comboTransition2.items.indexOf(ct2Opcion)])
                        var state: State? = automaton.getState(nodes[statesCombo.items.indexOf(verEdit)].value.toString())
                        state!!._transitions.add(Transition('e', nodes[statesCombo.items.indexOf(verEdit)].value.toString(), nodes[comboTransition2.items.indexOf(ct2Opcion)].value.toString()))
                    }
                }
                graph.refresh()
                comboTransition0.selectionModel.clearSelection()
                comboTransition1.selectionModel.clearSelection()
                comboTransition2.selectionModel.clearSelection()
            }

            deleteButton.onMouseClicked = EventHandler<MouseEvent> {
                if (deleteCombo.selectionModel.selectedIndex > -1) {
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
                var nfa = automaton.convertToNFA()
                println(nfa.evaluate(strToEval.text))
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Automaton Evaluation"
                alert.headerText = null
                alert.contentText = "The result of the evaluation is: " + nfa.evaluate(strToEval.text).toString()
                alert.showAndWait()
            }


            fButton.onMouseClicked = EventHandler<MouseEvent> {
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()))
                if (isTransformed) {
                    //reconstruir AFN
                    isTransformed = false
                } else {
                    //build dfa
                    NFA = automaton.convertToNFA()
                    DFA.states.clear()
                    val dfaList: MutableList<mxCell> = mutableListOf()
                    val listaDeAceptados: MutableList<String> = mutableListOf()
                    for (t in NFA.states.indices) {
                        if (NFA.states[t]._isAcceptanceState) {
                            listaDeAceptados.add(NFA.states[t]._name)
                        }
                    }

                    var nuevosVertex: MutableList<State> = mutableListOf()
                    val initVertex = NFA.getInitialState() as State
                    nuevosVertex.add(State(initVertex._name, true, initVertex._isAcceptanceState))

                    var acc = initVertex._isAcceptanceState
                    var style = "shape=ellipse;fillColor=white"
                    if (acc)
                        style = "shape=doubleEllipse;fillColor=white"
                    val vertex2 = graph.insertVertex(parent, null, initVertex._name, 740.0, 350.0, 100.0, 100.0, style)
                    dfaList.add(vertex2 as mxCell)
                    var contadorIndices = 0
                    val mapaIndices: MutableMap<String, Int> = mutableMapOf()
                    mapaIndices.put(initVertex._name, contadorIndices)
                    contadorIndices++

                    DFA.alphabet = NFA.alphabet
                    DFA.states.add(State(initVertex._name, true, initVertex._isAcceptanceState))
                    while (nuevosVertex.count() > 0) {
                        val vertexActual = nuevosVertex[0]
                        val mapaNombres: MutableMap<Char, String> = mutableMapOf()

                        for (p in DFA.alphabet.indices) {
                            val separados = vertexActual._name.split(",")
                            for (w in separados.indices) {
                                val subVertexActual = NFA.getState(separados[w]) as State
                                for (o in subVertexActual._transitions.indices) {
                                    if (subVertexActual._transitions[o]._symbol == DFA.alphabet[p]) {
                                        if (mapaNombres.containsKey(DFA.alphabet[p])) {
                                            val miStr = (mapaNombres.get(DFA.alphabet[p])) as String
                                            val cortado = miStr.split(",")
                                            if (!cortado.contains(subVertexActual._transitions[o]._destiny))
                                                mapaNombres.set(DFA.alphabet[p], miStr + "," + subVertexActual._transitions[o]._destiny)
                                        } else {
                                            mapaNombres.put(DFA.alphabet[p], subVertexActual._transitions[o]._destiny)
                                        }
                                    }
                                }
                            }
                        }
                        val contador1 = mapaNombres.count()
                        var contador2 = 0
                        var misNombres = mapaNombres.entries
                        while (contador1 > contador2) {
                            val miStr = misNombres.elementAt(contador2).value
                            val miStrSeparado = miStr.split(",")
                            println(miStrSeparado)
                            for (m in miStrSeparado.indices) {
                                println(miStrSeparado[m])
                            }
                            val nombreV = yaExisteString(DFA, miStr)
                            if (!yaExisteBoolean(DFA, miStr)) {
                                val isAccept = esVertexAceptable(listaDeAceptados, nombreV)
                                var accept = "shape=ellipse;fillColor=white"
                                if (isAccept)
                                    accept = "shape=doubleEllipse;fillColor=white"
                                val state = graph.insertVertex(parent, null, nombreV, 740.0, 350.0, 50.0, 50.0, accept)
                                dfaList.add(state as mxCell)
                                mapaIndices.put(nombreV, contadorIndices)
                                contadorIndices++
                                DFA.states.add(State(nombreV, false, isAccept))
                                nuevosVertex.add(State(nombreV, false, isAccept))
                            }
                            graph.insertEdge(parent, null, misNombres.elementAt(contador2).key, dfaList.elementAt(mapaIndices.get(vertexActual._name) as Int), dfaList.elementAt(mapaIndices.get(nombreV) as Int)) //Si es uno que ya existe no lo agrega
                            var state: State? = DFA.getState(vertexActual._name)
                            state!!._transitions.add(Transition(misNombres.elementAt(contador2).key, vertexActual._name, nombreV))
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

            sceneRoot.add(SwingNode().apply {
                content = graphComponent2
            }, 1, 0)

            sceneRoot.add(SwingNode().apply {
                content = graphComponent3
            }, 2, 0)

            sceneRoot.add(insertLabel, 0, 1)
            sceneRoot.add(stateNameTextField, 0, 2)
            sceneRoot.add(isAcceptanceState, 0, 3)
            sceneRoot.add(acceptanceCombo, 0, 4)
            sceneRoot.add(initialState, 0, 5)
            sceneRoot.add(initialCombo, 0, 6)
            sceneRoot.add(statesButton, 0, 7)
            sceneRoot.add(separator, 0, 8)
            sceneRoot.add(editVertex, 0, 9)
            sceneRoot.add(statesCombo, 0, 10)
            sceneRoot.add(trans0, 0, 11)
            sceneRoot.add(comboTransition0, 0, 12)
            sceneRoot.add(trans1, 0, 13)
            sceneRoot.add(comboTransition1, 0, 14)
            sceneRoot.add(trans2, 0, 15)
            sceneRoot.add(comboTransition2, 0, 16)
            sceneRoot.add(transitionsButton, 0, 17)
            sceneRoot.add(separator2, 0, 18)
            sceneRoot.add(deleteLabel, 0, 19)
            sceneRoot.add(deleteCombo, 0, 20)
            sceneRoot.add(deleteButton, 0, 21)
            sceneRoot.add(separator3, 0, 22)

            sceneRoot.add(insertLabel2, 1, 1)
            sceneRoot.add(stateNameTextField2, 1, 2)
            sceneRoot.add(isAcceptanceState2, 1, 3)
            sceneRoot.add(acceptanceCombo2, 1, 4)
            sceneRoot.add(initialState2, 1, 5)
            sceneRoot.add(initialCombo2, 1, 6)
            sceneRoot.add(statesButton2, 1, 7)
            sceneRoot.add(separator1, 1, 8)
            sceneRoot.add(editVertex2, 1, 9)
            sceneRoot.add(statesCombo2, 1, 10)
            sceneRoot.add(trans02, 1, 11)
            sceneRoot.add(comboTransition02, 1, 12)
            sceneRoot.add(trans12, 1, 13)
            sceneRoot.add(comboTransition12, 1, 14)
            sceneRoot.add(trans22, 1, 15)
            sceneRoot.add(comboTransition22, 1, 16)
            sceneRoot.add(transitionsButton2, 1, 17)
            sceneRoot.add(separator22, 1, 18)
            sceneRoot.add(deleteLabel2, 1, 19)
            sceneRoot.add(deleteCombo2, 1, 20)
            sceneRoot.add(deleteButton2, 1, 21)
            sceneRoot.add(separator32, 1, 22)
//        sceneRoot.add(alphabetLabel,0,21)
//        sceneRoot.add(alphabet,0,22)
            sceneRoot.add(strToEvalLabel, 0, 23)
            sceneRoot.add(strToEval, 0, 24)
            sceneRoot.add(evalButton, 0, 25)
            sceneRoot.add(separator4, 0, 26)
            sceneRoot.add(TransformLabel, 0, 27)
            sceneRoot.add(fButton, 0, 28)

            sceneRoot.add(strToEvalLabel2, 1, 23)
            sceneRoot.add(strToEval2, 1, 24)
            sceneRoot.add(evalButton2, 1, 25)
            sceneRoot.add(separator42, 1, 26)
            sceneRoot.add(TransformLabel2, 1, 27)
            sceneRoot.add(fButton2, 1, 28)

            sceneRoot.add(operationCombo, 2, 1)
            sceneRoot.add(operationEval, 2, 2)

            stage.scene = Scene(sceneRoot, 825.0, 1000.0)
            stage.isMaximized = true

            stage.show()
        }


        private fun mxGraph.update(block: () -> Any) {
            model.beginUpdate()
            try {
                block()
            } finally {
                model.endUpdate()
            }
        }

        open fun yaExisteString(miAFD: deterministicFiniteAutomaton, miStr: String): String {
            for (i in miAFD.states.indices) {
                val strCompare = miAFD.states[i]._name
                val miArr = strCompare.split(",")
                val miArr2 = miStr.split(",")
                if (miArr.count() == miArr2.count()) {
                    val set1 = HashSet<String>()
                    set1.addAll(miArr)
                    val set2 = HashSet<String>()
                    set2.addAll(miArr2)
                    if (set1.equals(set2)) {
                        return strCompare
                    }
                }
            }
            return miStr
        }

        open fun yaExisteBoolean(dfa: deterministicFiniteAutomaton, miStr: String): Boolean {
            for (i in dfa.states.indices) {
                val strCompare = dfa.states[i]._name
                val miArr = strCompare.split(",")
                val miArr2 = miStr.split(",")
                if (miArr.count() == miArr2.count()) {
                    val set1 = HashSet<String>()
                    set1.addAll(miArr)
                    val set2 = HashSet<String>()
                    set2.addAll(miArr2)
                    if (set1.equals(set2)) {
                        return true
                    }
                }
            }
            return false
        }

        open fun esVertexAceptable(acceptanceStates: kotlin.collections.MutableList<String>, str: String): Boolean {
            val toCmp = str.split(",")
            for (i in acceptanceStates.indices) {
                if (toCmp.contains(acceptanceStates[i])) {
                    return true
                }
            }
            return false
        }

        open fun drawAutomata(graph: mxGraph, dibujar: deterministicFiniteAutomaton) {
            graph.removeCells(graph.getChildVertices(graph.getDefaultParent()))
            val mapaVertices: MutableMap<String, mxCell> = mutableMapOf()
            for (vertex in dibujar.states) {
                var forma = "shape=ellipse;fillColor=white"
                var tam = 50.0
                if (vertex._isAcceptanceState) {
                    forma = "shape=doubleEllipse;fillColor=white"
                }
                if (vertex._initialState) {
                    tam = 100.0
                }
                var vertex2 = graph.insertVertex(graph.getDefaultParent(), null, vertex._name, 740.0, 350.0, tam, tam, forma)
                mapaVertices.put(vertex._name, vertex2 as mxCell)
            }
            for (vertex in dibujar.states) {
                for (transicion in vertex._transitions) {
                    graph.insertEdge(graph.getDefaultParent(), null, transicion._symbol, mapaVertices[transicion._origin], mapaVertices[transicion._destiny])
                }
            }
            graph.refresh()
        }
    }
