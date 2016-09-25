/**
 * Created by jenamorado on 8/30/16.
 */

import KotlinAlgorithm.*
import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxCellRenderer
import com.mxgraph.util.mxConstants
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph
import com.mxgraph.view.mxStylesheet
import javafx.application.Application
import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.*
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    Application.launch(Ui::class.java)
}

class Ui: Application() {

    //Automaton gridPane
    val automatonTypeComboBox = ComboBox<String>()
    var stringToEvaluateTextField = TextField()
    var evaluateAutomatonButton = Button("Ok")

    //States griPane
    var stateNameTextField = TextField()
    val initialComboBox = ComboBox<String>()
    val acceptanceComboBox = ComboBox<String>()
    var createStateButton = Button("Ok")
    val deleteStateComboBox = ComboBox<String>()
    var deleteStateButton = Button("Ok")

    //Alphabet gridPane
    var alphabetTextField = TextField()
    var createAlphabetButton = Button("Ok")
    var alphabet:MutableList<String> = mutableListOf()

    //Transition gridPane
    var symbolTextField = TextField()
    var originComboBox = ComboBox<String>()
    var destinyComboBox = ComboBox<String>()
    var operationComboBox = ComboBox<String>()
    var createTransitionButton = Button("Ok")

    //operations gridPane
    var convertToDfaButton = Button("Ok")
    var complementButton = Button("Ok")
    var subtractionButton = Button("Ok")
    var intersectionButton = Button("Ok")
    var unionButton = Button("Ok")
    var minifyButton = Button("Ok")


    //graph components
    val graph = mxGraph()
    val parent = graph.defaultParent

    //Additional Functionalities
    val exportAsPngButton = Button("Ok")
    val clearAutomatonButton = Button("Ok")

    var nodes:MutableList<mxCell> = mutableListOf()
    var edges:MutableList<mxCell> = mutableListOf()

    var thisStage:Stage = Stage()

    override fun start(stage: Stage) {
        thisStage = stage
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
        fileMenu.getStyleClass().add("fileMenu")
        val newMenuItem = MenuItem("Open")
        newMenuItem.setOnAction ({
            var automatonToDraw = loadAutomaton()
            if (automatonToDraw != null) {
                alphabet = automatonToDraw.alphabet
                automatonTypeComboBox.value = automatonToDraw.type
                drawAutomaton(automatonToDraw)
            }
        })

        val saveMenuItem = MenuItem("Save")
        saveMenuItem.setOnAction({
            var fileChooser = FileChooser()
            fileChooser.setTitle("Save Automaton")
            val file = fileChooser.showSaveDialog(thisStage)
            if (file != null) {
                try {
                    val rval = FileOutputStream(file.path+".ser")
                    val out = ObjectOutputStream(rval)
                    out.writeObject(generateAutomaton())
                    out.close()
                    rval.close()

                } catch (ex: IOException) {
                    var alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Automaton Export"
                    alert.headerText = null
                    alert.contentText = "Exporting not able to complete due to: " + ex.message
                    alert.showAndWait()
                    println(ex.message)
                }

            }
        })
        val exitMenuItem = MenuItem("Exit")
        exitMenuItem.setOnAction({ actionEvent -> Platform.exit() })
        fileMenu.getItems().addAll(newMenuItem, saveMenuItem, SeparatorMenuItem(), exitMenuItem)
        menuBar.getMenus().add(fileMenu)
        menuBar.getStyleClass().add("menuBar")
     //------------End of menuBar components-----------------
     //-------------Accordion components---------------------

        val automatonPane = TitledPane()
        val automatonGrid = GridPane()
        automatonGrid.setVgap(4.0)
        automatonGrid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        automatonTypeComboBox.setPrefSize(175.0,20.0)
        evaluateAutomatonButton.setPrefSize(175.0, 20.0)
        automatonGrid.add(Label("Type: "), 0, 0)
        automatonGrid.add(automatonTypeComboBox, 1, 0)
        automatonTypeComboBox.value = "dfa"
        automatonTypeComboBox.items.addAll("dfa", "nfa", "ε-nfa", "pda", "Turing Machine")
        automatonGrid.add(Label("Value"), 0, 1)
        automatonGrid.add(stringToEvaluateTextField, 1, 1)
        automatonGrid.add(Label("Evaluate "), 0, 2)
        automatonGrid.add(evaluateAutomatonButton, 1, 2)
        automatonPane.text = "Automaton"
        automatonPane.content = automatonGrid
        automatonPane.getStyleClass().add("button")



        val statesPane = TitledPane()
        val grid = GridPane()
        grid.setVgap(4.0)
        grid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        acceptanceComboBox.setPrefSize(100.0,20.0)
        deleteStateComboBox.setPrefSize(100.0, 20.0)
        stateNameTextField.setPrefSize(100.0,20.0)
        createStateButton.setPrefSize(100.0,20.0)
        deleteStateButton.setPrefSize(100.0,20.0)
        initialComboBox.setPrefSize(100.0,20.0)
        acceptanceComboBox.items.add("True")
        acceptanceComboBox.items.add("False")
        initialComboBox.items.add("True")
        initialComboBox.items.add("False")
        acceptanceComboBox.value = "False"
        initialComboBox.value = "False"
        val stateCreationHeader = Text("Creation")
        stateCreationHeader.setFont(Font.font ("Verdana", FontWeight.BOLD, 12.5))
        stateCreationHeader.setFill(Color.valueOf("#6C7A89"))
        grid.add(stateCreationHeader, 0, 0)
        grid.add(Label("State name: "), 0, 1)
        grid.add(stateNameTextField, 1, 1)
        grid.add(Label("Is initial State: "), 0, 2)
        grid.add(initialComboBox, 1, 2)
        grid.add(Label("Is Acceptance State: "), 0, 3)
        grid.add(acceptanceComboBox, 1, 3)
        grid.add(Label("Insert State: "), 0, 4)
        grid.add(createStateButton,1,4)
        val stateDeletionHeader = Text("Removal")
        stateDeletionHeader.setFont(Font.font ("Verdana", FontWeight.BOLD, 12.5))
        stateDeletionHeader.setFill(Color.valueOf("#6C7A89"))
        grid.add(stateDeletionHeader, 0, 5)
        grid.add(Label("State: "), 0, 6)
        grid.add(deleteStateComboBox, 1, 6)
        grid.add(Label("Delete State"), 0, 7)
        grid.add(deleteStateButton, 1, 7)
        statesPane.text = "States"
        statesPane.content = grid
        statesPane.getStyleClass().add("button")

        //alphabet accordion grid
        alphabetTextField.setPrefSize(95.0,20.0)
        createAlphabetButton.setPrefSize(125.0, 20.0)
        val alphabetPane = TitledPane()
        val alphabetGrid = GridPane()
        alphabetGrid.setVgap(4.0)
        alphabetGrid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        alphabetGrid.add(Label("Alphabet: "), 0, 0)
        alphabetGrid.add(alphabetTextField, 1, 0)
        alphabetGrid.add(Label("Split by ','"), 0, 1)
        alphabetGrid.add(Label("Define Alphabet: "), 0, 2)
        alphabetGrid.add(createAlphabetButton,1,2)
        alphabetPane.content = alphabetGrid
        alphabetPane.text = "Alphabet"
        alphabetPane.getStyleClass().add("button")

        //operations gridPane
        convertToDfaButton.setPrefSize(130.0, 20.0)
        complementButton.setPrefSize(130.0, 20.0)
        subtractionButton.setPrefSize(130.0, 20.0)
        unionButton.setPrefSize(130.0, 20.0)
        intersectionButton.setPrefSize(130.0, 20.0)
        val operationsPane = TitledPane()
        val operationsGrid = GridPane()
        operationsGrid.setVgap(4.0)
        operationsGrid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        operationsGrid.add(Label("Convert to DFA: "), 0, 0)
        operationsGrid.add(convertToDfaButton, 1, 0)
        operationsGrid.add(Label("Complement: "), 0, 1)
        operationsGrid.add(complementButton, 1, 1)
        operationsGrid.add(Label("Subtraction: "), 0, 2)
        operationsGrid.add(subtractionButton, 1, 2)
        operationsGrid.add(Label("Union: "), 0, 3)
        operationsGrid.add(unionButton, 1, 3)
        operationsGrid.add(Label("Intersection: "), 0, 4)
        operationsGrid.add(intersectionButton, 1, 4)
        operationsGrid.add(Label("Minify : "), 0, 5)
        operationsGrid.add(minifyButton, 1, 5)
//        alphabetGrid.add(createAlphabetButton,1,2)
        operationsPane.content = operationsGrid
        operationsPane.text = "Automaton Operations"
        operationsPane.getStyleClass().add("button")

        //transition accordion grid
        originComboBox.value = ""
        destinyComboBox.value = ""
        symbolTextField.setPrefSize(145.0,20.0)
        originComboBox.setPrefSize(175.0,20.0)
        destinyComboBox.setPrefSize(175.0, 20.0)
        operationComboBox.setPrefSize(175.0, 20.0)
        createTransitionButton.setPrefSize(175.0, 20.0)
        val transitionsPane = TitledPane()
        val transitionGrid = GridPane()
        transitionGrid.setVgap(4.0)
        transitionGrid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        transitionGrid.add(Label("Symbol: "), 0, 1)
        transitionGrid.add(symbolTextField, 1, 1)
        transitionGrid.add(Label("Origin: "), 0, 2)
        transitionGrid.add(originComboBox, 1, 2)
        transitionGrid.add(Label("Destiny: "), 0, 3)
        transitionGrid.add(destinyComboBox, 1, 3)
        transitionGrid.add(Label("Action: "), 0, 4)
        transitionGrid.add(operationComboBox, 1 ,4)
        operationComboBox.value = "Create"
        operationComboBox.items.addAll("Create", "Delete")
        transitionGrid.add(Label("Execute: "), 0, 5)
        transitionGrid.add(createTransitionButton,1,5)
        transitionsPane.content = transitionGrid
        transitionsPane.text = "Transition"
        transitionsPane.getStyleClass().add("button")

        //Additional Operations
        clearAutomatonButton.setPrefSize(121.0, 20.0)
        exportAsPngButton.setPrefSize(121.0, 20.0)
        val additionalOpetationPane = TitledPane()
        val additionalOperationsGrid = GridPane()
        additionalOperationsGrid.setVgap(4.0)
        additionalOperationsGrid.padding = Insets(5.0, 5.0, 5.0, 5.0)
        additionalOperationsGrid.add(Label("Clear Automaton: "), 0, 0)
        additionalOperationsGrid.add(clearAutomatonButton, 1, 0)
        additionalOperationsGrid.add(Label("Export as PNG: "), 0, 1)
        additionalOperationsGrid.add(exportAsPngButton, 1, 1)
//        alphabetGrid.add(createAlphabetButton,1,2)
        additionalOpetationPane.content = additionalOperationsGrid
        additionalOpetationPane.text = "Additional Operations"
        additionalOpetationPane.getStyleClass().add("button")

        val accordion = Accordion()
        accordion.panes.addAll(automatonPane, statesPane, transitionsPane, alphabetPane, operationsPane, additionalOpetationPane)
        //-------------End of Accordion components--------------
     //------------------Hbox Components---------------------
        //--------------------stage to draw automaton------------

        graph.update {}
        graph.minimumGraphSize = mxRectangle(0.0,0.0,750.0,scene.height-23)
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

        stage.scene = scene
        stage.isResizable = false
        stage.icons.add(Image("ad.png"))
        stage.title = "Automaton Drawer 1.0"
        stage.show()
        applyEdgeDefaults()
        logic()

        var test = RegEx()
        var nfa = test.regexToNfae("(0+1.0)*.1+(0+1.0)*.1.1.(1+0.1)*+(0+1.0)*.1.1.(1+0.1)*.0+(0+1.0)*")
        automatonTypeComboBox.value = "ε-nfa"
        alphabet.add("0")
        alphabet.add("1")
        drawAutomaton(nfa)
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
        createStateButton.onMouseClicked = EventHandler<MouseEvent> {
            insertState(stateNameTextField.text, initialComboBox.value.toLowerCase(), acceptanceComboBox.value.toLowerCase())
        }
        deleteStateButton.onMouseClicked = EventHandler<MouseEvent> {
            deleteState()
        }
        createTransitionButton.onMouseClicked = EventHandler<MouseEvent> {
            createTransition(symbolTextField.text, originComboBox.value, destinyComboBox.value)
        }
        createAlphabetButton.onMouseClicked = EventHandler<MouseEvent> {
            generateAlphabet()
        }
        evaluateAutomatonButton.onMouseClicked = EventHandler<MouseEvent> {
            evaluateAutomaton()
        }
        complementButton.onMouseClicked = EventHandler<MouseEvent> {
            complementAutomaton(generateAutomaton())
        }
        convertToDfaButton.onMouseClicked = EventHandler<MouseEvent> {
            convertToDfa(generateAutomaton())
        }
        exportAsPngButton.onMouseClicked = EventHandler<MouseEvent> {
            exportImage()
        }
        clearAutomatonButton.onMouseClicked = EventHandler<MouseEvent> {
            clearAutomaton()
        }
        subtractionButton.onMouseClicked = EventHandler<MouseEvent> {
            automatonsOperations("subtraction")
        }
        unionButton.onMouseClicked = EventHandler<MouseEvent> {
            automatonsOperations("union")
        }
        intersectionButton.onMouseClicked = EventHandler<MouseEvent> {
            automatonsOperations("intersection")
        }
        minifyButton.onMouseClicked = EventHandler<MouseEvent> {
            minify()
        }
    }

    fun minify() {
        var automaton = generateAutomaton()
        if (automaton is nonDeterministicFiniteAutomaton) { // same as !(obj is String)
            var automata = automaton.convertToDFA().minimize()
            clearAutomaton()
            drawAutomaton(automata)
        }
        else if (automaton is nonDeterministicAutomatonEpsilon){
            var automata = automaton.convertToNFA().convertToDFA().minimize()
            clearAutomaton()
            drawAutomaton(automata)
        } else if (automaton is deterministicFiniteAutomaton) {
            var automata = automaton.minimize()
            clearAutomaton()
            drawAutomaton(automata)
        }
    }
    fun automatonsOperations(operation: String) {
        if (automatonTypeComboBox.value.equals("dfa")) {
            var automatonA = generateAutomaton() as deterministicFiniteAutomaton

            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Automatons Operation"
            alert.headerText = null
            alert.contentText = "Second Automaton must be loaded"
            alert.showAndWait()

            val automatonB = loadAutomaton() as deterministicFiniteAutomaton

            if (automatonA != null && automatonB != null) {
                 var newAutomaton = automatonOps().operation(automatonA, automatonB, operation)
                clearAutomaton()
                drawAutomaton(newAutomaton)
            }
        } else {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Automatons Operations"
            alert.headerText = null
            alert.contentText = "Automatons must both be dfa type!"
            alert.showAndWait()
        }
    }
    private fun  generateAutomaton(): Automaton? {
        return AutomatonGenerator(automatonTypeComboBox.value).generateAutomaton(nodes,edges, alphabet)
    }

    private fun complementAutomaton(automaton: Automaton?) {
        if (automaton!!.states.size >= 1) {
            if (automaton is deterministicFiniteAutomaton) {
                var automatonToComplement = automatonOps().complement(automaton)
                if (automatonToComplement != null) {
                    drawAutomaton(automatonToComplement)
                }
            } else {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Complement Automaton"
                alert.headerText = null
                alert.contentText = "Method can only be applied to DFA"
                alert.showAndWait()
            }
        } else {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Complement Automaton"
            alert.headerText = null
            alert.contentText = "No automaton found"
            alert.showAndWait()
        }
    }

    private fun exportImage() {
        if (graph.getChildVertices(graph.defaultParent).size > 0) {

            val fileChooser = FileChooser()
            fileChooser.setTitle("Save Automaton")
            val file = fileChooser.showSaveDialog(thisStage)
            if (file != null) {
                try {
                    val image = mxCellRenderer.createBufferedImage(graph, null, 1.0, java.awt.Color.WHITE , true, null)
                    ImageIO.write(image, "png", File(file.path + ".png"))
                } catch (ex: IOException) {
                    var alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Automaton Export"
                    alert.headerText = null
                    alert.contentText = "Exporting not able to complete due to: " + ex.message
                    alert.showAndWait()
                    println(ex.message)
                }

            }

        } else {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Automaton Export"
            alert.headerText = null
            alert.contentText = "There is no automaton drawn to export!"
            alert.showAndWait()
        }
    }

    private fun convertToDfa(automaton: Automaton?) {
        if (automaton != null ) {
            if (automaton is nonDeterministicFiniteAutomaton) {

                val dfa = (automaton).convertToDFA()
                drawAutomaton(dfa)
            }
            else if (automaton is nonDeterministicAutomatonEpsilon){

                var dfa = ((automaton).transformToDfa())
                drawAutomaton(dfa)
            }
            else {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Convert to DFA"
                alert.headerText = null
                alert.contentText = "Can only convert from NFA or ε-NFA"
                alert.showAndWait()
            }
            automatonTypeComboBox.value = "dfa"
        } else {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Convert to DFA"
            alert.headerText = null
            alert.contentText = "No automaton found"
            alert.showAndWait()
        }
    }

    fun clearAutomaton() {
            graph.removeCells(graph.getChildVertices(graph.defaultParent))
            nodes.clear()
            edges.clear()
            graph.refresh()
            graph.update {  }
            deleteStateComboBox.items.clear()
            originComboBox.items.clear()
            destinyComboBox.items.clear()
            clearStatesForm()
            clearTransitionsForm()
    }

    private fun drawAutomaton(automaton: Automaton) {
        clearAutomaton()
        for (state in automaton.states) {
            insertState(state._name, state._initialState.toString(), state._isAcceptanceState.toString())
        }
        for (state in automaton.states) {
            for (transition in state._transitions) {
                createTransition(transition._symbol, transition._origin, transition._destiny)
            }
        }
    }

    fun insertState(stateName: String, isInitial: String, isAcceptance: String) {
        var style = "shape=ellipse;fillColor=white"
        if (stateName != "") {

            if (getNode(stateName) == null) {
                if(isAcceptance == "true" && isInitial != "true") {
                    style = "shape=doubleEllipse;fillColor=#22A7F0"
                } else if (isAcceptance == "true" && isInitial == "true") {
                    if (!initialStateExists()) {
                        style = "shape=doubleEllipse;fillColor=#4ECDC4"
                    }
                    else {
                        alertDuplicateInitialState()
                        return
                    }
                } else if(isInitial == "true" && isAcceptance != "true"){
                    if (!initialStateExists()) {
                        style = "shape=ellipse;fillColor=#4ECDC4"
                    }
                    else {
                        alertDuplicateInitialState()
                        return
                    }
                }

                val vertex = graph.insertVertex(parent, null, stateName, 150.0, 150.0, 50.00, 50.00, style) as mxCell
                nodes.add(vertex)
                originComboBox.items.add(stateName)
                destinyComboBox.items.add(stateName)
                deleteStateComboBox.items.add(stateName)
            } else {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "State Addition"
                alert.headerText = null
                alert.contentText = "State name cannot be repeated!"
                alert.showAndWait()
                return
            }
        } else {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "State Creation"
            alert.headerText = null
            alert.contentText = "State must have a name!"
            alert.showAndWait()
        }
        clearStatesForm()
        graph.refresh()
        graph.update {  }
    }

    fun deleteState() {
        var stateToDelete = deleteStateComboBox.value
        if (deleteStateComboBox.selectionModel.selectedIndex >= 0) {
            var mxCellToDelete = getNode(stateToDelete)
            mxCellToDelete!!.removeFromParent()
            graph.removeSelectionCell(mxCellToDelete)
            nodes.remove(mxCellToDelete)
            originComboBox.items.removeAt(deleteStateComboBox.items.indexOf(stateToDelete))
            destinyComboBox.items.removeAt(deleteStateComboBox.items.indexOf(stateToDelete))
            deleteStateComboBox.items.remove(stateToDelete)
            graph.refresh()
        } else {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "State Removal"
            alert.headerText = null
            alert.contentText = "Please select a state to remove!"
            alert.showAndWait()
            return
        }
        clearStatesForm()
        graph.refresh()
        graph.update {  }
    }

    fun clearStatesForm() {
        acceptanceComboBox.value = "False"
        initialComboBox.value = "False"
        stateNameTextField.text = ""
        deleteStateComboBox.value = ""
    }

    fun createTransition(symbol: String, origin: String, destiny: String) {
        if (!automatonTypeComboBox.value.equals("pda") && !automatonTypeComboBox.value.equals("Turing Machine")) {
            if (symbol.length >1) {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Transition Creation"
                alert.headerText = null
                alert.contentText = "Symbol must be a single character!"
                alert.showAndWait()
                return
            }
            if (!alphabet.contains(symbol) && !symbol.equals("e") ) {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Transition Creation"
                alert.headerText = null
                alert.contentText = "Symbol must be in the alphabet!"
                alert.showAndWait()
                return
            }
        }
        if (symbol != "" && origin != "" && destiny != "") {
            if (operationComboBox.value == "Create") {
                var mxCellToInsert = getEdge(symbol, origin, destiny)
                if (mxCellToInsert == null) {
                    var edge = graph.insertEdge(parent, null, symbol, getNode(origin), getNode(destiny)) as mxCell
                    //edge.put(mxConstants.STYLE_ROUNDED, true);
                    edges.add(edge)
                } else {
                    var alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Transition Addition"
                    alert.headerText = null
                    alert.contentText = "Transition already exists!"
                    alert.showAndWait()
                    return
                }
            } else {
                var mxCellToDelete = getEdge(symbol, origin, destiny)
                if (mxCellToDelete != null) {
                    mxCellToDelete!!.removeFromParent()
                    graph.removeSelectionCell(mxCellToDelete)
                    edges.remove(mxCellToDelete)
                    graph.refresh()
                } else {
                    var alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Transition Removal"
                    alert.headerText = null
                    alert.contentText = "Transition does not exist!"
                    alert.showAndWait()
                    return
                }
            }
        }
        if(symbol == "") {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Transition Creation"
            alert.headerText = null
            alert.contentText = "Transition must have a symbol!"
            alert.showAndWait()
            return
        }
        if (origin == "") {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Transition Creation"
            alert.headerText = null
            alert.contentText = "Transition must have an origin!"
            alert.showAndWait()
            return
        }
        if (destiny == "") {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Transition Creation"
            alert.headerText = null
            alert.contentText = "Transition must have a destiny!"
            alert.showAndWait()
            return
        }
        clearTransitionsForm()
        graph.refresh()
        graph.update {  }
    }

    fun clearTransitionsForm() {
        originComboBox.value = ""
        destinyComboBox.value = ""
        symbolTextField.text = ""
    }

    fun getNode(nodeName:String):mxCell? {
        for (item in nodes) {
            if (item.value == nodeName) {
                return item
            }
        }
        return null
    }

    fun getEdge(symbol:String, source:String, target:String):mxCell? {
        for (item in edges) {
            if (item.value.toString().equals(symbol) && item.source.value.toString().equals(source) && item.target.value.toString().equals(target)) {
                return item
            }
        }
        return null
    }


    fun evaluateAutomaton() {
        if (alphabet.size < 1) {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Automaton Evaluation"
            alert.headerText = null
            alert.contentText = "Automaton must contain an alphabet!"
            alert.showAndWait()
            return
        }
        if (!initialStateExists()) {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Automaton Evaluation"
            alert.headerText = null
            alert.contentText = "Automaton must contain an initial state!"
            alert.showAndWait()
            return
        }
        var edgesToAdd:MutableList<mxCell> = mutableListOf()
        for (item in nodes) {
            var transitions =  graph.getEdges(item)
            for (ougoingTrans in transitions) {
                if (!edgesToAdd.contains(ougoingTrans as mxCell)) {
                    edgesToAdd.add(ougoingTrans)
                }
            }
        }
        var automaton = AutomatonGenerator(automatonTypeComboBox.value).generateAutomaton(nodes, edges, alphabet)
        if ( automaton != null && automaton!!.states.size >=1) {
            for (state in automaton.states) {
                for (transition in state._transitions) {
                    println(state._name+":"+transition._symbol+"->"+transition._destiny)
                }
            }
            var result = automaton!!.evaluate(stringToEvaluateTextField.text)
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Automaton Evaluation"
            alert.headerText = null
            if (result == true) {
                alert.contentText = "String is accepted!"
                alert.showAndWait()
                return
            } else {
                alert.contentText = "String is not an accepted value!"
                alert.showAndWait()
                return
            }
        }
    }

    fun generateAlphabet() {
        alphabet.clear()
        var _alphabet = alphabetTextField.text.split(",")
        if (_alphabet.contains("e")) {
            var alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Alphabet Creation"
            alert.headerText = null
            alert.contentText = "Symbol 'e' is reserved for epsilon representation!"
            alert.showAndWait()
            return
        }
        for (symbol in _alphabet) {
            if(symbol.length == 1) {
                alphabet.add(symbol.toString())
            }
            else if (_alphabet.size <= 1 || _alphabet.size == null) {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Alphabet Creation"
                alert.headerText = null
                alert.contentText = "Alphabet cannot be empty!"
                alert.showAndWait()
                return
            }else if (symbol.equals("")) {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Alphabet Creation"
                alert.headerText = null
                alert.contentText = "Remove last comma from input!"
                alert.showAndWait()
                return
            }
            else {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Alphabet Creation"
                alert.headerText = null
                alert.contentText = "Alphabet must be composed by single characters separated by commas!"
                alert.showAndWait()
                return
            }
        }
        alphabetTextField.text = ""
    }
    fun initialStateExists():Boolean {
        for (node in nodes) {
            if (node.style.equals("shape=ellipse;fillColor=#4ECDC4") || node.style.equals("shape=doubleEllipse;fillColor=#4ECDC4")) {
                return true
            }
        }
        return false
    }
    fun alertDuplicateInitialState() {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "State Addition"
        alert.headerText = null
        alert.contentText = "There can only be one initial state!"
        alert.showAndWait()
    }

    fun loadAutomaton(): Automaton? {
        var fileChooser = FileChooser()
        fileChooser.setTitle("Load Automaton")
        val file = fileChooser.showOpenDialog(thisStage)
        if (file != null) {
            try {
                val fileInput = FileInputStream(file.path)
                val input = ObjectInputStream(fileInput)
                val loadedAutomaton = input.readObject() as Automaton
                input.close()
                fileInput.close()
                return  loadedAutomaton

            } catch (ex: IOException) {
                var alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Automaton Import"
                alert.headerText = null
                alert.contentText = "Importing not able to complete due to: " + ex.message
                alert.showAndWait()
                println(ex.message)
            }
        }
        return null
    }

    private fun applyEdgeDefaults() {
        // Settings for edges
        val edge = HashMap<String, Any>()
        edge.put(mxConstants.STYLE_ROUNDED, true)
        edge.put(mxConstants.STYLE_ORTHOGONAL, false)
        edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle")
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR)
        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC)
        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE)
        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT)
        edge.put(mxConstants.STYLE_STROKECOLOR, "#000000") // default is #6482B9
        edge.put(mxConstants.STYLE_FONTCOLOR, "#446299")

        val edgeStyle = mxStylesheet()
        edgeStyle.setDefaultEdgeStyle(edge)
        graph.stylesheet = edgeStyle
    }
}