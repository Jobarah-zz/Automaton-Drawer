/**
 * Created by jenamorado on 8/30/16.
 */

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph
import javafx.application.Application
import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Stage

fun main(args: Array<String>) {
    Application.launch(Ui::class.java)
}

class Ui: Application() {
    var stateName = TextField()
    val initialCombo = ComboBox<String>()
    val acceptanceCombo = ComboBox<String>()
    var createState = Button("Ok")
    val deleteLabel = Label("Delete vertex")
    val deleteCombo = ComboBox<String>()

    var alphabet = TextField()

    val graph = mxGraph()
    val parent = graph.defaultParent


    override fun start(stage: Stage) {

//====================Scene components==========================================================
        //Main container inside scene
        val root = GridPane()
        //accordion elements
        val g = Group()
        //MenuBar itself
        val menuBar = MenuBar()
        menuBar.prefWidthProperty().bind(stage.widthProperty())
        //Scene and it's styles
        var scene = Scene(root, 1000.0, 600.0)
        scene.getStylesheets().add("css/styles.css")

     //-------------MenuBar components----------------------
        // File menu - new, save, exit
        val fileMenu = Menu("File")
        val newMenuItem = MenuItem("Open")
        val saveMenuItem = MenuItem("Save")
        val exitMenuItem = MenuItem("Exit")
        exitMenuItem.setOnAction({ actionEvent -> Platform.exit() })

        fileMenu.getItems().addAll(newMenuItem, saveMenuItem,
                SeparatorMenuItem(), exitMenuItem)

        menuBar.getMenus().add(fileMenu)
        menuBar.getStyleClass().add("menuBar")
     //------------End of menuBar components-----------------
     //-------------Accordion components---------------------


        val statesPane = TitledPane()
        val alphabetPane = TitledPane()
        val alphabetGrid = GridPane()
        val grid = GridPane()
        grid.setVgap(4.0)
        grid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        acceptanceCombo.setPrefSize(100.0,20.0)
        acceptanceCombo.items.add("True")
        acceptanceCombo.items.add("False")
        acceptanceCombo.value = "False"
        initialCombo.setPrefSize(100.0,20.0)
        initialCombo.items.add("True")
        initialCombo.items.add("False")
        initialCombo.value = "False"
        stateName.setPrefSize(100.0,20.0)
        grid.add(Label("State name: "), 0, 0)
        grid.add(stateName, 1, 0)
        grid.add(Label("Is initial State: "), 0, 1)
        grid.add(initialCombo, 1, 1)
        grid.add(Label("Is Acceptance State: "), 0, 2)
        grid.add(acceptanceCombo, 1, 2)
        createState.setPrefSize(100.0,20.0)
        grid.add(Label("Insert State: "), 0, 3)
        grid.add(createState,1,3)
        statesPane.text = "States"
        statesPane.content = grid
        statesPane.getStyleClass().add("button")

        alphabetGrid.add(Label("Alphabet: "), 0, 0)
        alphabetGrid.add(alphabet, 1, 0)
        alphabetGrid.add(Label("Split by ','"), 0, 1)
        alphabetPane.content = alphabetGrid
        alphabetPane.text = "Alphabet"
        alphabetPane.getStyleClass().add("button")

        val t2 = TitledPane("Transitions", Button("B2"))
        t2.getStyleClass().add("button")
        val t3 = TitledPane("Alphabet", Button("B3"))
        t3.getStyleClass().add("button")
        val accordion = Accordion()
        accordion.panes.addAll(statesPane, t2, alphabetPane)
     //-------------End of Accordion components--------------
     //------------------Hbox Components---------------------
        //--------------------stage to draw automaton------------

        graph.update {}
        graph.minimumGraphSize = mxRectangle(0.0,0.0,800.0,scene.height)
        val graphComponent = mxGraphComponent(graph)
        graph.isAllowLoops = true
        graph.isAllowDanglingEdges = false
        graph.isEdgeLabelsMovable = false
        graph.isCellsResizable = false
        //--------------------End of stage to draw automaton-----

        val container = HBox()
        val hb = HBox()
        hb.setId("hbox-custom")
        container.getStyleClass().add("hbox")
        hb.getChildren().add(g)

        hb.getChildren().add(SwingNode().apply {
            content = graphComponent
        })

        g.children.add(accordion)
        container.children.add(hb)

     //------------------End of Hbox Components--------------
//==============================================================================================

    //addition of created components to the main container
        root.add(menuBar, 0, 0)
        root.add(container, 0, 1)

        logic()
        stage.scene = scene
        stage.isResizable = false
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

    fun logic() {
        createState.onMouseClicked = EventHandler<MouseEvent> {
            var sizeX=0.0
            var sizeY=0.0
            var acceptance:Boolean =false
            var initial:Boolean = false
            var style = "shape=ellipse;fillColor=white"
            if (stateName.text != "") {
                if(acceptanceCombo.value == "True"){
                    acceptance = true
                    style = "shape=doubleEllipse;fillColor=white"
                }
                if(initialCombo.value == "True"){
                    initial = true
                    sizeX = 100.00
                    sizeY = 100.00
                }else{
                    sizeX = 50.00
                    sizeY = 50.00
                }

                val vertex = graph.insertVertex(parent, null, stateName.text, 150.0, 150.0, sizeX, sizeY, style)
                print(vertex.toString())
            }
        }
    }
}