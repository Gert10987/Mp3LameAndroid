package info.gert.mp3lameAndroid;

/**
 * Created by gert on 03.05.17.
 */

@SuppressWarnings("JniMissingFunction")
public class Mp3Encoder {

    static {
        System.loadLibrary("mp3lame");
    }
    public native int encode(String sourcePath, String targetPath);
}
