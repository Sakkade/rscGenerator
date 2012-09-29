package amarandus.rscGenerator.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jdom2.Element;

public class DirAdder extends Thread
{
    String  dir;
    Element node;

    public DirAdder(String dir, Element node)
    {
        this.dir = dir;
        this.node = node;
    }

    @Override
    public void run()
    {
        addDirNode(this.node, new File(this.dir));
    }

    private void addDirNode(Element root, File dir)
    {
        Element dirnode = new Element("Directory");

        root.addContent(dirnode);

        dirnode.setAttribute("name", dir.getName());

        File[] list = dir.listFiles();

        for (File f : list)
        {
            if (f.isFile() && f.exists())
            {
                addFileNode(dirnode, f);
            } else if (f.isDirectory())
            {
                addDirNode(dirnode, f);
            }
        }
    }

    private void addFileNode(Element dirnode, File f)
    {
        Element filenode = new Element("File");
        dirnode.addContent(filenode);

        filenode.setAttribute("size", f.length() + "");
        filenode.setAttribute("sha1", calcSHA1(f));
        filenode.setAttribute("name", f.getName());
    }

    private String calcSHA1(File f)
    {
        MessageDigest hashDigester = null;
        FileInputStream readFileStream;
        StringBuffer sha1StringBuffer = null;
        try
        {
            hashDigester = MessageDigest.getInstance("SHA-1");
            readFileStream = new FileInputStream(f);
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1)
            {
                n = readFileStream.read(buffer);
                if (n > 0)
                {
                    hashDigester.update(buffer, 0, n);
                }
            }
            readFileStream.close();

            byte[] mdbytes = hashDigester.digest();

            // convert the byte to hex format
            sha1StringBuffer = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++)
            {
                sha1StringBuffer.append(Integer.toString(
                        (mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sha1StringBuffer.toString();
    }
}
