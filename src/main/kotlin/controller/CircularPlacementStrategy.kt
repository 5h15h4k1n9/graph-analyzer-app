package controller

import com.example.demo.logger.log
import model.UndirectedGraph
import model.finders.CommunitiesFinder
import view.GraphView
import view.VertexView
import view.props
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import tornadofx.Controller
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class CircularPlacementStrategy: Controller(), RepresentationStrategy {
    override fun <V> place(width: Double, height: Double, vertices: Collection<VertexView<V>>) {
        if (vertices.isEmpty()) {
            println("CircularPlacementStrategy.place: there is nothing to place 👐🏻")
            return
        }

        val center = Point2D(width / 2, height / 2)
        val angle = -360.0 / vertices.size

        val sorted = vertices.sortedBy { it.vertex.element.toString().toLowerCase() }
        val first = sorted.first()
        var point = Point2D(center.x, center.y - min(width, height) / 2 + first.radius * 2)
        first.position = point.x to point.y
        first.color = Color.GRAY

        sorted
            .drop(1)
            .onEach {
                point = point.rotate(center, angle)
                it.position = point.x to point.y
                it.color = Color.GRAY
            }
    }

    override fun <V, E> showCommunities(graph: GraphView<String, Long>, nIteration: String, resolution: String){
        log("community finding starting...")

        val finder = CommunitiesFinder<V, E>()
        val returnCode = finder.findCommunity(props.SAMPLE_GRAPH as UndirectedGraph<V, E>, nIteration, resolution)
        if(!returnCode) return
        graph.vertices()
            .onEach {
                val com = it.vertex.community
                it.color = generateRandomColor(com * 100)
            }
    }

    private fun Point2D.rotate(pivot: Point2D, degrees: Double): Point2D {
        val angle = Math.toRadians(degrees)
        val sin = sin(angle)
        val cos = cos(angle)

        val diff = subtract(pivot)
        val rotated = Point2D(
            diff.x * cos - diff.y * sin,
            diff.x * sin + diff.y * cos,
        )
        return rotated.add(pivot)
    }

    private fun generateRandomColor(base: Int): Color {
        // This is the base color which will be mixed with the generated one
        val mRandom = Random(base);
        val red: Int = (base + mRandom.nextInt(256)) / 2
        val green: Int = (base + mRandom.nextInt(256)) / 2
        val blue: Int = (base + mRandom.nextInt(256)) / 2
        return Color.rgb(red % 256, green % 256 , blue % 256)
    }
}
