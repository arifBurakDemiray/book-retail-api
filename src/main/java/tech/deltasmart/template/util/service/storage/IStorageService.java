package com.bookretail.util.service.storage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

public interface IStorageService {
    URL put(File file);

    URL put(File file, Path path);

    URL put(InputStream inputStream, Path path);

    URL put(InputStream inputStream);

    InputStream get(Path path);

    void delete(Path path);

    void delete(URL url);

    URL getRemoteUrl(Path path);
}
