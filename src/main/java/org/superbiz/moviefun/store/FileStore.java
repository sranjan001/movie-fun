package org.superbiz.moviefun;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Optional;
import static java.lang.String.format;

@Component
public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {

        File targetFile = getFile(blob.name);

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while((bytesRead = blob.inputStream.read(buffer)) !=-1){
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {

        File targetFile = getFile(name);

        if(!targetFile.exists())
            return Optional.empty();

        FileInputStream inputStream = new FileInputStream(targetFile);

        Blob blob = new Blob(name, inputStream, null);

        return Optional.of(blob);
    }

    @Override
    public void deleteAll() {
        File coverFolder = getFile("cover");

        if(coverFolder.exists()) {
            deleteDirectory(coverFolder);
        }
    }

    private File getFile(String albumName) {
        File file = new File(format("cover/%s", albumName));

        return file;

    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

}
