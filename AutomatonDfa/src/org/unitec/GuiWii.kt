package org.unitec

import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.util.mxRectangle
import com.mxgraph.view.mxGraph
import javafx.application.Application
import javafx.application.Platform
import javafx.embed.swing.SwingNode
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.image.Image
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Stage

/**
 * Created by Jobarah on 9/20/2016.
 */

fun main(args: Array<String>) {
    Application.launch(GuiWii::class.java)
}

class GuiWii:Application() {
    //graph components
    val graph = mxGraph()
    val parent = graph.defaultParent
    var thisStage = Stage()

    override fun start(stage: Stage) {
        thisStage = stage
        val root = GridPane()
        val menuBar = MenuBar()
        menuBar.prefWidthProperty().bind(stage.widthProperty())
        var scene = Scene(root, 1000.0, 600.0)

        val fileMenu = Menu("File")
        fileMenu.getStyleClass().add("fileMenu")
        val exitMenuItem = MenuItem("Exit")
        exitMenuItem.setOnAction({ actionEvent -> Platform.exit() })
        fileMenu.getItems().addAll(SeparatorMenuItem(), exitMenuItem)
        menuBar.getMenus().add(fileMenu)


        graph.update {}
        graph.minimumGraphSize = mxRectangle(0.0,0.0,scene.width,scene.height)
        val graphComponent = mxGraphComponent(graph)
        graph.isAllowLoops = true
        graph.isAllowDanglingEdges = false
        graph.isEdgeLabelsMovable = false
        graph.isCellsResizable = false

        val container = HBox()

        container.getChildren().add(SwingNode().apply {
            content = graphComponent
        })

        root.add(menuBar, 0, 0)
        root.add(container, 0, 1)

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
}