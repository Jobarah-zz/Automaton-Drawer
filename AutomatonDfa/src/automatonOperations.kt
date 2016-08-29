import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Paint
import javafx.stage.Stage
import java.awt.Color
import java.awt.Label

/**
 * Created by Jobarah on 8/27/2016.
 */

fun main(args: Array<String>) {
    Application.launch(automatonOperations::class.java)
}

class automatonOperations: Application() {
    override fun start(stage: Stage) {
        stage.title = "Tabs"
        val root = Group()
        val scene = Scene(root, 400.0, 250.0)

        val tabPane = TabPane()

        val borderPane = BorderPane()
        for (i in 0..4) {
            val tab = Tab()
            tab.setText("Tab" + i)
            val hbox = HBox()
            var a = javafx.scene.control.Label("Tab" + i)
            hbox.getChildren().add(a)
            hbox.setAlignment(Pos.CENTER)
            tab.setContent(hbox)
            tabPane.getTabs().add(tab)
        }
        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty())
        borderPane.prefWidthProperty().bind(scene.widthProperty())

        borderPane.setCenter(tabPane)
        root.getChildren().add(borderPane)
        stage.scene = scene
        stage.show()
    }
}