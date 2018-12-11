package flappyBird

import java.awt.Graphics

import javax.swing.JPanel

class Renderer : JPanel() {

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        FlappyBird.flappyBird?.repaint(g)
    }

    companion object {

        private val serialVersionUID = 1L
    }

}