package com.eddystudio.shuttletracker.Symbols;

import com.eddystudio.shuttletracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SymbolContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SymbolItem> ITEMS = new ArrayList<SymbolItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SymbolItem> ITEM_MAP = new HashMap<String, SymbolItem>();


    private static String[] rout_name = {
            "Arriba&Noble",
            "Coaster East&West",
            "Campus Loop Shuttle",
            "East Regents",
            "East Campus Connector",
            "Hillcrest",
            "Mesa Housing",
            "Scripps Institution"
    };
    private static String[] content = {
            "Runs between campus and the Regents and Nobel areas.",
            "Runs between the Sorrento Valley Coaster Station and campus",
            "Runs clockwise and counterclockwise weekdays during fall, winter, and spring quarters",
            "Runs weekdays between Rupertus Ln. near the Student Services Center and parking lot P704",
            "Runs weekdays between UC San Diego Health – La Jolla, east campus parking areas and UC San Diego Health",
            "Travels between UC San Diego Medical Center in Hillcrest, Old Town Transit Center, and UC San Diego Medical Center in La Jolla",
            "Runs in a clockwise loop between campus and the Mesa Housing complex off Regents Road",
            "Runs in a counterclockwise loop between Mandeville Ln. and SIO"
    };
    private static int[] image_name = {
            R.mipmap.a_icon,
            R.mipmap.c_icon,
            R.mipmap.cl_icon,
            R.mipmap.er_icon,
            R.mipmap.ecc_icon,
            R.mipmap.hc_icon,
            R.mipmap.m_icon,
            R.mipmap.sio_icon
            /*
            "file:///android_asset/symbol_image/A-icon.png",
            "file:///android_asset/symbol_image/C-icon.png",
            "file:///android_asset/symbol_image/ER-icon.png",
            "file:///android_asset/symbol_image/HC-icon.png",
            "file:///android_asset/symbol_image/M-icon.png",
            "file:///android_asset/symbol_image/SC-icon.png",
            "file:///android_asset/symbol_image/SIO-icon.png"
            */,
    };


    static {
        // Add some sample items.
        for (int i = 1; i <= rout_name.length; i++) {
            addItem(creatSymbol(i, rout_name[i-1], content[i-1], image_name[i-1]));
        }
    }

    private static void addItem(SymbolItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SymbolItem creatSymbol(int id, String rout_name, String content, int Image_name) {
        return new SymbolItem(String.valueOf(id), rout_name, content, Image_name );
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class SymbolItem {
        public final String id;
        public final String rout_name;
        public final String content;
        public final int Image_name;

        public SymbolItem(String id, String rout_name, String content, int Image_name) {
            this.rout_name = rout_name;
            this.content = content;
            this.Image_name = Image_name;
            this.id = id;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
