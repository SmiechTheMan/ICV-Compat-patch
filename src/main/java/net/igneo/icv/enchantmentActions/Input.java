package net.igneo.icv.enchantmentActions;

public enum Input {
    FORWARD,
    FORWARD_RIGHT,
    RIGHT,
    BACKWARD_RIGHT,
    BACKWARD,
    BACKWARD_LEFT,
    LEFT,
    FORWARD_LEFT;

    public static Input getInput(int type) {
        return switch (type) {
            default -> Input.FORWARD;
            case (1) -> Input.FORWARD_RIGHT;
            case (2) -> Input.RIGHT;
            case (3) -> Input.BACKWARD_RIGHT;
            case (4) -> Input.BACKWARD;
            case (5) -> Input.BACKWARD_LEFT;
            case (6) -> Input.LEFT;
            case (7) -> Input.FORWARD_LEFT;
        };
    }

    public static Input flattenInput(Input input) {
        return switch (input) {
            default -> input;
            case FORWARD_RIGHT,BACKWARD_RIGHT -> Input.RIGHT;
            case FORWARD_LEFT,BACKWARD_LEFT -> Input.LEFT;
        };
    }

    public static int getRotation(Input input) {
        return switch (input) {
            case FORWARD -> 0;
            case FORWARD_RIGHT -> 45;
            case RIGHT -> 90;
            case BACKWARD_RIGHT -> 135;
            case BACKWARD -> 180;
            case BACKWARD_LEFT -> 225;
            case LEFT -> 270;
            case FORWARD_LEFT -> 315;
        };
    }
}
