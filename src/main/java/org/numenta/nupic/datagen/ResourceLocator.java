package org.numenta.nupic.datagen;

import java.io.File;
import java.net.URI;
import java.net.URL;


public interface ResourceLocator {
    @FunctionalInterface
    interface Resource {
        File get();
    }
    
    static URI uri(String s) {
        try {
            URL url = new URL(s);
            return url.toURI();
        }catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    static String path(String s) {
        URL url = ResourceLocator.class.getResource(s);
        if(url == null) {
            url = ResourceLocator.class.getClassLoader().getResource(s);
        }
        return new File(url.getPath()).getPath();
    }
    
    static String locate(String s) {
        return ResourceLocator.class.getPackage().getName().replace('.', '/') + File.separator + s;
    }
}
