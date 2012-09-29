package amarandus.rscGenerator.utils;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdom2.Document;

public class XMLTreeModel implements TreeModel
{
    private final Document document;

    // constructor used to set the document to view
    public XMLTreeModel(Document doc)
    {
        document = doc;
    }

    // override from TreeModel
    @Override
    public Object getRoot()
    {
        if (document == null)
            return null;
        return new XMLNodeWrapper(document.getRootElement());
    }

    // override from TreeModel
    @Override
    public Object getChild(Object parent, int index)
    {
        XMLNodeWrapper node = (XMLNodeWrapper) parent;
        return node.child(index);
    }

    // override from TreeModel
    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        XMLNodeWrapper node = (XMLNodeWrapper) parent;
        return node.index((XMLNodeWrapper) child);
    }

    // override from TreeModel
    @Override
    public int getChildCount(Object parent)
    {
        XMLNodeWrapper elementNode = (XMLNodeWrapper) parent;
        return elementNode.childCount();
    }

    // override from TreeModel
    @Override
    public boolean isLeaf(Object node)
    {
        XMLNodeWrapper elementNode = (XMLNodeWrapper) node;
        return (elementNode.getE().getChildren().size() == 0);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        // TODO Auto-generated method stub

    }

}
