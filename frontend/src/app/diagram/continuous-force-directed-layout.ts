import * as go from 'gojs';
import { OverviewEntity } from '../shared/types/model/OverviewEntity';
import { Router } from "@angular/router";

export class ContinuousForceDirectedLayout extends go.ForceDirectedLayout {
  private _isObserving: boolean = false;
  private myDiagram: go.Diagram = new go.Diagram();
  private overviewEntities: OverviewEntity[];

  constructor(myDiagram: go.Diagram, overviewEntities: OverviewEntity[], private router: Router) {
    super();
    this.myDiagram = myDiagram;
    this.overviewEntities = overviewEntities;
  }

  eventListener() {
    window.addEventListener('DOMContentLoaded', this.init);
  }

  updateDiagram(overviewEntities: OverviewEntity[]) {
    this.myDiagram = generateData(this.myDiagram, overviewEntities);
    this.myDiagram.layout.invalidateLayout();
    this.myDiagram.layoutDiagram();
  }

  override isFixed(v: go.LayoutVertex) {
    return v.node!.isSelected;
  }

  override doLayout(coll: any) {
    if (!this._isObserving) {
      this._isObserving = true;
      // cacheing the network means we need to recreate it if nodes or links have been added or removed or relinked,
      // so we need to track structural model changes to discard the saved network.
      this.diagram!.addModelChangedListener((e) => {
        // modelChanges include a few cases that we don't actually care about, such as
        // "nodeCategory" or "linkToPortId", but we'll go ahead and recreate the network anyway.
        // Also clear the network when replacing the model.
        if (
          e.modelChange !== '' ||
          (e.change === go.ChangedEvent.Transaction && e.propertyName === 'StartingFirstTransaction')
        ) {
          this.network = null;
        }
      });
    }
    var net = this.network;
    if (net === null) {
      // the first time, just create the network as normal
      this.network = net = this.makeNetwork(coll);
    } else {
      // but on reuse we need to update the LayoutVertex.bounds for selected nodes
      this.diagram!.nodes.each((n) => {
        var v = net!.findVertex(n);
        if (v !== null) v.bounds = n.actualBounds;
      });
    }
    // now perform the normal layout
    super.doLayout(coll);
    // doLayout normally discards the LayoutNetwork by setting Layout.network to null;
    // here we remember it for next time
    this.network = net;
  }

  async init() {
    const $ = go.GraphObject.make;
    this.myDiagram = new go.Diagram(
      'myDiagramDiv', // must name or refer to the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform, // an initial automatic zoom-to-fit
        contentAlignment: go.Spot.Center, // align document to the center of the viewport
        layout: $(
          ContinuousForceDirectedLayout, // our class extends go.ForceDirectedLayout
          {
            defaultSpringLength: 60,
            defaultElectricalCharge: 150,
          },
        ),
        // do an extra layout at the end of a move
        SelectionMoved: (e) => e.diagram.layout.invalidateLayout(),
        InitialLayoutCompleted: (e) => {
          // if not all Nodes have real locations, force a layout to happen
          if (!e.diagram.nodes.all((n) => n.location.isReal())) {
            e.diagram.layoutDiagram(true);
          }
        },
      },
    );

    // dragging a node invalidates the Diagram.layout, causing a layout during the drag
    this.myDiagram.toolManager.draggingTool.doMouseMove = function () {
      // method override must be function, not =>
      go.DraggingTool.prototype.doMouseMove.call(this);
      if (this.isActive) {
        this.diagram!.layout.doLayout(this.diagram!.nodes);
      }
    };

    // define each Node's appearance
    this.myDiagram.nodeTemplate = $(
      go.Node,
      'Auto', // the whole node panel
      // define the node's outer shape, which will surround the TextBlock
      $(
        go.Shape,
        'Circle',
        {
          name: 'BUBBLE',
          fill: 'white',
          stroke: null,
          desiredSize: new go.Size(100, 100),
        },
        new go.Binding('fill', 'color'),
        new go.Binding('name', 'name'),
        new go.Binding('desiredSize', 'size'),
      ),
      {
        shadowColor: 'gray',
        mouseEnter: function (e, node) {
          handleNodeAction(node, false);
        },
        mouseLeave: function (e, node) {
          handleNodeAction(node, true);
        },
        click: function (e, node) {
          handleNodeAction(node, true);
        },
      },
      $(
        go.TextBlock,
        {
          font: 'normal 18px Roboto',
          textAlign: 'center',
          stroke: 'black',
          wrap: go.TextBlock.WrapDesiredSize,
        },
        new go.Binding('text', 'text'),
        new go.Binding('font', 'font'),
        new go.Binding('margin', 'margin'),
        new go.Binding('maxSize', 'maxSize'),
        new go.Binding('stroke', 'textColor'),
      ),
    );
    // the rest of this app is the same as samples/conceptMap.html

    // replace the default Link template in the linkTemplateMap
    this.myDiagram.linkTemplate = $(
      go.Link, // the whole link panel
      // {
      //   routing: go.Link.AvoidsNodes
      // },
      $(
        go.Shape, // the link shape
        { stroke: '#1C355E' },
      ),
      $(
        go.Shape, // the arrowhead
        { toArrow: 'standard', stroke: null },
      ),
    );

    this.myDiagram.addDiagramListener('ObjectSingleClicked', (e: any) => {
      var part = e.subject.part;
      console.log('Selected Node ' + this.myDiagram!.findNodeForKey(part.ub.key));
      if (part.ub.text == 'puzzle') return;
      if (part.ub.key.charAt(0) == 1) return;
      if (!(part instanceof go.Link)) {
        let type = part.ub.key.charAt(0) == 2 ? 'Objective' : 'KeyResult';
        document.getElementById('clickedElement')!.innerHTML = 'It is a : ' + type + ' with text: ' + part.ub.text;
        this.router.navigate([type.toLowerCase(), part.ub.key.substring(1)]);
      }
    });

    generateData(this.myDiagram, this.overviewEntities);
  }
}

function generateData(diagram: go.Diagram, overviewEntities: OverviewEntity[]) {
  let diagramData = createBubbles(overviewEntities);

  diagram.model = new go.GraphLinksModel(diagramData[0], diagramData[1]);

  return diagram;
}

function handleNodeAction(node: any, isLeaving: boolean) {
  if (!node.part!.findObject('PUZZLE') && !node.part!.findObject('TEAM')) {
    const goNode = node as go.Node;
    if (goNode) {
      if (isLeaving) {
        goNode.isShadowed = false;
      } else {
        if (!goNode.isSelected) {
          goNode.isShadowed = true;
        }
      }
      if (node.part!.findObject('KR')) {
        const krObject1 = node.part!.findObject('KR') as go.Shape;
        if (krObject1) {
          if (isLeaving) {
            krObject1.fill = '#E5E8EB';
          } else {
            krObject1.fill = '#F7F8F9';
          }
        }
        const krObject2 = node.part!.findObject('KR') as go.Shape;
        if (krObject2) {
          if (isLeaving) {
            krObject2.stroke = null;
          } else {
            krObject2.stroke = '#5D6974';
          }
        }
      }
    }
  }
}

function createBubbles(overviewEntities: OverviewEntity[]) {
  var nodeDataArray: any[] = [];
  var linkDataArray: any[] = [];

  let puzzle = {
    key: 1,
    text: 'puzzle',
    font: 'bold 70px OKRFont, sans-serif',
    margin: new go.Margin(20, 0, 0, 0),
    name: 'PUZZLE',
    textColor: 'white',
    color: '#1E5A96',
    size: new go.Size(200, 200),
  };
  nodeDataArray.push(puzzle);

  overviewEntities.forEach((overviewEntity) => {
    let team = {
      key: '1' + overviewEntity.team.id,
      text: overviewEntity.team.name,
      font: 'normal 32px Roboto',
      name: 'TEAM',
      color: '#238BCA',
      maxSize: new go.Size(90, 90),
      size: new go.Size(160, 160),
    };
    nodeDataArray.push(team);
    let teamLink = { from: '1' + overviewEntity.team.id, to: 1 };
    linkDataArray.push(teamLink);

    overviewEntity.objectives.forEach((objective) => {
      let objectiveBubble = {
        key: '2' + objective.id,
        text: objective.title.length > 25 ? split_at_index(objective.title, 25) : objective.title,
        color: '#2C97A6',
        maxSize: new go.Size(90, 90),
        size: new go.Size(160, 160),
      };
      nodeDataArray.push(objectiveBubble);
      let objectiveLink = { from: '2' + objective.id, to: '1' + overviewEntity.team.id };
      linkDataArray.push(objectiveLink);

      objective.keyResults.forEach((keyResult) => {
        let keyResultBubble = {
          key: '3' + keyResult.id,
          text: keyResult.title.length > 20 ? split_at_index(keyResult.title, 20) : keyResult.title,
          font: 'normal 14px Roboto',
          name: 'KR',
          color: '#E5E8EB',
          maxSize: new go.Size(90, 90),
          size: new go.Size(120, 120),
        };
        nodeDataArray.push(keyResultBubble);
        let keyResultLink = { from: '3' + keyResult.id, to: '2' + objective.id };
        linkDataArray.push(keyResultLink);
      });
    });
  });

  return [nodeDataArray, linkDataArray];
}

function split_at_index(value: any, index: any) {
  return value.substring(0, index) + '...';
}
