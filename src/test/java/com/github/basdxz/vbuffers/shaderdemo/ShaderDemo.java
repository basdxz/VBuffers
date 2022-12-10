package com.github.basdxz.vbuffers.shaderdemo;

import com.github.basdxz.vbuffers.internal.feature.BufferHandler;
import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static com.github.basdxz.vbuffers.shaderdemo.SeascapeDemo.ballerFragmentShader;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class ShaderDemo {
    public static final int CANVAS_WIDTH = 1000;
    public static final int CANVAS_HEIGHT = 1000;
    public static final int POINT_COUNT = CANVAS_WIDTH * CANVAS_HEIGHT;

    public static Vector2f iResolution = new Vector2f(CANVAS_WIDTH, CANVAS_HEIGHT);

    public static void main(String[] args) {
        // Create shader buffer
        val buffer = BufferHandler.newBuffer(ShaderLayout.class, ByteBuffer::allocateDirect, POINT_COUNT);

        // Apply UVs to the buffer
        for (var y = 0; y < CANVAS_HEIGHT; y++) {
            for (var x = 0; x < CANVAS_WIDTH; x++) {
                applyUV(buffer, x, y);
                buffer.v$next();
            }
        }
        buffer.v$flip();

        // Render to the buffer
        buffer.v$parallelStream().forEach(ShaderDemo::render);

        // Create canvas
        val canvas = new Canvas(buffer);
        canvas.render();
        canvas.showImage();
    }

    private static void applyUV(@NonNull ShaderLayout buffer, int x, int y) {
        buffer.uv(new Vector2f(x, y));
    }

    private static void render(ShaderLayout stride) {
//        stride.color(fragmentShader(stride.uv()));
        stride.color(ballerFragmentShader(stride.uv()));
    }

    private static Vector3f fragmentShader(Vector2f uv) {
        // vec2 st = 2.0*uv -1.0;
        val st = new Vector2f(uv).mul(2F).sub(1F, 1F);
        // fragColor = vec4(uv,0.5+0.5*sin(iTime),1.0);  where time is 3F
        val fragColor = new Vector3f(uv, 0.5F + 0.5F * (float) Math.sin(3F));
        // vec3 cam = vec3(0.0, 0.0, -1.0);
        val cam = new Vector3f(0, 0, -1F);
        // vec3 ray = vec3(st.x, st.y, 1);
        val ray = new Vector3f(st.x, st.y, 1F);
        // ray = normalize(ray);
        ray.normalize();
        // float depth = 0.0;
        float depth = 0F;
        for (var i = 0; i < 64; i++) {
            // vec3 p = cam + depth * ray;
            val point = new Vector3f(cam).add(new Vector3f(depth).mul(ray));
            // depth = depth + sphere(p)
            depth += sphere(point);
        }
        // vec3 p = cam + depth * ray;
        val point = new Vector3f(cam).add(new Vector3f(depth).mul(ray));
        // if(abs(sphere(p)) < 0.1)
        if (Math.abs(sphere(point)) < 0.1F) {
            // fragColor = vec4(0.3+dot(normalize(p), normalize(vec3(1.0,1.0,0.0))));
            fragColor.set(0.3F + new Vector3f(point).normalize().dot(new Vector3f(1F, 1F, 0F).normalize()));
        }
        // clamp color between 0 and 1 by hand
        fragColor.x = Math.min(Math.max(fragColor.x, 0F), 1F);
        fragColor.y = Math.min(Math.max(fragColor.y, 0F), 1F);
        fragColor.z = Math.min(Math.max(fragColor.z, 0F), 1F);
        return fragColor;
    }

    private static float sphere(Vector3f point) {
        return point.length() - 0.5F;
    }

    @AllArgsConstructor
    private static class Canvas {
        private final ShaderLayout buffer;
        private final BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);

        public void render() {
            buffer.v$parallelStream().forEach(this::renderPixel);
        }

        private void renderPixel(ShaderLayout shaderLayout) {
            val uv = shaderLayout.uv();
            val color = shaderLayout.color();
            val x = Math.round(uv.x() * CANVAS_WIDTH);
            val y = Math.round(uv.y() * CANVAS_HEIGHT);
            val rgb = (int) (color.x() * 255) << 16 | (int) (color.y() * 255) << 8 | (int) (color.z() * 255) | 0xFF000000;
            image.setRGB(x, y, rgb);
        }

        public void showImage() {
            // Create a frame to display the image
            val frame = new JFrame();
            frame.getContentPane()
                 .add(new JLabel(new ImageIcon(image.getScaledInstance(1000, 1000, 1))));
            frame.pack();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }
}
