package dev.prism.ui.renderer.rounded;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.prism.ui.api.render.style.Outline;
import dev.prism.ui.api.render.style.Shadow;
import meteordevelopment.meteorclient.utils.render.color.Color;

public interface RoundedRendererInternal {
    void begin();

    void end();

    void render(PoseStack stack);

    void render(double x, double y,
                double width, double height,
                float topLeft, float topRight,
                float bottomLeft, float bottomRight,
                Color fillColor, Outline outline, Shadow shadow);

    void flipFrame();
}
