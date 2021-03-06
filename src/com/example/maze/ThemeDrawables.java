package com.example.maze;

import android.util.SparseArray;

import java.util.HashMap;

/**
 * Class to provide the id's to the drawables of the given theme
 *
 * Set the theme to one of the provided theme names
 */
public class ThemeDrawables {

    public final static int INDUSTRIAL_THEME = 0;
    public final static int RETRO_THEME = 1;

    private static int currentTheme = 0;
    private final static String FINISH = "finish";
    private final static String START = "start";
    private final static String WALL = "wall";
    private final static String PATH = "path";
    private final static String NORTH = "guy_north";
    private final static String EAST = "guy_east";
    private final static String SOUTH = "guy_south";
    private final static String WEST = "guy_west";
    private final static String PREVIEW = "preview";
    private final static String BACKGROUND = "background";

    private static HashMap<String, Integer> industrial_map;

    static {
        industrial_map = new HashMap<String, Integer>();
        industrial_map.put(FINISH, R.drawable.industrial_finish);
        industrial_map.put(START, R.drawable.industrial_start);
        industrial_map.put(WALL, R.drawable.industrial_wall);
        industrial_map.put(PATH, R.drawable.industrial_path);
        industrial_map.put(NORTH, R.drawable.industrial_guy_north);
        industrial_map.put(EAST, R.drawable.industrial_guy_east);
        industrial_map.put(SOUTH, R.drawable.industrial_guy_south);
        industrial_map.put(WEST, R.drawable.industrial_guy_west);
        industrial_map.put(PREVIEW, R.drawable.industrial_preview);
        industrial_map.put(BACKGROUND, R.drawable.industrial_path);
    }

    private static HashMap<String, Integer> retro_map;

    static {
        retro_map = new HashMap<String, Integer>();
        retro_map.put(FINISH, R.drawable.retro_finish);
        retro_map.put(START, R.drawable.retro_path);
        retro_map.put(WALL, R.drawable.retro_wall);
        retro_map.put(PATH, R.drawable.retro_path);
        retro_map.put(NORTH, R.drawable.retro_guy_north);
        retro_map.put(EAST, R.drawable.retro_guy_east);
        retro_map.put(SOUTH, R.drawable.retro_guy_south);
        retro_map.put(WEST, R.drawable.retro_guy_west);
        retro_map.put(PREVIEW, R.drawable.retro_preview);
        retro_map.put(BACKGROUND, R.drawable.retro_path);
    }

    private static SparseArray<HashMap<String, Integer>> themeMap;

    static {
        themeMap = new SparseArray<HashMap<String, Integer>>();
        themeMap.put(INDUSTRIAL_THEME, industrial_map);
        themeMap.put(RETRO_THEME, retro_map);
    }

    public static int getFinish() {
        return themeMap.get(currentTheme).get(FINISH);
    }

    public static int getStart() {
        return themeMap.get(currentTheme).get(START);
    }

    public static int getWall() {
        return themeMap.get(currentTheme).get(WALL);
    }

    public static int getPath() {
        return themeMap.get(currentTheme).get(PATH);
    }

    public static int getGuyNorth() {
        return themeMap.get(currentTheme).get(NORTH);
    }

    public static int getGuyEast() {
        return themeMap.get(currentTheme).get(EAST);
    }

    public static int getGuySouth() {
        return themeMap.get(currentTheme).get(SOUTH);
    }

    public static int getGuyWest() {
        return themeMap.get(currentTheme).get(WEST);
    }

    public static int getPreview() {
        return themeMap.get(currentTheme).get(PREVIEW);
    }

    public static int getBackground() {
        return themeMap.get(currentTheme).get(BACKGROUND);
    }

    public static void setTheme(int theme) {
        currentTheme = theme;
    }

    private ThemeDrawables() {
    }
}
