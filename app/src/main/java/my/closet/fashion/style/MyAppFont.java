package my.closet.fashion.style;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;


import java.lang.reflect.Field;


public class MyAppFont extends Application  {
    // Uses the Croscore_fonts: Arimo (sans), Tinos (serif) and Cousine (monospace)

    // Defining sans as the normal (default) typeface.
    private static final String DEFAULT_NORMAL_BOLD_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_NORMAL_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_NORMAL_NORMAL_FONT_FILENAME = "Rubik-Regular.ttf";

    private static final String DEFAULT_SANS_BOLD_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_SANS_BOLD_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_SANS_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_SANS_NORMAL_FONT_FILENAME = "Rubik-Regular.ttf";

    private static final String DEFAULT_SERIF_BOLD_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_SERIF_BOLD_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_SERIF_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_SERIF_NORMAL_FONT_FILENAME = "Rubik-Regular.ttf";

    private static final String DEFAULT_MONOSPACE_BOLD_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_MONOSPACE_BOLD_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_MONOSPACE_ITALIC_FONT_FILENAME = "Rubik-Regular.ttf";
    private static final String DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME = "Rubik-Regular.ttf";

    // Constants found in the Android documentation
    // http://developer.android.com/reference/android/widget/TextView.html#attr_android:typeface
    private static final int normal_idx = 0;
    private static final int sans_idx = 1;
    private static final int serif_idx = 2;
    private static final int monospace_idx = 3;

    public static SharedPreferences preferences;

    private String myState;

    public String getState(){
        return myState;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            setDefaultFonts();
            // The following code is only necessary if you are using the android:typeface attribute
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setDefaultFontForTypeFaceSans();
                setDefaultFontForTypeFaceSansSerif();
                setDefaultFontForTypeFaceMonospace();
            }
        } catch (Throwable e) {
            // Must not crash app if there is a failure with overriding fonts!
            logFontError(e);
        }
    }

    private void setDefaultFonts() throws NoSuchFieldException, IllegalAccessException {
        final Typeface bold = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(getAssets(), DEFAULT_NORMAL_NORMAL_FONT_FILENAME);

        Field defaultField = Typeface.class.getDeclaredField("DEFAULT");
        defaultField.setAccessible(true);
        defaultField.set(null, normal);

        Field defaultBoldField = Typeface.class.getDeclaredField("DEFAULT_BOLD");
        defaultBoldField.setAccessible(true);
        defaultBoldField.set(null, bold);

        Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
        sDefaults.setAccessible(true);
        sDefaults.set(null, new Typeface[]{normal, bold, italic, boldItalic});

        final Typeface normal_sans = Typeface.createFromAsset(getAssets(), DEFAULT_SANS_NORMAL_FONT_FILENAME);
        Field sansSerifDefaultField = Typeface.class.getDeclaredField("SANS_SERIF");
        sansSerifDefaultField.setAccessible(true);
        sansSerifDefaultField.set(null, normal_sans);

        final Typeface normal_serif = Typeface.createFromAsset(getAssets(), DEFAULT_SERIF_NORMAL_FONT_FILENAME);
        Field serifDefaultField = Typeface.class.getDeclaredField("SERIF");
        serifDefaultField.setAccessible(true);
        serifDefaultField.set(null, normal_serif);

        final Typeface normal_monospace = Typeface.createFromAsset(getAssets(), DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME);
        Field monospaceDefaultField = Typeface.class.getDeclaredField("MONOSPACE");
        monospaceDefaultField.setAccessible(true);
        monospaceDefaultField.set(null, normal_monospace);
    }

    private void setDefaultFontForTypeFaceSans() throws NoSuchFieldException, IllegalAccessException {
        final Typeface bold = Typeface.createFromAsset(getAssets(), DEFAULT_SANS_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(getAssets(), DEFAULT_SANS_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(getAssets(), DEFAULT_SANS_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(getAssets(), DEFAULT_SANS_NORMAL_FONT_FILENAME);

        setTypeFaceDefaults(normal, bold, italic, boldItalic, sans_idx);
    }

    private void setDefaultFontForTypeFaceSansSerif() throws NoSuchFieldException, IllegalAccessException {
        final Typeface bold = Typeface.createFromAsset(getAssets(), DEFAULT_SERIF_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(getAssets(), DEFAULT_SERIF_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(getAssets(), DEFAULT_SERIF_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(getAssets(), DEFAULT_SERIF_NORMAL_FONT_FILENAME);

        setTypeFaceDefaults(normal, bold, italic, boldItalic, serif_idx);
    }

    private void setDefaultFontForTypeFaceMonospace() throws NoSuchFieldException, IllegalAccessException {
        final Typeface bold = Typeface.createFromAsset(getAssets(), DEFAULT_MONOSPACE_BOLD_FONT_FILENAME);
        final Typeface italic = Typeface.createFromAsset(getAssets(), DEFAULT_MONOSPACE_ITALIC_FONT_FILENAME);
        final Typeface boldItalic = Typeface.createFromAsset(getAssets(), DEFAULT_MONOSPACE_BOLD_ITALIC_FONT_FILENAME);
        final Typeface normal = Typeface.createFromAsset(getAssets(), DEFAULT_MONOSPACE_NORMAL_FONT_FILENAME);

        setTypeFaceDefaults(normal, bold, italic, boldItalic, monospace_idx);
    }

    private void setTypeFaceDefaults(Typeface normal, Typeface bold, Typeface italic, Typeface boldItalic, int typefaceIndex) throws NoSuchFieldException, IllegalAccessException {
        Field typeFacesField = Typeface.class.getDeclaredField("sTypefaceCache");
        typeFacesField.setAccessible(true);

        LongSparseArray<LongSparseArray<Typeface>> sTypefaceCacheLocal = new LongSparseArray<LongSparseArray<Typeface>>(3);
        typeFacesField.get(sTypefaceCacheLocal);

        LongSparseArray<Typeface> newValues = new LongSparseArray<Typeface>(4);
        newValues.put(Typeface.NORMAL, normal);
        newValues.put(Typeface.BOLD, bold);
        newValues.put(Typeface.ITALIC, italic);
        newValues.put(Typeface.BOLD_ITALIC, boldItalic);
        sTypefaceCacheLocal.put(typefaceIndex, newValues);

        typeFacesField.set(null, sTypefaceCacheLocal);
    }

    private void logFontError(Throwable e) {
        Log.e("font_override", "Error overriding font", e);
    }

}
