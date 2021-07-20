package io.masterplan.ui.util.icon;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Icon Custom Component
 */
public class Icon extends Region {

    private final SVGPath icon = new SVGPath();

    public final double ICON_SIZE_DEFAULT = 20;
    public final Color ICON_COLOR_DEFAULT = Color.WHITE;

    public Icon() {
        this.getStylesheets().add(getClass().getResource("Icon.css").toExternalForm());

        setShape(icon);

        setIconColor(ICON_COLOR_DEFAULT);
        setIconSize(ICON_SIZE_DEFAULT);
    }

    public void setWidthSize(double size) {
        setMinWidth(size);
        setPrefWidth(size);
        setMaxWidth(size);
    }

    public double getWidthSize() {
        return super.getWidth();
    }

    public void setHeightSize(double size) {
        setMinHeight(size);
        setPrefHeight(size);
        setMaxHeight(size);
    }

    public double getHeightSize() {
        return super.getHeight();
    }

    public void setIconSize(double size) {
        setWidthSize(size);
        setHeightSize(size);
    }

    public double getIconSize() {
        return getWidth();
    }

    public void setIconColor(Color iconColor) {
        setBackground(new Background(new BackgroundFill(iconColor, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public Color getIconColor() {
        return (Color) getBackground().getFills().get(0).getFill();
    }

    /**
     * Sets icon shape from filename
     * @param filename name of svg file ("home.svg")
     */
    public void setIcon(String filename) {

        File file = new File("ui/src/main/resources/icons/" + filename);
        String fileData;

        try {
            fileData = Files.readString(Paths.get(file.getPath()));
        } catch (IOException e) {
            System.out.println("Exception loading icon: " + file.getPath());
            throw new RuntimeException(e);
        }
        icon.setContent(fileData);
    }

    /**
     * Returns icon shape as SVG String
     * @return icon shape as SVG String
     */
    public String getIcon() {
        return icon.getContent();
    }
}
