package acidhax.cordova.chromecast;

import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.TextTrackStyle;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

class ChromecastUtilities {
    static String getMediaIdleReason(MediaStatus mediaStatus) {
        switch (mediaStatus.getIdleReason()) {
            case MediaStatus.IDLE_REASON_CANCELED:
                return "canceled";
            case MediaStatus.IDLE_REASON_ERROR:
                return "error";
            case MediaStatus.IDLE_REASON_FINISHED:
                return "finished";
            case MediaStatus.IDLE_REASON_INTERRUPTED:
                return "interrupted";
            case MediaStatus.IDLE_REASON_NONE:
                return "none";
            default:
                return null;
        }
    }

    static String getMediaPlayerState(MediaStatus mediaStatus) {
        switch (mediaStatus.getPlayerState()) {
            case MediaStatus.PLAYER_STATE_BUFFERING:
                return "BUFFERING";
            case MediaStatus.PLAYER_STATE_IDLE:
                return "IDLE";
            case MediaStatus.PLAYER_STATE_PAUSED:
                return "PAUSED";
            case MediaStatus.PLAYER_STATE_PLAYING:
                return "PLAYING";
            case MediaStatus.PLAYER_STATE_UNKNOWN:
                return "UNKNOWN";
            default:
                return null;
        }
    }

    static String getMediaInfoStreamType(MediaInfo mediaInfo) {
        switch (mediaInfo.getStreamType()) {
            case MediaInfo.STREAM_TYPE_BUFFERED:
                return "buffered";
            case MediaInfo.STREAM_TYPE_LIVE:
                return "live";
            case MediaInfo.STREAM_TYPE_NONE:
                return "other";
            default:
                return null;
        }
    }

    static String getTrackType(MediaTrack track) {
        switch (track.getType()) {
            case MediaTrack.TYPE_AUDIO:
                return "AUDIO";
            case MediaTrack.TYPE_TEXT:
                return "TEXT";
            case MediaTrack.TYPE_VIDEO:
                return "VIDEO";
            default:
                return null;
        }
    }

    static String getTrackSubtype(MediaTrack track) {
        switch (track.getSubtype()) {
            case MediaTrack.SUBTYPE_CAPTIONS:
                return "CAPTIONS";
            case MediaTrack.SUBTYPE_CHAPTERS:
                return "CHAPTERS";
            case MediaTrack.SUBTYPE_DESCRIPTIONS:
                return "DESCRIPTIONS";
            case MediaTrack.SUBTYPE_METADATA:
                return "METADATA";
            case MediaTrack.SUBTYPE_SUBTITLES:
                return "SUBTITLES";
            case MediaTrack.SUBTYPE_NONE:
                return null;
            default:
                return null;
        }
    }

    static String getEdgeType(TextTrackStyle textTrackStyle) {
        switch (textTrackStyle.getEdgeType()) {
            case TextTrackStyle.EDGE_TYPE_DEPRESSED:
                return "DEPRESSED";
            case TextTrackStyle.EDGE_TYPE_DROP_SHADOW:
                return "DROP_SHADOW";
            case TextTrackStyle.EDGE_TYPE_OUTLINE:
                return "OUTLINE";
            case TextTrackStyle.EDGE_TYPE_RAISED:
                return "RAISED";
            case TextTrackStyle.EDGE_TYPE_NONE:
            default:
                return "NONE";
        }
    }

    static String getFontGenericFamily(TextTrackStyle textTrackStyle) {
        switch (textTrackStyle.getFontGenericFamily()) {
            case TextTrackStyle.FONT_FAMILY_CURSIVE:
                return "CURSIVE";
            case TextTrackStyle.FONT_FAMILY_MONOSPACED_SANS_SERIF:
                return "MONOSPACED_SANS_SERIF";
            case TextTrackStyle.FONT_FAMILY_MONOSPACED_SERIF:
                return "MONOSPACED_SERIF";
            case TextTrackStyle.FONT_FAMILY_SANS_SERIF:
                return "SANS_SERIF";
            case TextTrackStyle.FONT_FAMILY_SERIF:
                return "SERIF";
            case TextTrackStyle.FONT_FAMILY_SMALL_CAPITALS:
                return "SMALL_CAPITALS";
            default:
                return "SERIF";
        }
    }

    static String getFontStyle(TextTrackStyle textTrackStyle) {
        switch (textTrackStyle.getFontStyle()) {
            case TextTrackStyle.FONT_STYLE_NORMAL:
                return "NORMAL";
            case TextTrackStyle.FONT_STYLE_BOLD:
                return "BOLD";
            case TextTrackStyle.FONT_STYLE_BOLD_ITALIC:
                return "BOLD_ITALIC";
            case TextTrackStyle.FONT_STYLE_ITALIC:
                return "ITALIC";
            case TextTrackStyle.FONT_STYLE_UNSPECIFIED:
            default:
                return "NORMAL";
        }
    }

    static String getWindowType(TextTrackStyle textTrackStyle) {
        switch (textTrackStyle.getWindowType()) {
            case TextTrackStyle.WINDOW_TYPE_NORMAL:
                return "NORMAL";
            case TextTrackStyle.WINDOW_TYPE_ROUNDED:
                return "ROUNDED_CORNERS";
            case TextTrackStyle.WINDOW_TYPE_NONE:
            default:
                return "NONE";
        }
    }

    /**
     * Replace CSS Color String if contains alpha channel. In Android Color
     * the format is #AARRGGBB and not #RRGGBBAA.
     */
    private static int parseColor(String cssString) {
      String regex = "\\#(.{6})(.{2})?"; // #RRGGBBAA
      String replaced = cssString.replaceAll(regex, "#$2$1"); // #AARRGGBB

      return Color.parseColor(replaced);
    }

    static TextTrackStyle parseTextTrackStyle(JSONObject textTrackSytle) {
        TextTrackStyle out = new TextTrackStyle();

        if (textTrackSytle == null) {
            return out;
        }

        try {
            if (!textTrackSytle.isNull("backgroundColor")) {
                out.setBackgroundColor(parseColor(textTrackSytle.getString("backgroundColor")));
            }

            if (!textTrackSytle.isNull("edgeColor")) {
                out.setEdgeColor(parseColor(textTrackSytle.getString("edgeColor")));
            }

            if (!textTrackSytle.isNull("edgeType")) {
                out.setEdgeType(parseEdgeType(textTrackSytle.getString("edgeType")));
            }

            if (!textTrackSytle.isNull("fontFamily")) {
                out.setFontFamily(textTrackSytle.getString("fontFamily"));
            }

            if (!textTrackSytle.isNull("fontGenericFamily")) {
                out.setFontGenericFamily(parseFontGenericFamily(textTrackSytle.getString("fontGenericFamily")));
            }

            if (!textTrackSytle.isNull("fontScale")) {
                out.setFontScale((float)textTrackSytle.getDouble("fontScale"));
            }

            if (!textTrackSytle.isNull("fontStyle")) {
                out.setFontStyle(parseFontStyle(textTrackSytle.getString("fontStyle")));
            }

            if (!textTrackSytle.isNull("foregroundColor")) {
                out.setForegroundColor(parseColor(textTrackSytle.getString("foregroundColor")));
            }

            if (!textTrackSytle.isNull("windowColor")) {
                out.setWindowColor(parseColor(textTrackSytle.getString("windowColor")));
            }

            if (!textTrackSytle.isNull("windowCornerRadius")) {
                out.setWindowCornerRadius(textTrackSytle.getInt("windowCornerRadius"));
            }

            if (!textTrackSytle.isNull("windowType")) {
                out.setWindowType(parseWindowType(textTrackSytle.getString("windowType")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return out;
    }

    static int parseEdgeType(String style) {
        switch (style) {
            case "DEPRESSED":
                return TextTrackStyle.EDGE_TYPE_DEPRESSED;
            case "DROP_SHADOW":
                return TextTrackStyle.EDGE_TYPE_DROP_SHADOW;
            case "OUTLINE":
                return TextTrackStyle.EDGE_TYPE_OUTLINE;
            case "RAISED":
                return TextTrackStyle.EDGE_TYPE_RAISED;
            case "NONE":
            default:
                return TextTrackStyle.EDGE_TYPE_NONE;
        }
    }

    static int parseFontGenericFamily(String style) {
        switch (style) {
            case "CURSIVE":
                return TextTrackStyle.FONT_FAMILY_CURSIVE;
            case "MONOSPACED_SANS_SERIF":
                return TextTrackStyle.FONT_FAMILY_MONOSPACED_SANS_SERIF;
            case "MONOSPACED_SERIF":
                return TextTrackStyle.FONT_FAMILY_MONOSPACED_SERIF;
            case "SANS_SERIF":
                return TextTrackStyle.FONT_FAMILY_SANS_SERIF;
            case "SERIF":
                return TextTrackStyle.FONT_FAMILY_SERIF;
            case "SMALL_CAPITALS":
                return TextTrackStyle.FONT_FAMILY_SMALL_CAPITALS;
            default:
                return TextTrackStyle.FONT_FAMILY_SERIF;
        }
    }

    static int parseFontStyle(String style) {
        switch (style) {
            case "NORMAL":
                return TextTrackStyle.FONT_STYLE_NORMAL;
            case "BOLD":
                return TextTrackStyle.FONT_STYLE_BOLD;
            case "BOLD_ITALIC":
                return TextTrackStyle.FONT_STYLE_BOLD_ITALIC;
            case "ITALIC":
                return TextTrackStyle.FONT_STYLE_ITALIC;
            default:
                return TextTrackStyle.FONT_STYLE_UNSPECIFIED;
        }
    }

    static int parseWindowType(String style) {
        switch (style) {
            case "NORMAL":
                return TextTrackStyle.WINDOW_TYPE_NORMAL;
            case "ROUNDED_CORNERS":
                return TextTrackStyle.WINDOW_TYPE_ROUNDED;
            case "NONE":
            default:
                return TextTrackStyle.WINDOW_TYPE_NONE;
        }
    }

    static String getHexColor(int color) {
        return "#" + Integer.toHexString(color);
    }

    static JSONObject createTextTrackObject(TextTrackStyle textTrackStyle) {
        JSONObject out = new JSONObject();
        try {
            out.put("backgroundColor", getHexColor(textTrackStyle.getBackgroundColor()));
            out.put("customData", textTrackStyle.getCustomData());
            out.put("edgeColor", getHexColor(textTrackStyle.getEdgeColor()));
            out.put("edgeType", getEdgeType(textTrackStyle));
            out.put("fontFamily", textTrackStyle.getFontFamily());
            out.put("fontGenericFamily", getFontGenericFamily(textTrackStyle));
            out.put("fontScale", textTrackStyle.getFontScale());
            out.put("fontStyle", getFontStyle(textTrackStyle));
            out.put("foregroundColor", getHexColor(textTrackStyle.getForegroundColor()));
            out.put("windowColor", getHexColor(textTrackStyle.getWindowColor()));
            out.put("windowRoundedCornerRadius", textTrackStyle.getWindowCornerRadius());
            out.put("windowType", getWindowType(textTrackStyle));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return out;
    }
}
