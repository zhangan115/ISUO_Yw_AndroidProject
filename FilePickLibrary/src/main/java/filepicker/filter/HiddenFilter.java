package filepicker.filter;

import java.io.File;

public class HiddenFilter implements FileFilter {

    @Override
    public boolean accept(File f) {
        return !f.isHidden();
    }
}
