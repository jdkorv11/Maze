package com.example.maze;

import java.util.HashMap;

/**
 * Class to provide the id's to the drawables of the given theme
 *
 * Set the theme to one of the provided theme names
 */
public class ThemeDrawables {

    public final static int INDUSTRIAL_THEME = 0;
    public final static int OTHER_THEME = 1;

    private static int currentTheme;
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

    private static HashMap<String, Integer> other_map;

    static {
        other_map = new HashMap<String, Integer>();
        other_map.put(FINISH, R.drawable.industrial_finish);
        other_map.put(START, R.drawable.industrial_start);
        other_map.put(WALL, R.drawable.industrial_wall);
        other_map.put(PATH, R.drawable.industrial_path);
        other_map.put(NORTH, R.drawable.industrial_guy_north);
        other_map.put(EAST, R.drawable.industrial_guy_east);
        other_map.put(SOUTH, R.drawable.industrial_guy_south);
        other_map.put(WEST, R.drawable.industrial_guy_west);
        other_map.put(PREVIEW, R.drawable.industrial_preview);
        other_map.put(BACKGROUND, R.drawable.industrial_path);
    }

    private static HashMap<Integer, HashMap<String, Integer>> themeMap;

    static {
        themeMap = new HashMap<Integer, HashMap<String, Integer>>();
        themeMap.put(INDUSTRIAL_THEME, industrial_map);
        themeMap.put(OTHER_THEME, other_map);
    }

    public int getFinish() {
        return themeMap.get(currentTheme).get(FINISH);
    }

    public int getStart() {
        return themeMap.get(currentTheme).get(START);
    }

    public int getWall() {
        return themeMap.get(currentTheme).get(WALL);
    }

    public int getPath() {
        return themeMap.get(currentTheme).get(PATH);
    }

    public int getGuyNorth() {
        return themeMap.get(currentTheme).get(NORTH);
    }

    public int getGuyEast() {
        return themeMap.get(currentTheme).get(EAST);
    }

    public int getGuySouth() {
        return themeMap.get(currentTheme).get(SOUTH);
    }

    public int getGuyWest() {
        return themeMap.get(currentTheme).get(WEST);
    }

    public int getPreview() {
        return themeMap.get(currentTheme).get(PREVIEW);
    }

    public int getBackground() {
        return themeMap.get(currentTheme).get(BACKGROUND);
    }

    public static void setTheme(int theme) {
        currentTheme = theme;
    }

    private ThemeDrawables() {
    }
}
