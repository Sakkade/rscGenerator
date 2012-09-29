package amarandus.rscGenerator.utils;

import org.jdom2.Element;

public class XMLNodeWrapper
{
    private final Element e;

    public XMLNodeWrapper(Element e)
    {
        this.e = e;
    }

    public int index(XMLNodeWrapper child)
    {

        int count = childCount();
        for (int i = 0; i < count; i++)
        {
            if (child.getE() == this.child(i).getE())
            {
                return i;
            }
        }
        return -1; // Should never get here.
    }

    public int childCount()
    {
        return this.e.getChildren().size();
    }

    public XMLNodeWrapper child(int searchIndex)
    {
        Element child = this.e.getChildren().get(searchIndex);
        return new XMLNodeWrapper(child);
    }

    public Element getE()
    {
        return this.e;
    }

    @Override
    public String toString()
    {
        return (e.getAttributeValue("name") != null ? e
                .getAttributeValue("name") : e.getName());
    }
}
