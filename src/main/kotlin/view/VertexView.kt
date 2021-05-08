package view

import model.Vertex
import javafx.beans.property.DoubleProperty
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import tornadofx.text

class VertexView<V>(
    val vertex: Vertex<V>,
    x: Double,
    y: Double,
    r: DoubleProperty,
    color: Color,
) : Circle(x, y, r.get(), color) {
    init {
        radiusProperty().bind(r)
    }

    var position: Pair<Double, Double>
        get() = centerX to centerY
        set(value) {
            centerX = value.first
            centerY = value.second
        }

    var color: Color
        get() = fill as Color
        set(value) {
            fill = value
        }

    val label = text(vertex.element.toString()) {
        visibleProperty().bind(props.vertex.label)
        xProperty().bind(centerXProperty().subtract(layoutBounds.width / 2))
        yProperty().bind(centerYProperty().add(radiusProperty()).add(layoutBounds.height))
    }

    val community = text(vertex.community.toString()){
        visibleProperty().bind(props.vertex.community)
        xProperty().bind(centerXProperty().subtract(layoutBounds.width / 2 + 10))
        yProperty().bind(centerYProperty().add(radiusProperty()).add(layoutBounds.height))
    }
}
