package io.masterplan.ui.util.layoutmanager;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class RatioConstraint extends Pane {

    private final Pane pane;

    public static final double RATIO_DEFAULT = 1;
    private double ratio = RATIO_DEFAULT;

    public RatioConstraint() {
        pane = new Pane();

        super.getChildren().add(pane);
    }

    @Override
    protected void layoutChildren() {
        double width = ratio * getHeight();
        double height = (1/ratio) * getWidth();

        pane.resize(width, height);
    }

    @Override
    public ObservableList<Node> getChildren() {
         return this.pane.getChildren();
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
        layoutChildren();
    }

    public double getRatio() {
        return ratio;
    }

}
