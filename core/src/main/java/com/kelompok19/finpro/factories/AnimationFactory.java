package com.kelompok19.finpro.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AnimationFactory implements Disposable {
    private static AnimationFactory instance;
    private final Map<String, Animation<TextureRegion>> cache = new HashMap<>();
    private final Map<String, Texture> loadedTextures = new HashMap<>();

    public static AnimationFactory getInstance() {
        if (instance == null) {
            instance = new AnimationFactory();
        }

        return instance;
    }

    public Animation<TextureRegion> getAnimation(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        try {
            Texture sheet = new Texture(Gdx.files.internal(path));
            loadedTextures.put(path, sheet);

            TextureRegion[][] tmp = TextureRegion.split(sheet, 100, 100);
            int cols = tmp[0].length;
            TextureRegion[] frames = new TextureRegion[cols];
            System.arraycopy(tmp[0], 0, frames, 0, cols);

            Animation<TextureRegion> anim = new Animation<>(0.1f, frames);

            cache.put(path, anim);

            return anim;

        }

        catch (Exception e) {
            Gdx.app.error("AnimationFactory", "Failed to load: " + path);
            return null;
        }
    }

    @Override
    public void dispose() {
        for (Texture t : loadedTextures.values()) {
            t.dispose();
        }

        loadedTextures.clear();
        cache.clear();
    }
}
