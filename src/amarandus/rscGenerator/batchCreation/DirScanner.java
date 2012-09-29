package amarandus.rscGenerator.batchCreation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultListModel;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DirScanner extends Thread
{
    String            targetFolder;
    String            outputDir;
    int               listID;
    DefaultListModel  listModel;
    BoundedRangeModel progressModel;

    public DirScanner(String targetFolder, DefaultListModel listModel,
            String outputDir, BoundedRangeModel progressModel)
    {
        this.targetFolder = targetFolder;
        this.listModel = listModel;
        this.listID = listModel.indexOf(targetFolder);
        this.outputDir = outputDir;
        this.progressModel = progressModel;
    }

    @Override
    public void run()
    {
        Element root = new Element("RsCollection");
        Document document = new Document(root, new DocType("RsCollection"));

        Timestamp currentTimestamp = new java.sql.Timestamp(Calendar
                .getInstance().getTime().getTime());

        File dirFile = new File(this.targetFolder);
        addDirNode(root, dirFile);

        File output = new File(this.outputDir + dirFile.getName() + "."
                + currentTimestamp.getTime() + ".rsCollection");

        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat()
                .setOmitDeclaration(true));

        FileWriter writer;
        try
        {
            writer = new FileWriter(output);
            xmlOutput.output(document, writer);
            writer.flush();
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        listModel.set(listID, targetFolder + " - (DONE)");
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
                this.progressModel
                        .setMaximum(this.progressModel.getMaximum() + 1);
                addFileNode(dirnode, f);
            } else if (f.isDirectory())
            {
                this.progressModel
                        .setMaximum(this.progressModel.getMaximum() + 1);
                addDirNode(dirnode, f);
            }
        }

        this.progressModel.setValue(this.progressModel.getValue() + 1);
    }

    private void addFileNode(Element dirnode, File f)
    {
        Element filenode = new Element("File");
        dirnode.addContent(filenode);

        filenode.setAttribute("size", f.length() + "");
        filenode.setAttribute("sha1", calcSHA1(f));
        filenode.setAttribute("name", f.getName());

        this.progressModel.setValue(this.progressModel.getValue() + 1);
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
