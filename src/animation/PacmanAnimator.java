package animation;

import model.PacmanModel;
import view.CharacterRenderer;

// Animation class responsible for Pacman animation using Thread
public class PacmanAnimator implements Runnable {
    private PacmanModel pacman;
    private CharacterRenderer renderer;
    private Thread animationThread;
    private volatile boolean running;
    private static final int ANIMATION_DELAY_MS = 200;

    public PacmanAnimator(PacmanModel pacman, CharacterRenderer renderer) {
        this.pacman = pacman;
        this.renderer = renderer;
        this.running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public void stop() {
        running = false;
        if (animationThread != null) {
            animationThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Update the animation frame
                pacman.nextFrame();
                // Use the updated method name
                renderer.updateImage();

                // Sleep for the animation delay
                Thread.sleep(ANIMATION_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}