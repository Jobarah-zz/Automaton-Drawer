// If this imports do not work, then you didn't add the library correctly
import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph

//Everything should come from JavaFX, otherwise you've done something wrong
import javafx.application.Application
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.stage.Stage


// This is what makes this file the starting point of the program
fun main(args: Array<String>) {
    // The only thing it does is to launch our JavaFX application defined below
    Application.launch(MainApplication::class.java)
}

class MainApplication : Application() {
    override fun start(stage: Stage) {
        //Create a simple Graph Object with two vertices and one edge
        val graph = mxGraph()
        val parent = graph.defaultParent

        // This is a call to an extension function defined below
        // In other languages this would have looked like this: graph.update( () -> doSomething() )
        // In JavaScript, it would have looked like this: graph.update( function(){ //code; //moreCode; } )
        graph.update {
            // Creates a vertex
            // Gives it a parent, position, dimensions and style
//            val vertexOne = graph.insertVertex(parent, null, "q0", 20.0, 20.0, 50.0, 50.0, "shape=ellipse;fillColor=white")
//
//            // Creates another vertex. But this one looks like an accepted state
//            val vertexTwo = graph.insertVertex(parent, null, "q1", 740.0, 350.0, 50.0, 50.0, "shape=doubleEllipse;fillColor=white")
//
//            // Creates an edge between the two vertices
//            graph.insertEdge(parent, null, "1", vertexOne, vertexTwo)
//
//            // Creates an edge from one vertex to itself to demonstrate loops
//            graph.insertEdge(parent, null, "0", vertexTwo, vertexTwo, "rounded=true;arcSize=12")

        }

        graph.minimumGraphSize = mxRectangle(0.0,0.0,800.0,320.0)

        // Creates the embeddable graph swing component
        val graphComponent = mxGraphComponent(graph)

        //Allows vertices to have edges from them to themselves
        graph.isAllowLoops = true

        //Prevents edges from pointing to nothing
        graph.isAllowDanglingEdges = false

        //Prevent edge labels from being dragged somewhere absurd
        graph.isEdgeLabelsMovable = false

        //Allows the vertices to receive edges when they're touched anywhere and not just the center
        mxConstants.DEFAULT_HOTSPOT = 1.0

        val miCombo = ComboBox<String>()

        val nodes: MutableList<mxCell> = mutableListOf()

        val label1 = Label("New Vertex name: ")
        val editVertex = Label("Edit vertex")
        val trans0 = Label("Transition 0")
        val ct0 = ComboBox<String>()
        val trans1 = Label("Transition 1")
        val ct1 = ComboBox<String>()
        val isAcceptanceState = Label("Is accepted state")
        val ac = ComboBox<String>()
        ac.items.add("True")
        ac.items.add("False")
        val borrar = Label("Delete vertex")
        val bc = ComboBox<String>()
        val separator = Separator()
        val separator2 = Separator()
        val separador3 = Label(" // ")
        val separador4 = Label(" // ")
        //Creates a TextField
        val aTextField = TextField()
        aTextField.setMaxSize(120.0,20.0)

        // Creates a button with some text
        val aButton = Button("Insertar")
        val bButton = Button("Apply Changes")
        val cButton = Button("Delete")

        // Creates an event handler for the button
        aButton.onMouseClicked = EventHandler<MouseEvent> {

            val aceptado = ac.getSelectionModel().getSelectedItem().toString()
            if(ac.selectionModel.selectedIndex>=0 && aTextField.text!=""){
                var style=""
                if(aceptado == "True"){
                    style = "shape=doubleEllipse;fillColor=white"
                }else{
                    style = "shape=ellipse;fillColor=white"
                }
                val vertex = graph.insertVertex(parent, null, aTextField.text, 150.0, 150.0, 50.0, 50.0,style)
                miCombo.items.add(aTextField.text)
                ct0.items.add(aTextField.text)
                ct1.items.add(aTextField.text)
                bc.items.add(aTextField.text)
                nodes.add(vertex as mxCell)
            }
            //val str = miCombo.getSelectionModel().getSelectedItem().toString()
        }

        bButton.onMouseClicked = EventHandler<MouseEvent> {
            val verEdit = miCombo.getSelectionModel().getSelectedItem().toString()

            if(miCombo.selectionModel.selectedIndex >=0){
                var ct0Opcion = ""
                if(ct0.selectionModel.selectedIndex>=0){
                    ct0Opcion = ct0.getSelectionModel().getSelectedItem().toString()
                }

                var ct1Opcion = ""
                if(ct1.selectionModel.selectedIndex >=0){
                    ct1Opcion = ct1.getSelectionModel().getSelectedItem().toString()
                }

                var cantEdges = nodes[miCombo.items.indexOf(verEdit)].edgeCount
                if(cantEdges==1){
                    nodes[miCombo.items.indexOf(verEdit)].getEdgeAt(0).removeFromParent()
                }

                if(cantEdges==2){
                    nodes[miCombo.items.indexOf(verEdit)].getEdgeAt(0).removeFromParent()
                    nodes[miCombo.items.indexOf(verEdit)].getEdgeAt(1).removeFromParent()
                }

                if(ct0.selectionModel.selectedIndex >=0){
                    graph.insertEdge(parent, null, "0", nodes[miCombo.items.indexOf(verEdit)], nodes[ct0.items.indexOf(ct0Opcion)])
                }

                if(ct1.selectionModel.selectedIndex >=0){
                    graph.insertEdge(parent, null, "1", nodes[miCombo.items.indexOf(verEdit)], nodes[ct1.items.indexOf(ct1Opcion)])
                }
            }
        }

        cButton.onMouseClicked = EventHandler<MouseEvent> {
            if(bc.selectionModel.selectedIndex > -1){
                val verDelete = bc.getSelectionModel().getSelectedItem().toString()
                nodes[miCombo.items.indexOf(verDelete)].removeFromParent()
                graph.removeSelectionCell(nodes[miCombo.items.indexOf(verDelete)])
                graph.refresh()
                nodes.removeAt(bc.items.indexOf(verDelete))
                ct0.items.removeAt(bc.items.indexOf(verDelete))
                ct1.items.removeAt(bc.items.indexOf(verDelete))
                miCombo.items.removeAt(bc.items.indexOf(verDelete))
                bc.items.removeAt(bc.items.indexOf(verDelete))
            }
        }


        //Create the actual container for the GUI
        val sceneRoot = GridPane()
        sceneRoot.setPrefSize(400.0, 400.0)
        sceneRoot.setVgap(5.0)
        sceneRoot.setHgap(1.0)
        sceneRoot.add(SwingNode().apply {
            //Sets the graph as the content of the swing node
            content = graphComponent
        }, 0, 0)
        sceneRoot.add(label1, 0, 1)
        sceneRoot.add(aTextField, 0, 2)
        sceneRoot.add(isAcceptanceState, 0, 3)
        sceneRoot.add(ac, 0, 4)
        sceneRoot.add(aButton, 0, 5)
        sceneRoot.add(separator, 0, 6)
        sceneRoot.add(editVertex, 0, 7)
        sceneRoot.add(miCombo, 0, 8)
        sceneRoot.add(trans0, 0, 9)
        sceneRoot.add(ct0, 0, 10)
        sceneRoot.add(trans1, 0, 11)
        sceneRoot.add(ct1, 0, 12)
        sceneRoot.add(bButton, 0, 13)
        sceneRoot.add(separator2, 0, 14)
        sceneRoot.add(borrar, 0, 15)
        sceneRoot.add(bc, 0, 16)
        sceneRoot.add(cButton, 0, 17)

        //Adds the Scene to the Stage, a.k.a the actual Window
        stage.scene = Scene(sceneRoot, 825.0, 900.0)

        //Shows the window
        stage.show()
    }

    // This function is an extension to the mxGraph class
    // I have no idea about how on this thing works
    // UPDATE: now I now how it works. this "block" parameter is actually a callable function object
    private fun  mxGraph.update(block: () -> Any) {
        // Apparently the model variable is a child of mxGraph instance we are extending
        model.beginUpdate()
        try {
            // I guess this prevents other threads from updating the model
            // Or it makes the Earth orbit around the sun, I'm not sure
            // UPDATE: turns out it has nothing to do with Earth's orbit
            block()
            // Thanks to Alex for pointing out that the "block" object come as a parameter,
            // And since it's a function, it can be executed by appending parenthesis
        }
        finally {
            model.endUpdate()
        }
    }
}