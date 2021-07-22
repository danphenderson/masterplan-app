package io.masterplan.ui.util;


import io.masterplan.infrastucture.components.Colour;
import javafx.scene.paint.Color;

public class ColorConverter {


    public static Color convertToFXColor(Colour color) {
        double[] rgbo = color.getRGBO();
        return new Color(rgbo[0], rgbo[1], rgbo[2], rgbo[3]);
    }

    public static Colour convertToColor(Color color) {
        int r = (int) (color.getRed() * Colour.COLOR_MAX);
        int g = (int) (color.getGreen() * Colour.COLOR_MAX);
        int b = (int) (color.getBlue() * Colour.COLOR_MAX);
        int o = (int) (color.getOpacity() * Colour.COLOR_MAX);

        return new Colour(r, g, b, o);
    }

}
