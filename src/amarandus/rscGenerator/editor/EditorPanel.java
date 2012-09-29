package amarandus.rscGenerator.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import amarandus.rscGenerator.utils.FileWrapper;
import amarandus.rscGenerator.utils.FolderTreeModel;
import amarandus.rscGenerator.utils.XMLNodeWrapper;
import amarandus.rscGenerator.utils.XMLTreeModel;

public class EditorPanel extends JPanel
{

    private final JTree     folderTree;
    private final JComboBox driveBox;

    /**
     * Create the panel.
     */
    public EditorPanel()
    {
        setLayout(new BorderLayout(0, 0));

        JPanel buttonBar = new JPanel();
        add(buttonBar, BorderLayout.NORTH);

        JButton btnNew = new JButton("New");
        buttonBar.add(btnNew);

        JButton btnOpen = new JButton("Open");
        buttonBar.add(btnOpen);

        JButton btnSave = new JButton("Save");
        buttonBar.add(btnSave);

        JButton btnSaveAs = new JButton("Save as");
        buttonBar.add(btnSaveAs);

        JPanel inputChoosePanel = new JPanel();
        this.add(inputChoosePanel, BorderLayout.WEST);
        inputChoosePanel.setLayout(new BorderLayout(0, 0));

        JScrollPane folderScrollPane = new JScrollPane();
        inputChoosePanel.add(folderScrollPane);
        folderScrollPane.setMaximumSize(new Dimension(250, 5000));

        this.folderTree = new JTree(new DefaultTreeModel(null));
        folderTree.setRootVisible(false);
        folderTree.setDragEnabled(true);
        folderTree.setTransferHandler(new TransferHandler()
        {
            @Override
            public int getSourceActions(JComponent c)
            {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c)
            {
                JTree t = (JTree) c;

                TreePath path = t.getSelectionPath();
                String filePath = ((File) driveBox.getModel().getSelectedItem())
                        .getPath();
                for (Object o : path.getPath())
                {
                    if (!o.toString().equals(""))
                        filePath += o.toString() + File.separator;
                }

                return new StringSelection(filePath);
            }
        });
        folderScrollPane.setViewportView(this.folderTree);

        JPanel captionPanel = new JPanel();
        inputChoosePanel.add(captionPanel, BorderLayout.NORTH);
        captionPanel.setLayout(new BorderLayout(0, 0));

        JLabel lblChooseInputFolders = new JLabel("Choose input folders");
        captionPanel.add(lblChooseInputFolders, BorderLayout.NORTH);

        this.driveBox = new JComboBox(File.listRoots());
        captionPanel.add(this.driveBox);
        this.driveBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                folderTree.setModel(new FolderTreeModel(new FileWrapper(
                        (File) driveBox.getModel().getSelectedItem()), true));
            }
        });

        folderTree.setModel(new FolderTreeModel(new FileWrapper(
                (File) this.driveBox.getModel().getSelectedItem()), true));

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);

        File f = new File("a.rsCollection");

        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try
        {
            doc = builder.build(f);
        } catch (JDOMException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final JTree tree = new JTree(new XMLTreeModel(doc));
        tree.setRootVisible(false);
        tree.setTransferHandler(new TransferHandler()
        {
            @Override
            public boolean canImport(TransferSupport support)
            {
                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor))
                {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferSupport support)
            {
                if (!canImport(support))
                {
                    return false;
                }

                Transferable t = support.getTransferable();

                String data = null;
                try
                {
                    data = (String) t.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                XMLNodeWrapper targetNode = (XMLNodeWrapper) tree
                        .getSelectionPath().getLastPathComponent();

                new DirAdder(data, targetNode.getE()).start();

                return true;
            }
        });
        tree.setDropMode(DropMode.ON_OR_INSERT);
        scrollPane.setViewportView(tree);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.SOUTH);

        JButton btnAdd = new JButton("Add");
        panel_1.add(btnAdd);

        JButton btnNewFolder = new JButton("New Folder");
        panel_1.add(btnNewFolder);

        JButton btnRemove = new JButton("Remove");
        panel_1.add(btnRemove);

    }
}
