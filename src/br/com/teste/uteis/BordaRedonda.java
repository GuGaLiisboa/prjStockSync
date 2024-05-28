package br.com.teste.uteis;
 
/**
 *
 * @author 
 */
import java.awt.*;
import javax.swing.border.*;

public class BordaRedonda extends AbstractBorder {
    private final int radius;

    public BordaRedonda(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c.getForeground()); // Usa a cor do texto como cor da borda
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Desenha o retângulo com cantos arredondados
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius, this.radius, this.radius, this.radius);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(this.radius, this.radius, this.radius, this.radius);
        return insets;
    }
}
