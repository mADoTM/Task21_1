package ru.vsu.cs.dolzhenkoms;

import javax.swing.*;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphUI extends JFrame {
    private mxGraph graph;
    private Object parent;

    private JButton addVertexButton;
    private JButton addEdgeButton;
    private JButton executeButton;

    private JLabel startPointLabel;
    private JLabel endPointLabel;
    private JLabel semiShortestPathDistanceLabel;

    private JTextArea edgeArea;
    private JTextArea startPointArea;
    private JTextArea endPointArea;

    private List<mxCell> vertexes = new ArrayList<mxCell>();
    private List<mxCell> edges = new ArrayList<mxCell>();

    public static void main(String[] args) {
        GraphUI frame = new GraphUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public GraphUI()
    {
        super("Graph");

        graph = new mxGraph();
        parent = graph.getDefaultParent();
        Box panel = new Box(BoxLayout.Y_AXIS);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        initButtons();
        initFields();
        initLabels();

        panel.add(graphComponent);
        panel.add(addVertexButton);

        panel.add(edgeArea);
        panel.add(addEdgeButton);

        panel.add(startPointLabel);
        panel.add(startPointArea);

        panel.add(endPointLabel);
        panel.add(endPointArea);
        
        panel.add(executeButton);
        panel.add(semiShortestPathDistanceLabel);
        getContentPane().add(panel);
    }

    private void initButtons() {
        addVertexButton = new JButton("Добавить город");
        addVertexButton.setSize(50,20);
        addVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVertex();
            }
        });

        addEdgeButton = new JButton("Добавить дорогу");
        addEdgeButton.setSize(50,20);
        addEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEdgeFromString(edgeArea.getText());
            }
        });

        executeButton = new JButton("Выполнить");
        executeButton.setSize(50,20);
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintCorrectPath(new Path(new LinkedList<>(), 0));
                findSemiShortestPath();
            }
        });
    }

    private void initFields() {
        edgeArea = new JTextArea();
        edgeArea.setMaximumSize(new Dimension(400, 200));

        startPointArea = new JTextArea();
        startPointArea.setMaximumSize(new Dimension(400, 200));

        endPointArea = new JTextArea();
        endPointArea.setMaximumSize(new Dimension(400, 200));
    }

    private void initLabels() {
        startPointLabel = new JLabel("Начальный пункт");
        endPointLabel = new JLabel("Конечный пункт");

        semiShortestPathDistanceLabel = new JLabel("Длина почти самого короткого пути ");
        semiShortestPathDistanceLabel.setVisible(false);
    }

    private void addVertex() {
        graph.getModel().beginUpdate();

        vertexes.add((mxCell) graph.insertVertex(parent, null,  "" + vertexes.size(), 0, 10, 30, 30));

        graph.getModel().endUpdate();
    }

    private void addEdge(mxCell source, mxCell target, int distance) {
        graph.getModel().beginUpdate();

        String edgeId = source.getValue() + " " + target.getValue();

        edges.add((mxCell) graph.insertEdge(parent, edgeId, distance, source, target));

        graph.getModel().endUpdate();
    }

    private void addEdgeFromString(String str) {
        String[] details = str.split("\s");

        Integer sourceIndex = Integer.parseInt(details[0]);
        Integer targetIndex = Integer.parseInt(details[1]);
        Integer distance = Integer.parseInt(details[2]);

        mxCell source = vertexes.get(sourceIndex);
        mxCell target = vertexes.get(targetIndex);

        addEdge(source, target, distance);
    }

    private void findSemiShortestPath() {
        Graph graph = new Graph(startPointArea.getText(), endPointArea.getText());

        for(mxCell edge : edges) {
            String source = edge.getSource().getValue().toString();
            String target = edge.getTarget().getValue().toString();
            Integer distance = Integer.parseInt(edge.getValue().toString());
            graph.addEdge(source, target, distance);
        }

        LinkedList<String> visited = new LinkedList();
        visited.add(startPointArea.getText());
        graph.depthFirst(visited);

        Path path = graph.getPaths().get(1);
        paintCorrectPath(path);

        semiShortestPathDistanceLabel.setText("Длина почти самого короткого пути " + path.getDistance());
        semiShortestPathDistanceLabel.setVisible(true);
    }

    private void paintCorrectPath(Path path) {
        graph.getModel().beginUpdate();

        graph.setCellStyle("ROUNDED;strokeColor=red;fillColor=green", vertexes.stream().filter(x -> path.containsVertex(x.getValue().toString())).toArray());

        graph.getModel().endUpdate();
    }
}