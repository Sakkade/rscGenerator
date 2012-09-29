package amarandus.rscGenerator.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import amarandus.rscGenerator.batchCreation.BatchCreatorPanel;
import amarandus.rscGenerator.editor.EditorPanel;

public class GUIrscGenerator extends JFrame
{

    private final JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    GUIrscGenerator frame = new GUIrscGenerator();
                    frame.setVisible(true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public GUIrscGenerator()
    {
        setTitle("rsCollection Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(100, 100, 758, 470);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("rscGenerator", null, new BatchCreatorPanel(),
                "Create rsCollections from folders as a batch process.");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("rscEditor", null, new EditorPanel(),
                "Edit rsCollections.");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);

    }

}
