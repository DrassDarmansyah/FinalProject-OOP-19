package com.kelompok19.finpro.states;

public interface GameState {
    void update(float delta);
    void render();
    void dispose();
}
