package amarandus.rscGenerator.utils;

import java.io.File;

public class FileWrapper
{
    private final File f;

    public FileWrapper(File f)
    {
        this.f = f;
    }

    public File getF()
    {
        return f;
    }

    @Override
    public String toString()
    {
        return f.getName();
    }
}
