package amarandus.rscGenerator.batchCreation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import amarandus.rscGenerator.utils.FileWrapper;
import amarandus.rscGenerator.utils.FolderTreeModel;

@SuppressWarnings("serial")
public class BatchCreatorPanel extends JPanel
{
    private final Action           addAction      = new ActionAdd();
    private final Action           removeAction   = new ActionRemove();
    private final Action           generateAction = new ActionGenerate();
    private final DefaultListModel listModel;
    private final JList            inputFolderList;
    private final JTree            folderTree;
    private final JComboBox        driveBox;
    private final JProgressBar     progressBar;

    /**
     * Create the frame.
     */
    public BatchCreatorPanel()
    {
        setBounds(100, 100, 606, 396);

        this.listModel = new DefaultListModel();
        setLayout(new BorderLayout(0, 0));

        JPanel buttonBarPanel = new JPanel();
        this.add(buttonBarPanel, BorderLayout.SOUTH);

        JButton btnAddNewFolder = new JButton("Add new Folder");
        btnAddNewFolder.setAction(this.addAction);
        buttonBarPanel.add(btnAddNewFolder);

        JButton btnRemove = new JButton("Remove");
        btnRemove.setAction(this.removeAction);
        buttonBarPanel.add(btnRemove);

        this.progressBar = new JProgressBar();
        buttonBarPanel.add(progressBar);

        JButton btnGenerateRsc = new JButton("Generate rsC");
        btnGenerateRsc.setAction(this.generateAction);
        buttonBarPanel.add(btnGenerateRsc);

        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(null);
        this.add(inputPanel);
        inputPanel.setLayout(new BorderLayout(0, 0));

        JScrollPane inputScrollPane = new JScrollPane();
        inputPanel.add(inputScrollPane);

        this.inputFolderList = new JList(this.listModel);
        this.inputFolderList
                .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inputScrollPane.setViewportView(this.inputFolderList);

        JLabel lblNewLabel = new JLabel("Input Folders");
        inputPanel.add(lblNewLabel, BorderLayout.NORTH);

        JPanel inputChoosePanel = new JPanel();
        this.add(inputChoosePanel, BorderLayout.WEST);
        inputChoosePanel.setLayout(new BorderLayout(0, 0));

        JScrollPane folderScrollPane = new JScrollPane();
        inputChoosePanel.add(folderScrollPane);
        folderScrollPane.setMaximumSize(new Dimension(250, 5000));

        this.folderTree = new JTree(new DefaultTreeModel(null));
        this.folderTree.setRootVisible(false);
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
                        (File) driveBox.getModel().getSelectedItem())));
            }
        });

        folderTree.setModel(new FolderTreeModel(new FileWrapper(
                (File) this.driveBox.getModel().getSelectedItem())));

    }

    private class ActionAdd extends AbstractAction
    {
        public ActionAdd()
        {
            putValue(NAME, "Add");
            putValue(SHORT_DESCRIPTION, "Add a new folder to the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (folderTree.getSelectionCount() == 1)
            {
                TreePath path = folderTree.getSelectionPath();
                String filePath = ((File) driveBox.getModel().getSelectedItem())
                        .getPath();
                for (Object o : path.getPath())
                {
                    if (!o.toString().equals(""))
                        filePath += o.toString() + File.separator;
                }

                if (!listModel.contains(filePath))
                {
                    listModel.addElement(filePath);
                }
            }
        }
    }

    private class ActionRemove extends AbstractAction
    {
        public ActionRemove()
        {
            putValue(NAME, "Remove");
            putValue(SHORT_DESCRIPTION, "Remove the folder from the list");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (inputFolderList.getSelectedIndex() != -1)
            {
                listModel.remove(inputFolderList.getSelectedIndex());
            }
        }
    }

    private class ActionGenerate extends AbstractAction
    {
        public ActionGenerate()
        {
            putValue(NAME, "Generate");
            putValue(SHORT_DESCRIPTION, "Generate the rsCollection-files");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (listModel.getSize() <= 0)
            {
                return;
            }

            progressBar.getModel().setRangeProperties(0, 1, 0, 0, true);

            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (JFileChooser.APPROVE_OPTION == chooser
                    .showOpenDialog(BatchCreatorPanel.this))
            {
                for (int i = 0; i < listModel.size(); i++)
                {
                    String dir = listModel.get(i).toString();
                    if (!dir.contains(" - (DONE)"))
                    {
                        new DirScanner(dir, listModel, chooser
                                .getSelectedFile().toString() + File.separator,
                                progressBar.getModel()).start();
                    }
                }
            }
        }
    }
}
