package mynameisjeff.simpletogglesprint.utils;

import mynameisjeff.simpletogglesprint.core.Config;

public class Color {
    public static int getColor() {
        if (Config.isAdvanced == 1) return new java.awt.Color(Config.displayStateRed, Config.displayStateGreen, Config.displayStateBlue).getRGB();
        switch (Config.simpleColor) {
            case 0:
                return ColorEnum.WHITE;
            case 1:
                return ColorEnum.LIGHT_GRAY;
            case 2:
                return ColorEnum.GRAY;
            case 3:
                return ColorEnum.DARK_GRAY;
            case 4:
                return ColorEnum.BLACK;
            case 5:
                return ColorEnum.RED;
            case 6:
                return ColorEnum.PINK;
            case 7:
                return ColorEnum.ORANGE;
            case 8:
                return ColorEnum.YELLOW;
            case 9:
                return ColorEnum.GREEN;
            case 10:
                return ColorEnum.MAGENTA;
            case 11:
                return ColorEnum.CYAN;
            case 12:
                return ColorEnum.BLUE;
            default:
                //honestly i have no idea why but chroma doesn't work if i put it in the static interface
                return java.awt.Color.HSBtoRGB(System.currentTimeMillis() % 2000L / 2000.0F, 0.8F, 0.8F);
        }
    }
    public enum ColorEnum {;
        static int WHITE = java.awt.Color.WHITE.getRGB();
        static int LIGHT_GRAY = java.awt.Color.LIGHT_GRAY.getRGB();
        static int GRAY = java.awt.Color.GRAY.getRGB();
        static int DARK_GRAY = java.awt.Color.DARK_GRAY.getRGB();
        static int BLACK = java.awt.Color.BLACK.getRGB();
        static int RED = java.awt.Color.RED.getRGB();
        static int PINK = java.awt.Color.PINK.getRGB();
        static int ORANGE = java.awt.Color.ORANGE.getRGB();
        static int YELLOW = java.awt.Color.YELLOW.getRGB();
        static int GREEN = java.awt.Color.GREEN.getRGB();
        static int MAGENTA = java.awt.Color.MAGENTA.getRGB();
        static int CYAN = java.awt.Color.CYAN.getRGB();
        static int BLUE = java.awt.Color.BLUE.getRGB();
    }
}