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
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.FlowPane
import javafx.stage.Stage
import KotlinAlgorithm.*
import javafx.scene.layout.GridPane

fun main(args: Array<String>) {
    Application.launch(MinifyAutomaton::class.java)
}


/**
 * Created by Jobarah on 8/25/2016.
 */
class MinifyAutomaton: Application() {
    override fun start(stage: Stage) {
        val graph = mxGraph()
        val parent = graph.defaultParent

        graph.update {}

        graph.minimumGraphSize = mxRectangle(0.0,0.0,800.0,320.0)

        var automata = dFAMinifier()
        val graphComponent = mxGraphComponent(graph)
        graph.isAllowLoops = true
        graph.isAllowDanglingEdges = false
        graph.isEdgeLabelsMovable = false
        graph.isCellsResizable = false
        mxConstants.DEFAULT_HOTSPOT = 1.0

        val miCombo = ComboBox<String>() //Primer combo box para seleccionar vertices solo texto
        val miLista: MutableList<mxCell> = mutableListOf() //Lista de vertices en si
        var estadoInicial = ""
        val label1 = Label("Vertex name to insert")
        val editVertex = Label("From")
        val trans0 = Label("Trancision")
        val ct0 = ComboBox<String>() //Transicion combo box
        val cTextField = TextField()
        val aceptacion = Label("Is accepted vertexActual")
        val ac = ComboBox<String>() //Accepted vertexActual combo box
        ac.items.add("True")
        ac.items.add("False")
        val borrar = Label("Delete vertex")
        val bc = ComboBox<String>() //Borrar esto combo box
        val ic = ComboBox<String>() //Initial State combo box
        val separador = Label(" // ")
        val separador2 = Label(" // ")
        val separador3 = Label(" // ")
        val separador4 = Label(" // ")
        val separador5 = Label(" // ")
        val separador6 = Label(" // ")
        val separador7 = Label(" // ")
        val aTextField = TextField()
        val aButton = Button("Insertar")
        val bButton = Button("Add transicion")
        val cButton = Button("Delete")
        val dButton = Button("Set como Estado Inicial")
        val eButton = Button("Evaluar")
        val fButton = Button("Minimizar")
        val bTextField = TextField()
        var resultado = Label("")
        var transformado = false;

        aButton.onMouseClicked = EventHandler<MouseEvent> {
            var aceptado = ""
            if(ac.selectionModel.selectedIndex > -1 && aTextField.text != ""){
                aceptado = ac.getSelectionModel().getSelectedItem().toString()
            }

            if(ac.selectionModel.selectedIndex > -1 && aTextField.text != ""){
                if(aceptado == "True"){
                    val vertex = graph.insertVertex(parent, null, aTextField.text, 740.0, 350.0, 50.0, 50.0, "shape=doubleEllipse;fillColor=white")
                    automata.states.add(State(aTextField.text,false,true))
                    miLista.add(vertex as mxCell)
                }else{
                    val vertex = graph.insertVertex(parent, null, aTextField.text, 740.0, 350.0, 50.0, 50.0, "shape=ellipse;fillColor=white")
                    automata.states.add(State(aTextField.text,false,false))
                    miLista.add(vertex as mxCell)
                }
                miCombo.items.add(aTextField.text)
                ct0.items.add(aTextField.text)
                bc.items.add(aTextField.text)
                ic.items.add(aTextField.text)
            }
        }

        bButton.onMouseClicked = EventHandler<MouseEvent> {
            if(miCombo.selectionModel.selectedIndex > -1 && cTextField.text!=""){
                val verEdit = miCombo.getSelectionModel().getSelectedItem().toString()
                val agregarAlfabeto = cTextField.text
                var ct0Opcion = ""
                if(ct0.selectionModel.selectedIndex > -1){
                    ct0Opcion = ct0.getSelectionModel().getSelectedItem().toString()
                }

                val misEdges = graph.getEdges(miLista[miCombo.items.indexOf(verEdit)],parent,false,true,true)
                var cont2 = 0
                while (cont2<misEdges.count()){
                    (misEdges[cont2] as mxICell).removeFromParent()
                    cont2++
                }
                for(transicion in ((automata.getState(verEdit))as State)._transitions){
                    if(transicion._symbol != agregarAlfabeto[0]){
                        graph.insertEdge(parent, null, transicion._symbol, miLista[miCombo.items.indexOf(verEdit)], miLista[ct0.items.indexOf(transicion._destiny)])
                    }else{
                        transicion._symbol = agregarAlfabeto[0]
                    }
                }
                graph.refresh()
                if(!automata.alphabet.contains(agregarAlfabeto[0])){
                    automata.alphabet.add(agregarAlfabeto[0])
                }
                if(ct0.selectionModel.selectedIndex > -1){
                    graph.insertEdge(parent, null, agregarAlfabeto[0], miLista[miCombo.items.indexOf(verEdit)], miLista[ct0.items.indexOf(ct0Opcion)])
                    var state:State? = automata.getState(miLista[miCombo.items.indexOf(verEdit)].value.toString())
                    state!!._transitions.add(Transition(agregarAlfabeto[0],miLista[miCombo.items.indexOf(verEdit)].value.toString(),miLista[ct0.items.indexOf(ct0Opcion)].value.toString()))
                }
            }
        }

        cButton.onMouseClicked = EventHandler<MouseEvent> {
            if(bc.selectionModel.selectedIndex > -1){
                val verDelete = bc.getSelectionModel().getSelectedItem().toString()
                miLista[miCombo.items.indexOf(verDelete)].removeFromParent()
                graph.removeSelectionCell(miLista[miCombo.items.indexOf(verDelete)])
                graph.refresh()
                miLista.removeAt(bc.items.indexOf(verDelete))
                ct0.items.removeAt(bc.items.indexOf(verDelete))
                ic.items.removeAt(bc.items.indexOf(verDelete))
                miCombo.items.removeAt(bc.items.indexOf(verDelete))
                bc.items.removeAt(bc.items.indexOf(verDelete))
            }
        }

        dButton.onMouseClicked = EventHandler<MouseEvent> {
            if(ic.selectionModel.selectedIndex > -1){
                var x = 0.0
                var y = 0.0
                val cont = miLista.count()
                var cont2 = 0
                while(cont2 < cont){
                    graph.resizeCell(miLista[cont2], mxRectangle(x,y,50.0,50.0))
                    x += 50.0
                    if(x == 900.0){
                        x = 0.0
                        y += 50.0
                    }
                    cont2++
                }
                graph.resizeCell(miLista[miCombo.items.indexOf(ic.getSelectionModel().getSelectedItem().toString())], mxRectangle(450.0,(y+100.0),100.0,100.0))
                estadoInicial = ic.getSelectionModel().getSelectedItem().toString()
                var cont3 = 0
                while(automata.states.count() > cont3){
                    if(automata.states[cont3]._name == estadoInicial){
                        automata.states[cont3]._initialState = true
                    }
                    cont3++
                }
                println(estadoInicial)
            }
        }

        eButton.onMouseClicked = EventHandler<MouseEvent> {
            if(bTextField.text!=""){
                if(transformado == true){
                    resultado.text = automata.evaluarMin(bTextField.text).toString()
                }else{
                    resultado.text = automata.evaluate(bTextField.text).toString()
                }
            }
        }

        fButton.onMouseClicked = EventHandler<MouseEvent> {
            graph.removeCells(graph.getChildVertices(graph.getDefaultParent()))
            var automataDibujar = deterministicFiniteAutomaton()
            val mapaVertices: MutableMap<String, mxCell> = mutableMapOf()
            if(transformado == true){
                automataDibujar = automata
            }else{
                automata.minimizar_automata()
                automataDibujar = automata.afd_minimizado
            }
            for(vertex in automataDibujar.states){
                var forma = "shape=ellipse;fillColor=white"
                var tam = 50.0
                if(vertex._isAcceptanceState){
                    forma = "shape=doubleEllipse;fillColor=white"
                }
                if(vertex._initialState){
                    tam = 100.0
                }
                var vertex2 = graph.insertVertex(parent, null, vertex._name, 740.0, 350.0, tam, tam, forma)
                mapaVertices.put(vertex._name, vertex2 as mxCell)
            }
            for(vertex in automataDibujar.states){
                for(transicion in vertex._transitions){
                    graph.insertEdge(parent, null, transicion._symbol, mapaVertices[transicion._origin], mapaVertices[transicion._destiny])
                }
            }
            transformado = !transformado
        }

        val sceneRoot = GridPane()
        sceneRoot.setPrefSize(400.0, 400.0)
        sceneRoot.setVgap(5.0)
        sceneRoot.setHgap(1.0)
        sceneRoot.add(SwingNode().apply {
            content = graphComponent
        }, 0, 0)
        sceneRoot.add(label1, 0, 1)
        aTextField.setMaxSize(120.0,20.0)
        cTextField.setMaxSize(120.0,20.0)
        bTextField.setMaxSize(120.0,20.0)
        sceneRoot.add(aTextField, 0, 2)
        sceneRoot.add(aceptacion, 0, 3)
        sceneRoot.add(ac, 0, 4)
        sceneRoot.add(aButton,0,5)
        sceneRoot.add(editVertex, 0, 6)
        sceneRoot.add(miCombo, 0, 7)
        sceneRoot.add(trans0, 0, 8)
        sceneRoot.add(cTextField, 0, 9)
        sceneRoot.add(ct0, 0, 10)
        sceneRoot.add(bButton, 0, 11)
        sceneRoot.add(borrar, 0, 12)
        sceneRoot.add(bc, 0, 13)
        sceneRoot.add(cButton, 0, 14)
        sceneRoot.add(dButton, 0, 15)
        sceneRoot.add(ic, 0, 16)
        sceneRoot.add(eButton, 0, 17)
        sceneRoot.add(bTextField, 0, 18)
        resultado.setMaxSize(120.0,20.0)
        sceneRoot.add(resultado, 0, 19)
        sceneRoot.add(fButton, 0, 20)
//        val sceneRoot = FlowPane(SwingNode().apply {
//            content = graphComponent
//        },
//                separador4,
//                label1,
//                aTextField,
//                aceptacion,
//                ac,
//                aButton,
//                separador,
//                editVertex,
//                miCombo,
//                trans0,
//                cTextField,
//                ct0,
//                bButton,
//                separador2,
//                borrar,
//                bc,
//                cButton,
//                separador3,
//                dButton,
//                ic,
//                separador5,
//                eButton,
//                bTextField,
//                resultado,
//                separador6 ,
//                fButton
//        )

        stage.scene = Scene(sceneRoot, 900.0, 700.0)

        stage.show()
        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()))
    }

    private fun mxGraph.update(block: () -> Any) {
        model.beginUpdate()
        try {
            block()
        }
        finally {
            model.endUpdate()
        }
    }
}

