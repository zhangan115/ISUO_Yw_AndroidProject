package filepicker.filter;

import java.io.File;
import java.io.Serializable;

public interface FileFilter extends Serializable {
    boolean accept(File pathname);
}
