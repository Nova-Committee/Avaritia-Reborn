package committee.nova.mods.avaritia.api.client.screen.coordinate;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Coordinate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public double x;
    public double y;
    public double width;
    public double height;

    public double u0;
    public double v0;
    public double uWidth;
    public double vHeight;
}
