package net.sf.enunciate.examples.xfire_client.schema.draw;

import net.sf.enunciate.examples.xfire_client.schema.structures.House;
import net.sf.enunciate.examples.xfire_client.schema.animals.Cat;
import net.sf.enunciate.examples.xfire_client.schema.vehicles.Bus;
import net.sf.enunciate.examples.xfire_client.schema.*;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.*;
import java.util.Collection;

/**
 * @author Ryan Heaton
 */
@XmlRootElement
public class Canvas {

  private Collection figures;
  private Collection shapes;
  private Collection<Line> lines;
  private int dimensionX;
  private int dimensionY;
  private DataHandler backgroundImage;

  @XmlElementRefs (
    {
      @XmlElementRef ( type = Circle.class ),
      @XmlElementRef ( type = Rectangle.class ),
      @XmlElementRef ( type = Triangle.class )
    }
  )
  public Collection getShapes() {
    return shapes;
  }

  public void setShapes(Collection shapes) {
    this.shapes = shapes;
  }

  @XmlElements (
    {
      @XmlElement ( name="cat", type = Cat.class ),
      @XmlElement ( name="house", type = House.class ),
      @XmlElement ( name="bus", type = Bus.class )
    }
  )
  public Collection getFigures() {
    return figures;
  }

  public void setFigures(Collection figures) {
    this.figures = figures;
  }

  public Collection<Line> getLines() {
    return lines;
  }

  public void setLines(Collection<Line> lines) {
    this.lines = lines;
  }

  public int getDimensionX() {
    return dimensionX;
  }

  public void setDimensionX(int dimensionX) {
    this.dimensionX = dimensionX;
  }

  public int getDimensionY() {
    return dimensionY;
  }

  public void setDimensionY(int dimensionY) {
    this.dimensionY = dimensionY;
  }

  @XmlAttachmentRef
  public DataHandler getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(DataHandler backgroundImage) {
    this.backgroundImage = backgroundImage;
  }
}