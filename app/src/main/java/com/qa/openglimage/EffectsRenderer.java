package com.qa.openglimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by sev_user on 06-Apr-15.
 */
public class EffectsRenderer implements GLSurfaceView.Renderer {

    private Bitmap photo;
    private int photoWidth, photoHeight;

    public EffectsRenderer(Context context) {
        super();

        photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.forest);
        photoWidth = photo.getWidth();
        photoHeight = photo.getHeight();
    }

    private int[] textures = new int[2];
    private Square mSquare;

    private void generateSquare() {
        GLES20.glGenTextures(2, textures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0);
        mSquare = new Square();
    }

    /**
     * Called when the surface is created or recreated.
     * <p/>
     * Called when the rendering thread
     * starts and whenever the EGL context is lost. The EGL context will typically
     * be lost when the Android device awakes after going to sleep.
     * <p/>
     * Since this method is called at the beginning of rendering, as well as
     * every time the EGL context is lost, this method is a convenient place to put
     * code to create resources that need to be created when the rendering
     * starts, and that need to be recreated when the EGL context is lost.
     * Textures are an example of a resource that you might want to create
     * here.
     * <p/>
     * Note that when the EGL context is lost, all OpenGL resources associated
     * with that context will be automatically deleted. You do not need to call
     * the corresponding "glDelete" methods such as glDeleteTextures to
     * manually delete these lost resources.
     * <p/>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param config the EGLConfig of the created surface. Can be used
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    /**
     * Called when the surface changed size.
     * <p/>
     * Called after the surface is created and whenever
     * the OpenGL ES surface size changes.
     * <p/>
     * Typically you will set your viewport here. If your camera
     * is fixed then you could also set your projection matrix here:
     * <pre class="prettyprint">
     * void onSurfaceChanged(GL10 gl, int width, int height) {
     * gl.glViewport(0, 0, width, height);
     * // for a fixed camera, set the projection too
     * float ratio = (float) width / height;
     * gl.glMatrixMode(GL10.GL_PROJECTION);
     * gl.glLoadIdentity();
     * gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
     * }
     * </pre>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0, 0, 0, 1);
        generateSquare();
    }

    /**
     * Called to draw the current frame.
     * <p/>
     * This method is responsible for drawing the current frame.
     * <p/>
     * The implementation of this method typically looks like this:
     * <pre class="prettyprint">
     * void onDrawFrame(GL10 gl) {
     * gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
     * //... other gl calls to render the scene ...
     * }
     * </pre>
     *
     * @param gl the GL interface. Use <code>instanceof</code> to
     *           test if the interface supports GL11 or higher interfaces.
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        if (mEffectContext == null) {
            mEffectContext = EffectContext.createWithCurrentGlContext();
        }

        if (mEffect != null) {
            mEffect.release();
        }

        // grayScaleEffect();
        // documentaryEffect();
        brightnessEffect();
        mSquare.draw(textures[1]);
    }

    private EffectContext mEffectContext;
    private Effect mEffect;

    private void grayScaleEffect() {
        EffectFactory factory = mEffectContext.getFactory();
        mEffect = factory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
        mEffect.apply(textures[0], photoWidth, photoHeight, textures[1]);
    }

    private void documentaryEffect() {
        EffectFactory factory = mEffectContext.getFactory();
        mEffect = factory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
        mEffect.apply(textures[0], photoWidth, photoHeight, textures[1]);
    }

    private void brightnessEffect() {
        EffectFactory factory = mEffectContext.getFactory();
        mEffect = factory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
        mEffect.setParameter("brightness", 2f);
        mEffect.apply(textures[0], photoWidth, photoHeight, textures[1]);
    }
}
