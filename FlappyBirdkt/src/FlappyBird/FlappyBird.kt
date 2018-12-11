package flappyBird

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.ArrayList
import java.util.Random

import javax.swing.JFrame
import javax.swing.Timer

class FlappyBird : ActionListener, MouseListener, KeyListener {

    val WIDTH = 800
    val HEIGHT = 800

    var renderer: Renderer

    var bird: Rectangle

    var columns: ArrayList<Rectangle>

    var ticks: Int = 0
    var yMotion: Int = 0
    var score: Int = 0

    var gameOver: Boolean = false
    var started: Boolean = false

    var rand: Random

    init {
        val jframe = JFrame()
        val timer = Timer(20, this)

        renderer = Renderer()
        rand = Random()

        jframe.add(renderer)
        jframe.title = "Flappy Bird"
        jframe.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        jframe.setSize(WIDTH, HEIGHT)
        jframe.addMouseListener(this)
        jframe.addKeyListener(this)
        jframe.isResizable = true
        jframe.isVisible = true

        bird = Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20)
        columns = ArrayList()

        addColumn(true)
        addColumn(true)
        addColumn(true)
        addColumn(true)

        timer.start()
    }

    fun addColumn(start: Boolean) {
        val space = 300
        val width = 100
        val height = 50 + rand.nextInt(300)

        if (start) {
            columns.add(Rectangle(WIDTH + width + columns.size * 300, HEIGHT - height - 120, width, height))
            columns.add(Rectangle(WIDTH + width + (columns.size - 1) * 300, 0, width, HEIGHT - height - space))
        } else {
            columns.add(Rectangle(columns[columns.size - 1].x + 600, HEIGHT - height - 120, width, height))
            columns.add(Rectangle(columns[columns.size - 1].x, 0, width, HEIGHT - height - space))
        }
    }

    fun paintColumn(g: Graphics, column: Rectangle) {
        g.color = Color.green.darker()
        g.fillRect(column.x, column.y, column.width, column.height)
    }

    fun jump() {
        if (gameOver) {
            bird = Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20)
            columns.clear()
            yMotion = 0
            score = 0

            addColumn(true)
            addColumn(true)
            addColumn(true)
            addColumn(true)

            gameOver = false
        }

        if (!started) {
            started = true
        } else if (!gameOver) {
            if (yMotion > 0) {
                yMotion = 0
            }

            yMotion -= 10
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        val speed = 5

        ticks++

        if (started) {
            for (i in columns.indices) {
                val column = columns[i]

                column.x -= speed
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2
            }

            for (i in columns.indices) {
                val column = columns[i]

                if (column.x + column.width < 0) {
                    columns.remove(column)

                    if (column.y == 0) {
                        addColumn(false)
                    }
                }
            }

            bird.y += yMotion

            for (column in columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++
                }

                if (column.intersects(bird)) {
                    gameOver = true

                    if (bird.x <= column.x) {
                        bird.x = column.x - bird.width

                    } else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height
                        } else if (bird.y < column.height) {
                            bird.y = column.height
                        }
                    }
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true
            }

            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height
            }
        }

        renderer.repaint()
    }

    fun repaint(g: Graphics) {
        g.color = Color.cyan
        g.fillRect(0, 0, WIDTH, HEIGHT)

        g.color = Color.orange
        g.fillRect(0, HEIGHT - 120, WIDTH, 120)

        g.color = Color.green
        g.fillRect(0, HEIGHT - 120, WIDTH, 20)

        g.color = Color.red
        g.fillRect(bird.x, bird.y, bird.width, bird.height)

        for (column in columns) {
            paintColumn(g, column)
        }

        g.color = Color.white
        g.font = Font("Arial", 1, 100)

        if (!started) {
            g.drawString("Click to start!", 75, HEIGHT / 2 - 50)
        }

        if (gameOver) {
            g.drawString("Game Over!", 100, HEIGHT / 2 - 50)
        }

        if (!gameOver && started) {
            g.drawString(score.toString(), WIDTH / 2 - 25, 100)
        }
    }

    override fun mouseClicked(e: MouseEvent) {
        jump()
    }

    override fun keyReleased(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_SPACE) {
            jump()
        }
    }

    override fun mousePressed(e: MouseEvent) {}

    override fun mouseReleased(e: MouseEvent) {}

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseExited(e: MouseEvent) {}

    override fun keyTyped(e: KeyEvent) {


    }

    override fun keyPressed(e: KeyEvent) {

    }

    companion object {

        var flappyBird: FlappyBird? = null

        @JvmStatic
        fun main(args: Array<String>) {
            flappyBird = FlappyBird()
        }
    }

}