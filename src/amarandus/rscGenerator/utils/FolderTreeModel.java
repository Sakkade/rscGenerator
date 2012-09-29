package amarandus.rscGenerator.utils;

import java.io.File;
import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FolderTreeModel implements TreeModel
{
    private final FileWrapper root;
    private final boolean     showFiles;

    public FolderTreeModel(FileWrapper root)
    {
        this.root = root;
        this.showFiles = false;
    }

    public FolderTreeModel(FileWrapper root, boolean showFiles)
    {
        this.root = root;
        this.showFiles = showFiles;
    }

    @Override
    public void addTreeModelListener(TreeModelListener arg0)
    {

    }

    @Override
    public Object getChild(Object node, int index)
    {
        File[] children = ((FileWrapper) node).getF().listFiles();
        ArrayList<File> childs = new ArrayList<File>();
        for (File f : children)
        {
            if (this.showFiles)
            {
                childs.add(f);
            } else if (f.isDirectory())
            {
                childs.add(f);
            }

        }
        return new FileWrapper(childs.get(index));
    }

    @Override
    public int getChildCount(Object node)
    {
        int i = 0;
        File d = ((FileWrapper) node).getF();
        for (File f : d.listFiles())
        {
            if (this.showFiles)
            {
                ++i;
            } else if (f.isDirectory())
            {
                ++i;
            }
        }
        return i;
    }

    @Override
    public int getIndexOfChild(Object node, Object child)
    {
        File[] children = ((FileWrapper) node).getF().listFiles();
        ArrayList<File> dirs = new ArrayList<File>();
        for (File f : children)
        {
            if (f.isDirectory())
                dirs.add(f);
        }
        return dirs.indexOf(child);
    }

    @Override
    public Object getRoot()
    {
        return this.root;
    }

    @Override
    public boolean isLeaf(Object node)
    {
        return ((FileWrapper) node).getF().isFile();
    }

    @Override
    public void removeTreeModelListener(TreeModelListener arg0)
    {

    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {

    }
}